package adx.util;

import adx.agent.AgentLogic;
import adx.agent.OnlineAgent;

public class AgentStartupUtil {
	public static void startOnline(AgentLogic logic, String[] args) {
		String host = args[0];
		int port = Integer.parseInt(args[1]);
		String name = args[2];
		
		new OnlineAgent(host, port, logic).connect(name);
	}
}
