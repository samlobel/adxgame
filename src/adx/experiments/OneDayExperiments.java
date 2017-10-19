package adx.experiments;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import adx.exceptions.AdXException;
import adx.util.Logging;

/**
 * Run one-day experiments.
 * 
 * @author Enrique Areyan Viqueira
 */
public class OneDayExperiments {
  
  public static final String directoryPrefix = "/home/eareyanv/workspace/adxgame/data/results/";

  /**
   * Basic Experiments.
   * 
   * @param args
   * @throws FileNotFoundException
   * @throws UnsupportedEncodingException
   * @throws AdXException
   */
  public static void basicExperiments(double demandDiscountFactor, int numberOfImpressions) throws FileNotFoundException, UnsupportedEncodingException, AdXException {
    Logging.log("Basic Experiments");

    ExperimentFactory.resultsDirectory = directoryPrefix + demandDiscountFactor + "-" + numberOfImpressions + "/";
    Logging.log("Results folder: " + ExperimentFactory.resultsDirectory);

    // Pure agents profiles, we are only interested in pure WE and WF.
    for (int j = 1; j < 21; j++) {
      Logging.log("All WE agents (" + j + ")");
      Experiment x = ExperimentFactory.allWEExperiment("/WEWF(" + j + "-0)", j, 0.0, numberOfImpressions);
      x.setDemandDiscountFactor(demandDiscountFactor);
      x.runExperiment();

      Logging.log("All WF agents (" + j + ")");
      Experiment y = ExperimentFactory.allWFExperiment("/WEWF(0-" + j + ")", j, 0.0, numberOfImpressions);
      y.setDemandDiscountFactor(demandDiscountFactor);
      y.runExperiment();

    }

    // Mix of agents.
    for (int j = 1; j < 21; j++) {
      for (int l = 1; l < 21; l++) {
        Logging.log("SI and WE agents (" + j + "," + l + ")");
        Experiment x = ExperimentFactory.SIandWEAgents("/SIWE(" + j + "-" + l + ")", j, l, 0.0, numberOfImpressions);
        x.setDemandDiscountFactor(demandDiscountFactor);
        x.runExperiment();

        Logging.log("SI and WF agents (" + j + "," + l + ")");
        Experiment y = ExperimentFactory.SIandWFAgents("/SIWF(" + j + "-" + l + ")", j, l, 0.0, numberOfImpressions);
        y.setDemandDiscountFactor(demandDiscountFactor);
        y.runExperiment();

        Logging.log("WE and WF agents (" + j + "," + l + ")");
        Experiment z = ExperimentFactory.WEandWFAgents("/WEWF(" + j + "-" + l + ")", j, l, 0.0, numberOfImpressions);
        z.setDemandDiscountFactor(demandDiscountFactor);
        z.runExperiment();
      }
    }
  }

  /**
   * Experiments with reserve.
   * 
   * @param args
   * @throws FileNotFoundException
   * @throws UnsupportedEncodingException
   * @throws AdXException
   */
  public static void eightWEWFAgentsSpecialCasesvaryReserve(double demandDiscountFactor, int numberOfImpressions) throws FileNotFoundException, UnsupportedEncodingException, AdXException {
    Logging.log("Eight Agents, Special Cases, Vary Reserve Experiment");
    
    ExperimentFactory.resultsDirectory = directoryPrefix + "8-agents-specialcases-varyreserve-" + demandDiscountFactor + "-" + numberOfImpressions + "/";
    Logging.log("Results folder: " + ExperimentFactory.resultsDirectory);

    double reserve = 0.0;
    for (int i = 0; i < 131; i++) {
      Experiment experiment1 = ExperimentFactory.allWEExperiment("WEWF(8,0)-" + i, 8, reserve, numberOfImpressions);
      Experiment experiment2 = ExperimentFactory.allWFExperiment("WEWF(0,8)-" + i, 8, reserve, numberOfImpressions);
      Experiment experiment3 = ExperimentFactory.WEandWFAgents("WEWF(4,4)-" + i, 4, 4, reserve, numberOfImpressions);
      experiment1.setDemandDiscountFactor(demandDiscountFactor);
      experiment2.setDemandDiscountFactor(demandDiscountFactor);
      experiment3.setDemandDiscountFactor(demandDiscountFactor);
      experiment1.runExperiment();
      experiment2.runExperiment();
      experiment3.runExperiment();
      reserve += 1.0 / 100.0;
    }
  }

  /**
   * Eight WE and WF agents, any combinations, varying reserve.
   * 
   * @param args
   * @throws FileNotFoundException
   * @throws UnsupportedEncodingException
   * @throws AdXException
   */
  public static void eightWEWFAgentsVaryReserve(double demandDiscountFactor, int numberOfImpressions) throws FileNotFoundException, UnsupportedEncodingException, AdXException {
    Logging.log("Eight Agents Vary Reserve Experiment");
    
    ExperimentFactory.resultsDirectory = directoryPrefix + "8-agents-varyreserve-" + demandDiscountFactor + "-" + numberOfImpressions + "/";
    Logging.log("Results folder: " + ExperimentFactory.resultsDirectory);

    double reserve = 0.0;
    int count = 0;
    for (int r = 0; r < 131; r++) {
      for (int i = 0; i < 9; i++) {
        Logging.log("WEWF(" + (8 - i) + "-" + i + "), r = " + reserve);
        Experiment experiment = ExperimentFactory.WEandWFAgents("WEWF(" + (8 - i) + "-" + i + ")-r(" + r + ")", 8 - i, i, reserve, numberOfImpressions);
        experiment.setDemandDiscountFactor(demandDiscountFactor);
        experiment.runExperiment();
        count++;
      }
      reserve += 1.0 / 100.0;
    }
    Logging.log("Total Count " + count);
  }

  /**
   * Main.
   * 
   * @param args
   * @throws FileNotFoundException
   * @throws UnsupportedEncodingException
   * @throws AdXException
   */
  public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException, AdXException {
    double demandDiscountFactor;
    int numberOfImpressions;

    if (args.length > 0) {
      // Running experiments via command line
      demandDiscountFactor = Double.parseDouble(args[0]);
      Logging.log("Received demandDiscountFactor via command line parameter: " + demandDiscountFactor);
      numberOfImpressions = Integer.parseInt(args[1]);
      Logging.log("Received numberOfImpressions via command line parameter: " + numberOfImpressions);
    } else {
      // Running experiments manually
      demandDiscountFactor = 0.25;
      Logging.log("Endogenous demandDiscountFactor: " + demandDiscountFactor);
      numberOfImpressions = 2000;
      Logging.log("Endogenous numberOfImpressions: " + numberOfImpressions);
    }
    // Experiments so far.
    OneDayExperiments.basicExperiments(demandDiscountFactor, numberOfImpressions);
    OneDayExperiments.eightWEWFAgentsSpecialCasesvaryReserve(demandDiscountFactor, numberOfImpressions);
    OneDayExperiments.eightWEWFAgentsVaryReserve(demandDiscountFactor, numberOfImpressions);
  }

}
