package com.hollow.prototypes.amazonlocal.assignment;

import java.time.Instant;

import com.hollow.prototypes.amazonlocal.order.CustomerOrder;
import com.hollow.prototypes.amazonlocal.voucher.Voucher;

public class RegularAssignVoucherService implements AssignVoucherService {

    @Override
    public synchronized boolean assign(CustomerOrder customerOrder, Voucher voucher) {
        if (!voucher.getCustomerId().equals("-1")) {
            return false;
        }
        customerOrder.setFulfillmentTime(Instant.now());
        customerOrder.setVoucher(voucher);
        voucher.setCustomerId(customerOrder.getCustomerId());
        return true;
    }
}
