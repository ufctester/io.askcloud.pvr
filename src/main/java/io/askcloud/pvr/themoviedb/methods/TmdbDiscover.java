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
package io.askcloud.pvr.themoviedb.methods;

import io.askcloud.pvr.themoviedb.MovieDbException;
import io.askcloud.pvr.themoviedb.model.discover.Discover;
import io.askcloud.pvr.themoviedb.model.movie.MovieBasic;
import io.askcloud.pvr.themoviedb.model.tv.TVBasic;
import io.askcloud.pvr.themoviedb.results.ResultList;
import io.askcloud.pvr.themoviedb.results.WrapperGenericList;
import io.askcloud.pvr.themoviedb.tools.ApiUrl;
import io.askcloud.pvr.themoviedb.tools.HttpTools;
import io.askcloud.pvr.themoviedb.tools.MethodBase;
import io.askcloud.pvr.themoviedb.tools.MethodSub;

import java.net.URL;

/**
 * Class to hold the Discover Methods
 *
 * @author stuart.boston
 */
public class TmdbDiscover extends AbstractMethod {

    /**
     * Constructor
     *
     * @param apiKey
     * @param httpTools
     */
    public TmdbDiscover(String apiKey, HttpTools httpTools) {
        super(apiKey, httpTools);
    }

    /**
     * Discover movies by different types of data like average rating, number of votes, genres and certifications.
     *
     * @param discover A discover object containing the search criteria required
     * @return
     * @throws MovieDbException
     */
    public ResultList<MovieBasic> getDiscoverMovies(Discover discover) throws MovieDbException {
        URL url = new ApiUrl(apiKey, MethodBase.DISCOVER).subMethod(MethodSub.MOVIE).buildUrl(discover.getParams());
        String webpage = httpTools.getRequest(url);
        WrapperGenericList<MovieBasic> wrapper = processWrapper(getTypeReference(MovieBasic.class), url, webpage);
        return wrapper.getResultsList();
    }

    /**
     * Discover movies by different types of data like average rating, number of votes, genres and certifications.
     *
     * @param discover A discover object containing the search criteria required
     * @return
     * @throws MovieDbException
     */
    public ResultList<TVBasic> getDiscoverTV(Discover discover) throws MovieDbException {
        URL url = new ApiUrl(apiKey, MethodBase.DISCOVER).subMethod(MethodSub.TV).buildUrl(discover.getParams());
        String webpage = httpTools.getRequest(url);
        WrapperGenericList<TVBasic> wrapper = processWrapper(getTypeReference(TVBasic.class), url, webpage);
        return wrapper.getResultsList();
    }
}
