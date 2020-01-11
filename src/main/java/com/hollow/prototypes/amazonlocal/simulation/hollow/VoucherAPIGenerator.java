package com.hollow.prototypes.amazonlocal.simulation.hollow;

import com.hollow.prototypes.amazonlocal.voucher.Voucher;
import com.netflix.hollow.api.codegen.HollowAPIGenerator;
import com.netflix.hollow.core.write.HollowWriteStateEngine;
import com.netflix.hollow.core.write.objectmapper.HollowObjectMapper;

public class VoucherAPIGenerator {
    public static void main(String[] args) throws Exception {
        HollowWriteStateEngine writeEngine = new HollowWriteStateEngine();
        HollowObjectMapper mapper = new HollowObjectMapper(writeEngine);
        mapper.initializeTypeState(Voucher.class);

        HollowAPIGenerator generator =
                new HollowAPIGenerator.Builder().withAPIClassname("VoucherAPI")
                        .withPackageName("com.hollow.prototypes.amazonlocal.voucher.api")
                        .withDataModel(writeEngine)
                        .withDestination("KellyCriterion/src/main/java")
                        .build();

        generator.generateSourceFiles();
    }
}
