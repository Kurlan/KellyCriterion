package com.hollow.prototypes.amazonlocal.simulation.hollow;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.hollow.prototypes.amazonlocal.assignment.AssignVoucherService;
import com.hollow.prototypes.amazonlocal.assignment.HollowAssignVoucherService;
import com.hollow.prototypes.amazonlocal.hollow.producer.HollowVoucherProducer;
import com.hollow.prototypes.amazonlocal.order.CustomerOrder;
import com.hollow.prototypes.amazonlocal.order.queue.OrderQueue;
import com.hollow.prototypes.amazonlocal.search.AmazonLocalActualSearchService;
import com.hollow.prototypes.amazonlocal.search.VoucherSearchService;
import com.hollow.prototypes.amazonlocal.simulation.VoucherSimulation;
import com.hollow.prototypes.amazonlocal.simulation.input.SimulationInput;
import com.hollow.prototypes.amazonlocal.simulation.input.SimulationInputGenerator;
import com.hollow.prototypes.amazonlocal.simulation.result.SimulationResult;
import com.hollow.prototypes.amazonlocal.voucher.pool.VoucherPool;

public class HollowSimulation implements VoucherSimulation {
    private static final String DESCRIPTION = "Voucher Assignment with Hollow.";
    private final VoucherPool voucherPool;
    private final OrderQueue orderQueue;
    private final VoucherSearchService voucherSearchService;
    private final AssignVoucherService assignVoucherService;

    private final SimulationResult simulationResult;
    private final ExecutorService fleetThreadExecutor;
    private final ExecutorService hollowProducerExecutor;
    private final int fleetSize;
    private final List<Callable<LinkedList<CustomerOrder>>> workers;
    private final List<CustomerOrder> fulfilledOrders;
    private final HollowVoucherProducer hollowVoucherProducer;

    public HollowSimulation(int n, int fleetSize) {
        SimulationInputGenerator simulationInputGenerator = new SimulationInputGenerator();
        SimulationInput simulationInput = simulationInputGenerator.getSimulationInput(n);
        this.voucherPool = simulationInput.getVoucherpool();
        this.fulfilledOrders = new LinkedList<>();
        orderQueue = simulationInput.getOrderQueue();
        voucherSearchService = new AmazonLocalActualSearchService(voucherPool);
        simulationResult = SimulationResult
                .builder()
                .description(DESCRIPTION)
                .build();
        fleetThreadExecutor = Executors.newFixedThreadPool(fleetSize);
        workers = new ArrayList<>();
        this.fleetSize = fleetSize;
        this.assignVoucherService = new HollowAssignVoucherService(voucherPool);
        for (int i = 0; i < fleetSize; i++) {
            workers.add(new HollowSimulationWorkerCallable(voucherPool, orderQueue, assignVoucherService));
        }

        hollowProducerExecutor = Executors.newSingleThreadExecutor();
        hollowVoucherProducer = new HollowVoucherProducer(voucherPool);
    }

    @Override
    public void run() {
        simulationResult.setStartTime(Instant.now());

        try {
            hollowProducerExecutor.submit(hollowVoucherProducer);

            List<Future<LinkedList<CustomerOrder>>> fulfilledOrdersFutures = fleetThreadExecutor.invokeAll(workers);

            for (Future<LinkedList<CustomerOrder>> fulfilledOrdersFuture : fulfilledOrdersFutures) {
                fulfilledOrders.addAll(fulfilledOrdersFuture.get());
            }


        } catch(InterruptedException | ExecutionException e) {
                e.printStackTrace();
        } finally {
            hollowProducerExecutor.shutdown();
            fleetThreadExecutor.shutdown();
            simulationResult.setEndtime(Instant.now());
        }
    }

    @Override
    public SimulationResult getResults() {
        simulationResult.setSimulationDurationSeconds(
                Duration.between(simulationResult.getStartTime(), simulationResult.getEndtime()).getSeconds());
        long searchTries = 0;
        long assignmentTries = 0;
        for (CustomerOrder customerOrder : fulfilledOrders) {
            searchTries += customerOrder.getSearchTries().get();
            assignmentTries += customerOrder.getAssignmentTries().get();
        }
        simulationResult.setSearchTries(searchTries);
        simulationResult.setAssignmentTries(assignmentTries);
        return simulationResult;
    }
}
