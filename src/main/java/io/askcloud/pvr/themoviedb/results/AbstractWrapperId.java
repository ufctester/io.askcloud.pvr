/*
 *      Copyright (c) 2004-2016 Stuart Boston
 *
 *      This file is part of TheMovieDB API.
 *
 *      TheMovieDB API is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      any later version.
 *
 *      TheMovieDB API is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with TheMovieDB API.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package io.askcloud.pvr.themoviedb.results;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.askcloud.pvr.themoviedb.interfaces.Identification;

/**
 * Base class for the wrappers
 *
 * @author Stuart
 */
public class AbstractWrapperId extends AbstractWrapperBase implements Identification {

    @JsonProperty("id")
    private int id;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Copy the wrapper values to the results
     *
     * @param results
     */
    @Override
    public void setResultProperties(AbstractWrapperIdPages results) {
        super.setResultProperties(results);
        results.setId(id);
    }

}
