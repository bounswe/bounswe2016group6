package org.learner.web.util;

import java.util.List;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown=true)
public class WikidataEntity {
	
	@JsonIgnore
	String searchinfo;
	
	public String getSearchinfo() {
		return searchinfo;
	}
	public void setSearchinfo(String searchinfo) {
		this.searchinfo = searchinfo;
	}
	
	List<WikidataSearchModel> search;
	public List<WikidataSearchModel> getSearch() {
		return search;
	}
	public void setSearch(List<WikidataSearchModel> search) {
		this.search = search;
	}
	
	public String getSuccess() {
		return success;
	}
	public void setSuccess(String success) {
		this.success = success;
	}
	String success;
}
