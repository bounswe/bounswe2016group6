package org.learner.web.util;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import com.fasterxml.jackson.databind.ObjectMapper;


public class WikidataSearch {
	static String wikiurl="https://www.wikidata.org/w/api.php?action=wbsearchentities&search=";
	
	static String wikiurl1 = "/w/api.php?action=wbsearchentities&search=";
	static String wikiurl2="&language=en&format=json";
	
	public static WikidataEntity wikidataQuery(String q){
		ObjectMapper om = new ObjectMapper();
		String queryURL = wikiurl + q.replaceAll(" ", "%20") + wikiurl2;
		System.out.println("Query URL : " + queryURL);
		//URI uri;
		URL url;
		try {
			//uri = new URI("https",
			//				"www.wikidata.org",
			//				queryURL,
			//				null);
			url = new URL(queryURL);
			System.out.println("URL : " + url );
			WikidataEntity wd = om.readValue(url, WikidataEntity.class);
			return wd;
		} catch (MalformedURLException e) {
			System.out.println("Malformed URL");
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
