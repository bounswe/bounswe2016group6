package org.learner.web.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown=true)
public class ConceptNetModel {
	
	List<ConceptNetEdge> edges;
	
	int numFound;
	
	
	public List<ConceptNetEdge> getEdges() {
		return edges;
	}
	public void setEdges(List<ConceptNetEdge> edges) {
		this.edges = edges;
	}
	public int getNumFound() {
		return numFound;
	}
	public void setNumFound(int numFound) {
		this.numFound = numFound;
	}

	
	public HashSet<String> retrieveConcepts(String q){
		
		HashSet<String> originalSet = new HashSet<String>();
		
		
		for(ConceptNetEdge cnedge: this.edges){
			int ei = cnedge.getEnd().lastIndexOf("/")+1;
			String end = cnedge.getEnd().substring(ei).replaceAll("_", " ");
			
			int si = cnedge.getStart().lastIndexOf("/")+1;
			String start = cnedge.getStart().substring(si).replaceAll("_", " ");
			
			int ri = cnedge.getRel().lastIndexOf("/")+1;
			String rel = cnedge.getRel().substring(ri).replaceAll("_", " ");
			
			System.out.println("Start : " + start + " , End : " + end + " ,Rel : " + rel + " Weight : " + cnedge.getWeigth());
			
			String related = end.equalsIgnoreCase(q) ? start : end ;
			String originalform =  end.equalsIgnoreCase(q) ? cnedge.getStart() : cnedge.getEnd();
			
			originalSet.add(originalform);
			
			System.out.println("Related : " + related);
		}
		
		return originalSet;
	}
	
}
