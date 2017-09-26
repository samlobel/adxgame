package adx.experiments;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import adx.exceptions.AdXException;
import adx.sim.agents.SimAgent;
import adx.util.Logging;

/**
 * Run one-day experiments.
 * 
 * @author Enrique Areyan Viqueira
 */
public class OneDayExperiments {

  /**
   * Main function.
   * 
   * @param args
   * @throws AdXException
   * @throws UnsupportedEncodingException
   * @throws FileNotFoundException
   */
  public static void main2(String[] args) throws AdXException, FileNotFoundException, UnsupportedEncodingException {
    // Pure agent experiments.
    /*
     * for (int j = 2; j < 11; j++) { Logging.log("All WE agents " + j); ExperimentFactory.allWEExperiment(j).runExperiment(); Logging.log("All WF agents " +
     * j); ExperimentFactory.allWFExperiment(j).runExperiment(); }
     */
    // Mix of 2 type of agents experiments (except SI v SI which is not interesting).
    for (int j = 1; j < 21; j++) {
      for (int l = 1; l < 21; l++) {
        Logging.log("SI and WE agents (" + j + "," + l + ")");
        ExperimentFactory.SIandWEAgents(j, l).runExperiment();
        Logging.log("SI and WF agents (" + j + "," + l + ")");
        ExperimentFactory.SIandWFAgents(j, l).runExperiment();
        Logging.log("WE and WF agents (" + j + "," + l + ")");
        ExperimentFactory.WEandWFAgents(j, l).runExperiment();
        // All 3 types of agents playing.
        /*
         * for (int k = 1; k < 21; k++) { Logging.log("SI and WE and WF agents (" + j + "," + l + "," + k + ")"); ExperimentFactory.SIandWEandWFAgents(j, l,
         * k).runExperiment(); }
         */
      }
    }
  }

  public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException, AdXException {
    List<SimAgent> simAgents = ExperimentFactory.listOfSIAgents(1);
    simAgents.addAll(ExperimentFactory.listOfWEAgents(1));
    for (int i = 100; i < 10001; i = i + 100) {
      Experiment x = new Experiment(ExperimentFactory.resultsDirectory + "/SIWE("+i+").csv", simAgents, ExperimentFactory.numberOfGames);
      x.setNumberOfImpressions(i);
      x.runExperiment();
    }

  }
}
