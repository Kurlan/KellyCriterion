package com.hollow.prototypes.amazonlocal.simulation.input;

import com.hollow.prototypes.amazonlocal.order.queue.OrderQueue;
import com.hollow.prototypes.amazonlocal.voucher.pool.VoucherPool;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SimulationInput {
    private final VoucherPool voucherpool;
    private final OrderQueue orderQueue;
}
