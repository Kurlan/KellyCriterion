package com.hollow.prototypes.amazonlocal.voucher.api;

import com.netflix.hollow.api.objects.delegate.HollowObjectAbstractDelegate;
import com.netflix.hollow.core.read.dataaccess.HollowObjectTypeDataAccess;
import com.netflix.hollow.core.schema.HollowObjectSchema;

@SuppressWarnings("all")
public class VoucherDelegateLookupImpl extends HollowObjectAbstractDelegate implements VoucherDelegate {

    private final VoucherTypeAPI typeAPI;

    public VoucherDelegateLookupImpl(VoucherTypeAPI typeAPI) {
        this.typeAPI = typeAPI;
    }

    public int getVoucherIdOrdinal(int ordinal) {
        return typeAPI.getVoucherIdOrdinal(ordinal);
    }

    public int getCustomerIdOrdinal(int ordinal) {
        return typeAPI.getCustomerIdOrdinal(ordinal);
    }

    public VoucherTypeAPI getTypeAPI() {
        return typeAPI;
    }

    @Override
    public HollowObjectSchema getSchema() {
        return typeAPI.getTypeDataAccess().getSchema();
    }

    @Override
    public HollowObjectTypeDataAccess getTypeDataAccess() {
        return typeAPI.getTypeDataAccess();
    }

}