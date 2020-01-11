package com.hollow.prototypes.amazonlocal.simulation.amazonlocal;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.hollow.prototypes.amazonlocal.assignment.AssignVoucherService;
import com.hollow.prototypes.amazonlocal.assignment.RegularAssignVoucherService;
import com.hollow.prototypes.amazonlocal.order.CustomerOrder;
import com.hollow.prototypes.amazonlocal.order.queue.OrderQueue;
import com.hollow.prototypes.amazonlocal.search.AmazonLocalActualSearchService;
import com.hollow.prototypes.amazonlocal.search.VoucherSearchService;
import com.hollow.prototypes.amazonlocal.simulation.VoucherSimulation;
import com.hollow.prototypes.amazonlocal.simulation.input.SimulationInput;
import com.hollow.prototypes.amazonlocal.simulation.input.SimulationInputGenerator;
import com.hollow.prototypes.amazonlocal.simulation.result.SimulationResult;
import com.hollow.prototypes.amazonlocal.voucher.pool.VoucherPool;

public class AmazonLocalActualSimulation implements VoucherSimulation {
    private static final String DESCRIPTION = "Amazon Local Production";

    private final VoucherPool voucherPool;
    private final OrderQueue orderQueue;
    private final VoucherSearchService voucherSearchService;
    private final AssignVoucherService assignVoucherService;

    private final SimulationResult simulationResult;
    private final ExecutorService executor;
    private final int fleetSize;
    private final List<Callable<LinkedList<CustomerOrder>>> worker;
    private final List<CustomerOrder> fulfilledOrders;

    public AmazonLocalActualSimulation(int n, int fleetSize) {
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
        executor = Executors.newFixedThreadPool(fleetSize);
        worker = new ArrayList<>();
        this.fleetSize = fleetSize;
        this.assignVoucherService = new RegularAssignVoucherService();
        for (int i = 0; i < fleetSize; i++) {
            worker.add(new AmazonLocalSimulationWorkerCallable(voucherPool, orderQueue, voucherSearchService, assignVoucherService));
        }

    }

    @Override
    public void run() {
        simulationResult.setStartTime(Instant.now());

        try {
            List<Future<LinkedList<CustomerOrder>>> fulfilledOrdersFutures = executor.invokeAll(worker);
            for (Future<LinkedList<CustomerOrder>> fulfilledOrdersFuture : fulfilledOrdersFutures) {
                fulfilledOrders.addAll(fulfilledOrdersFuture.get());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
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
