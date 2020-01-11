package com.hollow.prototypes.amazonlocal.voucher.api;

import com.netflix.hollow.api.objects.delegate.HollowObjectAbstractDelegate;
import com.netflix.hollow.core.read.dataaccess.HollowObjectTypeDataAccess;
import com.netflix.hollow.core.schema.HollowObjectSchema;
import com.netflix.hollow.api.custom.HollowTypeAPI;
import com.netflix.hollow.api.objects.delegate.HollowCachedDelegate;

@SuppressWarnings("all")
public class VoucherDelegateCachedImpl extends HollowObjectAbstractDelegate implements HollowCachedDelegate, VoucherDelegate {

    private final int voucherIdOrdinal;
    private final Integer customerId;
    private VoucherTypeAPI typeAPI;

    public VoucherDelegateCachedImpl(VoucherTypeAPI typeAPI, int ordinal) {
        this.voucherIdOrdinal = typeAPI.getVoucherIdOrdinal(ordinal);
        this.customerId = typeAPI.getCustomerIdBoxed(ordinal);
        this.typeAPI = typeAPI;
    }

    public int getVoucherIdOrdinal(int ordinal) {
        return voucherIdOrdinal;
    }

    public int getCustomerId(int ordinal) {
        if(customerId == null)
            return Integer.MIN_VALUE;
        return customerId.intValue();
    }

    public Integer getCustomerIdBoxed(int ordinal) {
        return customerId;
    }

    @Override
    public HollowObjectSchema getSchema() {
        return typeAPI.getTypeDataAccess().getSchema();
    }

    @Override
    public HollowObjectTypeDataAccess getTypeDataAccess() {
        return typeAPI.getTypeDataAccess();
    }

    public VoucherTypeAPI getTypeAPI() {
        return typeAPI;
    }

    public void updateTypeAPI(HollowTypeAPI typeAPI) {
        this.typeAPI = (VoucherTypeAPI) typeAPI;
    }

}