package dota2results.parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dota2results.Match;

public class IndexOfParser implements Parser {

	private static int cursor;
	
	private static Date getMatchTime(String s) {
		String sWhen = s.substring(s.indexOf("match-time\">", cursor) + 12,
				s.indexOf("<", s.indexOf("match-time\">", cursor) + 12));
		cursor = s.indexOf("<", s.indexOf("match-time\">", cursor) + 12);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Date when;
		try {
			when = formatter.parse(sWhen);
		} catch (ParseException e) {
			when = new Date();
			e.printStackTrace();
		}
		
		return when;
	}
	
	private static String getEventName(String s) {
		String where = s.substring(s.indexOf("eventm\">", cursor) + 8,
				s.indexOf("<", s.indexOf("eventm\">", cursor) + 8));
		cursor = s.indexOf("<", s.indexOf("eventm\">", cursor) + 8);
		
		return where;
	}
	
	private static long getMatchId(String s) {
		long id = Long.parseLong(s.substring(s.indexOf("match?m=", cursor) + 8,
				s.indexOf("\">", s.indexOf("match?m=", cursor) + 8)));
		cursor = s.indexOf("\">", s.indexOf("match?m=", cursor) + 8);
		return id;
	}
	
	private static String[] getTeams(String s, int matchCursor) {
		cursor = s.indexOf("teamtext\">", cursor) + 10;
		int team1cursor = cursor;
		
		
		String team1 = s.substring(s.indexOf("b>", cursor) + 2,
				s.indexOf("<", s.indexOf("b>", cursor) + 2));
		cursor = s.indexOf("<", s.indexOf("b>", cursor) + 2);

		cursor = s.indexOf("teamtext\">", cursor) + 10;
		int team2cursor = cursor;
		String team2 = s.substring(s.indexOf("b>", cursor) + 2,
				s.indexOf("<", s.indexOf("b>", cursor) + 2));
		cursor = s.indexOf("<", s.indexOf("b>", cursor) + 2);

		
		String winner = null;
		int resultCursor = s.indexOf("img/won.png", cursor);
		
		if ((resultCursor > matchCursor) && (resultCursor < team1cursor)) {
			winner = team1;
		} else if ((resultCursor > matchCursor) && (resultCursor > team1cursor)
				&& (resultCursor < team2cursor)) {
			winner = team2;
		}
		
		return new String[]{team1, team2, winner};
	}
	
	public static Match parseMatch(String s) {

		int matchCursor = cursor;
		
		Date when = getMatchTime(s); 

		String where = getEventName(s);
		
		long id = getMatchId(s);
		
		String[]teams = getTeams(s, matchCursor);
		
		long time = 0;

		return new Match(id, when, where, teams[0], teams[1], teams[2], time);
	}

	
	@Override
	public List<Match> parse(String page) {
		
		List<Match> matchlist = new ArrayList<>();
		for (cursor = 0; cursor < page.length(); cursor++) {
			if (page.startsWith("class=\"matchmain\"", cursor)) {
				matchlist.add(parseMatch(page));
			}
		}
		return matchlist;
	}
}
