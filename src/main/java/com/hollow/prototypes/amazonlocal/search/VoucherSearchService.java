package com.hollow.prototypes.amazonlocal.search;

import java.util.List;

import com.hollow.prototypes.amazonlocal.voucher.Voucher;

public interface VoucherSearchService {
    List<Voucher> getUnassignedVouchers();
}
