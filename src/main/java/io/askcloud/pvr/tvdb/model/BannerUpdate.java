package io.askcloud.pvr.tvdb.model;

import java.io.Serializable;

public class BannerUpdate extends BaseUpdate implements Serializable {

    // Default serial UID
    private static final long serialVersionUID = 1L;
    private String seasonNum;
    private String format;
    private String language;
    private String path;
    private String type;

    public String getSeasonNum() {
        return seasonNum;
    }

    public String getFormat() {
        return format;
    }

    public String getLanguage() {
        return language;
    }

    public String getPath() {
        return path;
    }

    public String getType() {
        return type;
    }

    public void setSeasonNum(String seasonNum) {
        this.seasonNum = seasonNum;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setType(String type) {
        this.type = type;
    }

}
