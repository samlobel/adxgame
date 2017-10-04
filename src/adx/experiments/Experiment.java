package adx.experiments;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

import adx.exceptions.AdXException;
import adx.sim.Simulator;
import adx.sim.agents.SimAgent;
import adx.statistics.Statistics;
import adx.util.Logging;

/**
 * Class that encapsulates the logic of an experiment, i.e., a run of multiple games.
 * 
 * @author Enrique Areyan Viqueira
 */
public class Experiment {
  /**
   * name of csv file where results are going to be stored.
   */
  private final String csvFileName;
  /**
   * the list of agents in the experiment.
   */
  private final List<SimAgent> simAgents;
  /**
   * how many games (trials) of the experiment.
   */
  private final int numberOfGames;
  /**
   * number of impressions.
   */
  private final int numberOfImpressions;
  /**
   * the reserve price.
   */
  private final double reserve;
  /**
   * The demand discount factor.
   */
  private double demandDiscountFactor = 1.0;

  /**
   * Constructor.
   * 
   * @param csvFileName
   * @param simAgents
   * @param numberOfGames
   * @param reserve
   */
  public Experiment(String csvFileName, List<SimAgent> simAgents, int numberOfGames, double reserve, int numberOfImpressions) {
    this.csvFileName = csvFileName;
    this.simAgents = simAgents;
    this.numberOfGames = numberOfGames;
    this.numberOfImpressions = numberOfImpressions;
    this.reserve = reserve;
  }

  /**
   * Setter.
   * 
   * @param demandDiscountFactor
   */
  public void setDemandDiscountFactor(double demandDiscountFactor) {
    this.demandDiscountFactor = demandDiscountFactor;
  }

  /**
   * Runs an experiments and saves the results to csv file.
   * 
   * @param csvFileName
   * @param simAgents
   * @param numberOfGames
   * @throws AdXException
   * @throws FileNotFoundException
   * @throws UnsupportedEncodingException
   */
  public void runExperiment() throws AdXException, FileNotFoundException, UnsupportedEncodingException {
    String ret = "";
    Logging.log("[experiment] running with parameters:" + "\n\t\t reserve = " + this.reserve + "\n\t\t numberOfImpressions = " + this.numberOfImpressions
        + "\n\t\t demandDiscountFactor = " + this.demandDiscountFactor);
    for (int g = 0; g < this.numberOfGames; g++) {
      // Run simulator.
      Simulator simulator = new Simulator(this.simAgents, this.reserve, this.numberOfImpressions, this.demandDiscountFactor);
      // Get statistics.
      Statistics statistics = simulator.run();
      ret += statistics.oneLineSummary(1, g);
      // Logging.log("Result : \n" + statistics.oneLineSummary(1, g));
    }
    // Logging.log(ret);
    // Logging.log("Save file to " + this.csvFileName);
    PrintWriter writer = new PrintWriter(this.csvFileName, "UTF-8");
    writer.println(ret);
    writer.close();
  }

}
