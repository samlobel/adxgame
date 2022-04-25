package adx.agent;

import java.util.Map;
import java.util.Map.Entry;

import adx.messages.EndOfDayMessage;
import adx.structures.BidBundle;
import adx.util.Pair;

public abstract class AgentLogic {
	protected String agentName;
	
	public AgentLogic() {
		agentName = null;
	}

	protected abstract BidBundle handleEndOfDayMessage(EndOfDayMessage endOfDayMessage);

	/**
	 * Keeps the statistics.
	 */
	protected Map<Integer, Pair<Integer, Double>> statistics;

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
					Pair<Integer, Double> newStats = new Pair<Integer, Double>(
							currentStats.getElement1() + x.getValue().getElement1(),
							currentStats.getElement2() + x.getValue().getElement2());
					this.statistics.put(x.getKey(), newStats);
				} else {
					this.statistics.put(x.getKey(), x.getValue());
				}
			}
		}
	}
}
