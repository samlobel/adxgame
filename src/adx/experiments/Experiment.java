package adx.experiments;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
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
   * The list of agents in the experiment.
   */
  private final List<SimAgent> simAgents;
  /**
   * Name of the directory where to save files.
   */
  private final String resultsDirectory;
  /**
   * Name of file where results are going to be stored.
   */
  private final String resultsFileName;
  /**
   * How many games (trials) of the experiment.
   */
  private final int numberOfGames;
  /**
   * Number of impressions.
   */
  private final int numberOfImpressions;
  /**
   * The demand discount factor.
   */
  private final double demandDiscountFactor;
  /**
   * The reserve price.
   */
  private final double reserve;

  /**
   * Constructor.
   * 
   * @param csvFileName
   * @param simAgents
   * @param numberOfGames
   * @param reserve
   */
  public Experiment(List<SimAgent> simAgents, String resultsDirectory, String resultsFileName, int numberOfGames, int numberOfImpressions,
      double demandDiscountFactor, double reserve) {
    this.simAgents = simAgents;
    this.resultsDirectory = resultsDirectory;
    this.resultsFileName = resultsFileName;
    this.numberOfGames = numberOfGames;
    this.numberOfImpressions = numberOfImpressions;
    this.demandDiscountFactor = demandDiscountFactor;
    this.reserve = reserve;
  }

  /**
   * Runs an experiments and saves the results to csv file.
   * 
   * @param resultsFileName
   * @param simAgents
   * @param numberOfGames
   * @throws AdXException
   * @throws IOException
   */
  public void runExperiment(boolean saveAgentData, boolean saveMarketMakerData, boolean saveBidsData) throws AdXException, IOException {
    String agentsResults = "";
    String marketMakerResults = "";
    String bidsLogs = "";
    Logging.log("[experiment]" + "\n\t reserve = " + this.reserve + "\n\t numberOfImpressions = " + this.numberOfImpressions + "\n\t demandDiscountFactor = "
        + this.demandDiscountFactor);
    for (int g = 0; g < this.numberOfGames; g++) {
      // Run simulator.
      Simulator simulator = new Simulator(this.simAgents, this.reserve, this.numberOfImpressions, this.demandDiscountFactor);
      // Get statistics.
      Statistics statistics = simulator.run();
      agentsResults += statistics.oneLineAgentsSummary(1, g);
      marketMakerResults += statistics.oneLineMarketMakerSummary(1, g, this.numberOfImpressions);
      bidsLogs += statistics.getStatisticsBids().logBidsToCSV(1);
      // Logging.log("Result Agents: " + statistics.oneLineAgentsSummary(1, g));
      // Logging.log("Result Market Maker: " + statistics.oneLineMarketMakerSummary(1, g, this.numberOfImpressions));
    }
    // Save results to .csv files.
    if (saveAgentData) {
      // Results from the agent point of view.
      Files.createDirectories(Paths.get(this.resultsDirectory + "agents/"));
      PrintWriter writerAgentsResults = new PrintWriter(this.resultsDirectory + "agents/" + this.resultsFileName + ".csv", "UTF-8");
      writerAgentsResults.println(agentsResults);
      writerAgentsResults.close();
    }
    if (saveMarketMakerData) {
      // Results from the market maker point of view.
      Files.createDirectories(Paths.get(this.resultsDirectory + "marketmaker/"));
      PrintWriter writerMarketMakerResults = new PrintWriter(this.resultsDirectory + "marketmaker/" + this.resultsFileName + ".csv", "UTF-8");
      writerMarketMakerResults.println(marketMakerResults);
      writerMarketMakerResults.close();
    }
    // Log of bids.
    if (saveBidsData) {
      Files.createDirectories(Paths.get(this.resultsDirectory + "bidlogs/"));
      PrintWriter writerBidsLog = new PrintWriter(this.resultsDirectory + "bidlogs/" + this.resultsFileName + ".csv", "UTF-8");
      writerBidsLog.println(bidsLogs);
      writerBidsLog.close();
    }

  }

}
