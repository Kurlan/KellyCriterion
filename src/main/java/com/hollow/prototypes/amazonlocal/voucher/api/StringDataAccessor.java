package com.hollow.prototypes.amazonlocal.voucher.api;

import com.netflix.hollow.api.consumer.HollowConsumer;
import com.netflix.hollow.api.consumer.data.AbstractHollowDataAccessor;
import com.netflix.hollow.core.index.key.PrimaryKey;
import com.netflix.hollow.core.read.engine.HollowReadStateEngine;

@SuppressWarnings("all")
public class StringDataAccessor extends AbstractHollowDataAccessor<HString> {

    public static final String TYPE = "HString";
    private VoucherAPI api;

    public StringDataAccessor(HollowConsumer consumer) {
        super(consumer, TYPE);
        this.api = (VoucherAPI)consumer.getAPI();
    }

    public StringDataAccessor(HollowReadStateEngine rStateEngine, VoucherAPI api) {
        super(rStateEngine, TYPE);
        this.api = api;
    }

    public StringDataAccessor(HollowReadStateEngine rStateEngine, VoucherAPI api, String ... fieldPaths) {
        super(rStateEngine, TYPE, fieldPaths);
        this.api = api;
    }

    public StringDataAccessor(HollowReadStateEngine rStateEngine, VoucherAPI api, PrimaryKey primaryKey) {
        super(rStateEngine, TYPE, primaryKey);
        this.api = api;
    }

    @Override public HString getRecord(int ordinal){
        return api.getHString(ordinal);
    }

}