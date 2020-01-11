package com.hollow.prototypes.amazonlocal.voucher.api;

import com.netflix.hollow.api.objects.HollowObject;
import com.netflix.hollow.core.schema.HollowObjectSchema;

import com.netflix.hollow.tools.stringifier.HollowRecordStringifier;

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

    public int getCustomerId() {
        return delegate().getCustomerId(ordinal);
    }

    public Integer getCustomerIdBoxed() {
        return delegate().getCustomerIdBoxed(ordinal);
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