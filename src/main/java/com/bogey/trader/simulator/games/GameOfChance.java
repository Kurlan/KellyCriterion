package com.bogey.trader.simulator.games;

import java.math.BigDecimal;

public interface GameOfChance {
    BigDecimal winningProbability();
    BigDecimal payoff();
    String name();
    BigDecimal initialBankroll();
    BigDecimal minBetAmount();
    BigDecimal maxBetAmount();
}
