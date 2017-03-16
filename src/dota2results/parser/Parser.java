package dota2results.parser;

import dota2results.Match;

import java.util.List;

public interface Parser {
	List<Match> parse(String page);
}
