package com.hollow.prototypes.amazonlocal.voucher;

import com.netflix.hollow.core.write.objectmapper.HollowPrimaryKey;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@HollowPrimaryKey(fields="voucherId")
public class Voucher {
    private final String voucherId;
    private int customerId;
}
