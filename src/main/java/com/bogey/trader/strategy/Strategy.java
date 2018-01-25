package com.bogey.trader.strategy;

import com.bogey.trader.model.WagerContext;
import com.bogey.trader.simulator.games.GameOfChance;

import java.math.BigDecimal;

public interface Strategy {
    BigDecimal calculateWagerSize(WagerContext wagerContext);
    String getStrategyName();
}
