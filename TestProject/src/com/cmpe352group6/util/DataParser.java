package com.cmpe352group6.util;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import java.util.ArrayList;

public class DataParser {

	/** Parses the ResultSet results with respect to the given headers in headers ArrayList
	 * and returns the data in a specific format as specified below:
	 * 
	 * For 4 header rows, we will have
	 * 
	 * elem1||elem2||elem3||elem4&&elem5||elem6||elem7||elem8&&...
	 * 
	 * @param results Jena ResultSet object
	 * @param headers Headers of the records in the given ResultSet object.
	 * @return Data in the specified format above.
	 */
	public static String jenaToData(ResultSet results, ArrayList<String> headers) {
		StringBuilder builder = new StringBuilder("");
		while (results.hasNext()) {
			QuerySolution next = results.nextSolution();
			String x = next.get(headers.get(0)).toString();
			if (x.contains("@")) {
				System.out.println("here");
				x = x.substring(0, x.indexOf('@'));
			}
			if (x.contains("Point(")) {
				x = x.substring(x.indexOf('(') + 1, x.indexOf(')'));
				x = x.replace(" ", "||");
			}
			builder.append(x);
			for (String s : headers.subList(1, headers.size())) {
				builder.append("||");
				x = next.get(s).toString();
				if (x.contains("@")) {
					System.out.println("here");
					x = x.substring(0, x.indexOf('@'));
				}
				if (x.contains("Point(")) {
					x = x.substring(x.indexOf('(') + 1, x.indexOf(')'));
					x = x.replace(" ", "||");
				}
				builder.append(x);
			}
			builder.append("&&");
		}
		return builder.toString();
	}
}
