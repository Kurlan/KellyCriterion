package com.hollow.prototypes.amazonlocal.voucher.api;

import com.netflix.hollow.api.custom.HollowObjectTypeAPI;
import com.netflix.hollow.core.read.dataaccess.HollowObjectTypeDataAccess;

@SuppressWarnings("all")
public class VoucherTypeAPI extends HollowObjectTypeAPI {

    private final VoucherDelegateLookupImpl delegateLookupImpl;

    public VoucherTypeAPI(VoucherAPI api, HollowObjectTypeDataAccess typeDataAccess) {
        super(api, typeDataAccess, new String[] {
            "voucherId",
            "customerId"
        });
        this.delegateLookupImpl = new VoucherDelegateLookupImpl(this);
    }

    public int getVoucherIdOrdinal(int ordinal) {
        if(fieldIndex[0] == -1)
            return missingDataHandler().handleReferencedOrdinal("Voucher", ordinal, "voucherId");
        return getTypeDataAccess().readOrdinal(ordinal, fieldIndex[0]);
    }

    public StringTypeAPI getVoucherIdTypeAPI() {
        return getAPI().getStringTypeAPI();
    }

    public int getCustomerId(int ordinal) {
        if(fieldIndex[1] == -1)
            return missingDataHandler().handleInt("Voucher", ordinal, "customerId");
        return getTypeDataAccess().readInt(ordinal, fieldIndex[1]);
    }

    public Integer getCustomerIdBoxed(int ordinal) {
        int i;
        if(fieldIndex[1] == -1) {
            i = missingDataHandler().handleInt("Voucher", ordinal, "customerId");
        } else {
            boxedFieldAccessSampler.recordFieldAccess(fieldIndex[1]);
            i = getTypeDataAccess().readInt(ordinal, fieldIndex[1]);
        }
        if(i == Integer.MIN_VALUE)
            return null;
        return Integer.valueOf(i);
    }



    public VoucherDelegateLookupImpl getDelegateLookupImpl() {
        return delegateLookupImpl;
    }

    @Override
    public VoucherAPI getAPI() {
        return (VoucherAPI) api;
    }

}