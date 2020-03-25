package adx.util;

import adx.agent.AgentLogic;
import adx.agent.OfflineAgent;
import adx.agent.OnlineAgent;
import adx.server.OfflineGameServerAbstract;

public class AgentStartupUtil {
	public static void startOnline(AgentLogic logic, String[] args) {
		String host = args[0];
		int port = Integer.parseInt(args[1]);
		String name = args[2];
		
		new OnlineAgent(host, port, logic).connect(name);
	}
	
	public static void testOffline(AgentLogic logic, OfflineGameServerAbstract server) {
		new OfflineAgent(server, logic).connect("test_agent");
	}
}
