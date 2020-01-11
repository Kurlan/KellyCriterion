package com.hollow.prototypes.amazonlocal.voucher.api;

import com.netflix.hollow.api.consumer.HollowConsumer;
import com.netflix.hollow.api.consumer.index.AbstractHollowUniqueKeyIndex;
import com.netflix.hollow.core.schema.HollowObjectSchema;

@SuppressWarnings("all")
public class VoucherPrimaryKeyIndex extends AbstractHollowUniqueKeyIndex<VoucherAPI, Voucher> {

    public VoucherPrimaryKeyIndex(HollowConsumer consumer) {
        this(consumer, true);
    }

    public VoucherPrimaryKeyIndex(HollowConsumer consumer, boolean isListenToDataRefresh) {
        this(consumer, isListenToDataRefresh, ((HollowObjectSchema)consumer.getStateEngine().getNonNullSchema("Voucher")).getPrimaryKey().getFieldPaths());
    }

    public VoucherPrimaryKeyIndex(HollowConsumer consumer, String... fieldPaths) {
        this(consumer, true, fieldPaths);
    }

    public VoucherPrimaryKeyIndex(HollowConsumer consumer, boolean isListenToDataRefresh, String... fieldPaths) {
        super(consumer, "Voucher", isListenToDataRefresh, fieldPaths);
    }

    public Voucher findMatch(Object... keys) {
        int ordinal = idx.getMatchingOrdinal(keys);
        if(ordinal == -1)
            return null;
        return api.getVoucher(ordinal);
    }

}