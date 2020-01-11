package com.hollow.prototypes.amazonlocal.simulation;

import com.hollow.prototypes.amazonlocal.simulation.result.SimulationResult;

public interface VoucherSimulation {
    void run();
    SimulationResult getResults();
}
