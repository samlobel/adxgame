package adx.agent;

import adx.messages.EndOfDayMessage;
import adx.structures.BidBundle;

public abstract class AgentLogic {
	protected String agentName = null;
	
	protected abstract BidBundle handleEndOfDayMessage(EndOfDayMessage endOfDayMessage);
}
