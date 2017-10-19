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
   * Name of the directory where to save files.
   */
  private final String resultsDirectory;
  /**
   * Name of file where results are going to be stored.
   */
  private final String resultsFileName;
  /**
   * The list of agents in the experiment.
   */
  private final List<SimAgent> simAgents;
  /**
   * How many games (trials) of the experiment.
   */
  private final int numberOfGames;
  /**
   * Number of impressions.
   */
  private final int numberOfImpressions;
  /**
   * The reserve price.
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
  public Experiment(String resultsDirectory, String csvFileName, List<SimAgent> simAgents, int numberOfGames, double reserve, int numberOfImpressions) {
    this.resultsDirectory = resultsDirectory;
    this.resultsFileName = csvFileName;
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
   * @param resultsFileName
   * @param simAgents
   * @param numberOfGames
   * @throws AdXException
   * @throws FileNotFoundException
   * @throws UnsupportedEncodingException
   */
  public void runExperiment() throws AdXException, FileNotFoundException, UnsupportedEncodingException {
    String agentsResults = "";
    String marketMakerResults = "";
    Logging.log("[experiment] running with parameters:" + "\n\t\t reserve = " + this.reserve + "\n\t\t numberOfImpressions = " + this.numberOfImpressions + "\n\t\t demandDiscountFactor = " + this.demandDiscountFactor);
    for (int g = 0; g < this.numberOfGames; g++) {
      // Run simulator.
      Simulator simulator = new Simulator(this.simAgents, this.reserve, this.numberOfImpressions, this.demandDiscountFactor);
      // Get statistics.
      Statistics statistics = simulator.run();
      agentsResults += statistics.oneLineAgentsSummary(1, g);
      marketMakerResults += statistics.oneLineMarketMakerSummary(1, g, this.numberOfImpressions);
      // Logging.log("Result Agents: " + statistics.oneLineAgentsSummary(1, g));
      // Logging.log("Result Market Maker: " + statistics.oneLineMarketMakerSummary(1, g, this.numberOfImpressions));
    }
    // Save results to .csv files.
    // Results from the agent point of view.
    PrintWriter writerAgentsResults = new PrintWriter(this.resultsDirectory + "agents/" + this.resultsFileName + ".csv", "UTF-8");
    writerAgentsResults.println(agentsResults);
    writerAgentsResults.close();
    // Results from the market maker point of view.
    PrintWriter writerMarketMakerResults = new PrintWriter(this.resultsDirectory + "marketmaker/" + this.resultsFileName + ".csv", "UTF-8");
    writerMarketMakerResults.println(marketMakerResults);
    writerMarketMakerResults.close();
  }

}
