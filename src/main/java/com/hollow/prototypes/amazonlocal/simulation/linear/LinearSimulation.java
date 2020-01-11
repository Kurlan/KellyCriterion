package com.hollow.prototypes.amazonlocal.simulation.linear;

import java.time.Duration;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;

import com.hollow.prototypes.amazonlocal.assignment.AssignVoucherService;
import com.hollow.prototypes.amazonlocal.assignment.RegularAssignVoucherService;
import com.hollow.prototypes.amazonlocal.order.CustomerOrder;
import com.hollow.prototypes.amazonlocal.order.queue.OrderQueue;
import com.hollow.prototypes.amazonlocal.search.LinearSearchService;
import com.hollow.prototypes.amazonlocal.search.VoucherSearchService;
import com.hollow.prototypes.amazonlocal.simulation.VoucherSimulation;
import com.hollow.prototypes.amazonlocal.simulation.input.SimulationInput;
import com.hollow.prototypes.amazonlocal.simulation.input.SimulationInputGenerator;
import com.hollow.prototypes.amazonlocal.simulation.result.SimulationResult;
import com.hollow.prototypes.amazonlocal.voucher.Voucher;
import com.hollow.prototypes.amazonlocal.voucher.pool.VoucherPool;

public class LinearSimulation implements VoucherSimulation {
    private static final int ASSIGNMENT_RETRIES = 3;
    private static final String DESCRIPTION = "Local and linear assignment";

    private final VoucherPool voucherPool;
    private final OrderQueue orderQueue;
    private final VoucherSearchService voucherSearchService;
    private final SimulationResult simulationResult;
    private final List<CustomerOrder> fulfilledOrders;

    public LinearSimulation(int n) {
        SimulationInputGenerator simulationInputGenerator = new SimulationInputGenerator();
        SimulationInput simulationInput = simulationInputGenerator.getSimulationInput(n);
        voucherPool = simulationInput.getVoucherpool();
        orderQueue = simulationInput.getOrderQueue();
        voucherSearchService = new LinearSearchService(voucherPool);
        simulationResult = SimulationResult
                .builder()
                .description(DESCRIPTION)
                .build();
        fulfilledOrders = new LinkedList<>();
    }

    @Override
    public void run() {

        simulationResult.setStartTime(Instant.now());
        AssignVoucherService assignmentVoucherService = new RegularAssignVoucherService();

        while (!orderQueue.getCustomerOrders().isEmpty()) {
            CustomerOrder customerOrder = orderQueue.getCustomerOrders().poll();
            customerOrder.getSearchTries().addAndGet(1);
            List<Voucher> searchResult = voucherSearchService.getUnassignedVouchers();
            boolean assignmentSuccess = false;
            for (int i = 0; i < Math.min (searchResult.size(), ASSIGNMENT_RETRIES); i++) {
                customerOrder.getAssignmentTries().addAndGet(1);
                if (assignmentVoucherService.assign(customerOrder, searchResult.get(i))) {
                    fulfilledOrders.add(customerOrder);
                    assignmentSuccess = true;
                    break;
                }
            }

            if (assignmentSuccess == false) {
                orderQueue.getCustomerOrders().offer(customerOrder);
            }
        }
        simulationResult.setEndtime(Instant.now());
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
