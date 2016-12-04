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
import io.askcloud.pvr.themoviedb.model.review.Review;
import io.askcloud.pvr.themoviedb.tools.ApiUrl;
import io.askcloud.pvr.themoviedb.tools.HttpTools;
import io.askcloud.pvr.themoviedb.tools.MethodBase;
import io.askcloud.pvr.themoviedb.tools.Param;
import io.askcloud.pvr.themoviedb.tools.TmdbParameters;

import static io.askcloud.pvr.themoviedb.methods.AbstractMethod.MAPPER;

import java.io.IOException;
import java.net.URL;
import org.yamj.api.common.exception.ApiExceptionType;

/**
 * Class to hold the Review Methods
 *
 * @author stuart.boston
 */
public class TmdbReviews extends AbstractMethod {

    /**
     * Constructor
     *
     * @param apiKey
     * @param httpTools
     */
    public TmdbReviews(String apiKey, HttpTools httpTools) {
        super(apiKey, httpTools);
    }

    /**
     * Get the full details of a review by ID.
     *
     * @param reviewId
     * @return
     * @throws MovieDbException
     */
    public Review getReview(String reviewId) throws MovieDbException {
        TmdbParameters parameters = new TmdbParameters();
        parameters.add(Param.ID, reviewId);

        URL url = new ApiUrl(apiKey, MethodBase.REVIEW).buildUrl(parameters);
        String webpage = httpTools.getRequest(url);

        try {
            return MAPPER.readValue(webpage, Review.class);
        } catch (IOException ex) {
            throw new MovieDbException(ApiExceptionType.MAPPING_FAILED, "Failed to get review", url, ex);
        }
    }

}
