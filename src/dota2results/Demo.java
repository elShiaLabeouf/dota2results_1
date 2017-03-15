package dota2results;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Demo {

	public static Match parseMatch(String s, int cursor) {

		int matchCursor = cursor;
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

		String where = s.substring(s.indexOf("eventm\">", cursor) + 8,
				s.indexOf("<", s.indexOf("eventm\">", cursor) + 8));
		cursor = s.indexOf("<", s.indexOf("eventm\">", cursor) + 8);

		long id = Long.parseLong(s.substring(s.indexOf("match?m=", cursor) + 8,
				s.indexOf("\">", s.indexOf("match?m=", cursor) + 8)));
		cursor = s.indexOf("\">", s.indexOf("match?m=", cursor) + 8);

		byte result = 0;
		int resultCursor = s.indexOf("img/won.png", cursor);

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

		if ((resultCursor > matchCursor) && (resultCursor < team1cursor)) {
			result = 1;
		} else if ((resultCursor > matchCursor) && (resultCursor > team1cursor)
				&& (resultCursor < team2cursor)) {
			result = 2;
		}

		long time = 0;

		return new Match(id, when, where, team1, team2, result, time);
	}

	public static void main(String[] args) {

		PageReader pr = new PageReader();
		String s = "";
		List<Match> matchlist = new ArrayList<>();

		try {
			s = pr.getSource("https://dota2lounge.com/");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (int cursor = 0; cursor < s.length(); cursor++) {
			if (s.startsWith("class=\"matchmain\"", cursor)) {
				matchlist.add(parseMatch(s, cursor));
			}
		}

		DBConnector dbcon = new DBConnector();
		for (int i = 0; i < matchlist.size(); i++) {
			if (matchlist.get(i).result > 0) {
				try {
					dbcon.insert(matchlist.get(i));
				} catch (com.mongodb.MongoWriteException ex) {
					// this match is already exists in db
				}

			}

		}
		// dbcon.disconnectFromDB();
		dbcon.select();

	}

}
