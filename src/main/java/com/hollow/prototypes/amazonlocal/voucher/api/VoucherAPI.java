package com.hollow.prototypes.amazonlocal.voucher.api;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.Map;
import com.netflix.hollow.api.consumer.HollowConsumerAPI;
import com.netflix.hollow.api.custom.HollowAPI;
import com.netflix.hollow.core.read.dataaccess.HollowDataAccess;
import com.netflix.hollow.core.read.dataaccess.HollowTypeDataAccess;
import com.netflix.hollow.core.read.dataaccess.HollowObjectTypeDataAccess;
import com.netflix.hollow.core.read.dataaccess.HollowListTypeDataAccess;
import com.netflix.hollow.core.read.dataaccess.HollowSetTypeDataAccess;
import com.netflix.hollow.core.read.dataaccess.HollowMapTypeDataAccess;
import com.netflix.hollow.core.read.dataaccess.missing.HollowObjectMissingDataAccess;
import com.netflix.hollow.core.read.dataaccess.missing.HollowListMissingDataAccess;
import com.netflix.hollow.core.read.dataaccess.missing.HollowSetMissingDataAccess;
import com.netflix.hollow.core.read.dataaccess.missing.HollowMapMissingDataAccess;
import com.netflix.hollow.api.objects.provider.HollowFactory;
import com.netflix.hollow.api.objects.provider.HollowObjectProvider;
import com.netflix.hollow.api.objects.provider.HollowObjectCacheProvider;
import com.netflix.hollow.api.objects.provider.HollowObjectFactoryProvider;
import com.netflix.hollow.api.sampling.HollowObjectCreationSampler;
import com.netflix.hollow.api.sampling.HollowSamplingDirector;
import com.netflix.hollow.api.sampling.SampleResult;
import com.netflix.hollow.core.util.AllHollowRecordCollection;

@SuppressWarnings("all")
public class VoucherAPI extends HollowAPI  {

    private final HollowObjectCreationSampler objectCreationSampler;

    private final StringTypeAPI stringTypeAPI;
    private final VoucherTypeAPI voucherTypeAPI;

    private final HollowObjectProvider stringProvider;
    private final HollowObjectProvider voucherProvider;

    public VoucherAPI(HollowDataAccess dataAccess) {
        this(dataAccess, Collections.<String>emptySet());
    }

    public VoucherAPI(HollowDataAccess dataAccess, Set<String> cachedTypes) {
        this(dataAccess, cachedTypes, Collections.<String, HollowFactory<?>>emptyMap());
    }

    public VoucherAPI(HollowDataAccess dataAccess, Set<String> cachedTypes, Map<String, HollowFactory<?>> factoryOverrides) {
        this(dataAccess, cachedTypes, factoryOverrides, null);
    }

    public VoucherAPI(HollowDataAccess dataAccess, Set<String> cachedTypes, Map<String, HollowFactory<?>> factoryOverrides, VoucherAPI previousCycleAPI) {
        super(dataAccess);
        HollowTypeDataAccess typeDataAccess;
        HollowFactory factory;

        objectCreationSampler = new HollowObjectCreationSampler("String","Voucher");

        typeDataAccess = dataAccess.getTypeDataAccess("String");
        if(typeDataAccess != null) {
            stringTypeAPI = new StringTypeAPI(this, (HollowObjectTypeDataAccess)typeDataAccess);
        } else {
            stringTypeAPI = new StringTypeAPI(this, new HollowObjectMissingDataAccess(dataAccess, "String"));
        }
        addTypeAPI(stringTypeAPI);
        factory = factoryOverrides.get("String");
        if(factory == null)
            factory = new StringHollowFactory();
        if(cachedTypes.contains("String")) {
            HollowObjectCacheProvider previousCacheProvider = null;
            if(previousCycleAPI != null && (previousCycleAPI.stringProvider instanceof HollowObjectCacheProvider))
                previousCacheProvider = (HollowObjectCacheProvider) previousCycleAPI.stringProvider;
            stringProvider = new HollowObjectCacheProvider(typeDataAccess, stringTypeAPI, factory, previousCacheProvider);
        } else {
            stringProvider = new HollowObjectFactoryProvider(typeDataAccess, stringTypeAPI, factory);
        }

        typeDataAccess = dataAccess.getTypeDataAccess("Voucher");
        if(typeDataAccess != null) {
            voucherTypeAPI = new VoucherTypeAPI(this, (HollowObjectTypeDataAccess)typeDataAccess);
        } else {
            voucherTypeAPI = new VoucherTypeAPI(this, new HollowObjectMissingDataAccess(dataAccess, "Voucher"));
        }
        addTypeAPI(voucherTypeAPI);
        factory = factoryOverrides.get("Voucher");
        if(factory == null)
            factory = new VoucherHollowFactory();
        if(cachedTypes.contains("Voucher")) {
            HollowObjectCacheProvider previousCacheProvider = null;
            if(previousCycleAPI != null && (previousCycleAPI.voucherProvider instanceof HollowObjectCacheProvider))
                previousCacheProvider = (HollowObjectCacheProvider) previousCycleAPI.voucherProvider;
            voucherProvider = new HollowObjectCacheProvider(typeDataAccess, voucherTypeAPI, factory, previousCacheProvider);
        } else {
            voucherProvider = new HollowObjectFactoryProvider(typeDataAccess, voucherTypeAPI, factory);
        }

    }

    public void detachCaches() {
        if(stringProvider instanceof HollowObjectCacheProvider)
            ((HollowObjectCacheProvider)stringProvider).detach();
        if(voucherProvider instanceof HollowObjectCacheProvider)
            ((HollowObjectCacheProvider)voucherProvider).detach();
    }

    public StringTypeAPI getStringTypeAPI() {
        return stringTypeAPI;
    }
    public VoucherTypeAPI getVoucherTypeAPI() {
        return voucherTypeAPI;
    }
    public Collection<HString> getAllHString() {
        return new AllHollowRecordCollection<HString>(getDataAccess().getTypeDataAccess("String").getTypeState()) {
            protected HString getForOrdinal(int ordinal) {
                return getHString(ordinal);
            }
        };
    }
    public HString getHString(int ordinal) {
        objectCreationSampler.recordCreation(0);
        return (HString)stringProvider.getHollowObject(ordinal);
    }
    public Collection<Voucher> getAllVoucher() {
        return new AllHollowRecordCollection<Voucher>(getDataAccess().getTypeDataAccess("Voucher").getTypeState()) {
            protected Voucher getForOrdinal(int ordinal) {
                return getVoucher(ordinal);
            }
        };
    }
    public Voucher getVoucher(int ordinal) {
        objectCreationSampler.recordCreation(1);
        return (Voucher)voucherProvider.getHollowObject(ordinal);
    }
    public void setSamplingDirector(HollowSamplingDirector director) {
        super.setSamplingDirector(director);
        objectCreationSampler.setSamplingDirector(director);
    }

    public Collection<SampleResult> getObjectCreationSamplingResults() {
        return objectCreationSampler.getSampleResults();
    }

}
