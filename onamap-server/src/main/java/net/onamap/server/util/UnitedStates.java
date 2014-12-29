package net.onamap.server.util;

import java.util.HashMap;
import java.util.Map;

public class UnitedStates {

	private static Map<String, String> s = null;

	public static Map<String, String> getMap() {
		if (s == null) {
			s = new HashMap<String, String>();
			s.put("AK", "Alaska");
			s.put("AL", "Alabama");
			s.put("AZ", "Arizona");
			s.put("AR", "Arkansas");
			s.put("CA", "California");
			s.put("CO", "Colorado");
			s.put("CT", "Connecticut");
			s.put("DE", "Delaware");
			s.put("FL", "Florida");
			s.put("GA", "Georgia");
			s.put("HI", "Hawaii");
			s.put("ID", "Idaho");
			s.put("IL", "Illinois");
			s.put("IN", "Indiana");
			s.put("IA", "Iowa");
			s.put("KS", "Kansas");
			s.put("KY", "Kentucky");
			s.put("LA", "Louisiana");
			s.put("ME", "Maine");
			s.put("MD", "Maryland");
			s.put("MA", "Massachusetts");
			s.put("MI", "Michigan");
			s.put("MN", "Minnesota");
			s.put("MS", "Mississippi");
			s.put("MO", "Missouri");
			s.put("MT", "Montana");
			s.put("NE", "Nebraska");
			s.put("NV", "Nevada");
			s.put("NH", "New Hampshire");
			s.put("NJ", "New Jersey");
			s.put("NM", "New Mexico");
			s.put("NY", "New York");
			s.put("NC", "North Carolina");
			s.put("ND", "North Dakota");
			s.put("OH", "Ohio");
			s.put("OK", "Oklahoma");
			s.put("OR", "Oregon");
			s.put("PA", "Pennsylvania");
			s.put("RI", "Rhode Island");
			s.put("SC", "South Carolina");
			s.put("SD", "South Dakota");
			s.put("TN", "Tennessee");
			s.put("TX", "Texas");
			s.put("UT", "Utah");
			s.put("VT", "Vermont");
			s.put("VA", "Virginia");
			s.put("WA", "Washington");
			s.put("WV", "West Virginia");
			s.put("WI", "Wisconsin");
			s.put("WY", "Wyoming");
		}
		return s;
	}

	public static Map<String, String> reverse() {
		Map<String, String> states = getMap();
		Map<String, String> statesReverse = new HashMap<String, String>();
		for (String key : states.keySet()) {
			String value = states.get(key);
			statesReverse.put(value, key);
		}
		return statesReverse;
	}

}
