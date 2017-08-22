package dota2results;

import java.util.Date;
import java.util.Objects;

public class Match {

	public long id;
	public Date when;
	public String where;
	public String team1;
	public String team2;
	public String result;
	public long time;

	public Match(long id, Date when, String where, String team1, String team2,
			String result, long time) {
		this.id = id;
		this.result = result;
		this.team1 = team1;
		this.team2 = team2;
		this.time = time;
		this.when = when;
		this.where = where;
	}

	public void printMatch() {
		System.out.println("id: " + id);
		System.out.println("when: " + when);
		System.out.println("where: " + where);
		System.out.println("team1: " + team1);
		System.out.println("team2: " + team2);
		System.out.println("result: " + result);
		System.out.println("time: " + time);
	}
	
	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Match match = (Match) o;
        return Objects.equals(id, match.id) &&
        		Objects.equals(when, match.when) &&
        		Objects.equals(where, match.where) &&
        		Objects.equals(team1, match.team1) &&
        		Objects.equals(team2, match.team2) &&
        		Objects.equals(result, match.result) &&
        		Objects.equals(time, match.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, when, where, team1, team2, result, time);
    }
}
