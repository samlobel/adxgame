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

  /**
   * Creates a list of SI agents.
   * 
   * @param numberOfAgents
   * @return
   */
  public static List<SimAgent> listOfSIAgents(int numberOfAgents, int numberOfImpressions, double reserve) {
    List<SimAgent> simAgents = new ArrayList<SimAgent>();
    for (int j = 0; j < numberOfAgents; j++) {
      simAgents.add(new SimpleSimAgent("SIAgent" + j, reserve, numberOfImpressions));
    }
    return simAgents;

  }

  /**
   * Creates a list of WE agents.
   * 
   * @param numberOfAgents
   * @return
   */
  public static List<SimAgent> listOfWEAgents(int numberOfAgents, int numberOfImpressions, double reserve) {
    List<SimAgent> simAgents = new ArrayList<SimAgent>();
    for (int j = 0; j < numberOfAgents; j++) {
      simAgents.add(new WEAgent("WEAgent" + j, reserve, numberOfImpressions));
    }
    return simAgents;
  }

  /**
   * Creates a list of WF agents.
   * 
   * @param numberOfAgents
   * @return
   */
  public static List<SimAgent> listOfWFAgents(int numberOfAgents, double reserve, int numberOfImpressions) {
    List<SimAgent> simAgents = new ArrayList<SimAgent>();
    for (int j = 0; j < numberOfAgents; j++) {
      simAgents.add(new WFAgent("WFAgent" + j, reserve, numberOfImpressions));
    }
    return simAgents;
  }
  
  /**
   * A game with only SI and WE agents.
   * 
   * @param numberWE
   * @param numberWF
   * @return
   */
  public static Experiment SIandWEAgents(int numberSI, int numberWE, String resultsDirectory, String resultsFileName, int numberOfGames, int numberOfImpressions, double demandDiscountFactor, double reserve) {
    List<SimAgent> simAgents = ExperimentFactory.listOfSIAgents(numberSI, numberOfImpressions, reserve);
    simAgents.addAll(ExperimentFactory.listOfWEAgents(numberWE, numberOfImpressions, reserve));
    return new Experiment(simAgents, resultsDirectory, resultsFileName, numberOfGames, numberOfImpressions, demandDiscountFactor, reserve);
  }

  /**
   * A game with only SI and WF agents.
   * 
   * @param numberWE
   * @param numberWF
   * @return
   */
  public static Experiment SIandWFAgents(int numberSI, int numberWF, String resultsDirectory, String resultsFileName, int numberOfGames, int numberOfImpressions, double demandDiscountFactor, double reserve) {
    List<SimAgent> simAgents = ExperimentFactory.listOfSIAgents(numberSI, numberOfImpressions, reserve);
    simAgents.addAll(ExperimentFactory.listOfWFAgents(numberWF, reserve, numberOfImpressions));
    return new Experiment(simAgents, resultsDirectory, resultsFileName, numberOfGames, numberOfImpressions, demandDiscountFactor, reserve);
  }

  /**
   * A game with only WE and WF agents.
   * 
   * @param numberWE
   * @param numberWF
   * @return
   */
  public static Experiment WEandWFAgents(int numberWE, int numberWF, String resultsDirectory, String resultsFileName, int numberOfGames, int numberOfImpressions, double demandDiscountFactor, double reserve) {
    List<SimAgent> simAgents = ExperimentFactory.listOfWEAgents(numberWE, numberOfImpressions, reserve);
    simAgents.addAll(ExperimentFactory.listOfWFAgents(numberWF, reserve, numberOfImpressions));
    return new Experiment(simAgents, resultsDirectory, resultsFileName, numberOfGames, numberOfImpressions, demandDiscountFactor, reserve);
  }

  /**
   * A game with SI, WE and WF agents.
   * 
   * @param numberWE
   * @param numberWF
   * @return
   */
  public static Experiment SIandWEandWFAgents(int numberSI, int numberWE, int numberWF, String resultsDirectory, String resultsFileName, int numberOfGames, int numberOfImpressions, double demandDiscountFactor, double reserve) {
    List<SimAgent> simAgents = ExperimentFactory.listOfSIAgents(numberSI, numberOfImpressions, reserve);
    simAgents.addAll(ExperimentFactory.listOfWEAgents(numberWE, numberOfImpressions, reserve));
    simAgents.addAll(ExperimentFactory.listOfWFAgents(numberWF, reserve, numberOfImpressions));
    return new Experiment(simAgents, resultsDirectory, resultsFileName, numberOfGames, numberOfImpressions, demandDiscountFactor, reserve);
  }

}
