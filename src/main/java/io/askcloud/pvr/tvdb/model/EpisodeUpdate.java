package io.askcloud.pvr.tvdb.model;

import java.io.Serializable;

public class EpisodeUpdate extends BaseUpdate implements Serializable {

    // Default serial UID
    private static final long serialVersionUID = 1L;
    private String episodeId;

    public String getEpisodeId() {
        return episodeId;
    }

    public void setEpisodeId(String episodeId) {
        this.episodeId = episodeId;
    }

}
