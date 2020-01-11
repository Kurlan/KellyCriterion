package com.hollow.prototypes.amazonlocal.voucher.api;

import com.netflix.hollow.api.consumer.HollowConsumer;
import com.netflix.hollow.api.consumer.data.AbstractHollowDataAccessor;
import com.netflix.hollow.core.index.key.PrimaryKey;
import com.netflix.hollow.core.read.engine.HollowReadStateEngine;

@SuppressWarnings("all")
public class VoucherDataAccessor extends AbstractHollowDataAccessor<Voucher> {

    public static final String TYPE = "Voucher";
    private VoucherAPI api;

    public VoucherDataAccessor(HollowConsumer consumer) {
        super(consumer, TYPE);
        this.api = (VoucherAPI)consumer.getAPI();
    }

    public VoucherDataAccessor(HollowReadStateEngine rStateEngine, VoucherAPI api) {
        super(rStateEngine, TYPE);
        this.api = api;
    }

    public VoucherDataAccessor(HollowReadStateEngine rStateEngine, VoucherAPI api, String ... fieldPaths) {
        super(rStateEngine, TYPE, fieldPaths);
        this.api = api;
    }

    public VoucherDataAccessor(HollowReadStateEngine rStateEngine, VoucherAPI api, PrimaryKey primaryKey) {
        super(rStateEngine, TYPE, primaryKey);
        this.api = api;
    }

    @Override public Voucher getRecord(int ordinal){
        return api.getVoucher(ordinal);
    }

}