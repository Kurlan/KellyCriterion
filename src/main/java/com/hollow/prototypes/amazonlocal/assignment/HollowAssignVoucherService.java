package com.hollow.prototypes.amazonlocal.assignment;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import com.hollow.prototypes.amazonlocal.order.CustomerOrder;
import com.hollow.prototypes.amazonlocal.voucher.Voucher;
import com.hollow.prototypes.amazonlocal.voucher.pool.VoucherPool;

public class HollowAssignVoucherService implements AssignVoucherService  {

    private final VoucherPool voucherPool;
    private final Map<String, Voucher> voucherMap;

    public HollowAssignVoucherService(VoucherPool voucherPool) {
        this.voucherPool = voucherPool;
        this.voucherMap = new HashMap<>();
        for (Voucher v: voucherPool.getPool()) {
            voucherMap.put(v.getVoucherId(), v);
        }
    }

    @Override
    public synchronized boolean assign(CustomerOrder customerOrder, Voucher voucher) {
        String voucherId = voucher.getVoucherId();
        Voucher actualVoucher = voucherMap.get(voucherId);
        if (!actualVoucher.getCustomerId().equals("-1")) {
            return false;
        }
        customerOrder.setFulfillmentTime(Instant.now());
        customerOrder.setVoucher(actualVoucher);
        actualVoucher.setCustomerId(customerOrder.getCustomerId());
        return true;
    }
}
