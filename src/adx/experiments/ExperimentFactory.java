package adx.experiments;

import java.util.ArrayList;
import java.util.List;

import adx.sim.agents.SimAgent;
import adx.sim.agents.SimpleSimAgent;
import adx.sim.agents.WE.WEAgent;
import adx.sim.agents.waterfall.WFAgent;

/**
 * Factory class to create experiments.
 * 
 * @author Enrique Areyan Viqueira
 */
public class ExperimentFactory {

  // Fixed parameters for all experiments.
  public static final int numberOfGames = 100;
  public static final String resultsDirectory = "results";

  /**
   * Creates a list of SI agents.
   * 
   * @param numberOfAgents
   * @return
   */
  public static List<SimAgent> listOfSIAgents(int numberOfAgents) {
    List<SimAgent> simAgents = new ArrayList<SimAgent>();
    for (int j = 0; j < numberOfAgents; j++) {
      // Simple Agents
      simAgents.add(new SimpleSimAgent("SIAgent" + j));
    }
    return simAgents;

  }

  /**
   * Creates a list of WE agents.
   * 
   * @param numberOfAgents
   * @return
   */
  public static List<SimAgent> listOfWEAgents(int numberOfAgents) {
    List<SimAgent> simAgents = new ArrayList<SimAgent>();
    for (int j = 0; j < numberOfAgents; j++) {
      // Walrasian Agents
      simAgents.add(new WEAgent("WEAgent" + j));
    }
    return simAgents;
  }

  /**
   * Creates a list of WF agents.
   * 
   * @param numberOfAgents
   * @return
   */
  public static List<SimAgent> listOfWFAgents(int numberOfAgents) {
    List<SimAgent> simAgents = new ArrayList<SimAgent>();
    for (int j = 0; j < numberOfAgents; j++) {
      // Waterfall Agents
      simAgents.add(new WFAgent("WFAgent" + j));
    }
    return simAgents;
  }

  /**
   * Experiment with all WE agents, parameterized by the number of agents
   * 
   * @param numberOfAgents
   * @return
   */
  public static Experiment allWEExperiment(int numberOfAgents) {
    return new Experiment(ExperimentFactory.resultsDirectory + "/allWE" + numberOfAgents + ".csv", ExperimentFactory.listOfWEAgents(numberOfAgents),
        ExperimentFactory.numberOfGames);
  }

  /**
   * Experiment with all WF agents, parameterized by the number of agents.
   * 
   * @param numberOfAgents
   * @return
   */
  public static Experiment allWFExperiment(int numberOfAgents) {
    return new Experiment(ExperimentFactory.resultsDirectory + "/allWF" + numberOfAgents + ".csv", ExperimentFactory.listOfWFAgents(numberOfAgents),
        ExperimentFactory.numberOfGames);
  }

  /**
   * A game with only SI and WE agents.
   * 
   * @param numberWE
   * @param numberWF
   * @return
   */
  public static Experiment SIandWEAgents(int numberSI, int numberWE) {
    List<SimAgent> simAgents = ExperimentFactory.listOfSIAgents(numberSI);
    simAgents.addAll(ExperimentFactory.listOfWEAgents(numberWE));
    return new Experiment(ExperimentFactory.resultsDirectory + "/SIWE(" + numberSI + "-" + numberWE + ")" + ".csv", simAgents, ExperimentFactory.numberOfGames);
  }

  /**
   * A game with only SI and WF agents.
   * 
   * @param numberWE
   * @param numberWF
   * @return
   */
  public static Experiment SIandWFAgents(int numberSI, int numberWF) {
    List<SimAgent> simAgents = ExperimentFactory.listOfSIAgents(numberSI);
    simAgents.addAll(ExperimentFactory.listOfWFAgents(numberWF));
    return new Experiment(ExperimentFactory.resultsDirectory + "/SIWF(" + numberSI + "-" + numberWF + ")" + ".csv", simAgents, ExperimentFactory.numberOfGames);
  }

  /**
   * A game with only WE and WF agents.
   * 
   * @param numberWE
   * @param numberWF
   * @return
   */
  public static Experiment WEandWFAgents(int numberWE, int numberWF) {
    List<SimAgent> simAgents = ExperimentFactory.listOfWEAgents(numberWE);
    simAgents.addAll(ExperimentFactory.listOfWFAgents(numberWF));
    return new Experiment(ExperimentFactory.resultsDirectory + "/WEWF(" + numberWE + "-" + numberWF + ")" + ".csv", simAgents, ExperimentFactory.numberOfGames);
  }

  /**
   * A game with SI, WE and WF agents.
   * 
   * @param numberWE
   * @param numberWF
   * @return
   */
  public static Experiment SIandWEandWFAgents(int numberSI, int numberWE, int numberWF) {
    List<SimAgent> simAgents = ExperimentFactory.listOfSIAgents(numberSI);
    simAgents.addAll(ExperimentFactory.listOfWEAgents(numberWE));
    simAgents.addAll(ExperimentFactory.listOfWFAgents(numberWF));
    return new Experiment(ExperimentFactory.resultsDirectory + "/SIWEWF(" + numberSI + "-" + numberWE + "-" + numberWF + ")" + ".csv", simAgents,
        ExperimentFactory.numberOfGames);
  }

}
