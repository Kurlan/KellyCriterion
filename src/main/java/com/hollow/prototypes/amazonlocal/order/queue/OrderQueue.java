package com.hollow.prototypes.amazonlocal.order.queue;

import java.util.concurrent.ConcurrentLinkedQueue;

import com.hollow.prototypes.amazonlocal.order.CustomerOrder;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderQueue {
    ConcurrentLinkedQueue<CustomerOrder> customerOrders;
}
