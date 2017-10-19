package com.wyamee.nlp;

import java.util.regex.Pattern;

public class NonAlphabeticTokenFilter implements ITokenFilter {

	@Override
	public boolean isFiltered(String token) {
		return Pattern.matches("\\p{Punct}", token) || token.matches("-?\\d+(\\.\\d+)?");
	}
}
