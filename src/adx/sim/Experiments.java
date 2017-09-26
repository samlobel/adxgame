package adx.sim;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import adx.exceptions.AdXException;
import adx.sim.agents.SimAgent;
import adx.sim.agents.SimpleSimAgent;
import adx.sim.agents.waterfall.WFAgent;
import adx.statistics.Statistics;
import adx.util.Logging;
import adx.util.Pair;
import adx.util.Parameters;

/**
 * Class to handle experiments in the simulator.
 * 
 * @author Enrique Areyan Viqueira
 */
public class Experiments {

  /**
   * Given a reserve price and number of Agents, run the one-day game, return
   * total revenue.
   * 
   * @param numberOfAgents
   * @param reserve
   * @return
   * @throws AdXException
   */
  public static double runExperiment(String typeOfAgents, int numberOfAgents, double reserve) throws AdXException {
    // Create the agents.
    List<SimAgent> simAgents = new ArrayList<SimAgent>();
    for (int i = 0; i < numberOfAgents; i++) {
      switch (typeOfAgents) {
      case "OneDayAgent":
        simAgents.add(new SimpleSimAgent("OneDayAgent" + i));
        break;
      case "WEAgent":
        //simAgents.add(new WEAgent("WEAgent" + i, reserve));
        break;
      case "WFAgent":
        simAgents.add(new WFAgent("WFAgent" + i));
        break;
      default:
        throw new AdXException("Unknow type of agent: " + typeOfAgents);
      }
    }

    // Create the simulator and run it.
    Simulator simulator = new Simulator(simAgents, reserve, Parameters.POPULATION_SIZE);
    Statistics statistics = simulator.run();

    // Compute total Profit of the simulation.
    double totalProfit = 0.0;
    for (SimAgent x : simAgents) {
      for (Pair<Integer, Double> y : statistics.getStatisticsAds().getDailySummaryStatistic(1, x.getName()).values()) {
        totalProfit += y.getElement2();
      }
    }
    return totalProfit;
  }

  /**
   * Main Method.
   * 
   * @param args
   * @throws AdXException
   */
  public static void main(String[] args) throws AdXException {
    if (args.length != 4) {
      Logging.log("Need four arguments: (0) types of agents, (1) number of agents, (2) number of runs, and (3) location of output file");
    } else {
      String typeOfAgents = args[0];
      int numberOfAgents = Integer.parseInt(args[1]);
      int sampleSize = Integer.parseInt(args[2]);
      String logFile = args[3];
      Logging.log("Run experiment with " + numberOfAgents + " many " + typeOfAgents + " agents, run " + sampleSize + " trials and save results to: " + logFile);
      for (double r = 0; r <= 4.01; r += 0.01) {
        DescriptiveStatistics s = new DescriptiveStatistics();
        for (int i = 0; i < sampleSize; i++) {
          double revenue = Experiments.runExperiment(typeOfAgents, numberOfAgents, r);
          s.addValue(revenue);
        }
        Logging.log(r + "," + s.getMean() + "," + (s.getStandardDeviation() / Math.sqrt(sampleSize)) + "\n", logFile);
      }
    }
  }

}
