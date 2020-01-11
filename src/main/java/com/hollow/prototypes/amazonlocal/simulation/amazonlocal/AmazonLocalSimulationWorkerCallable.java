package com.hollow.prototypes.amazonlocal.simulation.amazonlocal;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;

import com.hollow.prototypes.amazonlocal.assignment.AssignVoucherService;
import com.hollow.prototypes.amazonlocal.order.CustomerOrder;
import com.hollow.prototypes.amazonlocal.order.queue.OrderQueue;
import com.hollow.prototypes.amazonlocal.search.VoucherSearchService;
import com.hollow.prototypes.amazonlocal.voucher.Voucher;
import com.hollow.prototypes.amazonlocal.voucher.pool.VoucherPool;

public class AmazonLocalSimulationWorkerCallable implements Callable<LinkedList<CustomerOrder>> {
    private static final int ASSIGNMENT_RETRIES = 3;
    private final VoucherPool voucherPool;
    private final OrderQueue orderQueue;
    private final VoucherSearchService voucherSearchService;
    private final AssignVoucherService assignVoucherService;

    public AmazonLocalSimulationWorkerCallable(VoucherPool voucherPool, OrderQueue orderQueue, VoucherSearchService voucherSearchService, AssignVoucherService assignVoucherService) {
        this.orderQueue = orderQueue;
        this.voucherPool = voucherPool;
        this.voucherSearchService = voucherSearchService;
        this.assignVoucherService = assignVoucherService;
    }

    @Override
    public LinkedList<CustomerOrder> call() throws Exception {
        LinkedList<CustomerOrder> fulfilledOrders = new LinkedList<>();
            while (!orderQueue.getCustomerOrders().isEmpty()) {
                    CustomerOrder customerOrder = orderQueue.getCustomerOrders().poll();
                    if (customerOrder == null) {
                        continue;
                    }
                    customerOrder.getSearchTries().addAndGet(1);
                    List<Voucher> searchResult = voucherSearchService.getUnassignedVouchers();
                    boolean assignmentSuccess = false;
                    for (int i = 0; i < Math.min (searchResult.size(), ASSIGNMENT_RETRIES); i++) {
                        customerOrder.getAssignmentTries().addAndGet(1);
                        if (assignVoucherService.assign(customerOrder, searchResult.get(i))) {
                            fulfilledOrders.push(customerOrder);
                            assignmentSuccess = true;
                            break;
                        }
                    }

                    if (assignmentSuccess == false) {
                        orderQueue.getCustomerOrders().offer(customerOrder);
                    }
            }

        return fulfilledOrders;
    }
}
