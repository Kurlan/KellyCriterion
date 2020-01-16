package com.hollow.prototypes.amazonlocal.order.queue;

import java.time.Instant;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import com.hollow.prototypes.amazonlocal.order.CustomerOrder;

public class GenerateOrderQueueService {

    public OrderQueue generateQueue(int n) {
        ConcurrentLinkedQueue<CustomerOrder> customerOrders = new ConcurrentLinkedQueue<>();
        for (Integer i = 0; i < n; i++) {
            customerOrders.offer(CustomerOrder
                    .builder()
                    .customerId(i.toString())
                    .orderEnqueueTime(Instant.now())
                    .searchTries(new AtomicInteger(0))
                    .assignmentTries(new AtomicInteger(0))
                    .build());
        }
        return OrderQueue.builder()
                .customerOrders(customerOrders)
                .build();
    }
}
