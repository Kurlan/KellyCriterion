package com.bogey.trader.model;

import com.bogey.trader.strategy.Strategy;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.util.Map;

@Value
@Builder
public class SimulationRunResults {
    private final Integer runs;
    private final Integer trialsPerRun;
    private final Map<Strategy, BigDecimal> meanEndingBankrolls;
    private final Map<Strategy, Integer> numberOfBusts;
    private final Map<Strategy, Integer> evenOrInTheMoneyFrequency;
    private final Map<Strategy, BigDecimal> highestBankroll;

}
