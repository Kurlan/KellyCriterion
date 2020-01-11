package com.hollow.prototypes.amazonlocal.search;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.hollow.prototypes.amazonlocal.voucher.Voucher;
import com.hollow.prototypes.amazonlocal.voucher.pool.VoucherPool;

public class LinearSearchService implements VoucherSearchService {

    private final VoucherPool voucherPool;
    private final Iterator<Voucher> voucherIterator;

    public LinearSearchService(final VoucherPool voucherPool) {
        this.voucherPool = voucherPool;
        this.voucherIterator = voucherPool.getPool().listIterator();
    }

    @Override
    public List<Voucher> getUnassignedVouchers() {
        return ImmutableList.of(voucherIterator.next());
    }
}