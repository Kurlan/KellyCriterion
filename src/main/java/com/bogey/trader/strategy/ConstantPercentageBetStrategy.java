package com.bogey.trader.strategy;

import com.bogey.trader.model.WagerContext;

import java.math.BigDecimal;

public class ConstantPercentageBetStrategy implements Strategy {

    public final BigDecimal percentage;

    public ConstantPercentageBetStrategy( BigDecimal percentage) {
        this.percentage = percentage;
    }

    @Override
    public BigDecimal calculateWagerSize(WagerContext wagerContext) {
        BigDecimal bankroll = wagerContext.getBankroll();
        BigDecimal wagerSize = bankroll.multiply(percentage).setScale(2, BigDecimal.ROUND_HALF_EVEN);
        BigDecimal minBet = wagerContext.getGameOfChance().minBetAmount();
        BigDecimal maxBet = wagerContext.getGameOfChance().maxBetAmount();

        if (wagerSize.compareTo(minBet) < 0) {
            if (bankroll.compareTo(minBet) < 0) {
                return BigDecimal.ZERO;
            }
            return minBet;
        }

        if (wagerSize.compareTo(maxBet) > 0) {
            return maxBet;
        }
        return wagerSize;
    }

    @Override
    public String getStrategyName() {
        return "Constant p=" + percentage + " of bankroll";
    }
}
