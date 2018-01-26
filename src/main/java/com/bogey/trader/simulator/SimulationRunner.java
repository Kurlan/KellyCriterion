package com.bogey.trader.simulator;

import com.bogey.trader.model.SimulationRunResults;
import com.bogey.trader.simulator.games.CoinFlip;
import com.bogey.trader.simulator.games.GameOfChance;
import com.bogey.trader.strategy.ConstantBetStrategy;
import com.bogey.trader.strategy.ConstantPercentageBetStrategy;
import com.bogey.trader.strategy.Strategy;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class SimulationRunner {
    public static void main(String[] args) {

        GameOfChance gameOfChance = new CoinFlip(BigDecimal.valueOf(.6), BigDecimal.valueOf(100));

        //List<Strategy> bettingStrategies = generateBettingStrategies();
        List<Strategy> bettingStrategies = generateGraduatedBettingStrategies(100);

        Integer trialsPerRun = 300;
        Integer runs = 10000;

        Simulator simulator = new Simulator(gameOfChance, bettingStrategies, trialsPerRun, runs);

        SimulationRunResults simulationRunResults = simulator.run();

        printHumanReadable(simulationRunResults);
        printCSV(simulationRunResults);

    }

    private static List<Strategy> generateGraduatedBettingStrategies(int steps) {
        ImmutableList.Builder<Strategy> strategies = ImmutableList.builder();

        BigDecimal increment = BigDecimal.ONE.divide(BigDecimal.valueOf(steps), 2, BigDecimal.ROUND_HALF_EVEN);
        for (int i = 1; i <= steps; i++) {
            strategies.add(new ConstantPercentageBetStrategy(increment.multiply(BigDecimal.valueOf(i))));
        }
        return strategies.build();
    }

    private static List<Strategy> generateBettingStrategies() {
        ConstantBetStrategy constantBetStrategy = new ConstantBetStrategy(BigDecimal.valueOf(2));
        ConstantPercentageBetStrategy constantThreeQuarters = new ConstantPercentageBetStrategy(BigDecimal.valueOf(.75 ));
        ConstantPercentageBetStrategy constantHalf = new ConstantPercentageBetStrategy(BigDecimal.valueOf(.5 ));
        ConstantPercentageBetStrategy constantTwentyFive = new ConstantPercentageBetStrategy(BigDecimal.valueOf(.25));
        ConstantPercentageBetStrategy constantTwenty = new ConstantPercentageBetStrategy(BigDecimal.valueOf(.2));
        return ImmutableList.of(constantBetStrategy, constantThreeQuarters, constantHalf, constantTwentyFive, constantTwenty);
    }

    private static void printCSV(SimulationRunResults simulationRunResults) {
        List<String> header = new ArrayList();
        header.add("Strategy");
        header.add("Mean ending bankroll");
        header.add("Bust probability");
        header.add("Highest bankroll");
        header.add("Even money or better percentage");

        System.out.println(Joiner.on(",").join(header));

        for (Strategy strategy : simulationRunResults.getStrategies()) {
            List<String> fields = new ArrayList<>();
            Integer numberOfBusts = simulationRunResults.getNumberOfBusts().get(strategy);
            BigDecimal bustPercentage = BigDecimal.valueOf(numberOfBusts).divide(BigDecimal.valueOf(simulationRunResults.getRuns()), 2, BigDecimal.ROUND_HALF_EVEN);
            Integer evenOrInTheMoneyFrequency = simulationRunResults.getEvenOrInTheMoneyFrequency().get(strategy);
            BigDecimal evenOrInTheMoneyPercentage = BigDecimal.valueOf(evenOrInTheMoneyFrequency).divide(BigDecimal.valueOf(simulationRunResults.getRuns()), 2, BigDecimal.ROUND_HALF_EVEN);

            fields.add(strategy.getStrategyName());
            fields.add(simulationRunResults.getMeanEndingBankrolls().get(strategy).toString());
            fields.add(bustPercentage.toString());
            fields.add(simulationRunResults.getHighestBankroll().get(strategy).toString());
            fields.add(evenOrInTheMoneyPercentage.toString());

            System.out.println(Joiner.on(",").join(fields));
        }


    }

    private static void printHumanReadable(SimulationRunResults simulationRunResults) {
        System.out.println("Results for simulation of - " + simulationRunResults.getGameOfChance().name());
        System.out.println("Runs: " + simulationRunResults.getRuns());
        System.out.println("Trials per Run: " + simulationRunResults.getTrialsPerRun());
        for (Strategy strategy : simulationRunResults.getStrategies()) {
            Integer numberOfBusts = simulationRunResults.getNumberOfBusts().get(strategy);
            BigDecimal bustPercentage = BigDecimal.valueOf(numberOfBusts).divide(BigDecimal.valueOf(simulationRunResults.getRuns()), 2, BigDecimal.ROUND_HALF_EVEN);
            Integer evenOrInTheMoneyFrequency = simulationRunResults.getEvenOrInTheMoneyFrequency().get(strategy);
            BigDecimal evenOrInTheMoneyPercentage = BigDecimal.valueOf(evenOrInTheMoneyFrequency).divide(BigDecimal.valueOf(simulationRunResults.getRuns()), 2, BigDecimal.ROUND_HALF_EVEN);

            System.out.println("--------");
            System.out.println("Results for " + strategy.getStrategyName());
            System.out.println("Mean ending bankroll: " + simulationRunResults.getMeanEndingBankrolls().get(strategy));
            System.out.println("Highest bankroll: " + simulationRunResults.getHighestBankroll().get(strategy));
            System.out.println("Number of busts: " + numberOfBusts + " (p=" + bustPercentage + ")");
            System.out.println("Even money or better outcomes: " + evenOrInTheMoneyFrequency + " (p=" + evenOrInTheMoneyPercentage + ")");
        }



    }
}
