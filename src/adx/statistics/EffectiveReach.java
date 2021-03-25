package adx.statistics;

public enum EffectiveReach {
	LINEAR, SIGMOIDAL, ALL_OR_NOTHING;

	public static EffectiveReach ofString(String s) {
		if (s.equalsIgnoreCase("LINEAR")) {
			return LINEAR;
		}

		if (s.equalsIgnoreCase("SIGMOIDAL")) {
			return SIGMOIDAL;
		}

		if (s.equalsIgnoreCase("ALL_OR_NOTHING")) {
			return ALL_OR_NOTHING;
		}

		throw new IllegalArgumentException("'" + s + "' not recognized as a valid effective reach type.");
	}
}