package com.bogey.trader.strategy;

import com.bogey.trader.model.WagerContext;

import java.math.BigDecimal;

public class ConstantBetStrategy implements Strategy {

    private final BigDecimal constantAmount;

    public ConstantBetStrategy(BigDecimal constantAmount) {
        this.constantAmount = constantAmount;
    }

    @Override
    public BigDecimal calculateWagerSize(WagerContext wagerContext) {

        BigDecimal bankroll = wagerContext.getBankroll();
        BigDecimal wagerSize = constantAmount;

        if (wagerSize.compareTo(bankroll) > 0) {
            wagerSize = bankroll;
        }

        return wagerSize;
    }

    @Override
    public String getStrategyName() {
        return "Constant Bet of " + constantAmount + " Strategy";
    }
}
