package com.bogey.trader.simulator.games;

import java.math.BigDecimal;

public class CoinFlip implements GameOfChance {

    private final BigDecimal winningProbability;
    private final BigDecimal initialBankroll;

    public CoinFlip(BigDecimal winningProbability, BigDecimal initialBankroll) {
        this.winningProbability = winningProbability;
        this.initialBankroll = initialBankroll;
    }

    public BigDecimal winningProbability() {
        return winningProbability;
    }

    public String name() {
        return "Coin flip with winning probability of " + winningProbability;
    }

    public BigDecimal initialBankroll() {
        return initialBankroll;
    }

    @Override
    public BigDecimal minBetAmount() {
        return BigDecimal.ONE;
    }

    @Override
    public BigDecimal maxBetAmount() {
        return BigDecimal.valueOf(1000);
    }

    public BigDecimal payoff() {
        return BigDecimal.ONE;
    }
}
