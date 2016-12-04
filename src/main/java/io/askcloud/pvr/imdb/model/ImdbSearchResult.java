package io.askcloud.pvr.imdb.model;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.askcloud.pvr.imdb.search.SearchObject;

public class ImdbSearchResult extends AbstractJsonMapping {

    @JsonProperty("label")
    private String label = "";
    @JsonProperty("list")
    private List<SearchObject> searchObject = Collections.emptyList();

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<SearchObject> getSearchObject() {
        return searchObject;
    }

    public void setSearchObject(List<SearchObject> searchObject) {
        this.searchObject = searchObject;
    }

}
