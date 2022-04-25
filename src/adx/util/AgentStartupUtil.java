package adx.util;

import java.util.Map;

import adx.agent.AgentLogic;
import adx.agent.OfflineAgent;
import adx.agent.OnlineAgent;
import adx.exceptions.AdXException;
import adx.server.OfflineGameServerAbstract;

public class AgentStartupUtil {
	public static void startOnline(AgentLogic logic, String[] args, String name) {
		String host = args[0];
		int port = Integer.parseInt(args[1]);
		
		new OnlineAgent(host, port, logic).connect(name);
	}
	
	public static void testOffline(Map<String, AgentLogic> agents, OfflineGameServerAbstract server) throws AdXException {
		for (Map.Entry<String, AgentLogic> ent : agents.entrySet()) {
			new OfflineAgent(server, ent.getValue()).connect(ent.getKey());
		}
		server.runAdXGame();
	}
}
