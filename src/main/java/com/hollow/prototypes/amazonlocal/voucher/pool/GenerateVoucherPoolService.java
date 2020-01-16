package com.hollow.prototypes.amazonlocal.voucher.pool;

import java.util.ArrayList;
import java.util.List;

import com.hollow.prototypes.amazonlocal.voucher.Voucher;

public class GenerateVoucherPoolService {
    public VoucherPool generateVoucherPool(int numVouchers) {
        List<Voucher> pool = new ArrayList<>();
        for (int i = 0; i < numVouchers; i++) {
            pool.add(Voucher
                    .builder()
                    .voucherId("Voucher-" + i)
                    .customerId("-1")
                    .build());
        }
        return VoucherPool
                .builder()
                .pool(pool)
                .build();
    }
}
