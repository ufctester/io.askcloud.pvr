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
import io.askcloud.pvr.themoviedb.interfaces.WrapperDates;
import io.askcloud.pvr.themoviedb.interfaces.WrapperPages;

/**
 * Base class for the wrappers
 *
 * @author Stuart
 */
public class AbstractWrapperAll extends AbstractWrapperIdPages implements Identification, WrapperPages, WrapperDates {

    @JsonProperty("dates")
    private ResultDates dates = new ResultDates();

    @Override
    public ResultDates getDates() {
        return dates;
    }

    @Override
    public void setDates(ResultDates dates) {
        this.dates = dates;
    }
}
