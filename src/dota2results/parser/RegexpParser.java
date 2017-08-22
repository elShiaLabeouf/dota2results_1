package dota2results.parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dota2results.Match;

public class RegexpParser implements Parser {

	@Override
	public List<Match> parse(String page)  {
		List<Match> tagValues = new ArrayList<>();
	    Pattern p = Pattern.compile("\\<div class=\"matchheader\"\\>([\\w\\W\\s]*?)\\</a", Pattern.DOTALL);
	    Matcher m = p.matcher(page);
	    while (m.find()) {
	        try {
				tagValues.add(parseMatch(m.group(1)));
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.out.println(m.group(1));
			}
	    }
	    return tagValues;
	}
	
	private static Date getMatchTime(String s) {
		Pattern p = Pattern.compile("(match-time\"\\>)(.*?)(\\</span)");
		Matcher m = p.matcher(s);
		m.find();
		String sWhen = "";
		try {
			sWhen = m.group(2);
		} catch(IllegalStateException e) {
			System.out.println(s);
		}
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
		Pattern p = Pattern.compile("(eventm\"\\>)(.*?)(\\<)");
		Matcher m = p.matcher(s);
		m.find();
		String where = null;
		try {
			where = m.group(2);
		} catch (IllegalStateException ex) {
			System.out.println(s);
		}
		return where;
	}
	
	private static long getMatchId(String s) {
		Pattern p = Pattern.compile("(match\\?m=)(\\d+)(\"\\>)");
		Matcher m = p.matcher(s);
		m.find();
		long id = 0;
		try {
			id = Long.parseLong(m.group(2));
		} catch (Exception ex) {
			System.out.println(s);
		}
		return id;
	}
	
	private static String[] getTeams(String s) {
		Pattern p = Pattern.compile("(\\<div class=\"teamtext\"\\>\\s+\\<b\\>)(.+?)(\\</b\\>\\<br\\>)");
		Matcher m = p.matcher(s);
		m.find();
		String team1 = m.group(2);
		m.find();
		String team2 = m.group(2);
		
		p = Pattern.compile("(won\\.png).+?(teamtext\"\\>\\s+\\<b\\>)(.+?)(\\</b\\>\\<br\\>)");
		m = p.matcher(s);
		String winner = "TBD";
		if (m.find()) winner = m.group(3);
		
		return new String[] {team1, team2, winner};
	}
	
	private static Match parseMatch(String s) throws InterruptedException {

		long id = getMatchId(s);
		
		Date when = getMatchTime(s);
		
		String where = getEventName(s);

		String[] teams = getTeams(s);		 

		long time = 0;

		return new Match(id, when, where, teams[0], teams[1], teams[2], time);
	}
	
}
