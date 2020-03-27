package adx.agent;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import adx.messages.ACKMessage;
import adx.messages.ConnectServerMessage;
import adx.messages.EndOfDayMessage;
import adx.util.Logging;
import adx.util.Pair;
import adx.util.Startup;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

/**
 * This class implements common methods for an agent playing the game. This is an abstract class that must be extended by an agent that wants to play the game.
 * 
 * @author Enrique Areyan Viqueira
 */
public abstract class Agent {

  /**
   * Name of the agent
   */
  protected String agentName = null;

  /**
   * Kryo object to communicate with server.
   */
  private final Client client;

  /**
   * Keeps the statistics.
   */
  protected Map<Integer, Pair<Integer, Double>> statistics;

  /**
   * Constructor. Initializes with a null client.
   */
  public Agent() {
    this.client = null;
  }

  /**
   * Connects the agent and registers its name.
   * 
   * @param name
   * @param password
   */
  protected void connect(String name, String password) {
    if (this.agentName == null) {
      this.agentName = name;
    }
    try {
      ConnectServerMessage request = new ConnectServerMessage();
      request.setAgentName(name);
      request.setAgentPassword(password);
      this.getClient().sendTCP(request);
      while (true)
        ;
    } catch (Exception e) {
      Logging.log("[x] Error trying to connect to the server!");
      e.printStackTrace();
    }
  }

  /**
   * Constructor.
   * 
   * @param host
   * @param port
   */
  public Agent(String host, int port) {
    this.client = new Client();
    this.client.start();
    try {
      this.client.connect(5000, host, port, port);
      Startup.start(this.client.getKryo());
    } catch (IOException e) {
      Logging.log("[x] Error connecting to server -- > ");
      e.printStackTrace();
      System.exit(-1);
    }
    // Add listener for server messages.
    final Agent agent = this;
    this.client.addListener(new Listener() {
      public void received(Connection connection, Object message) {
        synchronized (agent) {
          // Logging.log("Received message from server " + message);
          try {
            if (message instanceof ConnectServerMessage) {
              handleConnectServerMessage((ConnectServerMessage) message);
            } else if (message instanceof EndOfDayMessage) {
              handleEndOfDayMessage((EndOfDayMessage) message);
            } else if (message instanceof ACKMessage) {
              handleACKMessage((ACKMessage) message);
            }
          } catch (Exception e) {
            Logging.log("[x] An exception occurred while trying to parse a message in the agent");
            e.printStackTrace();
          }
        }
      }
    });
  }

  /**
   * Handler for the JoinGame message.
   * 
   * @param connectServerMessage
   * @throws Exception
   */
  private void handleConnectServerMessage(ConnectServerMessage connectServerMessage) throws Exception {
    Logging.log("[-] Received ConnectServerMessage, server response is: " + connectServerMessage.getServerResponse());
    switch (connectServerMessage.getStatusCode()) {
    case 0:
    case 2:
    case 3:
      // In any of this cases the agent won't be able to play
      Logging.log("[x] Could not join the game!");
      System.exit(-1);
      break;
    case 1:
      // In this case the agent can play
      Logging.log("[-] Agent: " + this.agentName + " is in the game!");
      break;
    default:
      throw new Exception("[x] Unknown response code from server");
    }
  }

  /**
   * Gets Kryo client
   * 
   * @return kryo client.
   */
  protected Client getClient() {
    return this.client;
  }

  /**
   * This method hanldes the ACK message.
   * 
   * @param message
   */
  protected void handleACKMessage(ACKMessage message) {
    if (message.getCode()) {
      Logging.log("[-] ACK Message, all ok, " + message.getMessage());
    } else {
      Logging.log("[x] ACK Message, error, " + message.getMessage());
    }
  }

  /**
   * Update statistics from server.
   * 
   * @param statistics
   */
  public void updateStatistics(Map<Integer, Pair<Integer, Double>> statistics) {
    if (statistics != null) {
      for (Entry<Integer, Pair<Integer, Double>> x : statistics.entrySet()) {
        if (this.statistics.containsKey(x.getKey())) {
          Pair<Integer, Double> currentStats = this.statistics.get(x.getKey());
          Pair<Integer, Double> newStats = new Pair<Integer, Double>(currentStats.getElement1() + x.getValue().getElement1(), currentStats.getElement2() + x.getValue().getElement2());
          this.statistics.put(x.getKey(), newStats);
        } else {
          this.statistics.put(x.getKey(), x.getValue());
        }
      }
    }
  }

  /**
   * Handles for the end of day message.
   * 
   * @param endOfDayMessage
   */
  abstract protected void handleEndOfDayMessage(EndOfDayMessage endOfDayMessage);
}
