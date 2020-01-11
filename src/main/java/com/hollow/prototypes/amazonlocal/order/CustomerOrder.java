package com.hollow.prototypes.amazonlocal.order;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;

import com.hollow.prototypes.amazonlocal.voucher.Voucher;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerOrder {
    private final Integer customerId;
    private final Instant orderEnqueueTime;
    private Instant fulfillmentTime;
    private Voucher voucher;
    private final AtomicInteger searchTries;
    private final AtomicInteger assignmentTries;
}
