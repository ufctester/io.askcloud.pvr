package io.askcloud.pvr.imdb.wrapper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.askcloud.pvr.imdb.model.AbstractJsonMapping;
import io.askcloud.pvr.imdb.model.ImdbList;

import java.util.Collections;
import java.util.List;

@JsonIgnoreProperties({"@meta", "exp", "copyright", "@type", "db"})
public class WrapperList extends AbstractJsonMapping {

    @JsonProperty("label")
    private String label = "";
    @JsonProperty("list")
    private List<ImdbList> imdbList = Collections.emptyList();

    public List<ImdbList> getImdbList() {
        return imdbList;
    }

    public void setImdbList(List<ImdbList> imdbList) {
        this.imdbList = imdbList;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
