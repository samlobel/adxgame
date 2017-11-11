package adx.experiments;

import java.io.File;
import java.io.IOException;

import adx.exceptions.AdXException;
import adx.util.Logging;

public class ReserveExperiments {
  
  /**
   * 
   * @param numberOfGames
   * @param numberWE
   * @param numberWF
   * @param numberOfImpressions
   * @param demandDiscountFactor
   * @param reserve
   * @throws AdXException
   * @throws IOException
   */
  public static void profileFixedReserve(int numberOfGames, int numberWE, int numberWF, int numberOfImpressions, double demandDiscountFactor, int reserve) throws AdXException, IOException {
    String resultsDirectory = OneDayExperiments.getResultsDirectory((numberWE + numberWF) + "-agents-fixed-reserve", numberOfGames, numberOfImpressions, demandDiscountFactor);
    Experiment varyReserveExperiment = ExperimentFactory.WEandWFAgents(numberWE, numberWF, resultsDirectory, "WEWF(" + numberWE + "-" + numberWF + ")-r(" + reserve + ")", numberOfGames, numberOfImpressions, demandDiscountFactor, reserve * 0.01);
    varyReserveExperiment.runExperiment(false, false, true);
  }
  
  
  /**
   * WE and WF agents, any combinations, varying reserve.
   * 
   * @param args
   * @throws AdXException
   * @throws IOException
   */
  public static void agentsWEWFVaryReserve(int numberOfGames, int numberOfAgents, int numberOfImpressions, double demandDiscountFactor) throws AdXException, IOException {
    Logging.log(numberOfAgents + " Agents Vary Reserve Experiment");

    String resultsDirectory = OneDayExperiments.getResultsDirectory(numberOfAgents + "-agents-reserve", numberOfGames, numberOfImpressions, demandDiscountFactor);
    Logging.log("Results folder: " + resultsDirectory);

    double reserve = 0.0;
    int count = 0;
    for (int r = 0; r < 131; r++) {
      for (int i = 0; i < numberOfAgents + 1; i++) {
        Logging.log("WEWF(" + (numberOfAgents - i) + "-" + i + "), r = " + reserve);
        File f = new File(resultsDirectory + "agents/WEWF(" + (numberOfAgents - i) + "-" + i + ")-r(" + r + ").csv");
        if (f.exists() && !f.isDirectory()) {
          Logging.log("Experiment already ran, skipping");
        } else {
          Logging.log("Running Experiment");
          Experiment varyReserveExperiment = ExperimentFactory.WEandWFAgents(numberOfAgents - i, i, resultsDirectory, "WEWF(" + (numberOfAgents - i) + "-" + i + ")-r(" + r + ")", numberOfGames, numberOfImpressions, demandDiscountFactor, reserve);
          varyReserveExperiment.runExperiment(true, false, false);
        }
        count++;
      }
      reserve += 1.0 / 100.0;
    }
    Logging.log("Total Count " + count);
  }
  
  public static void main(String[] args) throws AdXException, IOException {
    Parameters parameters = Parameters.getParameters(args);
    ReserveExperiments.agentsWEWFVaryReserve(parameters.getNumberOfGames(), 20, parameters.getNumberOfImpressions(), parameters.getDemandDiscountFactor());
    //ReserveExperiments.profileFixedReserve(200, 0, 8, 2000, 0.25, 80);
  }
}
