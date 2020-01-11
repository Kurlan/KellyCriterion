package com.hollow.prototypes.amazonlocal.search;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.hollow.prototypes.amazonlocal.voucher.Voucher;
import com.hollow.prototypes.amazonlocal.voucher.pool.VoucherPool;

public class AmazonLocalActualSearchService implements VoucherSearchService {

    private final VoucherPool voucherPool;

    public AmazonLocalActualSearchService(final VoucherPool voucherPool) {
        this.voucherPool = voucherPool;
    }

    @Override
    public List<Voucher> getUnassignedVouchers() {
        List<Voucher> vouchers = voucherPool.getPool().stream()
                .filter(voucher -> voucher.getCustomerId() == -1)
                .limit(100).collect(Collectors.toList());

        Collections.shuffle(vouchers);
        return vouchers;
    }
}
