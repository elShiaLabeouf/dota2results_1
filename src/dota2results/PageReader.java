package dota2results;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

public class PageReader {

	public String getSource(String webpage) throws MalformedURLException,
			IOException {

		BufferedReader br = null;
		StringBuilder sb = null;

		URLConnection connection = new URL(webpage).openConnection();
		connection
				.setRequestProperty(
						"User-Agent",
						"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
		connection.connect();
		
		
		try {

			br = new BufferedReader(new InputStreamReader(
					connection.getInputStream(), Charset.forName("UTF-8")));

			String line;

			sb = new StringBuilder();

			while ((line = br.readLine()) != null) {

				sb.append(line);
				sb.append(System.lineSeparator());
			}

		} finally {

			if (br != null) {
				br.close();
			}
		}
		return sb.toString();
	}
}
