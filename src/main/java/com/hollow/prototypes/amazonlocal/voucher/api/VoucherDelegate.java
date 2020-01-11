package com.hollow.prototypes.amazonlocal.voucher.api;

import com.netflix.hollow.api.objects.delegate.HollowObjectDelegate;


@SuppressWarnings("all")
public interface VoucherDelegate extends HollowObjectDelegate {

    public int getVoucherIdOrdinal(int ordinal);

    public int getCustomerId(int ordinal);

    public Integer getCustomerIdBoxed(int ordinal);

    public VoucherTypeAPI getTypeAPI();

}