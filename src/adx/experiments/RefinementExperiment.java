package adx.experiments;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
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
          String resultsDirectory = OneDayExperiments.resultsDirectoryPrefix + numberOfGames + "/" + numberOfImpressions + "/" + demandDiscountFactor + "/";
          Experiment WEandWFExperiment = ExperimentFactory.WEandWFAgents(numberWE, numberWF, resultsDirectory, "/WEWF(" + numberWE + "-" + numberWF + ")",
              numberOfGames, numberOfImpressions, demandDiscountFactor, 0.0);
          WEandWFExperiment.runExperiment(true, true, true);
        }
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * 
   * @param numberOfSamples
   * @param reserve
   * @throws AdXException
   * @throws FileNotFoundException
   * @throws IOException
   */
  public static void samplingRefinementReserve(int numberOfSamples, int reserve) throws AdXException {
    Logging.log("Refinement Experiment");
    String csvFile = OneDayExperiments.dataDirectoryPrefix + "/stability-reserve/" + (numberOfSamples / 2) + "-" + numberOfSamples + "/stability-for-8-agents-reserve-" + reserve + ".csv";
    String line = "";
    String cvsSplitBy = ",";

    Logging.log("Sampling Refinement for 8 agents and reserve " + reserve);
    try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
      // Read the header of the file.
      line = br.readLine();
      while ((line = br.readLine()) != null) {
        // Read a line of the .csv file
        String[] data = line.split(cvsSplitBy);
        int numberWE1 = Integer.parseInt(data[0]);
        int numberWF1 = Integer.parseInt(data[1]);
        int numberWE2 = Integer.parseInt(data[2]);
        int numberWF2 = Integer.parseInt(data[3]);
        int numberOfImpressions = Integer.parseInt(data[4]);
        double demandDiscountFactor = Double.parseDouble(data[5]);
        String direction1 = data[6];
        String direction2 = data[7];
        Logging.log(numberWE1 + "-" + numberWF1 + "-" + numberWE2 + "-" + numberWF2 + "-" + numberOfImpressions + "-" + demandDiscountFactor + "-" + direction1 + "-" + direction2);
        if (!direction1.equals(direction2)) {
          int numberOfGames = numberOfSamples * 2;
          String resultsDirectory = OneDayExperiments.getResultsDirectory("8-agents-reserve", numberOfGames, numberOfImpressions, demandDiscountFactor);
          double reserveValue = (1.0 / 100.0) * reserve;
          Logging.log("\tNo agreement - sample more");
          // Check if the first strategy was already sampled.
          Logging.log("\t\t(WE, WF) = (" + numberWE1 + "," + numberWF1 + ")");
          File f1 = new File(resultsDirectory + "agents/WEWF(" + numberWE1 + "-" + numberWF1 + ")-r(" + reserve + ").csv");
          if (f1.exists() && !f1.isDirectory()) {
            Logging.log("Experiment already ran, skipping");
          } else {
            Experiment WEandWFExperiment1 = ExperimentFactory.WEandWFAgents(numberWE1, numberWF1, resultsDirectory, "/WEWF(" + numberWE1 + "-" + numberWF1 + ")-r(" + reserve + ")", numberOfGames, numberOfImpressions, demandDiscountFactor, reserveValue);
            WEandWFExperiment1.runExperiment(true, false, false);
          }
          // Check if the second strategy was already sampled.
          Logging.log("\t\t(WE, WF) = (" + numberWE2 + "," + numberWF2 + ")");
          File f2 = new File(resultsDirectory + "agents/WEWF(" + numberWE2 + "-" + numberWF2 + ")-r(" + reserve + ").csv");
          if (f2.exists() && !f2.isDirectory()) {
            Logging.log("Experiment already ran, skipping");
          } else {
            Experiment WEandWFExperiment2 = ExperimentFactory.WEandWFAgents(numberWE2, numberWF2, resultsDirectory, "/WEWF(" + numberWE2 + "-" + numberWF2 + ")-r(" + reserve + ")", numberOfGames, numberOfImpressions, demandDiscountFactor, reserveValue);
            WEandWFExperiment2.runExperiment(true, false, false);
          }
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * 
   * @throws AdXException
   */
  public static void worstBidSamplingEquilibria() throws AdXException {
    String csvFile = OneDayExperiments.dataDirectoryPrefix + "/worstequilibria-reserve/800/worst-equilibria-for-8-agents-all-reserves.csv";
    String line = "";
    String cvsSplitBy = ",";

    Logging.log("Worst equilibria bid sampling");
    
    try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
      // Read the header of the file.
      line = br.readLine();
      while ((line = br.readLine()) != null) {
        String[] data = line.split(cvsSplitBy);
        int reserve = Integer.parseInt(data[0]);
        double reserveValue = (1.0 / 100.0) * reserve;
        int numberOfImpressions = Integer.parseInt(data[1]);
        double demandDiscountFactor = Double.parseDouble(data[2]);
        int numberWE = Integer.parseInt(data[3]);
        int numberWF = Integer.parseInt(data[4]);
        int numberOfGames = 200;
        String resultsDirectory = OneDayExperiments.getResultsDirectory((numberWE + numberWF) + "-agents-worstequilibria-bids", numberOfGames, numberOfImpressions, demandDiscountFactor);
        // Check if the first strategy was already sampled.
        Logging.log("\t\t(WE, WF) = (" + numberWE + "," + numberWF + ")");
        File f1 = new File(resultsDirectory + "agents/WEWF(" + numberWE + "-" + numberWF + ")-r(" + reserve + ").csv");
        if (f1.exists() && !f1.isDirectory()) {
          Logging.log("Experiment already ran, skipping");
        } else {
          Experiment WEandWFExperiment = ExperimentFactory.WEandWFAgents(numberWE, numberWF, resultsDirectory, "/WEWF(" + numberWE + "-" + numberWF + ")-r(" + reserve + ")", numberOfGames, numberOfImpressions, demandDiscountFactor, reserveValue);
          WEandWFExperiment.runExperiment(false, false, true);
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
    if (args.length > 0) {
      //int numberOfGames = Integer.parseInt(args[0]);
      //int reserve = Integer.parseInt(args[1]);
      //samplingRefinementReserve(numberOfGames, reserve);
    } else {
      for (int i = 0; i < 131; i++) {
        //samplingRefinementReserve(400, i);
      }
    }
    RefinementExperiment.worstBidSamplingEquilibria();
  }

}
