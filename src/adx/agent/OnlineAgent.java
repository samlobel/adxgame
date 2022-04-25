package adx.agent;

import java.io.IOException;

import adx.messages.ACKMessage;
import adx.messages.ConnectServerMessage;
import adx.messages.EndOfDayMessage;
import adx.util.Logging;
import adx.util.Startup;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

/**
 * This class implements common methods for an agent playing the game. This is an abstract class that must be extended by an agent that wants to play the game.
 * 
 * @author Enrique Areyan Viqueira
 */
public class OnlineAgent extends AbsAgent {

	/**
	 * Kryo object to communicate with server.
	 */
	private final Client client;

	/**
	 * Constructor. Initializes with a null client.
	 */
	public OnlineAgent() {
		super();
		this.client = null;
	}

	/**
	 * Connects the agent and registers its name.
	 * 
	 * @param name
	 * @param password
	 */
	public void connect(String name, String password) {
		if (this.agentName == null) {
			this.agentName = name;
		}
		try {
			ConnectServerMessage request = new ConnectServerMessage();
			request.setAgentName(name);
			request.setAgentPassword(password);
			this.sendMessage(request);
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
	public OnlineAgent(String host, int port, AgentLogic logic) {
		super(logic);
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
		final OnlineAgent agent = this;
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
	 * Gets Kryo client
	 * 
	 * @return kryo client.
	 */
	protected Client getClient() {
		return this.client;
	}

	@Override
	protected void sendMessage(Object message) {
		if (message != null && this.client.isConnected()) {
			this.client.sendTCP(message);
		}
	}
}
