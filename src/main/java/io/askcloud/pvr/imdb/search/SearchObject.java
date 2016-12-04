package io.askcloud.pvr.imdb.search;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.askcloud.pvr.imdb.model.AbstractJsonMapping;
import io.askcloud.pvr.imdb.model.ImdbImageDetails;

public class SearchObject extends AbstractJsonMapping {

    @JsonProperty("image")
    private ImdbImageDetails image;

    public ImdbImageDetails getImage() {
        return image;
    }

    public void setImage(ImdbImageDetails image) {
        this.image = image;
    }
}
