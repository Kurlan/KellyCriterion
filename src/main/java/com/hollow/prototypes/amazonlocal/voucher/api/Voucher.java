package com.hollow.prototypes.amazonlocal.voucher.api;

import com.netflix.hollow.api.objects.HollowObject;

@SuppressWarnings("all")
public class Voucher extends HollowObject {

    public Voucher(VoucherDelegate delegate, int ordinal) {
        super(delegate, ordinal);
    }

    public HString getVoucherId() {
        int refOrdinal = delegate().getVoucherIdOrdinal(ordinal);
        if(refOrdinal == -1)
            return null;
        return  api().getHString(refOrdinal);
    }

    public HString getCustomerId() {
        int refOrdinal = delegate().getCustomerIdOrdinal(ordinal);
        if(refOrdinal == -1)
            return null;
        return  api().getHString(refOrdinal);
    }

    public VoucherAPI api() {
        return typeApi().getAPI();
    }

    public VoucherTypeAPI typeApi() {
        return delegate().getTypeAPI();
    }

    protected VoucherDelegate delegate() {
        return (VoucherDelegate)delegate;
    }

}