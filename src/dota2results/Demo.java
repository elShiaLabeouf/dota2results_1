package dota2results;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dota2results.parser.IndexOfParser;
import dota2results.parser.RegexpParser;

public class Demo {

	

	public static void main(String[] args) throws InterruptedException {

		PageReader pr = new PageReader();
		String s = "";
		String s2 = "";

		try {
			s = pr.getSource("https://dota2lounge.com/");
			s2 = pr.getSource("https://dota2lounge.com/");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		RegexpParser rp = new RegexpParser();
		IndexOfParser iop = new IndexOfParser();
		
		
		
		long iopTimer = System.nanoTime();
		List<Match> matchlist2 = iop.parse(s);
		iopTimer = System.nanoTime() - iopTimer;
		
		long regexpTimer = System.nanoTime();
		List<Match> matchlist = rp.parse(s2);
		regexpTimer = System.nanoTime() - regexpTimer;
		
		System.out.printf("%14s = %d", "Regexp time", regexpTimer);
		System.out.println();
		System.out.printf("%14s = %d", "IndexOf time", iopTimer);
		System.out.println();
		
		DBConnector dbc = new DBConnector();
		
		for (Match i : matchlist) {
			System.out.print(i.id + "  ");
			dbc.insert(i);
		}
		
		System.out.println();
		for (Match i : matchlist2) {
			System.out.print(i.id + "  ");
		}
		
		dbc.disconnectFromDB();
		
		
	}

	

}
