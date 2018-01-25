package com.bogey.trader.model;

import com.bogey.trader.simulator.games.GameOfChance;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.util.List;

@Value
@Builder(toBuilder = true)
public class WagerContext {
    private final List<Trial> priorTrials;
    private final GameOfChance gameOfChance;
    private final BigDecimal bankroll;
}
