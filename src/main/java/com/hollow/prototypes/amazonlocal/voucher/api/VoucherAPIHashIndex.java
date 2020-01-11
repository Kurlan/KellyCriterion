package com.hollow.prototypes.amazonlocal.voucher.api;

import com.netflix.hollow.api.consumer.HollowConsumer;
import com.netflix.hollow.core.index.HollowHashIndexResult;
import java.util.Collections;
import java.lang.Iterable;
import com.netflix.hollow.api.consumer.index.AbstractHollowHashIndex;
import com.netflix.hollow.api.consumer.data.AbstractHollowOrdinalIterable;


@SuppressWarnings("all")
public class VoucherAPIHashIndex extends AbstractHollowHashIndex<VoucherAPI> {

    public VoucherAPIHashIndex(HollowConsumer consumer, String queryType, String selectFieldPath, String... matchFieldPaths) {
        super(consumer, true, queryType, selectFieldPath, matchFieldPaths);
    }

    public VoucherAPIHashIndex(HollowConsumer consumer, boolean isListenToDataRefresh, String queryType, String selectFieldPath, String... matchFieldPaths) {
        super(consumer, isListenToDataRefresh, queryType, selectFieldPath, matchFieldPaths);
    }

    public Iterable<HString> findStringMatches(Object... keys) {
        HollowHashIndexResult matches = idx.findMatches(keys);
        if(matches == null) return Collections.emptySet();

        return new AbstractHollowOrdinalIterable<HString>(matches.iterator()) {
            public HString getData(int ordinal) {
                return api.getHString(ordinal);
            }
        };
    }

    public Iterable<Voucher> findVoucherMatches(Object... keys) {
        HollowHashIndexResult matches = idx.findMatches(keys);
        if(matches == null) return Collections.emptySet();

        return new AbstractHollowOrdinalIterable<Voucher>(matches.iterator()) {
            public Voucher getData(int ordinal) {
                return api.getVoucher(ordinal);
            }
        };
    }

}