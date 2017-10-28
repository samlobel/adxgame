package adx.experiments;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import adx.exceptions.AdXException;
import adx.util.Logging;

public class RefinementExperiment {

  /**
   * Given the number of samples and number of agents, reads the stability file and samples more as appropiate.
   * 
   * @param numberOfSamples
   * @param numberOfAgents
   * @throws AdXException
   */
  public static void sampleRefinement(int numberOfSamples, int numberOfAgents) throws AdXException {
    Logging.log("Refinement Experiment");
    String csvFile = "data/stability/" + (numberOfSamples / 2) + "-" + numberOfSamples + "/stability-for-" + numberOfAgents + "-agents.csv";
    String line = "";
    String cvsSplitBy = ",";

    Logging.log("Sampling Refinement for " + numberOfAgents + " agents");
    try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
      // Read the header of the file.
      line = br.readLine();
      while ((line = br.readLine()) != null) {
        // use comma as separator
        String[] data = line.split(cvsSplitBy);
        int numberWE = Integer.parseInt(data[0]);
        int numberWF = Integer.parseInt(data[1]);
        int numberOfImpressions = Integer.parseInt(data[2]);
        double demandDiscountFactor = Double.parseDouble(data[3]);
        double agreement = Double.parseDouble(data[4]);
        Logging.log(numberWE + "," + numberWF + "," + numberOfImpressions + "," + demandDiscountFactor + "," + agreement);
        if (agreement == 0) {
          Logging.log("No agreement - sample more");
          Logging.log("WE and WF agents (" + numberWE + "," + numberWF + ")");
          int numberOfGames = numberOfSamples * 2;
          String resultsDirectory = OneDayExperiments.directoryPrefix + numberOfGames + "/" + numberOfImpressions + "/" + demandDiscountFactor + "/";
          Experiment WEandWFExperiment = ExperimentFactory.WEandWFAgents(numberWE, numberWF, resultsDirectory, "/WEWF(" + numberWE + "-" + numberWF + ")",
              numberOfGames, numberOfImpressions, demandDiscountFactor, 0.0);
          WEandWFExperiment.runExperiment();
        }
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Main.
   * 
   * @param args
   * @throws AdXException
   */
  public static void main(String[] args) throws AdXException {
    int samples = 800;
    for (int i = 2; i < 16; i++) {
      RefinementExperiment.sampleRefinement(samples, i);
    }
  }

}
