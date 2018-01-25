package com.bogey.trader.model;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class Trial {
    private final Integer trialNumber;
    private final BigDecimal priorBankroll;
    private final BigDecimal currentBankroll;
    private final BigDecimal amountWon;
    private final Boolean winner;
    private final BigDecimal wager;
}
