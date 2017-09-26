package adx.experiments;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

import adx.exceptions.AdXException;
import adx.sim.Simulator;
import adx.sim.agents.SimAgent;
import adx.statistics.Statistics;
import adx.util.Parameters;

/**
 * Class that encapsulates the logic of an experiment, i.e., a run of multiple games.
 * 
 * @author Enrique Areyan Viqueira
 */
public class Experiment {
  private final String csvFileName;
  private final List<SimAgent> simAgents;
  private final int numberOfGames;
  private int numberOfImpressions;

  public Experiment(String csvFileName, List<SimAgent> simAgents, int numberOfGames) {
    this.csvFileName = csvFileName;
    this.simAgents = simAgents;
    this.numberOfGames = numberOfGames;
    this.numberOfImpressions = Parameters.POPULATION_SIZE;
  }
  
  public void setNumberOfImpressions(int numberOfImpressions) {
    this.numberOfImpressions = numberOfImpressions;
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
    for (int g = 0; g < this.numberOfGames; g++) {
      // Run simulator.
      Simulator simulator = new Simulator(this.simAgents, 0.0, this.numberOfImpressions);
      // Get statistics.
      Statistics statistics = simulator.run();
      ret += statistics.oneLineSummary(1, g);
      // Logging.log("Result : \n" + statistics.oneLineSummary(1, g));
    }
    // Logging.log(ret);
    PrintWriter writer = new PrintWriter(this.csvFileName, "UTF-8");
    writer.println(ret);
    writer.close();
  }

}
