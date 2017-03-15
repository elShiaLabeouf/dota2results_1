package dota2results;

import java.util.Date;

public class Match {
	
	public long id;
	public Date when;
	public String where;
	public String team1;
	public String team2;
	public byte result;
	public long time;
	
	public Match(long id, Date when, String where, String team1, String team2, byte result, long time){
		this.id = id;
		this.result = result;
		this.team1 = team1;
		this.team2 = team2;
		this.time = time;
		this.when = when;
		this.where = where;
	}
	
	public void printMatch(){
		System.out.println("id: " + id);
		System.out.println("when: " + when);
		System.out.println("where: " + where);
		System.out.println("team1: " + team1);
		System.out.println("team2: " + team2);
		System.out.println("result: " + result);
		System.out.println("time: " + time);
	}
}
