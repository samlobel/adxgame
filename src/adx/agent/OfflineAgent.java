package adx.agent;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

import adx.messages.ACKMessage;
import adx.messages.ConnectServerMessage;
import adx.messages.EndOfDayMessage;
import adx.server.OfflineGameServerAbstract;
import adx.structures.BidBundle;
import adx.util.Logging;
import adx.util.Startup;


/**
 * This class implements common methods for an agent playing the game. This is an abstract class that must be extended by an agent that wants to play the game.
 * 
 * @author Enrique Areyan Viqueira
 */
public class OfflineAgent extends AbsAgent {

	protected String agentName = null;

	private OfflineGameServerAbstract server;

	/**
	 * Constructor. Initializes with a null client.
	 */
	public OfflineAgent() {
		super();
		this.server = null;
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
	public OfflineAgent(OfflineGameServerAbstract server, AgentLogic logic) {
		super(logic);
		this.server = server;
	}

	public synchronized void receiveMessage(Object message) {
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

	@Override
	protected void sendMessage(Object message) {
		if (message != null) {
			this.server.receiveMessage(this, message);
		}
	}
}
