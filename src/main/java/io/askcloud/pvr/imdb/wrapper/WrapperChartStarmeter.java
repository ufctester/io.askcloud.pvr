package io.askcloud.pvr.imdb.wrapper;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.askcloud.pvr.imdb.model.AbstractJsonMapping;
import io.askcloud.pvr.imdb.model.ImdbChartStarmeter;

/**
 * JSON Wrapper class for the response from the API
 *
 * Not intended for use outside of the API
 *
 * @author stuart.boston
 */
@JsonIgnoreProperties({"@meta", "exp", "copyright", "@type", "db"})
public class WrapperChartStarmeter extends AbstractJsonMapping {

    @JsonProperty("data")
    private WrapperChartStarmeter data;
    @JsonProperty("list")
    private List<ImdbChartStarmeter> chartStarmeter = Collections.emptyList();

    public WrapperChartStarmeter getData() {
        return data;
    }

    public void setData(WrapperChartStarmeter data) {
        this.data = data;
    }

    public List<ImdbChartStarmeter> getChartStarmeter() {
        return chartStarmeter;
    }

    public void setChartStarmeter(List<ImdbChartStarmeter> chartStarmeter) {
        this.chartStarmeter = chartStarmeter;
    }
}
