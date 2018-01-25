package com.bogey.trader.simulator;

import com.bogey.trader.model.SimulationRunResults;
import com.bogey.trader.simulator.games.CoinFlip;
import com.bogey.trader.simulator.games.GameOfChance;
import com.bogey.trader.strategy.ConstantBetStrategy;
import com.bogey.trader.strategy.ConstantPercentageBetStrategy;
import com.bogey.trader.strategy.Strategy;
import com.google.common.collect.ImmutableList;

import java.math.BigDecimal;
import java.util.List;

public class SimulationRunner {
    public static void main(String[] args) {

        GameOfChance gameOfChance = new CoinFlip(BigDecimal.valueOf(.6), BigDecimal.valueOf(100));
        ConstantBetStrategy constantBetStrategy = new ConstantBetStrategy(BigDecimal.valueOf(2));
        ConstantPercentageBetStrategy constantThreeQuarters = new ConstantPercentageBetStrategy(BigDecimal.valueOf(.75 ));
        ConstantPercentageBetStrategy constantHalf = new ConstantPercentageBetStrategy(BigDecimal.valueOf(.5 ));
        ConstantPercentageBetStrategy constantTwenty = new ConstantPercentageBetStrategy(BigDecimal.valueOf(.2));
        List<Strategy> bettingStrategies = ImmutableList.of(constantBetStrategy, constantThreeQuarters, constantHalf, constantTwenty);
        Integer trialsPerRun = 300;
        Integer runs = 1000;

        Simulator simulator = new Simulator(gameOfChance, bettingStrategies, trialsPerRun, runs);

        SimulationRunResults simulationRunResults = simulator.run();

        System.out.println("Results for simulation of - " + gameOfChance.name());
        System.out.println("Runs: " + runs);
        System.out.println("Trials per Run: " + trialsPerRun);
        for (Strategy strategy : bettingStrategies) {
            Integer numberOfBusts = simulationRunResults.getNumberOfBusts().get(strategy);
            BigDecimal bustPercentage = BigDecimal.valueOf(numberOfBusts).divide(BigDecimal.valueOf(runs), 2, BigDecimal.ROUND_HALF_EVEN);
            Integer evenOrInTheMoneyFrequency = simulationRunResults.getEvenOrInTheMoneyFrequency().get(strategy);
            BigDecimal evenOrInTheMoneyPercentage = BigDecimal.valueOf(evenOrInTheMoneyFrequency).divide(BigDecimal.valueOf(runs), 2, BigDecimal.ROUND_HALF_EVEN);

            System.out.println("--------");
            System.out.println("Results for " + strategy.getStrategyName());
            System.out.println("Mean ending bankroll: " + simulationRunResults.getMeanEndingBankrolls().get(strategy));
            System.out.println("Highest bankroll: " + simulationRunResults.getHighestBankroll().get(strategy));
            System.out.println("Number of busts: " + numberOfBusts + " (p=" + bustPercentage + ")");
            System.out.println("Even money or better outcomes: " + evenOrInTheMoneyFrequency + " (p=" + evenOrInTheMoneyPercentage + ")");
        }

    }
}
