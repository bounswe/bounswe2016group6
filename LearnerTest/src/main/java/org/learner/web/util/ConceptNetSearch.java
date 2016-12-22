package org.learner.web.util;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import com.fasterxml.jackson.databind.ObjectMapper;


public class ConceptNetSearch {
	static String wikiurl="http://conceptnet5.media.mit.edu/data/5.3/c/en/";
	
	
	public static ConceptNetModel conceptNetQuery(String q){
		ObjectMapper om = new ObjectMapper();
		String queryURL = wikiurl + q.replaceAll(" ", "_");
		
		System.out.println("Query URL : " + queryURL);
		URL url;
		try {
			url = new URL(queryURL);
			System.out.println("URL : " + url );
			ConceptNetModel wd = om.readValue(url, ConceptNetModel.class);
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
