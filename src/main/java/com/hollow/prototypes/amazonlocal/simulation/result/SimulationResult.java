package com.hollow.prototypes.amazonlocal.simulation.result;

import java.time.Instant;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SimulationResult {
    private final String description;
    private Instant startTime;
    private Instant endtime;
    private Long simulationDurationSeconds;
    private long searchTries;
    private long assignmentTries;
}
