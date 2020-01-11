package com.hollow.prototypes.amazonlocal.voucher.api;

import com.netflix.hollow.api.client.HollowAPIFactory;
import com.netflix.hollow.api.custom.HollowAPI;
import com.netflix.hollow.api.objects.provider.HollowFactory;
import com.netflix.hollow.core.read.dataaccess.HollowDataAccess;
import java.util.Collections;
import java.util.Set;

@SuppressWarnings("all")
public class VoucherAPIFactory implements HollowAPIFactory {

    private final Set<String> cachedTypes;

    public VoucherAPIFactory() {
        this(Collections.<String>emptySet());
    }

    public VoucherAPIFactory(Set<String> cachedTypes) {
        this.cachedTypes = cachedTypes;
    }

    @Override
    public HollowAPI createAPI(HollowDataAccess dataAccess) {
        return new VoucherAPI(dataAccess, cachedTypes);
    }

    @Override
    public HollowAPI createAPI(HollowDataAccess dataAccess, HollowAPI previousCycleAPI) {
        if (!(previousCycleAPI instanceof VoucherAPI)) {
            throw new ClassCastException(previousCycleAPI.getClass() + " not instance of VoucherAPI");        }
        return new VoucherAPI(dataAccess, cachedTypes, Collections.<String, HollowFactory<?>>emptyMap(), (VoucherAPI) previousCycleAPI);
    }

}