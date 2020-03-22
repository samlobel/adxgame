package adx.agent;

import adx.messages.ACKMessage;
import adx.messages.ConnectServerMessage;
import adx.messages.EndOfDayMessage;
import adx.structures.BidBundle;
import adx.util.Logging;

public abstract class AbsAgent {
	protected String agentName;
	private AgentLogic logic;
	
	protected AbsAgent() {
		this.agentName = null;
		this.logic = null;
	}
	
	protected AbsAgent(AgentLogic logic) {
		this();
		this.logic = logic;
	}
	
	protected abstract void connect(String name, String password);
	
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


	protected void handleConnectServerMessage(ConnectServerMessage connectServerMessage) throws Exception {
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
			this.logic.agentName = this.agentName;
			Logging.log("[-] Agent: " + this.agentName + " is in the game!");
			break;
		default:
			throw new Exception("[x] Unknown response code from server");
		}
	}
	
	protected abstract void sendMessage(Object message);

	protected void handleEndOfDayMessage(EndOfDayMessage endOfDayMessage) {
		BidBundle bundle = this.logic.handleEndOfDayMessage(endOfDayMessage);
		this.sendMessage(bundle);
	}
}
