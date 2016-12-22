package org.learner.web.util;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown=true)
public class WikidataSearchModel {

    String id ;
    String concepturi;
    String  url ; 
    String  title ; 
    String  label ;  
    String  description ;
    @JsonIgnore
    String pageid;
    @JsonIgnore
    List<String> match;
	public List<String> getMatch() {
		return match;
	}
	public String getPageid() {
		return pageid;
	}
	public void setPageid(String pageid) {
		this.pageid = pageid;
	}
	public void setMatch(List<String> match) {
		this.match = match;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getConcepturi() {
		return concepturi;
	}
	public void setConcepturi(String concepturi) {
		this.concepturi = concepturi;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	} 

}
