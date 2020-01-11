package com.hollow.prototypes.amazonlocal.simulation.input;

import com.hollow.prototypes.amazonlocal.order.queue.GenerateOrderQueueService;
import com.hollow.prototypes.amazonlocal.order.queue.OrderQueue;
import com.hollow.prototypes.amazonlocal.voucher.pool.GenerateVoucherPoolService;
import com.hollow.prototypes.amazonlocal.voucher.pool.VoucherPool;

public class SimulationInputGenerator {
    public SimulationInput getSimulationInput(int n) {
        GenerateOrderQueueService generateOrderQueueService = new GenerateOrderQueueService();
        OrderQueue orderQueue = generateOrderQueueService.generateQueue(n);

        GenerateVoucherPoolService generateVoucherPoolService = new GenerateVoucherPoolService();
        VoucherPool voucherpool = generateVoucherPoolService.generateVoucherPool(n);

        return SimulationInput
                .builder()
                .orderQueue(orderQueue)
                .voucherpool(voucherpool)
                .build();
    }
}
