package com.hollow.prototypes.amazonlocal;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.hollow.prototypes.amazonlocal.simulation.VoucherSimulation;
import com.hollow.prototypes.amazonlocal.simulation.amazonlocal.AmazonLocalActualSimulation;
import com.hollow.prototypes.amazonlocal.simulation.hollow.HollowSimulation;
import com.hollow.prototypes.amazonlocal.simulation.linear.LinearSimulation;

public class VoucherSimulatorRunner {
    // private static final int EXPERIMENT_SIZE = 1_000_000;;
    private static final int EXPERIMENT_SIZE = 1000;
    private static final int THREADS_ACROSS_FLEET = 96;

    public static void main(String[] args) {
        List<VoucherSimulation> simulations = ImmutableList.of(
                new LinearSimulation(EXPERIMENT_SIZE),
                new AmazonLocalActualSimulation(EXPERIMENT_SIZE, THREADS_ACROSS_FLEET),
                new HollowSimulation(EXPERIMENT_SIZE, THREADS_ACROSS_FLEET)
        );

        simulations.stream().forEach(voucherSimulation -> {
            voucherSimulation.run();
            System.out.println(voucherSimulation.getResults());
        });
    }
}
