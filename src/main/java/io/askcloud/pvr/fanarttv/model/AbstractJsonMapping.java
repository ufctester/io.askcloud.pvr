/*
 *      Copyright (c) 2004-2016 Stuart Boston
 *
 *      This file is part of the FanartTV API.
 *
 *      The FanartTV API is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      any later version.
 *
 *      The FanartTV API is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with the FanartTV API.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package io.askcloud.pvr.fanarttv.model;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.askcloud.pvr.api.pvr.PlexPVRManager;

import java.io.Serializable;
import java.util.logging.Logger;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
/**
 * Abstract class to handle any unknown properties by outputting a log message
 *
 * @author stuart.boston
 */
public abstract class AbstractJsonMapping implements Serializable {

	private static Logger log = PlexPVRManager.getInstance().getLogger();
    /*
     * Error fields
     */
    @JsonProperty("status")
    private String status;
    @JsonProperty("error message")
    private String errorMessage;
    private boolean error = Boolean.FALSE;

    /**
     * Handle unknown properties and print a message
     *
     * @param key
     * @param value
     */
    @JsonAnySetter
    protected void handleUnknown(String key, Object value) {
        StringBuilder unknown = new StringBuilder(this.getClass().getSimpleName());
        unknown.append(": Unknown property='").append(key);
        unknown.append("' value='").append(value).append("'");

        log.info(unknown.toString());
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    /**
     * Get the status of the request
     *
     * @return
     */
    public String getStatus() {
        return status;
    }

    /**
     * Set the status of the request
     *
     * @param status
     */
    public void setStatus(String status) {
        this.status = status;
        // Set the error to true
        this.error = Boolean.TRUE;
    }

    /**
     * Get the error message associated with the request (if any)
     *
     * @return
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Set the error message
     *
     * @param errorMessage
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        // Set the error to true
        this.error = Boolean.TRUE;
    }

    /**
     * Does the request have an error?
     *
     * @return
     */
    public boolean isError() {
        return error;
    }
}
