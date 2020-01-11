package com.hollow.prototypes.amazonlocal.voucher.pool;

import java.util.List;

import com.hollow.prototypes.amazonlocal.voucher.Voucher;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class VoucherPool {
    private final List<Voucher> pool;

}
