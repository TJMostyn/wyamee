package com.wyamee.nlp;

public class PersonalPrepositionTokenFilter implements ITokenFilter {

	private static final String[] PREPOSITIONS = new String[] { 
		"he",
		"his",
		"she",
		"her",
		"they",
		"their",
		"it"
		
	};
	
	@Override
	public boolean isFiltered(String token) {
		for (String preposition : PREPOSITIONS) {
			if (preposition.equalsIgnoreCase(token))
				return true;
		}
		return false;
	}
}
