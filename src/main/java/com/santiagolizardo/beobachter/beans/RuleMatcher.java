package com.santiagolizardo.beobachter.beans;

import java.util.regex.Pattern;

public class RuleMatcher {

	private Rule rule;
	private Pattern rePattern;

	public RuleMatcher(final Rule rule) {
		this.rule = rule;

		if (rule.isRegularExpression()) {
			rePattern = Pattern.compile(rule.getPattern());
		}
	}

	public Rule getRule() {
		return rule;
	}

	public boolean matches(final String text) {
		if (null == text) {
			return false;
		}

		String pattern = rule.getPattern();

		if (rule.isRegularExpression()) {
			return rePattern.matcher(text).matches();
		} else {
			return (text.indexOf(pattern) != -1);
		}
	}
}
