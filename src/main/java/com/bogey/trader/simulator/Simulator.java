package com.bogey.trader.simulator;

import com.bogey.trader.model.SimulationRunResults;
import com.bogey.trader.model.Trial;
import com.bogey.trader.model.WagerContext;
import com.bogey.trader.simulator.games.GameOfChance;
import com.bogey.trader.strategy.Strategy;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Simulator {

    private final List<Strategy> strategyList;
    private final GameOfChance gameOfChance;
    private final Integer trialsPerRun;
    private final Integer runs;

    public Simulator(GameOfChance gameOfChance, List<Strategy> strategyList, Integer trialsPerRun, Integer runs) {
        this.strategyList = strategyList;
        this.gameOfChance = gameOfChance;
        this.trialsPerRun = trialsPerRun;
        this.runs = runs;
    }

    public SimulationRunResults run() {

        Map<Strategy, Integer> numberOfBusts = new HashMap<>();
        Map<Strategy, BigDecimal> sumEndingBankrolls = new HashMap<>();
        Map<Strategy, BigDecimal> meanEndingBankrolls = new HashMap<>();
        Map<Strategy, BigDecimal> highestBankroll = new HashMap<>();
        Map<Strategy, Integer> evenOrInTheMoneyFrequency = new HashMap<>();

        for (Strategy strategy : strategyList) {
            numberOfBusts.put(strategy, 0);
            sumEndingBankrolls.put(strategy, BigDecimal.ZERO);
            highestBankroll.put(strategy, BigDecimal.ZERO);
            evenOrInTheMoneyFrequency.put(strategy, 0);
        }

        for (int run = 0; run < runs; run++) {
            WagerContext initialContext = WagerContext
                    .builder()
                    .bankroll(gameOfChance.initialBankroll())
                    .gameOfChance(gameOfChance)
                    .priorTrials(new LinkedList<>())
                    .build();
            Map<Strategy, WagerContext> priorContexts = new HashMap<>();
            for (Strategy strategy : strategyList) {
                priorContexts.put(strategy, initialContext);
            }



            for (int trial = 0; trial < trialsPerRun ; trial++) {
                //System.out.println("Starting trial: " + trial + " run: " + run);
                BigDecimal outcome = BigDecimal.valueOf(Math.random());
                boolean success = outcome.compareTo(gameOfChance.winningProbability()) <= 0;

                for (Map.Entry<Strategy, WagerContext> strategyContextEntry : priorContexts.entrySet()) {
                    WagerContext wagerContext = strategyContextEntry.getValue();
                    Strategy strategy = strategyContextEntry.getKey();
                    BigDecimal wagerSize = strategy.calculateWagerSize(wagerContext);
                    BigDecimal bankroll = wagerContext.getBankroll();
                    if (wagerSize.compareTo(bankroll) > 0) {
                        throw new RuntimeException("Wager larger than bankroll.  WagerSize: " + wagerSize + " bankroll: " + bankroll);
                    }

                    BigDecimal amountWon = success? wagerSize.multiply(gameOfChance.payoff()) : wagerSize.negate();
                    BigDecimal updatedBankroll = bankroll.add(amountWon);
                    Trial currentTrial = Trial
                            .builder()
                            .amountWon(amountWon)
                            .priorBankroll(bankroll)
                            .currentBankroll(updatedBankroll)
                            .trialNumber(trial)
                            .wager(wagerSize)
                            .winner(success)
                            .build();

                    //System.out.println("Strategy: " + strategy.getStrategyName() + " trial: " +  currentTrial);
                    wagerContext.getPriorTrials().add(currentTrial);
                    wagerContext = wagerContext.toBuilder().bankroll(updatedBankroll).build();
                    priorContexts.put(strategy, wagerContext);

                }
            }

            for (Strategy strategy : strategyList) {
                WagerContext lastContext = priorContexts.get(strategy);
                if (lastContext.getBankroll().compareTo(gameOfChance.minBetAmount()) < 0) {
                    numberOfBusts.put(strategy, numberOfBusts.get(strategy) + 1);
                }

                sumEndingBankrolls.put(strategy, sumEndingBankrolls.get(strategy).add(lastContext.getBankroll()));
                if (lastContext.getBankroll().compareTo(highestBankroll.get(strategy)) > 0) {
                    highestBankroll.put(strategy, lastContext.getBankroll());
                }

                if (lastContext.getBankroll().compareTo(gameOfChance.initialBankroll()) >= 0) {
                    evenOrInTheMoneyFrequency.put(strategy, evenOrInTheMoneyFrequency.get(strategy) + 1);
                }
            }

        }

        for (Strategy strategy : strategyList) {
            meanEndingBankrolls.put(strategy, sumEndingBankrolls.get(strategy).divide(BigDecimal.valueOf(runs), 2, BigDecimal.ROUND_HALF_EVEN));
        }

        return SimulationRunResults
                .builder()
                .runs(runs)
                .trialsPerRun(trialsPerRun)
                .numberOfBusts(numberOfBusts)
                .meanEndingBankrolls(meanEndingBankrolls)
                .highestBankroll(highestBankroll)
                .evenOrInTheMoneyFrequency(evenOrInTheMoneyFrequency)
                .strategies(strategyList)
                .gameOfChance(gameOfChance)
                .build();
    }

}
