package io.askcloud.pvr.tvdb.model;

import java.io.Serializable;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.lang3.math.NumberUtils;

public class BaseUpdate implements Serializable {

    // Default serial UID
    private static final long serialVersionUID = 1L;
    private Integer seriesId;
    private String time;

    public Integer getSeriesId() {
        return seriesId;
    }

    public String getTime() {
        return time;
    }

    public void setSeriesId(int id) {
        this.seriesId = id;
    }

    public void setSeriesId(String id) {
        this.seriesId = NumberUtils.toInt(id, 0);
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
