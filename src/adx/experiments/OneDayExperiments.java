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

  /**
   * Experiments varying the number of impressions.
   * 
   * @param args
   * @throws FileNotFoundException
   * @throws UnsupportedEncodingException
   * @throws AdXException
   */
  public static void varyImpressions(String[] args) throws FileNotFoundException, UnsupportedEncodingException, AdXException {
    ExperimentFactory.resultsDirectory = "data/results-varying-impressions";
    for (int i = 100; i < 10001; i = i + 100) {
      // Experiment x = ExperimentFactory.SIandWEAgents("/SIWE(" + i + ").csv", 1, 1);
      // Experiment x = ExperimentFactory.SIandWFAgents("/SIWF(" + i + ").csv", 1, 1);
      Logging.log("Experiment with " + i + " impressions");
      ExperimentFactory.WEandWFAgents("/WEWF(" + i + ").csv", 1, 1, 0.0, i).runExperiment();
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
  public static void varyReserve(String[] args) throws FileNotFoundException, UnsupportedEncodingException, AdXException {
    Logging.log("Results varying reserve");
    if (args.length > 0) {
      ExperimentFactory.resultsDirectory = args[0];
      Logging.log("Received folder via command line parameter: " + ExperimentFactory.resultsDirectory);
    } else {
      ExperimentFactory.resultsDirectory = "data/results-varying-reserve/";
      Logging.log("Endogenous folder: " + ExperimentFactory.resultsDirectory);
    }
    double reserve = 0.0;
    for (int i = 0; i < 131; i++) {
      Experiment experiment1 = ExperimentFactory.allWEExperiment("WEWF(8,0)-" + i + ".csv", 8, reserve, 10000);
      Experiment experiment2 = ExperimentFactory.allWFExperiment("WEWF(0,8)-" + i + ".csv", 8, reserve, 10000);
      Experiment experiment3 = ExperimentFactory.WEandWFAgents("WEWF(4,4)-" + i + ".csv", 4, 4, reserve, 10000);
      experiment1.runExperiment();
      experiment2.runExperiment();
      experiment3.runExperiment();
      reserve += 1.0 / 100.0;
    }
  }

  /**
   * Basic Experiments.
   * 
   * @param args
   * @throws FileNotFoundException
   * @throws UnsupportedEncodingException
   * @throws AdXException
   */
  public static void basicExperiments(String[] args) throws FileNotFoundException, UnsupportedEncodingException, AdXException {
    double demandDiscountFactor;
    int numberOfImpressions;
    Logging.log("Results with demand discount factor");
    if (args.length > 0) {
      // Running experiments via command line
      ExperimentFactory.resultsDirectory = args[0];
      Logging.log("Received folder via command line parameter: " + ExperimentFactory.resultsDirectory);
      demandDiscountFactor = Double.parseDouble(args[1]);
      Logging.log("Received demandDiscountFactor via command line parameter: " + demandDiscountFactor);
      numberOfImpressions = Integer.parseInt(args[2]);
      Logging.log("Received numberOfImpressions via command line parameter: " + numberOfImpressions);
    } else {
      // Running experiments manually
      ExperimentFactory.resultsDirectory = "data/results1.25-2k-newreward/";
      Logging.log("Endogenous folder: " + ExperimentFactory.resultsDirectory);
      demandDiscountFactor = 1.25;
      Logging.log("Endogenous demandDiscountFactor: " + demandDiscountFactor);
      numberOfImpressions = 2000;
      Logging.log("Endogenous numberOfImpressions: " + numberOfImpressions);
    }

    // Pure agents profiles.
    for (int j = 1; j < 21; j++) {
      Logging.log("All WE agents (" + j + ")");
      Experiment x = ExperimentFactory.allWEExperiment("/WE(" + j + ").csv", j, 0.0, numberOfImpressions);
      x.setDemandDiscountFactor(demandDiscountFactor);
      x.runExperiment();

      Logging.log("All WF agents (" + j + ")");
      Experiment y = ExperimentFactory.allWFExperiment("/WF(" + j + ").csv", j, 0.0, numberOfImpressions);
      y.setDemandDiscountFactor(demandDiscountFactor);
      y.runExperiment();

    }

    /* Mix of agents.
    for (int j = 1; j < 21; j++) {
      for (int l = 1; l < 21; l++) {
        Logging.log("SI and WE agents (" + j + "," + l + ")");
        Experiment x = ExperimentFactory.SIandWEAgents("/SIWE(" + j + "-" + l + ").csv", j, l, 0.0, numberOfImpressions);
        x.setDemandDiscountFactor(demandDiscountFactor);
        x.runExperiment();

        Logging.log("SI and WF agents (" + j + "," + l + ")");
        Experiment y = ExperimentFactory.SIandWFAgents("/SIWF(" + j + "-" + l + ").csv", j, l, 0.0, numberOfImpressions);
        y.setDemandDiscountFactor(demandDiscountFactor);
        y.runExperiment();

        Logging.log("WE and WF agents (" + j + "," + l + ")");
        Experiment z = ExperimentFactory.WEandWFAgents("/WEWF(" + j + "-" + l + ").csv", j, l, 0.0, numberOfImpressions);
        z.setDemandDiscountFactor(demandDiscountFactor);
        z.runExperiment();
      }
    }*/
  }

  public static void eightAgentsVaryReserve(String[] args) throws FileNotFoundException, UnsupportedEncodingException, AdXException {
    Logging.log("Eight Agents Vary Reserve Experiment");

    double demandDiscountFactor = 3.0;
    Logging.log("Endogenous demandDiscountFactor: " + demandDiscountFactor);
    int numberOfImpressions = 2000;
    Logging.log("Endogenous numberOfImpressions: " + numberOfImpressions);
    ExperimentFactory.resultsDirectory = "data/results" + demandDiscountFactor + "-2k-8agents-varyreserve/";
    Logging.log("Endogenous folder: " + ExperimentFactory.resultsDirectory);

    double reserve = 0.0;
    int count = 0;
    for (int r = 0; r < 131; r++) {
      for (int i = 0; i < 9; i++) {
        Logging.log("WEWF(" + (8 - i) + "-" + i + "), r = " + reserve);
        Experiment experiment = ExperimentFactory.WEandWFAgents("WEWF(" + (8 - i) + "-" + i + ")-r(" + r + ").csv", 8 - i, i, reserve, numberOfImpressions);
        experiment.setDemandDiscountFactor(demandDiscountFactor);
        experiment.runExperiment();
        count++;
      }
      reserve += 1.0 / 100.0;
    }
    Logging.log("Total Count " + count);
  }
  
  public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException, AdXException {
    // OneDayExperiments.varyReserve(args);
    // OneDayExperiments.basicExperiments(args);
    OneDayExperiments.eightAgentsVaryReserve(args);
  }
  
}
