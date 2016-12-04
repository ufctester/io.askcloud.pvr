/*
 *      Copyright (C) 2005-2013 Team XBMC
 *      http://xbmc.org
 *
 *  This Program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2, or (at your option)
 *  any later version.
 *
 *  This Program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with XBMC Remote; see the file license.  If not, write to
 *  the Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139, USA.
 *  http://www.gnu.org/copyleft/gpl.html
 *
 */
package io.askcloud.pvr.kodi.jsonrpc.api.call;

import java.util.ArrayList;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;

import io.askcloud.pvr.kodi.jsonrpc.api.AbstractCall;
import io.askcloud.pvr.kodi.jsonrpc.api.AbstractModel;
import io.askcloud.pvr.kodi.jsonrpc.model.AudioModel;
import io.askcloud.pvr.kodi.jsonrpc.model.LibraryModel;
import io.askcloud.pvr.kodi.jsonrpc.model.ListModel;

public final class AudioLibrary {

  /**
   * Cleans the audio library from non-existent items.
   * <p/>
   * This class represents the API method <tt>AudioLibrary.Clean</tt>
   * <p/>
   * <i>This class was generated automatically from XBMC's JSON-RPC introspect.</i>
   */
  public static class Clean extends AbstractCall<String> {
    public final static String API_TYPE = "AudioLibrary.Clean";

    /**
     * Cleans the audio library from non-existent items.
     * 
     * @param showdialogs
     *          Whether or not to show the progress bar or any other GUI dialog.
     */
    public Clean(Boolean showdialogs) {
      super();
      addParameter("showdialogs", showdialogs);
    }

    @Override
    protected String parseOne(JsonNode node) {
      return node.getTextValue();
    }

    @Override
    public String getName() {
      return API_TYPE;
    }

    @Override
    protected boolean returnsList() {
      return false;
    }
  }

  /**
   * Exports all items from the audio library.
   * <p/>
   * This class represents the API method <tt>AudioLibrary.Export</tt>
   * <p/>
   * <i>This class was generated automatically from XBMC's JSON-RPC introspect.</i>
   */
  public static class Export extends AbstractCall<String> {
    public final static String API_TYPE = "AudioLibrary.Export";

    /**
     * Exports all items from the audio library.
     * 
     * @param options
     */
    public Export(OptionsPath options) {
      super();
      addParameter("options", options);
    }

    /**
     * Exports all items from the audio library.
     * 
     * @param options
     */
    public Export(OptionsImagesOverwrite options) {
      super();
      addParameter("options", options);
    }

    @Override
    protected String parseOne(JsonNode node) {
      return node.getTextValue();
    }

    @Override
    public String getName() {
      return API_TYPE;
    }

    @Override
    protected boolean returnsList() {
      return false;
    }

    /**
     * Note: This class is used as parameter only.<br/>
     * <i>This class was generated automatically from XBMC's JSON-RPC introspect.</i>
     */
    public static class OptionsPath extends AbstractModel {

      // field names
      public static final String PATH = "path";

      // class members
      public final String        path;

      /**
       * @param path
       */
      public OptionsPath(String path) {
        this.path = path;
      }

      @Override
      public JsonNode toJsonNode() {
        final ObjectNode node = OM.createObjectNode();
        node.put(PATH, path);
        return node;
      }

    }

    /**
     * Note: This class is used as parameter only.<br/>
     * <i>This class was generated automatically from XBMC's JSON-RPC introspect.</i>
     */
    public static class OptionsImagesOverwrite extends AbstractModel {

      // field names
      public static final String IMAGES    = "images";
      public static final String OVERWRITE = "overwrite";

      // class members
      public final Boolean       images;
      public final Boolean       overwrite;

      /**
       * @param images
       * @param overwrite
       */
      public OptionsImagesOverwrite(Boolean images, Boolean overwrite) {
        this.images = images;
        this.overwrite = overwrite;
      }

      @Override
      public JsonNode toJsonNode() {
        final ObjectNode node = OM.createObjectNode();
        node.put(IMAGES, images);
        node.put(OVERWRITE, overwrite);
        return node;
      }
    }
  }

  /**
   * Retrieve details about a specific album.
   * <p/>
   * This class represents the API method <tt>AudioLibrary.GetAlbumDetails</tt>
   * <p/>
   * <i>This class was generated automatically from XBMC's JSON-RPC introspect.</i>
   */
  public static class GetAlbumDetails extends AbstractCall<AudioModel.AlbumDetail> {
    public final static String API_TYPE = "AudioLibrary.GetAlbumDetails";

    public final static String RESULT   = "albumdetails";

    /**
     * Retrieve details about a specific album.
     * 
     * @param albumid
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>description</tt>, <tt>artist</tt>, <tt>genre</tt>, <tt>theme</tt>, <tt>mood</tt>, <tt>style</tt>,
     *          <tt>type</tt>, <tt>albumlabel</tt>, <tt>rating</tt>, <tt>year</tt>, <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>,
     *          <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>playcount</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>compilation</tt>, <tt>releasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.AlbumFields}.
     */
    public GetAlbumDetails(Integer albumid, String... properties) {
      super();
      addParameter("albumid", albumid);
      addParameter("properties", properties);
    }

    @Override
    protected AudioModel.AlbumDetail parseOne(JsonNode node) {
      return new AudioModel.AlbumDetail((ObjectNode) node.get(RESULT));
    }

    @Override
    public String getName() {
      return API_TYPE;
    }

    @Override
    protected boolean returnsList() {
      return false;
    }
  }

  /**
   * Retrieve all albums from specified artist or genre.
   * <p/>
   * This class represents the API method <tt>AudioLibrary.GetAlbums</tt>
   * <p/>
   * <i>This class was generated automatically from XBMC's JSON-RPC introspect.</i>
   */
  public static class GetAlbums extends AbstractCall<AudioModel.AlbumDetail> {
    public final static String API_TYPE = "AudioLibrary.GetAlbums";

    public final static String RESULT   = "albums";

    /**
     * Retrieve all albums from specified artist or genre.
     * 
     * @param limits
     * @param sort
     * @param filter
     * @param includesingles
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>description</tt>, <tt>artist</tt>, <tt>genre</tt>, <tt>theme</tt>, <tt>mood</tt>, <tt>style</tt>,
     *          <tt>type</tt>, <tt>albumlabel</tt>, <tt>rating</tt>, <tt>year</tt>, <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>,
     *          <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>playcount</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>compilation</tt>, <tt>releasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.AlbumFields}.
     */
    public GetAlbums(ListModel.Limits limits, ListModel.Sort sort, FilterGenreId filter, Boolean includesingles, String... properties) {
      super();
      addParameter("limits", limits);
      addParameter("sort", sort);
      addParameter("filter", filter);
      addParameter("includesingles", includesingles);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all albums from specified artist or genre.
     * 
     * @param limits
     * @param sort
     * @param filter
     * @param includesingles
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>description</tt>, <tt>artist</tt>, <tt>genre</tt>, <tt>theme</tt>, <tt>mood</tt>, <tt>style</tt>,
     *          <tt>type</tt>, <tt>albumlabel</tt>, <tt>rating</tt>, <tt>year</tt>, <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>,
     *          <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>playcount</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>compilation</tt>, <tt>releasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.AlbumFields}.
     */
    public GetAlbums(ListModel.Limits limits, ListModel.Sort sort, FilterGenre filter, Boolean includesingles, String... properties) {
      super();
      addParameter("limits", limits);
      addParameter("sort", sort);
      addParameter("filter", filter);
      addParameter("includesingles", includesingles);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all albums from specified artist or genre.
     * 
     * @param limits
     * @param sort
     * @param filter
     * @param includesingles
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>description</tt>, <tt>artist</tt>, <tt>genre</tt>, <tt>theme</tt>, <tt>mood</tt>, <tt>style</tt>,
     *          <tt>type</tt>, <tt>albumlabel</tt>, <tt>rating</tt>, <tt>year</tt>, <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>,
     *          <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>playcount</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>compilation</tt>, <tt>releasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.AlbumFields}.
     */
    public GetAlbums(ListModel.Limits limits, ListModel.Sort sort, FilterArtistId filter, Boolean includesingles, String... properties) {
      super();
      addParameter("limits", limits);
      addParameter("sort", sort);
      addParameter("filter", filter);
      addParameter("includesingles", includesingles);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all albums from specified artist or genre.
     * 
     * @param limits
     * @param sort
     * @param filter
     * @param includesingles
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>description</tt>, <tt>artist</tt>, <tt>genre</tt>, <tt>theme</tt>, <tt>mood</tt>, <tt>style</tt>,
     *          <tt>type</tt>, <tt>albumlabel</tt>, <tt>rating</tt>, <tt>year</tt>, <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>,
     *          <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>playcount</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>compilation</tt>, <tt>releasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.AlbumFields}.
     */
    public GetAlbums(ListModel.Limits limits, ListModel.Sort sort, FilterArtist filter, Boolean includesingles, String... properties) {
      super();
      addParameter("limits", limits);
      addParameter("sort", sort);
      addParameter("filter", filter);
      addParameter("includesingles", includesingles);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all albums from specified artist or genre.
     * 
     * @param limits
     * @param sort
     * @param filter
     * @param includesingles
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>description</tt>, <tt>artist</tt>, <tt>genre</tt>, <tt>theme</tt>, <tt>mood</tt>, <tt>style</tt>,
     *          <tt>type</tt>, <tt>albumlabel</tt>, <tt>rating</tt>, <tt>year</tt>, <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>,
     *          <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>playcount</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>compilation</tt>, <tt>releasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.AlbumFields}.
     */
    public GetAlbums(ListModel.Limits limits, ListModel.Sort sort, ListModel.AlbumFilter filter, Boolean includesingles, String... properties) {
      super();
      addParameter("limits", limits);
      addParameter("sort", sort);
      addParameter("filter", filter);
      addParameter("includesingles", includesingles);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all albums from specified artist or genre.
     * 
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>description</tt>, <tt>artist</tt>, <tt>genre</tt>, <tt>theme</tt>, <tt>mood</tt>, <tt>style</tt>,
     *          <tt>type</tt>, <tt>albumlabel</tt>, <tt>rating</tt>, <tt>year</tt>, <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>,
     *          <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>playcount</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>compilation</tt>, <tt>releasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.AlbumFields}.
     */
    public GetAlbums(String... properties) {
      super();
      addParameter("properties", properties);
    }

    /**
     * Retrieve all albums from specified artist or genre.
     * 
     * @param limits
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>description</tt>, <tt>artist</tt>, <tt>genre</tt>, <tt>theme</tt>, <tt>mood</tt>, <tt>style</tt>,
     *          <tt>type</tt>, <tt>albumlabel</tt>, <tt>rating</tt>, <tt>year</tt>, <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>,
     *          <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>playcount</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>compilation</tt>, <tt>releasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.AlbumFields}.
     */
    public GetAlbums(ListModel.Limits limits, String... properties) {
      super();
      addParameter("limits", limits);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all albums from specified artist or genre.
     * 
     * @param sort
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>description</tt>, <tt>artist</tt>, <tt>genre</tt>, <tt>theme</tt>, <tt>mood</tt>, <tt>style</tt>,
     *          <tt>type</tt>, <tt>albumlabel</tt>, <tt>rating</tt>, <tt>year</tt>, <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>,
     *          <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>playcount</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>compilation</tt>, <tt>releasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.AlbumFields}.
     */
    public GetAlbums(ListModel.Sort sort, String... properties) {
      super();
      addParameter("sort", sort);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all albums from specified artist or genre.
     * 
     * @param limits
     * @param sort
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>description</tt>, <tt>artist</tt>, <tt>genre</tt>, <tt>theme</tt>, <tt>mood</tt>, <tt>style</tt>,
     *          <tt>type</tt>, <tt>albumlabel</tt>, <tt>rating</tt>, <tt>year</tt>, <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>,
     *          <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>playcount</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>compilation</tt>, <tt>releasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.AlbumFields}.
     */
    public GetAlbums(ListModel.Limits limits, ListModel.Sort sort, String... properties) {
      super();
      addParameter("limits", limits);
      addParameter("sort", sort);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all albums from specified artist or genre.
     * 
     * @param filter
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>description</tt>, <tt>artist</tt>, <tt>genre</tt>, <tt>theme</tt>, <tt>mood</tt>, <tt>style</tt>,
     *          <tt>type</tt>, <tt>albumlabel</tt>, <tt>rating</tt>, <tt>year</tt>, <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>,
     *          <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>playcount</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>compilation</tt>, <tt>releasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.AlbumFields}.
     */
    public GetAlbums(FilterGenreId filter, String... properties) {
      super();
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all albums from specified artist or genre.
     * 
     * @param limits
     * @param filter
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>description</tt>, <tt>artist</tt>, <tt>genre</tt>, <tt>theme</tt>, <tt>mood</tt>, <tt>style</tt>,
     *          <tt>type</tt>, <tt>albumlabel</tt>, <tt>rating</tt>, <tt>year</tt>, <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>,
     *          <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>playcount</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>. See constants at
     *          {@link AudioModel.AlbumFields}.
     */
    public GetAlbums(ListModel.Limits limits, FilterGenreId filter, String... properties) {
      super();
      addParameter("limits", limits);
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all albums from specified artist or genre.
     * 
     * @param filter
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>description</tt>, <tt>artist</tt>, <tt>genre</tt>, <tt>theme</tt>, <tt>mood</tt>, <tt>style</tt>,
     *          <tt>type</tt>, <tt>albumlabel</tt>, <tt>rating</tt>, <tt>year</tt>, <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>,
     *          <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>playcount</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>compilation</tt>, <tt>releasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.AlbumFields}.
     */
    public GetAlbums(ListModel.Sort sort, FilterGenreId filter, String... properties) {
      super();
      addParameter("sort", sort);
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all albums from specified artist or genre.
     * 
     * @param limits
     * @param sort
     * @param filter
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>description</tt>, <tt>artist</tt>, <tt>genre</tt>, <tt>theme</tt>, <tt>mood</tt>, <tt>style</tt>,
     *          <tt>type</tt>, <tt>albumlabel</tt>, <tt>rating</tt>, <tt>year</tt>, <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>,
     *          <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>playcount</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>compilation</tt>, <tt>releasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.AlbumFields}.
     */
    public GetAlbums(ListModel.Limits limits, ListModel.Sort sort, FilterGenreId filter, String... properties) {
      super();
      addParameter("limits", limits);
      addParameter("sort", sort);
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all albums from specified artist or genre.
     * 
     * @param includesingles
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>description</tt>, <tt>artist</tt>, <tt>genre</tt>, <tt>theme</tt>, <tt>mood</tt>, <tt>style</tt>,
     *          <tt>type</tt>, <tt>albumlabel</tt>, <tt>rating</tt>, <tt>year</tt>, <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>,
     *          <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>playcount</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>compilation</tt>, <tt>releasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.AlbumFields}.
     */
    public GetAlbums(Boolean includesingles, String... properties) {
      super();
      addParameter("includesingles", includesingles);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all albums from specified artist or genre.
     * 
     * @param limits
     * @param includesingles
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>description</tt>, <tt>artist</tt>, <tt>genre</tt>, <tt>theme</tt>, <tt>mood</tt>, <tt>style</tt>,
     *          <tt>type</tt>, <tt>albumlabel</tt>, <tt>rating</tt>, <tt>year</tt>, <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>,
     *          <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>playcount</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>compilation</tt>, <tt>releasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.AlbumFields}.
     */
    public GetAlbums(ListModel.Limits limits, Boolean includesingles, String... properties) {
      super();
      addParameter("limits", limits);
      addParameter("includesingles", includesingles);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all albums from specified artist or genre.
     * 
     * @param sort
     * @param includesingles
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>description</tt>, <tt>artist</tt>, <tt>genre</tt>, <tt>theme</tt>, <tt>mood</tt>, <tt>style</tt>,
     *          <tt>type</tt>, <tt>albumlabel</tt>, <tt>rating</tt>, <tt>year</tt>, <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>,
     *          <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>playcount</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>compilation</tt>, <tt>releasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.AlbumFields}.
     */
    public GetAlbums(ListModel.Sort sort, Boolean includesingles, String... properties) {
      super();
      addParameter("sort", sort);
      addParameter("includesingles", includesingles);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all albums from specified artist or genre.
     * 
     * @param limits
     * @param sort
     * @param includesingles
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>description</tt>, <tt>artist</tt>, <tt>genre</tt>, <tt>theme</tt>, <tt>mood</tt>, <tt>style</tt>,
     *          <tt>type</tt>, <tt>albumlabel</tt>, <tt>rating</tt>, <tt>year</tt>, <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>,
     *          <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>playcount</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>compilation</tt>, <tt>releasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.AlbumFields}.
     */
    public GetAlbums(ListModel.Limits limits, ListModel.Sort sort, Boolean includesingles, String... properties) {
      super();
      addParameter("limits", limits);
      addParameter("sort", sort);
      addParameter("includesingles", includesingles);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all albums from specified artist or genre.
     * 
     * @param filter
     * @param includesingles
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>description</tt>, <tt>artist</tt>, <tt>genre</tt>, <tt>theme</tt>, <tt>mood</tt>, <tt>style</tt>,
     *          <tt>type</tt>, <tt>albumlabel</tt>, <tt>rating</tt>, <tt>year</tt>, <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>,
     *          <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>playcount</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>compilation</tt>, <tt>releasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.AlbumFields}.
     */
    public GetAlbums(FilterGenreId filter, Boolean includesingles, String... properties) {
      super();
      addParameter("filter", filter);
      addParameter("includesingles", includesingles);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all albums from specified artist or genre.
     * 
     * @param limits
     * @param filter
     * @param includesingles
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>description</tt>, <tt>artist</tt>, <tt>genre</tt>, <tt>theme</tt>, <tt>mood</tt>, <tt>style</tt>,
     *          <tt>type</tt>, <tt>albumlabel</tt>, <tt>rating</tt>, <tt>year</tt>, <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>,
     *          <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>playcount</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>compilation</tt>, <tt>releasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.AlbumFields}.
     */
    public GetAlbums(ListModel.Limits limits, FilterGenreId filter, Boolean includesingles, String... properties) {
      super();
      addParameter("limits", limits);
      addParameter("filter", filter);
      addParameter("includesingles", includesingles);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all albums from specified artist or genre.
     * 
     * @param filter
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>description</tt>, <tt>artist</tt>, <tt>genre</tt>, <tt>theme</tt>, <tt>mood</tt>, <tt>style</tt>,
     *          <tt>type</tt>, <tt>albumlabel</tt>, <tt>rating</tt>, <tt>year</tt>, <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>,
     *          <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>playcount</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>compilation</tt>, <tt>releasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.AlbumFields}.
     */
    public GetAlbums(FilterGenre filter, String... properties) {
      super();
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all albums from specified artist or genre.
     * 
     * @param limits
     * @param filter
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>description</tt>, <tt>artist</tt>, <tt>genre</tt>, <tt>theme</tt>, <tt>mood</tt>, <tt>style</tt>,
     *          <tt>type</tt>, <tt>albumlabel</tt>, <tt>rating</tt>, <tt>year</tt>, <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>,
     *          <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>playcount</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>compilation</tt>, <tt>releasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.AlbumFields}.
     */
    public GetAlbums(ListModel.Limits limits, FilterGenre filter, String... properties) {
      super();
      addParameter("limits", limits);
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all albums from specified artist or genre.
     * 
     * @param sort
     * @param filter
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>description</tt>, <tt>artist</tt>, <tt>genre</tt>, <tt>theme</tt>, <tt>mood</tt>, <tt>style</tt>,
     *          <tt>type</tt>, <tt>albumlabel</tt>, <tt>rating</tt>, <tt>year</tt>, <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>,
     *          <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>playcount</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>compilation</tt>, <tt>releasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.AlbumFields}.
     */
    public GetAlbums(ListModel.Sort sort, FilterGenre filter, String... properties) {
      super();
      addParameter("sort", sort);
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all albums from specified artist or genre.
     * 
     * @param limits
     * @param sort
     * @param filter
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>description</tt>, <tt>artist</tt>, <tt>genre</tt>, <tt>theme</tt>, <tt>mood</tt>, <tt>style</tt>,
     *          <tt>type</tt>, <tt>albumlabel</tt>, <tt>rating</tt>, <tt>year</tt>, <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>,
     *          <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>playcount</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>compilation</tt>, <tt>releasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.AlbumFields}.
     */
    public GetAlbums(ListModel.Limits limits, ListModel.Sort sort, FilterGenre filter, String... properties) {
      super();
      addParameter("limits", limits);
      addParameter("sort", sort);
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all albums from specified artist or genre.
     * 
     * @param filter
     * @param includesingles
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>description</tt>, <tt>artist</tt>, <tt>genre</tt>, <tt>theme</tt>, <tt>mood</tt>, <tt>style</tt>,
     *          <tt>type</tt>, <tt>albumlabel</tt>, <tt>rating</tt>, <tt>year</tt>, <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>,
     *          <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>playcount</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>compilation</tt>, <tt>releasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.AlbumFields}.
     */
    public GetAlbums(FilterGenre filter, Boolean includesingles, String... properties) {
      super();
      addParameter("filter", filter);
      addParameter("includesingles", includesingles);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all albums from specified artist or genre.
     * 
     * @param limits
     * @param filter
     * @param includesingles
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>description</tt>, <tt>artist</tt>, <tt>genre</tt>, <tt>theme</tt>, <tt>mood</tt>, <tt>style</tt>,
     *          <tt>type</tt>, <tt>albumlabel</tt>, <tt>rating</tt>, <tt>year</tt>, <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>,
     *          <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>playcount</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>compilation</tt>, <tt>releasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.AlbumFields}.
     */
    public GetAlbums(ListModel.Limits limits, FilterGenre filter, Boolean includesingles, String... properties) {
      super();
      addParameter("limits", limits);
      addParameter("filter", filter);
      addParameter("includesingles", includesingles);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all albums from specified artist or genre.
     * 
     * @param filter
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>description</tt>, <tt>artist</tt>, <tt>genre</tt>, <tt>theme</tt>, <tt>mood</tt>, <tt>style</tt>,
     *          <tt>type</tt>, <tt>albumlabel</tt>, <tt>rating</tt>, <tt>year</tt>, <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>,
     *          <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>playcount</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>compilation</tt>, <tt>releasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.AlbumFields}.
     */
    public GetAlbums(FilterArtistId filter, String... properties) {
      super();
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all albums from specified artist or genre.
     * 
     * @param limits
     * @param filter
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>description</tt>, <tt>artist</tt>, <tt>genre</tt>, <tt>theme</tt>, <tt>mood</tt>, <tt>style</tt>,
     *          <tt>type</tt>, <tt>albumlabel</tt>, <tt>rating</tt>, <tt>year</tt>, <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>,
     *          <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>playcount</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>compilation</tt>, <tt>releasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.AlbumFields}.
     */
    public GetAlbums(ListModel.Limits limits, FilterArtistId filter, String... properties) {
      super();
      addParameter("limits", limits);
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all albums from specified artist or genre.
     * 
     * @param sort
     * @param filter
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>description</tt>, <tt>artist</tt>, <tt>genre</tt>, <tt>theme</tt>, <tt>mood</tt>, <tt>style</tt>,
     *          <tt>type</tt>, <tt>albumlabel</tt>, <tt>rating</tt>, <tt>year</tt>, <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>,
     *          <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>playcount</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>compilation</tt>, <tt>releasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.AlbumFields}.
     */
    public GetAlbums(ListModel.Sort sort, FilterArtistId filter, String... properties) {
      super();
      addParameter("sort", sort);
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all albums from specified artist or genre.
     * 
     * @param limits
     * @param sort
     * @param filter
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>description</tt>, <tt>artist</tt>, <tt>genre</tt>, <tt>theme</tt>, <tt>mood</tt>, <tt>style</tt>,
     *          <tt>type</tt>, <tt>albumlabel</tt>, <tt>rating</tt>, <tt>year</tt>, <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>,
     *          <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>playcount</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>compilation</tt>, <tt>releasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.AlbumFields}.
     */
    public GetAlbums(ListModel.Limits limits, ListModel.Sort sort, FilterArtistId filter, String... properties) {
      super();
      addParameter("limits", limits);
      addParameter("sort", sort);
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all albums from specified artist or genre.
     * 
     * @param filter
     * @param includesingles
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>description</tt>, <tt>artist</tt>, <tt>genre</tt>, <tt>theme</tt>, <tt>mood</tt>, <tt>style</tt>,
     *          <tt>type</tt>, <tt>albumlabel</tt>, <tt>rating</tt>, <tt>year</tt>, <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>,
     *          <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>playcount</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>compilation</tt>, <tt>releasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.AlbumFields}.
     */
    public GetAlbums(FilterArtistId filter, Boolean includesingles, String... properties) {
      super();
      addParameter("filter", filter);
      addParameter("includesingles", includesingles);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all albums from specified artist or genre.
     * 
     * @param limits
     * @param filter
     * @param includesingles
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>description</tt>, <tt>artist</tt>, <tt>genre</tt>, <tt>theme</tt>, <tt>mood</tt>, <tt>style</tt>,
     *          <tt>type</tt>, <tt>albumlabel</tt>, <tt>rating</tt>, <tt>year</tt>, <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>,
     *          <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>playcount</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>compilation</tt>, <tt>releasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.AlbumFields}.
     */
    public GetAlbums(ListModel.Limits limits, FilterArtistId filter, Boolean includesingles, String... properties) {
      super();
      addParameter("limits", limits);
      addParameter("filter", filter);
      addParameter("includesingles", includesingles);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all albums from specified artist or genre.
     * 
     * @param filter
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>description</tt>, <tt>artist</tt>, <tt>genre</tt>, <tt>theme</tt>, <tt>mood</tt>, <tt>style</tt>,
     *          <tt>type</tt>, <tt>albumlabel</tt>, <tt>rating</tt>, <tt>year</tt>, <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>,
     *          <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>playcount</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>compilation</tt>, <tt>releasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.AlbumFields}.
     */
    public GetAlbums(FilterArtist filter, String... properties) {
      super();
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all albums from specified artist or genre.
     * 
     * @param limits
     * @param filter
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>description</tt>, <tt>artist</tt>, <tt>genre</tt>, <tt>theme</tt>, <tt>mood</tt>, <tt>style</tt>,
     *          <tt>type</tt>, <tt>albumlabel</tt>, <tt>rating</tt>, <tt>year</tt>, <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>,
     *          <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>playcount</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>compilation</tt>, <tt>releasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.AlbumFields}.
     */
    public GetAlbums(ListModel.Limits limits, FilterArtist filter, String... properties) {
      super();
      addParameter("limits", limits);
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all albums from specified artist or genre.
     * 
     * @param sort
     * @param filter
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>description</tt>, <tt>artist</tt>, <tt>genre</tt>, <tt>theme</tt>, <tt>mood</tt>, <tt>style</tt>,
     *          <tt>type</tt>, <tt>albumlabel</tt>, <tt>rating</tt>, <tt>year</tt>, <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>,
     *          <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>playcount</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>compilation</tt>, <tt>releasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.AlbumFields}.
     */
    public GetAlbums(ListModel.Sort sort, FilterArtist filter, String... properties) {
      super();
      addParameter("sort", sort);
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all albums from specified artist or genre.
     * 
     * @param limits
     * @param sort
     * @param filter
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>description</tt>, <tt>artist</tt>, <tt>genre</tt>, <tt>theme</tt>, <tt>mood</tt>, <tt>style</tt>,
     *          <tt>type</tt>, <tt>albumlabel</tt>, <tt>rating</tt>, <tt>year</tt>, <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>,
     *          <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>playcount</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>compilation</tt>, <tt>releasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.AlbumFields}.
     */
    public GetAlbums(ListModel.Limits limits, ListModel.Sort sort, FilterArtist filter, String... properties) {
      super();
      addParameter("limits", limits);
      addParameter("sort", sort);
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all albums from specified artist or genre.
     * 
     * @param filter
     * @param includesingles
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>description</tt>, <tt>artist</tt>, <tt>genre</tt>, <tt>theme</tt>, <tt>mood</tt>, <tt>style</tt>,
     *          <tt>type</tt>, <tt>albumlabel</tt>, <tt>rating</tt>, <tt>year</tt>, <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>,
     *          <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>playcount</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>compilation</tt>, <tt>releasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.AlbumFields}.
     */
    public GetAlbums(FilterArtist filter, Boolean includesingles, String... properties) {
      super();
      addParameter("filter", filter);
      addParameter("includesingles", includesingles);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all albums from specified artist or genre.
     * 
     * @param limits
     * @param filter
     * @param includesingles
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>description</tt>, <tt>artist</tt>, <tt>genre</tt>, <tt>theme</tt>, <tt>mood</tt>, <tt>style</tt>,
     *          <tt>type</tt>, <tt>albumlabel</tt>, <tt>rating</tt>, <tt>year</tt>, <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>,
     *          <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>playcount</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>compilation</tt>, <tt>releasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.AlbumFields}.
     */
    public GetAlbums(ListModel.Limits limits, FilterArtist filter, Boolean includesingles, String... properties) {
      super();
      addParameter("limits", limits);
      addParameter("filter", filter);
      addParameter("includesingles", includesingles);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all albums from specified artist or genre.
     * 
     * @param filter
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>description</tt>, <tt>artist</tt>, <tt>genre</tt>, <tt>theme</tt>, <tt>mood</tt>, <tt>style</tt>,
     *          <tt>type</tt>, <tt>albumlabel</tt>, <tt>rating</tt>, <tt>year</tt>, <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>,
     *          <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>playcount</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>compilation</tt>, <tt>releasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.AlbumFields}.
     */
    public GetAlbums(ListModel.AlbumFilter filter, String... properties) {
      super();
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all albums from specified artist or genre.
     * 
     * @param limits
     * @param filter
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>description</tt>, <tt>artist</tt>, <tt>genre</tt>, <tt>theme</tt>, <tt>mood</tt>, <tt>style</tt>,
     *          <tt>type</tt>, <tt>albumlabel</tt>, <tt>rating</tt>, <tt>year</tt>, <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>,
     *          <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>playcount</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>compilation</tt>, <tt>releasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.AlbumFields}.
     */
    public GetAlbums(ListModel.Limits limits, ListModel.AlbumFilter filter, String... properties) {
      super();
      addParameter("limits", limits);
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all albums from specified artist or genre.
     * 
     * @param sort
     * @param filter
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>description</tt>, <tt>artist</tt>, <tt>genre</tt>, <tt>theme</tt>, <tt>mood</tt>, <tt>style</tt>,
     *          <tt>type</tt>, <tt>albumlabel</tt>, <tt>rating</tt>, <tt>year</tt>, <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>,
     *          <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>playcount</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>compilation</tt>, <tt>releasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.AlbumFields}.
     */
    public GetAlbums(ListModel.Sort sort, ListModel.AlbumFilter filter, String... properties) {
      super();
      addParameter("sort", sort);
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all albums from specified artist or genre.
     * 
     * @param limits
     * @param sort
     * @param filter
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>description</tt>, <tt>artist</tt>, <tt>genre</tt>, <tt>theme</tt>, <tt>mood</tt>, <tt>style</tt>,
     *          <tt>type</tt>, <tt>albumlabel</tt>, <tt>rating</tt>, <tt>year</tt>, <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>,
     *          <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>playcount</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>compilation</tt>, <tt>releasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.AlbumFields}.
     */
    public GetAlbums(ListModel.Limits limits, ListModel.Sort sort, ListModel.AlbumFilter filter, String... properties) {
      super();
      addParameter("limits", limits);
      addParameter("sort", sort);
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all albums from specified artist or genre.
     * 
     * @param filter
     * @param includesingles
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>description</tt>, <tt>artist</tt>, <tt>genre</tt>, <tt>theme</tt>, <tt>mood</tt>, <tt>style</tt>,
     *          <tt>type</tt>, <tt>albumlabel</tt>, <tt>rating</tt>, <tt>year</tt>, <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>,
     *          <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>playcount</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>compilation</tt>, <tt>releasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.AlbumFields}.
     */
    public GetAlbums(ListModel.AlbumFilter filter, Boolean includesingles, String... properties) {
      super();
      addParameter("filter", filter);
      addParameter("includesingles", includesingles);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all albums from specified artist or genre.
     * 
     * @param limits
     * @param filter
     * @param includesingles
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>description</tt>, <tt>artist</tt>, <tt>genre</tt>, <tt>theme</tt>, <tt>mood</tt>, <tt>style</tt>,
     *          <tt>type</tt>, <tt>albumlabel</tt>, <tt>rating</tt>, <tt>year</tt>, <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>,
     *          <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>playcount</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>compilation</tt>, <tt>releasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.AlbumFields}.
     */
    public GetAlbums(ListModel.Limits limits, ListModel.AlbumFilter filter, Boolean includesingles, String... properties) {
      super();
      addParameter("limits", limits);
      addParameter("filter", filter);
      addParameter("includesingles", includesingles);
      addParameter("properties", properties);
    }

    @Override
    protected ArrayList<AudioModel.AlbumDetail> parseMany(JsonNode node) {
      final ArrayNode albums = parseResults(node, RESULT);
      if (albums != null) {
        final ArrayList<AudioModel.AlbumDetail> ret = new ArrayList<AudioModel.AlbumDetail>(albums.size());
        for (int i = 0; i < albums.size(); i++) {
          final ObjectNode item = (ObjectNode) albums.get(i);
          ret.add(new AudioModel.AlbumDetail(item));
        }
        return ret;
      }
      else {
        return new ArrayList<AudioModel.AlbumDetail>(0);
      }
    }

    @Override
    public String getName() {
      return API_TYPE;
    }

    @Override
    protected boolean returnsList() {
      return true;
    }

    /**
     * Note: This class is used as parameter only.<br/>
     * <i>This class was generated automatically from XBMC's JSON-RPC introspect.</i>
     */
    public static class FilterGenreId extends AbstractModel {

      // field names
      public static final String GENREID = "genreid";

      // class members
      public final Integer       genreid;

      /**
       * @param genreid
       */
      public FilterGenreId(Integer genreid) {
        this.genreid = genreid;
      }

      @Override
      public JsonNode toJsonNode() {
        final ObjectNode node = OM.createObjectNode();
        node.put(GENREID, genreid);
        return node;
      }
    }

    /**
     * Note: This class is used as parameter only.<br/>
     * <i>This class was generated automatically from XBMC's JSON-RPC introspect.</i>
     */
    public static class FilterGenre extends AbstractModel {

      // field names
      public static final String GENRE = "genre";

      // class members
      public final String        genre;

      /**
       * @param genre
       */
      public FilterGenre(String genre) {
        this.genre = genre;
      }

      @Override
      public JsonNode toJsonNode() {
        final ObjectNode node = OM.createObjectNode();
        node.put(GENRE, genre);
        return node;
      }
    }

    /**
     * Note: This class is used as parameter only.<br/>
     * <i>This class was generated automatically from XBMC's JSON-RPC introspect.</i>
     */
    public static class FilterArtistId extends AbstractModel {

      // field names
      public static final String ARTISTID = "artistid";

      // class members
      public final Integer       artistid;

      /**
       * @param artistid
       */
      public FilterArtistId(Integer artistid) {
        this.artistid = artistid;
      }

      @Override
      public JsonNode toJsonNode() {
        final ObjectNode node = OM.createObjectNode();
        node.put(ARTISTID, artistid);
        return node;
      }
    }

    /**
     * Note: This class is used as parameter only.<br/>
     * <i>This class was generated automatically from XBMC's JSON-RPC introspect.</i>
     */
    public static class FilterArtist extends AbstractModel {

      // field names
      public static final String ARTIST = "artist";

      // class members
      public final String        artist;

      /**
       * @param artist
       */
      public FilterArtist(String artist) {
        this.artist = artist;
      }

      @Override
      public JsonNode toJsonNode() {
        final ObjectNode node = OM.createObjectNode();
        node.put(ARTIST, artist);
        return node;
      }
    }
  }

  /**
   * Retrieve details about a specific artist.
   * <p/>
   * This class represents the API method <tt>AudioLibrary.GetArtistDetails</tt>
   * <p/>
   * <i>This class was generated automatically from XBMC's JSON-RPC introspect.</i>
   */
  public static class GetArtistDetails extends AbstractCall<AudioModel.ArtistDetail> {
    public final static String API_TYPE = "AudioLibrary.GetArtistDetails";
    public final static String RESULT   = "artistdetails";

    /**
     * Retrieve details about a specific artist.
     * 
     * @param artistid
     * @param properties
     *          One or more of: <tt>instrument</tt>, <tt>style</tt>, <tt>mood</tt>, <tt>born</tt>, <tt>formed</tt>, <tt>description</tt>,
     *          <tt>genre</tt>, <tt>died</tt>, <tt>disbanded</tt>, <tt>yearsactive</tt>, <tt>musicbrainzartistid</tt>, <tt>fanart</tt>,
     *          <tt>thumbnail</tt>, <tt>compilationartist</tt>. See constants at {@link AudioModel.ArtistFields}.
     */
    public GetArtistDetails(Integer artistid, String... properties) {
      super();
      addParameter("artistid", artistid);
      addParameter("properties", properties);
    }

    @Override
    protected AudioModel.ArtistDetail parseOne(JsonNode node) {
      return new AudioModel.ArtistDetail((ObjectNode) node.get(RESULT));
    }

    @Override
    public String getName() {
      return API_TYPE;
    }

    @Override
    protected boolean returnsList() {
      return false;
    }
  }

  /**
   * Retrieve all artists.
   * <p/>
   * This class represents the API method <tt>AudioLibrary.GetArtists</tt>
   * <p/>
   * <i>This class was generated automatically from XBMC's JSON-RPC introspect.</i>
   */
  public static class GetArtists extends AbstractCall<AudioModel.ArtistDetail> {
    public final static String API_TYPE = "AudioLibrary.GetArtists";
    public final static String RESULT   = "artists";

    /**
     * Retrieve all artists.
     * 
     * @param albumartistsonly
     *          Whether or not to include artists only appearing in compilations. If the parameter is not passed or is passed as null the GUI setting
     *          will be used.
     * @param limits
     * @param sort
     * @param filter
     * @param properties
     *          One or more of: <tt>instrument</tt>, <tt>style</tt>, <tt>mood</tt>, <tt>born</tt>, <tt>formed</tt>, <tt>description</tt>,
     *          <tt>genre</tt>, <tt>died</tt>, <tt>disbanded</tt>, <tt>yearsactive</tt>, <tt>musicbrainzartistid</tt>, <tt>fanart</tt>,
     *          <tt>thumbnail</tt>, <tt>compilationartist</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.ArtistFields}.
     */
    public GetArtists(Boolean albumartistsonly, ListModel.Limits limits, ListModel.Sort sort, FilterGenreId filter, String... properties) {
      super();
      addParameter("albumartistsonly", albumartistsonly);
      addParameter("limits", limits);
      addParameter("sort", sort);
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all artists.
     * 
     * @param albumartistsonly
     *          Whether or not to include artists only appearing in compilations. If the parameter is not passed or is passed as null the GUI setting
     *          will be used.
     * @param limits
     * @param sort
     * @param filter
     * @param properties
     *          One or more of: <tt>instrument</tt>, <tt>style</tt>, <tt>mood</tt>, <tt>born</tt>, <tt>formed</tt>, <tt>description</tt>,
     *          <tt>genre</tt>, <tt>died</tt>, <tt>disbanded</tt>, <tt>yearsactive</tt>, <tt>musicbrainzartistid</tt>, <tt>fanart</tt>,
     *          <tt>thumbnail</tt>, <tt>compilationartist</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.ArtistFields}.
     */
    public GetArtists(Boolean albumartistsonly, ListModel.Limits limits, ListModel.Sort sort, FilterGenre filter, String... properties) {
      super();
      addParameter("albumartistsonly", albumartistsonly);
      addParameter("limits", limits);
      addParameter("sort", sort);
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all artists.
     * 
     * @param albumartistsonly
     *          Whether or not to include artists only appearing in compilations. If the parameter is not passed or is passed as null the GUI setting
     *          will be used.
     * @param limits
     * @param sort
     * @param filter
     * @param properties
     *          One or more of: <tt>instrument</tt>, <tt>style</tt>, <tt>mood</tt>, <tt>born</tt>, <tt>formed</tt>, <tt>description</tt>,
     *          <tt>genre</tt>, <tt>died</tt>, <tt>disbanded</tt>, <tt>yearsactive</tt>, <tt>musicbrainzartistid</tt>, <tt>fanart</tt>,
     *          <tt>thumbnail</tt>, <tt>compilationartist</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.ArtistFields}.
     */
    public GetArtists(Boolean albumartistsonly, ListModel.Limits limits, ListModel.Sort sort, FilterAlbumId filter, String... properties) {
      super();
      addParameter("albumartistsonly", albumartistsonly);
      addParameter("limits", limits);
      addParameter("sort", sort);
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all artists.
     * 
     * @param albumartistsonly
     *          Whether or not to include artists only appearing in compilations. If the parameter is not passed or is passed as null the GUI setting
     *          will be used.
     * @param limits
     * @param sort
     * @param filter
     * @param properties
     *          One or more of: <tt>instrument</tt>, <tt>style</tt>, <tt>mood</tt>, <tt>born</tt>, <tt>formed</tt>, <tt>description</tt>,
     *          <tt>genre</tt>, <tt>died</tt>, <tt>disbanded</tt>, <tt>yearsactive</tt>, <tt>musicbrainzartistid</tt>, <tt>fanart</tt>,
     *          <tt>thumbnail</tt>, <tt>compilationartist</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.ArtistFields}.
     */
    public GetArtists(Boolean albumartistsonly, ListModel.Limits limits, ListModel.Sort sort, FilterAlbum filter, String... properties) {
      super();
      addParameter("albumartistsonly", albumartistsonly);
      addParameter("limits", limits);
      addParameter("sort", sort);
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all artists.
     * 
     * @param albumartistsonly
     *          Whether or not to include artists only appearing in compilations. If the parameter is not passed or is passed as null the GUI setting
     *          will be used.
     * @param limits
     * @param sort
     * @param filter
     * @param properties
     *          One or more of: <tt>instrument</tt>, <tt>style</tt>, <tt>mood</tt>, <tt>born</tt>, <tt>formed</tt>, <tt>description</tt>,
     *          <tt>genre</tt>, <tt>died</tt>, <tt>disbanded</tt>, <tt>yearsactive</tt>, <tt>musicbrainzartistid</tt>, <tt>fanart</tt>,
     *          <tt>thumbnail</tt>, <tt>compilationartist</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.ArtistFields}.
     */
    public GetArtists(Boolean albumartistsonly, ListModel.Limits limits, ListModel.Sort sort, FilterSongId filter, String... properties) {
      super();
      addParameter("albumartistsonly", albumartistsonly);
      addParameter("limits", limits);
      addParameter("sort", sort);
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all artists.
     * 
     * @param albumartistsonly
     *          Whether or not to include artists only appearing in compilations. If the parameter is not passed or is passed as null the GUI setting
     *          will be used.
     * @param limits
     * @param sort
     * @param filter
     * @param properties
     *          One or more of: <tt>instrument</tt>, <tt>style</tt>, <tt>mood</tt>, <tt>born</tt>, <tt>formed</tt>, <tt>description</tt>,
     *          <tt>genre</tt>, <tt>died</tt>, <tt>disbanded</tt>, <tt>yearsactive</tt>, <tt>musicbrainzartistid</tt>, <tt>fanart</tt>,
     *          <tt>thumbnail</tt>, <tt>compilationartist</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.ArtistFields}.
     */
    public GetArtists(Boolean albumartistsonly, ListModel.Limits limits, ListModel.Sort sort, ListModel.ArtistFilter filter, String... properties) {
      super();
      addParameter("albumartistsonly", albumartistsonly);
      addParameter("limits", limits);
      addParameter("sort", sort);
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all artists.
     * 
     * @param properties
     *          One or more of: <tt>instrument</tt>, <tt>style</tt>, <tt>mood</tt>, <tt>born</tt>, <tt>formed</tt>, <tt>description</tt>,
     *          <tt>genre</tt>, <tt>died</tt>, <tt>disbanded</tt>, <tt>yearsactive</tt>, <tt>musicbrainzartistid</tt>, <tt>fanart</tt>,
     *          <tt>thumbnail</tt>, <tt>compilationartist</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.ArtistFields}.
     */
    public GetArtists(String... properties) {
      super();
      addParameter("properties", properties);
    }

    /**
     * Retrieve all artists.
     * 
     * @param albumartistsonly
     *          Whether or not to include artists only appearing in compilations. If the parameter is not passed or is passed as null the GUI setting
     *          will be used.
     * @param properties
     *          One or more of: <tt>instrument</tt>, <tt>style</tt>, <tt>mood</tt>, <tt>born</tt>, <tt>formed</tt>, <tt>description</tt>,
     *          <tt>genre</tt>, <tt>died</tt>, <tt>disbanded</tt>, <tt>yearsactive</tt>, <tt>musicbrainzartistid</tt>, <tt>fanart</tt>,
     *          <tt>thumbnail</tt>, <tt>compilationartist</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.ArtistFields}.
     */
    public GetArtists(Boolean albumartistsonly, String... properties) {
      super();
      addParameter("albumartistsonly", albumartistsonly);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all artists.
     * 
     * @param limits
     * @param properties
     *          One or more of: <tt>instrument</tt>, <tt>style</tt>, <tt>mood</tt>, <tt>born</tt>, <tt>formed</tt>, <tt>description</tt>,
     *          <tt>genre</tt>, <tt>died</tt>, <tt>disbanded</tt>, <tt>yearsactive</tt>, <tt>musicbrainzartistid</tt>, <tt>fanart</tt>,
     *          <tt>thumbnail</tt>, <tt>compilationartist</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.ArtistFields}.
     */
    public GetArtists(ListModel.Limits limits, String... properties) {
      super();
      addParameter("limits", limits);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all artists.
     * 
     * @param albumartistsonly
     *          Whether or not to include artists only appearing in compilations. If the parameter is not passed or is passed as null the GUI setting
     *          will be used.
     * @param limits
     * @param properties
     *          One or more of: <tt>instrument</tt>, <tt>style</tt>, <tt>mood</tt>, <tt>born</tt>, <tt>formed</tt>, <tt>description</tt>,
     *          <tt>genre</tt>, <tt>died</tt>, <tt>disbanded</tt>, <tt>yearsactive</tt>, <tt>musicbrainzartistid</tt>, <tt>fanart</tt>,
     *          <tt>thumbnail</tt>, <tt>compilationartist</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.ArtistFields}.
     */
    public GetArtists(Boolean albumartistsonly, ListModel.Limits limits, String... properties) {
      super();
      addParameter("albumartistsonly", albumartistsonly);
      addParameter("limits", limits);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all artists.
     * 
     * @param sort
     * @param properties
     *          One or more of: <tt>instrument</tt>, <tt>style</tt>, <tt>mood</tt>, <tt>born</tt>, <tt>formed</tt>, <tt>description</tt>,
     *          <tt>genre</tt>, <tt>died</tt>, <tt>disbanded</tt>, <tt>yearsactive</tt>, <tt>musicbrainzartistid</tt>, <tt>fanart</tt>,
     *          <tt>thumbnail</tt>, <tt>compilationartist</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.ArtistFields}.
     */
    public GetArtists(ListModel.Sort sort, String... properties) {
      super();
      addParameter("sort", sort);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all artists.
     * 
     * @param albumartistsonly
     *          Whether or not to include artists only appearing in compilations. If the parameter is not passed or is passed as null the GUI setting
     *          will be used.
     * @param sort
     * @param properties
     *          One or more of: <tt>instrument</tt>, <tt>style</tt>, <tt>mood</tt>, <tt>born</tt>, <tt>formed</tt>, <tt>description</tt>,
     *          <tt>genre</tt>, <tt>died</tt>, <tt>disbanded</tt>, <tt>yearsactive</tt>, <tt>musicbrainzartistid</tt>, <tt>fanart</tt>,
     *          <tt>thumbnail</tt>, <tt>compilationartist</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.ArtistFields}.
     */
    public GetArtists(Boolean albumartistsonly, ListModel.Sort sort, String... properties) {
      super();
      addParameter("albumartistsonly", albumartistsonly);
      addParameter("sort", sort);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all artists.
     * 
     * @param limits
     * @param sort
     * @param properties
     *          One or more of: <tt>instrument</tt>, <tt>style</tt>, <tt>mood</tt>, <tt>born</tt>, <tt>formed</tt>, <tt>description</tt>,
     *          <tt>genre</tt>, <tt>died</tt>, <tt>disbanded</tt>, <tt>yearsactive</tt>, <tt>musicbrainzartistid</tt>, <tt>fanart</tt>,
     *          <tt>thumbnail</tt>, <tt>compilationartist</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.ArtistFields}.
     */
    public GetArtists(ListModel.Limits limits, ListModel.Sort sort, String... properties) {
      super();
      addParameter("limits", limits);
      addParameter("sort", sort);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all artists.
     * 
     * @param albumartistsonly
     *          Whether or not to include artists only appearing in compilations. If the parameter is not passed or is passed as null the GUI setting
     *          will be used.
     * @param limits
     * @param sort
     * @param properties
     *          One or more of: <tt>instrument</tt>, <tt>style</tt>, <tt>mood</tt>, <tt>born</tt>, <tt>formed</tt>, <tt>description</tt>,
     *          <tt>genre</tt>, <tt>died</tt>, <tt>disbanded</tt>, <tt>yearsactive</tt>, <tt>musicbrainzartistid</tt>, <tt>fanart</tt>,
     *          <tt>thumbnail</tt>, <tt>compilationartist</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.ArtistFields}.
     */
    public GetArtists(Boolean albumartistsonly, ListModel.Limits limits, ListModel.Sort sort, String... properties) {
      super();
      addParameter("albumartistsonly", albumartistsonly);
      addParameter("limits", limits);
      addParameter("sort", sort);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all artists.
     * 
     * @param filter
     * @param properties
     *          One or more of: <tt>instrument</tt>, <tt>style</tt>, <tt>mood</tt>, <tt>born</tt>, <tt>formed</tt>, <tt>description</tt>,
     *          <tt>genre</tt>, <tt>died</tt>, <tt>disbanded</tt>, <tt>yearsactive</tt>, <tt>musicbrainzartistid</tt>, <tt>fanart</tt>,
     *          <tt>thumbnail</tt>, <tt>compilationartist</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.ArtistFields}.
     */
    public GetArtists(FilterGenreId filter, String... properties) {
      super();
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all artists.
     * 
     * @param albumartistsonly
     *          Whether or not to include artists only appearing in compilations. If the parameter is not passed or is passed as null the GUI setting
     *          will be used.
     * @param filter
     * @param properties
     *          One or more of: <tt>instrument</tt>, <tt>style</tt>, <tt>mood</tt>, <tt>born</tt>, <tt>formed</tt>, <tt>description</tt>,
     *          <tt>genre</tt>, <tt>died</tt>, <tt>disbanded</tt>, <tt>yearsactive</tt>, <tt>musicbrainzartistid</tt>, <tt>fanart</tt>,
     *          <tt>thumbnail</tt>, <tt>compilationartist</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.ArtistFields}.
     */
    public GetArtists(Boolean albumartistsonly, FilterGenreId filter, String... properties) {
      super();
      addParameter("albumartistsonly", albumartistsonly);
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all artists.
     * 
     * @param limits
     * @param filter
     * @param properties
     *          One or more of: <tt>instrument</tt>, <tt>style</tt>, <tt>mood</tt>, <tt>born</tt>, <tt>formed</tt>, <tt>description</tt>,
     *          <tt>genre</tt>, <tt>died</tt>, <tt>disbanded</tt>, <tt>yearsactive</tt>, <tt>musicbrainzartistid</tt>, <tt>fanart</tt>,
     *          <tt>thumbnail</tt>, <tt>compilationartist</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.ArtistFields}.
     */
    public GetArtists(ListModel.Limits limits, FilterGenreId filter, String... properties) {
      super();
      addParameter("limits", limits);
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all artists.
     * 
     * @param albumartistsonly
     *          Whether or not to include artists only appearing in compilations. If the parameter is not passed or is passed as null the GUI setting
     *          will be used.
     * @param limits
     * @param filter
     * @param properties
     *          One or more of: <tt>instrument</tt>, <tt>style</tt>, <tt>mood</tt>, <tt>born</tt>, <tt>formed</tt>, <tt>description</tt>,
     *          <tt>genre</tt>, <tt>died</tt>, <tt>disbanded</tt>, <tt>yearsactive</tt>, <tt>musicbrainzartistid</tt>, <tt>fanart</tt>,
     *          <tt>thumbnail</tt>, <tt>compilationartist</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.ArtistFields}.
     */
    public GetArtists(Boolean albumartistsonly, ListModel.Limits limits, FilterGenreId filter, String... properties) {
      super();
      addParameter("albumartistsonly", albumartistsonly);
      addParameter("limits", limits);
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all artists.
     * 
     * @param sort
     * @param filter
     * @param properties
     *          One or more of: <tt>instrument</tt>, <tt>style</tt>, <tt>mood</tt>, <tt>born</tt>, <tt>formed</tt>, <tt>description</tt>,
     *          <tt>genre</tt>, <tt>died</tt>, <tt>disbanded</tt>, <tt>yearsactive</tt>, <tt>musicbrainzartistid</tt>, <tt>fanart</tt>,
     *          <tt>thumbnail</tt>, <tt>compilationartist</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.ArtistFields}.
     */
    public GetArtists(ListModel.Sort sort, FilterGenreId filter, String... properties) {
      super();
      addParameter("sort", sort);
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all artists.
     * 
     * @param albumartistsonly
     *          Whether or not to include artists only appearing in compilations. If the parameter is not passed or is passed as null the GUI setting
     *          will be used.
     * @param sort
     * @param filter
     * @param properties
     *          One or more of: <tt>instrument</tt>, <tt>style</tt>, <tt>mood</tt>, <tt>born</tt>, <tt>formed</tt>, <tt>description</tt>,
     *          <tt>genre</tt>, <tt>died</tt>, <tt>disbanded</tt>, <tt>yearsactive</tt>, <tt>musicbrainzartistid</tt>, <tt>fanart</tt>,
     *          <tt>thumbnail</tt>, <tt>compilationartist</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.ArtistFields}.
     */
    public GetArtists(Boolean albumartistsonly, ListModel.Sort sort, FilterGenreId filter, String... properties) {
      super();
      addParameter("albumartistsonly", albumartistsonly);
      addParameter("sort", sort);
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all artists.
     * 
     * @param filter
     * @param properties
     *          One or more of: <tt>instrument</tt>, <tt>style</tt>, <tt>mood</tt>, <tt>born</tt>, <tt>formed</tt>, <tt>description</tt>,
     *          <tt>genre</tt>, <tt>died</tt>, <tt>disbanded</tt>, <tt>yearsactive</tt>, <tt>musicbrainzartistid</tt>, <tt>fanart</tt>,
     *          <tt>thumbnail</tt>, <tt>compilationartist</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.ArtistFields}.
     */
    public GetArtists(FilterGenre filter, String... properties) {
      super();
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all artists.
     * 
     * @param albumartistsonly
     *          Whether or not to include artists only appearing in compilations. If the parameter is not passed or is passed as null the GUI setting
     *          will be used.
     * @param filter
     * @param properties
     *          One or more of: <tt>instrument</tt>, <tt>style</tt>, <tt>mood</tt>, <tt>born</tt>, <tt>formed</tt>, <tt>description</tt>,
     *          <tt>genre</tt>, <tt>died</tt>, <tt>disbanded</tt>, <tt>yearsactive</tt>, <tt>musicbrainzartistid</tt>, <tt>fanart</tt>,
     *          <tt>thumbnail</tt>, <tt>compilationartist</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.ArtistFields}.
     */
    public GetArtists(Boolean albumartistsonly, FilterGenre filter, String... properties) {
      super();
      addParameter("albumartistsonly", albumartistsonly);
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all artists.
     * 
     * @param limits
     * @param filter
     * @param properties
     *          One or more of: <tt>instrument</tt>, <tt>style</tt>, <tt>mood</tt>, <tt>born</tt>, <tt>formed</tt>, <tt>description</tt>,
     *          <tt>genre</tt>, <tt>died</tt>, <tt>disbanded</tt>, <tt>yearsactive</tt>, <tt>musicbrainzartistid</tt>, <tt>fanart</tt>,
     *          <tt>thumbnail</tt>, <tt>compilationartist</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.ArtistFields}.
     */
    public GetArtists(ListModel.Limits limits, FilterGenre filter, String... properties) {
      super();
      addParameter("limits", limits);
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all artists.
     * 
     * @param albumartistsonly
     *          Whether or not to include artists only appearing in compilations. If the parameter is not passed or is passed as null the GUI setting
     *          will be used.
     * @param limits
     * @param filter
     * @param properties
     *          One or more of: <tt>instrument</tt>, <tt>style</tt>, <tt>mood</tt>, <tt>born</tt>, <tt>formed</tt>, <tt>description</tt>,
     *          <tt>genre</tt>, <tt>died</tt>, <tt>disbanded</tt>, <tt>yearsactive</tt>, <tt>musicbrainzartistid</tt>, <tt>fanart</tt>,
     *          <tt>thumbnail</tt>, <tt>compilationartist</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.ArtistFields}.
     */
    public GetArtists(Boolean albumartistsonly, ListModel.Limits limits, FilterGenre filter, String... properties) {
      super();
      addParameter("albumartistsonly", albumartistsonly);
      addParameter("limits", limits);
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all artists.
     * 
     * @param sort
     * @param filter
     * @param properties
     *          One or more of: <tt>instrument</tt>, <tt>style</tt>, <tt>mood</tt>, <tt>born</tt>, <tt>formed</tt>, <tt>description</tt>,
     *          <tt>genre</tt>, <tt>died</tt>, <tt>disbanded</tt>, <tt>yearsactive</tt>, <tt>musicbrainzartistid</tt>, <tt>fanart</tt>,
     *          <tt>thumbnail</tt>, <tt>compilationartist</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.ArtistFields}.
     */
    public GetArtists(ListModel.Sort sort, FilterGenre filter, String... properties) {
      super();
      addParameter("sort", sort);
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all artists.
     * 
     * @param albumartistsonly
     *          Whether or not to include artists only appearing in compilations. If the parameter is not passed or is passed as null the GUI setting
     *          will be used.
     * @param sort
     * @param filter
     * @param properties
     *          One or more of: <tt>instrument</tt>, <tt>style</tt>, <tt>mood</tt>, <tt>born</tt>, <tt>formed</tt>, <tt>description</tt>,
     *          <tt>genre</tt>, <tt>died</tt>, <tt>disbanded</tt>, <tt>yearsactive</tt>, <tt>musicbrainzartistid</tt>, <tt>fanart</tt>,
     *          <tt>thumbnail</tt>, <tt>compilationartist</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.ArtistFields}.
     */
    public GetArtists(Boolean albumartistsonly, ListModel.Sort sort, FilterGenre filter, String... properties) {
      super();
      addParameter("albumartistsonly", albumartistsonly);
      addParameter("sort", sort);
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all artists.
     * 
     * @param filter
     * @param properties
     *          One or more of: <tt>instrument</tt>, <tt>style</tt>, <tt>mood</tt>, <tt>born</tt>, <tt>formed</tt>, <tt>description</tt>,
     *          <tt>genre</tt>, <tt>died</tt>, <tt>disbanded</tt>, <tt>yearsactive</tt>, <tt>musicbrainzartistid</tt>, <tt>fanart</tt>,
     *          <tt>thumbnail</tt>, <tt>compilationartist</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.ArtistFields}.
     */
    public GetArtists(FilterAlbumId filter, String... properties) {
      super();
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all artists.
     * 
     * @param albumartistsonly
     *          Whether or not to include artists only appearing in compilations. If the parameter is not passed or is passed as null the GUI setting
     *          will be used.
     * @param filter
     * @param properties
     *          One or more of: <tt>instrument</tt>, <tt>style</tt>, <tt>mood</tt>, <tt>born</tt>, <tt>formed</tt>, <tt>description</tt>,
     *          <tt>genre</tt>, <tt>died</tt>, <tt>disbanded</tt>, <tt>yearsactive</tt>, <tt>musicbrainzartistid</tt>, <tt>fanart</tt>,
     *          <tt>thumbnail</tt>, <tt>compilationartist</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.ArtistFields}.
     */
    public GetArtists(Boolean albumartistsonly, FilterAlbumId filter, String... properties) {
      super();
      addParameter("albumartistsonly", albumartistsonly);
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all artists.
     * 
     * @param limits
     * @param filter
     * @param properties
     *          One or more of: <tt>instrument</tt>, <tt>style</tt>, <tt>mood</tt>, <tt>born</tt>, <tt>formed</tt>, <tt>description</tt>,
     *          <tt>genre</tt>, <tt>died</tt>, <tt>disbanded</tt>, <tt>yearsactive</tt>, <tt>musicbrainzartistid</tt>, <tt>fanart</tt>,
     *          <tt>thumbnail</tt>, <tt>compilationartist</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.ArtistFields}.
     */
    public GetArtists(ListModel.Limits limits, FilterAlbumId filter, String... properties) {
      super();
      addParameter("limits", limits);
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all artists.
     * 
     * @param albumartistsonly
     *          Whether or not to include artists only appearing in compilations. If the parameter is not passed or is passed as null the GUI setting
     *          will be used.
     * @param limits
     * @param filter
     * @param properties
     *          One or more of: <tt>instrument</tt>, <tt>style</tt>, <tt>mood</tt>, <tt>born</tt>, <tt>formed</tt>, <tt>description</tt>,
     *          <tt>genre</tt>, <tt>died</tt>, <tt>disbanded</tt>, <tt>yearsactive</tt>, <tt>musicbrainzartistid</tt>, <tt>fanart</tt>,
     *          <tt>thumbnail</tt>, <tt>compilationartist</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.ArtistFields}.
     */
    public GetArtists(Boolean albumartistsonly, ListModel.Limits limits, FilterAlbumId filter, String... properties) {
      super();
      addParameter("albumartistsonly", albumartistsonly);
      addParameter("limits", limits);
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all artists.
     * 
     * @param sort
     * @param filter
     * @param properties
     *          One or more of: <tt>instrument</tt>, <tt>style</tt>, <tt>mood</tt>, <tt>born</tt>, <tt>formed</tt>, <tt>description</tt>,
     *          <tt>genre</tt>, <tt>died</tt>, <tt>disbanded</tt>, <tt>yearsactive</tt>, <tt>musicbrainzartistid</tt>, <tt>fanart</tt>,
     *          <tt>thumbnail</tt>, <tt>compilationartist</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.ArtistFields}.
     */
    public GetArtists(ListModel.Sort sort, FilterAlbumId filter, String... properties) {
      super();
      addParameter("sort", sort);
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all artists.
     * 
     * @param albumartistsonly
     *          Whether or not to include artists only appearing in compilations. If the parameter is not passed or is passed as null the GUI setting
     *          will be used.
     * @param sort
     * @param filter
     * @param properties
     *          One or more of: <tt>instrument</tt>, <tt>style</tt>, <tt>mood</tt>, <tt>born</tt>, <tt>formed</tt>, <tt>description</tt>,
     *          <tt>genre</tt>, <tt>died</tt>, <tt>disbanded</tt>, <tt>yearsactive</tt>, <tt>musicbrainzartistid</tt>, <tt>fanart</tt>,
     *          <tt>thumbnail</tt>, <tt>compilationartist</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.ArtistFields}.
     */
    public GetArtists(Boolean albumartistsonly, ListModel.Sort sort, FilterAlbumId filter, String... properties) {
      super();
      addParameter("albumartistsonly", albumartistsonly);
      addParameter("sort", sort);
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all artists.
     * 
     * @param filter
     * @param properties
     *          One or more of: <tt>instrument</tt>, <tt>style</tt>, <tt>mood</tt>, <tt>born</tt>, <tt>formed</tt>, <tt>description</tt>,
     *          <tt>genre</tt>, <tt>died</tt>, <tt>disbanded</tt>, <tt>yearsactive</tt>, <tt>musicbrainzartistid</tt>, <tt>fanart</tt>,
     *          <tt>thumbnail</tt>, <tt>compilationartist</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.ArtistFields}.
     */
    public GetArtists(FilterAlbum filter, String... properties) {
      super();
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all artists.
     * 
     * @param albumartistsonly
     *          Whether or not to include artists only appearing in compilations. If the parameter is not passed or is passed as null the GUI setting
     *          will be used.
     * @param filter
     * @param properties
     *          One or more of: <tt>instrument</tt>, <tt>style</tt>, <tt>mood</tt>, <tt>born</tt>, <tt>formed</tt>, <tt>description</tt>,
     *          <tt>genre</tt>, <tt>died</tt>, <tt>disbanded</tt>, <tt>yearsactive</tt>, <tt>musicbrainzartistid</tt>, <tt>fanart</tt>,
     *          <tt>thumbnail</tt>, <tt>compilationartist</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.ArtistFields}.
     */
    public GetArtists(Boolean albumartistsonly, FilterAlbum filter, String... properties) {
      super();
      addParameter("albumartistsonly", albumartistsonly);
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all artists.
     * 
     * @param limits
     * @param filter
     * @param properties
     *          One or more of: <tt>instrument</tt>, <tt>style</tt>, <tt>mood</tt>, <tt>born</tt>, <tt>formed</tt>, <tt>description</tt>,
     *          <tt>genre</tt>, <tt>died</tt>, <tt>disbanded</tt>, <tt>yearsactive</tt>, <tt>musicbrainzartistid</tt>, <tt>fanart</tt>,
     *          <tt>thumbnail</tt>, <tt>compilationartist</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.ArtistFields}.
     */
    public GetArtists(ListModel.Limits limits, FilterAlbum filter, String... properties) {
      super();
      addParameter("limits", limits);
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all artists.
     * 
     * @param albumartistsonly
     *          Whether or not to include artists only appearing in compilations. If the parameter is not passed or is passed as null the GUI setting
     *          will be used.
     * @param limits
     * @param filter
     * @param properties
     *          One or more of: <tt>instrument</tt>, <tt>style</tt>, <tt>mood</tt>, <tt>born</tt>, <tt>formed</tt>, <tt>description</tt>,
     *          <tt>genre</tt>, <tt>died</tt>, <tt>disbanded</tt>, <tt>yearsactive</tt>, <tt>musicbrainzartistid</tt>, <tt>fanart</tt>,
     *          <tt>thumbnail</tt>, <tt>compilationartist</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.ArtistFields}.
     */
    public GetArtists(Boolean albumartistsonly, ListModel.Limits limits, FilterAlbum filter, String... properties) {
      super();
      addParameter("albumartistsonly", albumartistsonly);
      addParameter("limits", limits);
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all artists.
     * 
     * @param sort
     * @param filter
     * @param properties
     *          One or more of: <tt>instrument</tt>, <tt>style</tt>, <tt>mood</tt>, <tt>born</tt>, <tt>formed</tt>, <tt>description</tt>,
     *          <tt>genre</tt>, <tt>died</tt>, <tt>disbanded</tt>, <tt>yearsactive</tt>, <tt>musicbrainzartistid</tt>, <tt>fanart</tt>,
     *          <tt>thumbnail</tt>, <tt>compilationartist</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.ArtistFields}.
     */
    public GetArtists(ListModel.Sort sort, FilterAlbum filter, String... properties) {
      super();
      addParameter("sort", sort);
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all artists.
     * 
     * @param albumartistsonly
     *          Whether or not to include artists only appearing in compilations. If the parameter is not passed or is passed as null the GUI setting
     *          will be used.
     * @param sort
     * @param filter
     * @param properties
     *          One or more of: <tt>instrument</tt>, <tt>style</tt>, <tt>mood</tt>, <tt>born</tt>, <tt>formed</tt>, <tt>description</tt>,
     *          <tt>genre</tt>, <tt>died</tt>, <tt>disbanded</tt>, <tt>yearsactive</tt>, <tt>musicbrainzartistid</tt>, <tt>fanart</tt>,
     *          <tt>thumbnail</tt>, <tt>compilationartist</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.ArtistFields}.
     */
    public GetArtists(Boolean albumartistsonly, ListModel.Sort sort, FilterAlbum filter, String... properties) {
      super();
      addParameter("albumartistsonly", albumartistsonly);
      addParameter("sort", sort);
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all artists.
     * 
     * @param filter
     * @param properties
     *          One or more of: <tt>instrument</tt>, <tt>style</tt>, <tt>mood</tt>, <tt>born</tt>, <tt>formed</tt>, <tt>description</tt>,
     *          <tt>genre</tt>, <tt>died</tt>, <tt>disbanded</tt>, <tt>yearsactive</tt>, <tt>musicbrainzartistid</tt>, <tt>fanart</tt>,
     *          <tt>thumbnail</tt>, <tt>compilationartist</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.ArtistFields}.
     */
    public GetArtists(FilterSongId filter, String... properties) {
      super();
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all artists.
     * 
     * @param albumartistsonly
     *          Whether or not to include artists only appearing in compilations. If the parameter is not passed or is passed as null the GUI setting
     *          will be used.
     * @param filter
     * @param properties
     *          One or more of: <tt>instrument</tt>, <tt>style</tt>, <tt>mood</tt>, <tt>born</tt>, <tt>formed</tt>, <tt>description</tt>,
     *          <tt>genre</tt>, <tt>died</tt>, <tt>disbanded</tt>, <tt>yearsactive</tt>, <tt>musicbrainzartistid</tt>, <tt>fanart</tt>,
     *          <tt>thumbnail</tt>, <tt>compilationartist</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.ArtistFields}.
     */
    public GetArtists(Boolean albumartistsonly, FilterSongId filter, String... properties) {
      super();
      addParameter("albumartistsonly", albumartistsonly);
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all artists.
     * 
     * @param limits
     * @param filter
     * @param properties
     *          One or more of: <tt>instrument</tt>, <tt>style</tt>, <tt>mood</tt>, <tt>born</tt>, <tt>formed</tt>, <tt>description</tt>,
     *          <tt>genre</tt>, <tt>died</tt>, <tt>disbanded</tt>, <tt>yearsactive</tt>, <tt>musicbrainzartistid</tt>, <tt>fanart</tt>,
     *          <tt>thumbnail</tt>, <tt>compilationartist</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.ArtistFields}.
     */
    public GetArtists(ListModel.Limits limits, FilterSongId filter, String... properties) {
      super();
      addParameter("limits", limits);
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all artists.
     * 
     * @param albumartistsonly
     *          Whether or not to include artists only appearing in compilations. If the parameter is not passed or is passed as null the GUI setting
     *          will be used.
     * @param limits
     * @param filter
     * @param properties
     *          One or more of: <tt>instrument</tt>, <tt>style</tt>, <tt>mood</tt>, <tt>born</tt>, <tt>formed</tt>, <tt>description</tt>,
     *          <tt>genre</tt>, <tt>died</tt>, <tt>disbanded</tt>, <tt>yearsactive</tt>, <tt>musicbrainzartistid</tt>, <tt>fanart</tt>,
     *          <tt>thumbnail</tt>, <tt>compilationartist</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.ArtistFields}.
     */
    public GetArtists(Boolean albumartistsonly, ListModel.Limits limits, FilterSongId filter, String... properties) {
      super();
      addParameter("albumartistsonly", albumartistsonly);
      addParameter("limits", limits);
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all artists.
     * 
     * @param sort
     * @param filter
     * @param properties
     *          One or more of: <tt>instrument</tt>, <tt>style</tt>, <tt>mood</tt>, <tt>born</tt>, <tt>formed</tt>, <tt>description</tt>,
     *          <tt>genre</tt>, <tt>died</tt>, <tt>disbanded</tt>, <tt>yearsactive</tt>, <tt>musicbrainzartistid</tt>, <tt>fanart</tt>,
     *          <tt>thumbnail</tt>, <tt>compilationartist</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.ArtistFields}.
     */
    public GetArtists(ListModel.Sort sort, FilterSongId filter, String... properties) {
      super();
      addParameter("sort", sort);
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all artists.
     * 
     * @param albumartistsonly
     *          Whether or not to include artists only appearing in compilations. If the parameter is not passed or is passed as null the GUI setting
     *          will be used.
     * @param sort
     * @param filter
     * @param properties
     *          One or more of: <tt>instrument</tt>, <tt>style</tt>, <tt>mood</tt>, <tt>born</tt>, <tt>formed</tt>, <tt>description</tt>,
     *          <tt>genre</tt>, <tt>died</tt>, <tt>disbanded</tt>, <tt>yearsactive</tt>, <tt>musicbrainzartistid</tt>, <tt>fanart</tt>,
     *          <tt>thumbnail</tt>, <tt>compilationartist</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.ArtistFields}.
     */
    public GetArtists(Boolean albumartistsonly, ListModel.Sort sort, FilterSongId filter, String... properties) {
      super();
      addParameter("albumartistsonly", albumartistsonly);
      addParameter("sort", sort);
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all artists.
     * 
     * @param filter
     * @param properties
     *          One or more of: <tt>instrument</tt>, <tt>style</tt>, <tt>mood</tt>, <tt>born</tt>, <tt>formed</tt>, <tt>description</tt>,
     *          <tt>genre</tt>, <tt>died</tt>, <tt>disbanded</tt>, <tt>yearsactive</tt>, <tt>musicbrainzartistid</tt>, <tt>fanart</tt>,
     *          <tt>thumbnail</tt>, <tt>compilationartist</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.ArtistFields}.
     */
    public GetArtists(ListModel.ArtistFilter filter, String... properties) {
      super();
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all artists.
     * 
     * @param albumartistsonly
     *          Whether or not to include artists only appearing in compilations. If the parameter is not passed or is passed as null the GUI setting
     *          will be used.
     * @param filter
     * @param properties
     *          One or more of: <tt>instrument</tt>, <tt>style</tt>, <tt>mood</tt>, <tt>born</tt>, <tt>formed</tt>, <tt>description</tt>,
     *          <tt>genre</tt>, <tt>died</tt>, <tt>disbanded</tt>, <tt>yearsactive</tt>, <tt>musicbrainzartistid</tt>, <tt>fanart</tt>,
     *          <tt>thumbnail</tt>, <tt>compilationartist</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.ArtistFields}.
     */
    public GetArtists(Boolean albumartistsonly, ListModel.ArtistFilter filter, String... properties) {
      super();
      addParameter("albumartistsonly", albumartistsonly);
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all artists.
     * 
     * @param limits
     * @param filter
     * @param properties
     *          One or more of: <tt>instrument</tt>, <tt>style</tt>, <tt>mood</tt>, <tt>born</tt>, <tt>formed</tt>, <tt>description</tt>,
     *          <tt>genre</tt>, <tt>died</tt>, <tt>disbanded</tt>, <tt>yearsactive</tt>, <tt>musicbrainzartistid</tt>, <tt>fanart</tt>,
     *          <tt>thumbnail</tt>, <tt>compilationartist</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.ArtistFields}.
     */
    public GetArtists(ListModel.Limits limits, ListModel.ArtistFilter filter, String... properties) {
      super();
      addParameter("limits", limits);
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all artists.
     * 
     * @param albumartistsonly
     *          Whether or not to include artists only appearing in compilations. If the parameter is not passed or is passed as null the GUI setting
     *          will be used.
     * @param limits
     * @param filter
     * @param properties
     *          One or more of: <tt>instrument</tt>, <tt>style</tt>, <tt>mood</tt>, <tt>born</tt>, <tt>formed</tt>, <tt>description</tt>,
     *          <tt>genre</tt>, <tt>died</tt>, <tt>disbanded</tt>, <tt>yearsactive</tt>, <tt>musicbrainzartistid</tt>, <tt>fanart</tt>,
     *          <tt>thumbnail</tt>, <tt>compilationartist</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.ArtistFields}.
     */
    public GetArtists(Boolean albumartistsonly, ListModel.Limits limits, ListModel.ArtistFilter filter, String... properties) {
      super();
      addParameter("albumartistsonly", albumartistsonly);
      addParameter("limits", limits);
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all artists.
     * 
     * @param sort
     * @param filter
     * @param properties
     *          One or more of: <tt>instrument</tt>, <tt>style</tt>, <tt>mood</tt>, <tt>born</tt>, <tt>formed</tt>, <tt>description</tt>,
     *          <tt>genre</tt>, <tt>died</tt>, <tt>disbanded</tt>, <tt>yearsactive</tt>, <tt>musicbrainzartistid</tt>, <tt>fanart</tt>,
     *          <tt>thumbnail</tt>, <tt>compilationartist</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.ArtistFields}.
     */
    public GetArtists(ListModel.Sort sort, ListModel.ArtistFilter filter, String... properties) {
      super();
      addParameter("sort", sort);
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all artists.
     * 
     * @param albumartistsonly
     *          Whether or not to include artists only appearing in compilations. If the parameter is not passed or is passed as null the GUI setting
     *          will be used.
     * @param sort
     * @param filter
     * @param properties
     *          One or more of: <tt>instrument</tt>, <tt>style</tt>, <tt>mood</tt>, <tt>born</tt>, <tt>formed</tt>, <tt>description</tt>,
     *          <tt>genre</tt>, <tt>died</tt>, <tt>disbanded</tt>, <tt>yearsactive</tt>, <tt>musicbrainzartistid</tt>, <tt>fanart</tt>,
     *          <tt>thumbnail</tt>, <tt>compilationartist</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.ArtistFields}.
     */
    public GetArtists(Boolean albumartistsonly, ListModel.Sort sort, ListModel.ArtistFilter filter, String... properties) {
      super();
      addParameter("albumartistsonly", albumartistsonly);
      addParameter("sort", sort);
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    @Override
    protected ArrayList<AudioModel.ArtistDetail> parseMany(JsonNode node) {
      final ArrayNode artists = parseResults(node, RESULT);
      if (artists != null) {
        final ArrayList<AudioModel.ArtistDetail> ret = new ArrayList<AudioModel.ArtistDetail>(artists.size());
        for (int i = 0; i < artists.size(); i++) {
          final ObjectNode item = (ObjectNode) artists.get(i);
          ret.add(new AudioModel.ArtistDetail(item));
        }
        return ret;
      }
      else {
        return new ArrayList<AudioModel.ArtistDetail>(0);
      }
    }

    @Override
    public String getName() {
      return API_TYPE;
    }

    @Override
    protected boolean returnsList() {
      return true;
    }

    /**
     * Note: This class is used as parameter only.<br/>
     * <i>This class was generated automatically from XBMC's JSON-RPC introspect.</i>
     */
    public static class FilterGenreId extends AbstractModel {

      // field names
      public static final String GENREID = "genreid";

      // class members
      public final Integer       genreid;

      /**
       * @param genreid
       */
      public FilterGenreId(Integer genreid) {
        this.genreid = genreid;
      }

      @Override
      public JsonNode toJsonNode() {
        final ObjectNode node = OM.createObjectNode();
        node.put(GENREID, genreid);
        return node;
      }
    }

    /**
     * Note: This class is used as parameter only.<br/>
     * <i>This class was generated automatically from XBMC's JSON-RPC introspect.</i>
     */
    public static class FilterGenre extends AbstractModel {

      // field names
      public static final String GENRE = "genre";

      // class members
      public final String        genre;

      /**
       * @param genre
       */
      public FilterGenre(String genre) {
        this.genre = genre;
      }

      @Override
      public JsonNode toJsonNode() {
        final ObjectNode node = OM.createObjectNode();
        node.put(GENRE, genre);
        return node;
      }
    }

    /**
     * Note: This class is used as parameter only.<br/>
     * <i>This class was generated automatically from XBMC's JSON-RPC introspect.</i>
     */
    public static class FilterAlbumId extends AbstractModel {

      // field names
      public static final String ALBUMID = "albumid";

      // class members
      public final Integer       albumid;

      /**
       * @param albumid
       */
      public FilterAlbumId(Integer albumid) {
        this.albumid = albumid;
      }

      @Override
      public JsonNode toJsonNode() {
        final ObjectNode node = OM.createObjectNode();
        node.put(ALBUMID, albumid);
        return node;
      }
    }

    /**
     * Note: This class is used as parameter only.<br/>
     * <i>This class was generated automatically from XBMC's JSON-RPC introspect.</i>
     */
    public static class FilterAlbum extends AbstractModel {

      // field names
      public static final String ALBUM = "album";

      // class members
      public final String        album;

      /**
       * @param album
       */
      public FilterAlbum(String album) {
        this.album = album;
      }

      @Override
      public JsonNode toJsonNode() {
        final ObjectNode node = OM.createObjectNode();
        node.put(ALBUM, album);
        return node;
      }
    }

    /**
     * Note: This class is used as parameter only.<br/>
     * <i>This class was generated automatically from XBMC's JSON-RPC introspect.</i>
     */
    public static class FilterSongId extends AbstractModel {

      // field names
      public static final String SONGID = "songid";

      // class members
      public final Integer       songid;

      /**
       * @param songid
       */
      public FilterSongId(Integer songid) {
        this.songid = songid;
      }

      @Override
      public JsonNode toJsonNode() {
        final ObjectNode node = OM.createObjectNode();
        node.put(SONGID, songid);
        return node;
      }
    }
  }

  /**
   * Retrieve all genres.
   * <p/>
   * This class represents the API method <tt>AudioLibrary.GetGenres</tt>
   * <p/>
   * <i>This class was generated automatically from XBMC's JSON-RPC introspect.</i>
   */
  public static class GetGenres extends AbstractCall<LibraryModel.GenreDetail> {
    public final static String API_TYPE = "AudioLibrary.GetGenres";
    public final static String RESULT   = "genres";

    /**
     * Retrieve all genres.
     * 
     * @param limits
     * @param sort
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>thumbnail</tt>. See constants at {@link LibraryModel.GenreFields}.
     */
    public GetGenres(ListModel.Limits limits, ListModel.Sort sort, String... properties) {
      super();
      addParameter("limits", limits);
      addParameter("sort", sort);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all genres.
     * 
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>thumbnail</tt>. See constants at {@link LibraryModel.GenreFields}.
     */
    public GetGenres(String... properties) {
      super();
      addParameter("properties", properties);
    }

    /**
     * Retrieve all genres.
     * 
     * @param limits
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>thumbnail</tt>. See constants at {@link LibraryModel.GenreFields}.
     */
    public GetGenres(ListModel.Limits limits, String... properties) {
      super();
      addParameter("limits", limits);
      addParameter("properties", properties);
    }

    @Override
    protected ArrayList<LibraryModel.GenreDetail> parseMany(JsonNode node) {
      final ArrayNode genres = parseResults(node, RESULT);
      if (genres != null) {
        final ArrayList<LibraryModel.GenreDetail> ret = new ArrayList<LibraryModel.GenreDetail>(genres.size());
        for (int i = 0; i < genres.size(); i++) {
          final ObjectNode item = (ObjectNode) genres.get(i);
          ret.add(new LibraryModel.GenreDetail(item));
        }
        return ret;
      }
      else {
        return new ArrayList<LibraryModel.GenreDetail>(0);
      }
    }

    @Override
    public String getName() {
      return API_TYPE;
    }

    @Override
    protected boolean returnsList() {
      return true;
    }
  }

  /**
   * Retrieve recently added albums.
   * <p/>
   * This class represents the API method <tt>AudioLibrary.GetRecentlyAddedAlbums</tt>
   * <p/>
   * <i>This class was generated automatically from XBMC's JSON-RPC introspect.</i>
   */
  public static class GetRecentlyAddedAlbums extends AbstractCall<AudioModel.AlbumDetail> {
    public final static String API_TYPE = "AudioLibrary.GetRecentlyAddedAlbums";
    public final static String RESULT   = "albums";

    /**
     * Retrieve recently added albums.
     * 
     * @param limits
     * @param sort
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>description</tt>, <tt>artist</tt>, <tt>genre</tt>, <tt>theme</tt>, <tt>mood</tt>, <tt>style</tt>,
     *          <tt>type</tt>, <tt>albumlabel</tt>, <tt>rating</tt>, <tt>year</tt>, <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>,
     *          <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>playcount</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>compilation</tt>, <tt>releasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.AlbumFields}.
     */
    public GetRecentlyAddedAlbums(ListModel.Limits limits, ListModel.Sort sort, String... properties) {
      super();
      addParameter("limits", limits);
      addParameter("sort", sort);
      addParameter("properties", properties);
    }

    /**
     * Retrieve recently added albums.
     * 
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>description</tt>, <tt>artist</tt>, <tt>genre</tt>, <tt>theme</tt>, <tt>mood</tt>, <tt>style</tt>,
     *          <tt>type</tt>, <tt>albumlabel</tt>, <tt>rating</tt>, <tt>year</tt>, <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>,
     *          <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>playcount</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>compilation</tt>, <tt>releasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.AlbumFields}.
     */
    public GetRecentlyAddedAlbums(String... properties) {
      super();
      addParameter("properties", properties);
    }

    /**
     * Retrieve recently added albums.
     * 
     * @param limits
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>description</tt>, <tt>artist</tt>, <tt>genre</tt>, <tt>theme</tt>, <tt>mood</tt>, <tt>style</tt>,
     *          <tt>type</tt>, <tt>albumlabel</tt>, <tt>rating</tt>, <tt>year</tt>, <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>,
     *          <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>playcount</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>compilation</tt>, <tt>releasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.AlbumFields}.
     */
    public GetRecentlyAddedAlbums(ListModel.Limits limits, String... properties) {
      super();
      addParameter("limits", limits);
      addParameter("properties", properties);
    }

    @Override
    protected ArrayList<AudioModel.AlbumDetail> parseMany(JsonNode node) {
      final ArrayNode albums = parseResults(node, RESULT);
      if (albums != null) {
        final ArrayList<AudioModel.AlbumDetail> ret = new ArrayList<AudioModel.AlbumDetail>(albums.size());
        for (int i = 0; i < albums.size(); i++) {
          final ObjectNode item = (ObjectNode) albums.get(i);
          ret.add(new AudioModel.AlbumDetail(item));
        }
        return ret;
      }
      else {
        return new ArrayList<AudioModel.AlbumDetail>(0);
      }
    }

    @Override
    public String getName() {
      return API_TYPE;
    }

    @Override
    protected boolean returnsList() {
      return true;
    }
  }

  /**
   * Retrieve recently added songs.
   * <p/>
   * This class represents the API method <tt>AudioLibrary.GetRecentlyAddedSongs</tt>
   * <p/>
   * <i>This class was generated automatically from XBMC's JSON-RPC introspect.</i>
   */
  public static class GetRecentlyAddedSongs extends AbstractCall<AudioModel.SongDetail> {
    public final static String API_TYPE = "AudioLibrary.GetRecentlyAddedSongs";
    public final static String RESULT   = "songs";

    /**
     * Retrieve recently added songs.
     * 
     * @param albumlimit
     *          The amount of recently added albums from which to return the songs.
     * @param limits
     * @param sort
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>artist</tt>, <tt>albumartist</tt>, <tt>genre</tt>, <tt>year</tt>, <tt>rating</tt>, <tt>album</tt>,
     *          <tt>track</tt>, <tt>duration</tt>, <tt>comment</tt>, <tt>lyrics</tt>, <tt>musicbrainztrackid</tt>, <tt>musicbrainzartistid</tt>,
     *          <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>, <tt>playcount</tt>, <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>file</tt>
     *          , <tt>albumid</tt>, <tt>lastplayed</tt>, <tt>disc</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>albumartistid</tt>, <tt>albumreleasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.SongFields}.
     */
    public GetRecentlyAddedSongs(Integer albumlimit, ListModel.Limits limits, ListModel.Sort sort, String... properties) {
      super();
      addParameter("albumlimit", albumlimit);
      addParameter("limits", limits);
      addParameter("sort", sort);
      addParameter("properties", properties);
    }

    /**
     * Retrieve recently added songs.
     * 
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>artist</tt>, <tt>albumartist</tt>, <tt>genre</tt>, <tt>year</tt>, <tt>rating</tt>, <tt>album</tt>,
     *          <tt>track</tt>, <tt>duration</tt>, <tt>comment</tt>, <tt>lyrics</tt>, <tt>musicbrainztrackid</tt>, <tt>musicbrainzartistid</tt>,
     *          <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>, <tt>playcount</tt>, <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>file</tt>
     *          , <tt>albumid</tt>, <tt>lastplayed</tt>, <tt>disc</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>albumartistid</tt>, <tt>albumreleasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.SongFields}.
     */
    public GetRecentlyAddedSongs(String... properties) {
      super();
      addParameter("properties", properties);
    }

    /**
     * Retrieve recently added songs.
     * 
     * @param albumlimit
     *          The amount of recently added albums from which to return the songs.
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>artist</tt>, <tt>albumartist</tt>, <tt>genre</tt>, <tt>year</tt>, <tt>rating</tt>, <tt>album</tt>,
     *          <tt>track</tt>, <tt>duration</tt>, <tt>comment</tt>, <tt>lyrics</tt>, <tt>musicbrainztrackid</tt>, <tt>musicbrainzartistid</tt>,
     *          <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>, <tt>playcount</tt>, <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>file</tt>
     *          , <tt>albumid</tt>, <tt>lastplayed</tt>, <tt>disc</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>albumartistid</tt>, <tt>albumreleasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.SongFields}.
     */
    public GetRecentlyAddedSongs(Integer albumlimit, String... properties) {
      super();
      addParameter("albumlimit", albumlimit);
      addParameter("properties", properties);
    }

    /**
     * Retrieve recently added songs.
     * 
     * @param limits
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>artist</tt>, <tt>albumartist</tt>, <tt>genre</tt>, <tt>year</tt>, <tt>rating</tt>, <tt>album</tt>,
     *          <tt>track</tt>, <tt>duration</tt>, <tt>comment</tt>, <tt>lyrics</tt>, <tt>musicbrainztrackid</tt>, <tt>musicbrainzartistid</tt>,
     *          <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>, <tt>playcount</tt>, <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>file</tt>
     *          , <tt>albumid</tt>, <tt>lastplayed</tt>, <tt>disc</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>albumartistid</tt>, <tt>albumreleasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.SongFields}.
     */
    public GetRecentlyAddedSongs(ListModel.Limits limits, String... properties) {
      super();
      addParameter("limits", limits);
      addParameter("properties", properties);
    }

    /**
     * Retrieve recently added songs.
     * 
     * @param albumlimit
     *          The amount of recently added albums from which to return the songs.
     * @param limits
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>artist</tt>, <tt>albumartist</tt>, <tt>genre</tt>, <tt>year</tt>, <tt>rating</tt>, <tt>album</tt>,
     *          <tt>track</tt>, <tt>duration</tt>, <tt>comment</tt>, <tt>lyrics</tt>, <tt>musicbrainztrackid</tt>, <tt>musicbrainzartistid</tt>,
     *          <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>, <tt>playcount</tt>, <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>file</tt>
     *          , <tt>albumid</tt>, <tt>lastplayed</tt>, <tt>disc</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>albumartistid</tt>, <tt>albumreleasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.SongFields}.
     */
    public GetRecentlyAddedSongs(Integer albumlimit, ListModel.Limits limits, String... properties) {
      super();
      addParameter("albumlimit", albumlimit);
      addParameter("limits", limits);
      addParameter("properties", properties);
    }

    /**
     * Retrieve recently added songs.
     * 
     * @param sort
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>artist</tt>, <tt>albumartist</tt>, <tt>genre</tt>, <tt>year</tt>, <tt>rating</tt>, <tt>album</tt>,
     *          <tt>track</tt>, <tt>duration</tt>, <tt>comment</tt>, <tt>lyrics</tt>, <tt>musicbrainztrackid</tt>, <tt>musicbrainzartistid</tt>,
     *          <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>, <tt>playcount</tt>, <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>file</tt>
     *          , <tt>albumid</tt>, <tt>lastplayed</tt>, <tt>disc</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>albumartistid</tt>, <tt>albumreleasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.SongFields}.
     */
    public GetRecentlyAddedSongs(ListModel.Sort sort, String... properties) {
      super();
      addParameter("sort", sort);
      addParameter("properties", properties);
    }

    /**
     * Retrieve recently added songs.
     * 
     * @param albumlimit
     *          The amount of recently added albums from which to return the songs.
     * @param sort
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>artist</tt>, <tt>albumartist</tt>, <tt>genre</tt>, <tt>year</tt>, <tt>rating</tt>, <tt>album</tt>,
     *          <tt>track</tt>, <tt>duration</tt>, <tt>comment</tt>, <tt>lyrics</tt>, <tt>musicbrainztrackid</tt>, <tt>musicbrainzartistid</tt>,
     *          <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>, <tt>playcount</tt>, <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>file</tt>
     *          , <tt>albumid</tt>, <tt>lastplayed</tt>, <tt>disc</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>albumartistid</tt>, <tt>albumreleasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.SongFields}.
     */
    public GetRecentlyAddedSongs(Integer albumlimit, ListModel.Sort sort, String... properties) {
      super();
      addParameter("albumlimit", albumlimit);
      addParameter("sort", sort);
      addParameter("properties", properties);
    }

    @Override
    protected ArrayList<AudioModel.SongDetail> parseMany(JsonNode node) {
      final ArrayNode songs = parseResults(node, RESULT);
      if (songs != null) {
        final ArrayList<AudioModel.SongDetail> ret = new ArrayList<AudioModel.SongDetail>(songs.size());
        for (int i = 0; i < songs.size(); i++) {
          final ObjectNode item = (ObjectNode) songs.get(i);
          ret.add(new AudioModel.SongDetail(item));
        }
        return ret;
      }
      else {
        return new ArrayList<AudioModel.SongDetail>(0);
      }
    }

    @Override
    public String getName() {
      return API_TYPE;
    }

    @Override
    protected boolean returnsList() {
      return true;
    }
  }

  /**
   * Retrieve recently played albums.
   * <p/>
   * This class represents the API method <tt>AudioLibrary.GetRecentlyPlayedAlbums</tt>
   * <p/>
   * <i>This class was generated automatically from XBMC's JSON-RPC introspect.</i>
   */
  public static class GetRecentlyPlayedAlbums extends AbstractCall<AudioModel.AlbumDetail> {
    public final static String API_TYPE = "AudioLibrary.GetRecentlyPlayedAlbums";
    public final static String RESULT   = "albums";

    /**
     * Retrieve recently played albums.
     * 
     * @param limits
     * @param sort
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>description</tt>, <tt>artist</tt>, <tt>genre</tt>, <tt>theme</tt>, <tt>mood</tt>, <tt>style</tt>,
     *          <tt>type</tt>, <tt>albumlabel</tt>, <tt>rating</tt>, <tt>year</tt>, <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>,
     *          <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>playcount</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>compilation</tt>, <tt>releasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.AlbumFields}.
     */
    public GetRecentlyPlayedAlbums(ListModel.Limits limits, ListModel.Sort sort, String... properties) {
      super();
      addParameter("limits", limits);
      addParameter("sort", sort);
      addParameter("properties", properties);
    }

    /**
     * Retrieve recently played albums.
     * 
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>description</tt>, <tt>artist</tt>, <tt>genre</tt>, <tt>theme</tt>, <tt>mood</tt>, <tt>style</tt>,
     *          <tt>type</tt>, <tt>albumlabel</tt>, <tt>rating</tt>, <tt>year</tt>, <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>,
     *          <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>playcount</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>compilation</tt>, <tt>releasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.AlbumFields}.
     */
    public GetRecentlyPlayedAlbums(String... properties) {
      super();
      addParameter("properties", properties);
    }

    /**
     * Retrieve recently played albums.
     * 
     * @param limits
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>description</tt>, <tt>artist</tt>, <tt>genre</tt>, <tt>theme</tt>, <tt>mood</tt>, <tt>style</tt>,
     *          <tt>type</tt>, <tt>albumlabel</tt>, <tt>rating</tt>, <tt>year</tt>, <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>,
     *          <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>playcount</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>compilation</tt>, <tt>releasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.AlbumFields}.
     */
    public GetRecentlyPlayedAlbums(ListModel.Limits limits, String... properties) {
      super();
      addParameter("limits", limits);
      addParameter("properties", properties);
    }

    @Override
    protected ArrayList<AudioModel.AlbumDetail> parseMany(JsonNode node) {
      final ArrayNode albums = parseResults(node, RESULT);
      if (albums != null) {
        final ArrayList<AudioModel.AlbumDetail> ret = new ArrayList<AudioModel.AlbumDetail>(albums.size());
        for (int i = 0; i < albums.size(); i++) {
          final ObjectNode item = (ObjectNode) albums.get(i);
          ret.add(new AudioModel.AlbumDetail(item));
        }
        return ret;
      }
      else {
        return new ArrayList<AudioModel.AlbumDetail>(0);
      }
    }

    @Override
    public String getName() {
      return API_TYPE;
    }

    @Override
    protected boolean returnsList() {
      return true;
    }
  }

  /**
   * Retrieve recently played songs.
   * <p/>
   * This class represents the API method <tt>AudioLibrary.GetRecentlyPlayedSongs</tt>
   * <p/>
   * <i>This class was generated automatically from XBMC's JSON-RPC introspect.</i>
   */
  public static class GetRecentlyPlayedSongs extends AbstractCall<AudioModel.SongDetail> {
    public final static String API_TYPE = "AudioLibrary.GetRecentlyPlayedSongs";
    public final static String RESULT   = "songs";

    /**
     * Retrieve recently played songs.
     * 
     * @param limits
     * @param sort
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>artist</tt>, <tt>albumartist</tt>, <tt>genre</tt>, <tt>year</tt>, <tt>rating</tt>, <tt>album</tt>,
     *          <tt>track</tt>, <tt>duration</tt>, <tt>comment</tt>, <tt>lyrics</tt>, <tt>musicbrainztrackid</tt>, <tt>musicbrainzartistid</tt>,
     *          <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>, <tt>playcount</tt>, <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>file</tt>
     *          , <tt>albumid</tt>, <tt>lastplayed</tt>, <tt>disc</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>albumartistid</tt>, <tt>albumreleasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.SongFields}.
     */
    public GetRecentlyPlayedSongs(ListModel.Limits limits, ListModel.Sort sort, String... properties) {
      super();
      addParameter("limits", limits);
      addParameter("sort", sort);
      addParameter("properties", properties);
    }

    /**
     * Retrieve recently played songs.
     * 
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>artist</tt>, <tt>albumartist</tt>, <tt>genre</tt>, <tt>year</tt>, <tt>rating</tt>, <tt>album</tt>,
     *          <tt>track</tt>, <tt>duration</tt>, <tt>comment</tt>, <tt>lyrics</tt>, <tt>musicbrainztrackid</tt>, <tt>musicbrainzartistid</tt>,
     *          <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>, <tt>playcount</tt>, <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>file</tt>
     *          , <tt>albumid</tt>, <tt>lastplayed</tt>, <tt>disc</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>albumartistid</tt>, <tt>albumreleasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.SongFields}.
     */
    public GetRecentlyPlayedSongs(String... properties) {
      super();
      addParameter("properties", properties);
    }

    /**
     * Retrieve recently played songs.
     * 
     * @param limits
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>artist</tt>, <tt>albumartist</tt>, <tt>genre</tt>, <tt>year</tt>, <tt>rating</tt>, <tt>album</tt>,
     *          <tt>track</tt>, <tt>duration</tt>, <tt>comment</tt>, <tt>lyrics</tt>, <tt>musicbrainztrackid</tt>, <tt>musicbrainzartistid</tt>,
     *          <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>, <tt>playcount</tt>, <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>file</tt>
     *          , <tt>albumid</tt>, <tt>lastplayed</tt>, <tt>disc</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>albumartistid</tt>, <tt>albumreleasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.SongFields}.
     */
    public GetRecentlyPlayedSongs(ListModel.Limits limits, String... properties) {
      super();
      addParameter("limits", limits);
      addParameter("properties", properties);
    }

    @Override
    protected ArrayList<AudioModel.SongDetail> parseMany(JsonNode node) {
      final ArrayNode songs = parseResults(node, RESULT);
      if (songs != null) {
        final ArrayList<AudioModel.SongDetail> ret = new ArrayList<AudioModel.SongDetail>(songs.size());
        for (int i = 0; i < songs.size(); i++) {
          final ObjectNode item = (ObjectNode) songs.get(i);
          ret.add(new AudioModel.SongDetail(item));
        }
        return ret;
      }
      else {
        return new ArrayList<AudioModel.SongDetail>(0);
      }
    }

    @Override
    public String getName() {
      return API_TYPE;
    }

    @Override
    protected boolean returnsList() {
      return true;
    }
  }

  /**
   * Retrieve details about a specific song.
   * <p/>
   * This class represents the API method <tt>AudioLibrary.GetSongDetails</tt>
   * <p/>
   * <i>This class was generated automatically from XBMC's JSON-RPC introspect.</i>
   */
  public static class GetSongDetails extends AbstractCall<AudioModel.SongDetail> {
    public final static String API_TYPE = "AudioLibrary.GetSongDetails";
    public final static String RESULT   = "songdetails";

    /**
     * Retrieve details about a specific song.
     * 
     * @param songid
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>artist</tt>, <tt>albumartist</tt>, <tt>genre</tt>, <tt>year</tt>, <tt>rating</tt>, <tt>album</tt>,
     *          <tt>track</tt>, <tt>duration</tt>, <tt>comment</tt>, <tt>lyrics</tt>, <tt>musicbrainztrackid</tt>, <tt>musicbrainzartistid</tt>,
     *          <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>, <tt>playcount</tt>, <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>file</tt>
     *          , <tt>albumid</tt>, <tt>lastplayed</tt>, <tt>disc</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>albumartistid</tt>, <tt>albumreleasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.SongFields}.
     */
    public GetSongDetails(Integer songid, String... properties) {
      super();
      addParameter("songid", songid);
      addParameter("properties", properties);
    }

    @Override
    protected AudioModel.SongDetail parseOne(JsonNode node) {
      return new AudioModel.SongDetail((ObjectNode) node.get(RESULT));
    }

    @Override
    public String getName() {
      return API_TYPE;
    }

    @Override
    protected boolean returnsList() {
      return false;
    }
  }

  /**
   * Retrieve all songs from specified album, artist or genre.
   * <p/>
   * This class represents the API method <tt>AudioLibrary.GetSongs</tt>
   * <p/>
   * <i>This class was generated automatically from XBMC's JSON-RPC introspect.</i>
   */
  public static class GetSongs extends AbstractCall<AudioModel.SongDetail> {
    public final static String API_TYPE = "AudioLibrary.GetSongs";
    public final static String RESULT   = "songs";

    /**
     * Retrieve all songs from specified album, artist or genre.
     * 
     * @param limits
     * @param sort
     * @param filter
     * @param includesingles
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>artist</tt>, <tt>albumartist</tt>, <tt>genre</tt>, <tt>year</tt>, <tt>rating</tt>, <tt>album</tt>,
     *          <tt>track</tt>, <tt>duration</tt>, <tt>comment</tt>, <tt>lyrics</tt>, <tt>musicbrainztrackid</tt>, <tt>musicbrainzartistid</tt>,
     *          <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>, <tt>playcount</tt>, <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>file</tt>
     *          , <tt>albumid</tt>, <tt>lastplayed</tt>, <tt>disc</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>albumartistid</tt>, <tt>albumreleasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.SongFields}.
     */
    public GetSongs(ListModel.Limits limits, ListModel.Sort sort, FilterGenreId filter, Boolean includesingles, String... properties) {
      super();
      addParameter("limits", limits);
      addParameter("sort", sort);
      addParameter("filter", filter);
      addParameter("includesingles", includesingles);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all songs from specified album, artist or genre.
     * 
     * @param limits
     * @param sort
     * @param filter
     * @param includesingles
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>artist</tt>, <tt>albumartist</tt>, <tt>genre</tt>, <tt>year</tt>, <tt>rating</tt>, <tt>album</tt>,
     *          <tt>track</tt>, <tt>duration</tt>, <tt>comment</tt>, <tt>lyrics</tt>, <tt>musicbrainztrackid</tt>, <tt>musicbrainzartistid</tt>,
     *          <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>, <tt>playcount</tt>, <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>file</tt>
     *          , <tt>albumid</tt>, <tt>lastplayed</tt>, <tt>disc</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>albumartistid</tt>, <tt>albumreleasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.SongFields}.
     */
    public GetSongs(ListModel.Limits limits, ListModel.Sort sort, FilterGenre filter, Boolean includesingles, String... properties) {
      super();
      addParameter("limits", limits);
      addParameter("sort", sort);
      addParameter("filter", filter);
      addParameter("includesingles", includesingles);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all songs from specified album, artist or genre.
     * 
     * @param limits
     * @param sort
     * @param filter
     * @param includesingles
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>artist</tt>, <tt>albumartist</tt>, <tt>genre</tt>, <tt>year</tt>, <tt>rating</tt>, <tt>album</tt>,
     *          <tt>track</tt>, <tt>duration</tt>, <tt>comment</tt>, <tt>lyrics</tt>, <tt>musicbrainztrackid</tt>, <tt>musicbrainzartistid</tt>,
     *          <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>, <tt>playcount</tt>, <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>file</tt>
     *          , <tt>albumid</tt>, <tt>lastplayed</tt>, <tt>disc</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>albumartistid</tt>, <tt>albumreleasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.SongFields}.
     */
    public GetSongs(ListModel.Limits limits, ListModel.Sort sort, FilterArtistId filter, Boolean includesingles, String... properties) {
      super();
      addParameter("limits", limits);
      addParameter("sort", sort);
      addParameter("filter", filter);
      addParameter("includesingles", includesingles);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all songs from specified album, artist or genre.
     * 
     * @param limits
     * @param sort
     * @param filter
     * @param includesingles
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>artist</tt>, <tt>albumartist</tt>, <tt>genre</tt>, <tt>year</tt>, <tt>rating</tt>, <tt>album</tt>,
     *          <tt>track</tt>, <tt>duration</tt>, <tt>comment</tt>, <tt>lyrics</tt>, <tt>musicbrainztrackid</tt>, <tt>musicbrainzartistid</tt>,
     *          <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>, <tt>playcount</tt>, <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>file</tt>
     *          , <tt>albumid</tt>, <tt>lastplayed</tt>, <tt>disc</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>albumartistid</tt>, <tt>albumreleasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.SongFields}.
     */
    public GetSongs(ListModel.Limits limits, ListModel.Sort sort, FilterArtist filter, Boolean includesingles, String... properties) {
      super();
      addParameter("limits", limits);
      addParameter("sort", sort);
      addParameter("filter", filter);
      addParameter("includesingles", includesingles);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all songs from specified album, artist or genre.
     * 
     * @param limits
     * @param sort
     * @param filter
     * @param includesingles
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>artist</tt>, <tt>albumartist</tt>, <tt>genre</tt>, <tt>year</tt>, <tt>rating</tt>, <tt>album</tt>,
     *          <tt>track</tt>, <tt>duration</tt>, <tt>comment</tt>, <tt>lyrics</tt>, <tt>musicbrainztrackid</tt>, <tt>musicbrainzartistid</tt>,
     *          <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>, <tt>playcount</tt>, <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>file</tt>
     *          , <tt>albumid</tt>, <tt>lastplayed</tt>, <tt>disc</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>albumartistid</tt>, <tt>albumreleasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.SongFields}.
     */
    public GetSongs(ListModel.Limits limits, ListModel.Sort sort, FilterAlbumId filter, Boolean includesingles, String... properties) {
      super();
      addParameter("limits", limits);
      addParameter("sort", sort);
      addParameter("filter", filter);
      addParameter("includesingles", includesingles);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all songs from specified album, artist or genre.
     * 
     * @param limits
     * @param sort
     * @param filter
     * @param includesingles
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>artist</tt>, <tt>albumartist</tt>, <tt>genre</tt>, <tt>year</tt>, <tt>rating</tt>, <tt>album</tt>,
     *          <tt>track</tt>, <tt>duration</tt>, <tt>comment</tt>, <tt>lyrics</tt>, <tt>musicbrainztrackid</tt>, <tt>musicbrainzartistid</tt>,
     *          <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>, <tt>playcount</tt>, <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>file</tt>
     *          , <tt>albumid</tt>, <tt>lastplayed</tt>, <tt>disc</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>albumartistid</tt>, <tt>albumreleasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.SongFields}.
     */
    public GetSongs(ListModel.Limits limits, ListModel.Sort sort, FilterAlbum filter, Boolean includesingles, String... properties) {
      super();
      addParameter("limits", limits);
      addParameter("sort", sort);
      addParameter("filter", filter);
      addParameter("includesingles", includesingles);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all songs from specified album, artist or genre.
     * 
     * @param limits
     * @param sort
     * @param filter
     * @param includesingles
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>artist</tt>, <tt>albumartist</tt>, <tt>genre</tt>, <tt>year</tt>, <tt>rating</tt>, <tt>album</tt>,
     *          <tt>track</tt>, <tt>duration</tt>, <tt>comment</tt>, <tt>lyrics</tt>, <tt>musicbrainztrackid</tt>, <tt>musicbrainzartistid</tt>,
     *          <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>, <tt>playcount</tt>, <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>file</tt>
     *          , <tt>albumid</tt>, <tt>lastplayed</tt>, <tt>disc</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>albumartistid</tt>, <tt>albumreleasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.SongFields}.
     */
    public GetSongs(ListModel.Limits limits, ListModel.Sort sort, ListModel.SongFilter filter, Boolean includesingles, String... properties) {
      super();
      addParameter("limits", limits);
      addParameter("sort", sort);
      addParameter("filter", filter);
      addParameter("includesingles", includesingles);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all songs from specified album, artist or genre.
     * 
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>artist</tt>, <tt>albumartist</tt>, <tt>genre</tt>, <tt>year</tt>, <tt>rating</tt>, <tt>album</tt>,
     *          <tt>track</tt>, <tt>duration</tt>, <tt>comment</tt>, <tt>lyrics</tt>, <tt>musicbrainztrackid</tt>, <tt>musicbrainzartistid</tt>,
     *          <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>, <tt>playcount</tt>, <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>file</tt>
     *          , <tt>albumid</tt>, <tt>lastplayed</tt>, <tt>disc</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>albumartistid</tt>, <tt>albumreleasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.SongFields}.
     */
    public GetSongs(String... properties) {
      super();
      addParameter("properties", properties);
    }

    /**
     * Retrieve all songs from specified album, artist or genre.
     * 
     * @param limits
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>artist</tt>, <tt>albumartist</tt>, <tt>genre</tt>, <tt>year</tt>, <tt>rating</tt>, <tt>album</tt>,
     *          <tt>track</tt>, <tt>duration</tt>, <tt>comment</tt>, <tt>lyrics</tt>, <tt>musicbrainztrackid</tt>, <tt>musicbrainzartistid</tt>,
     *          <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>, <tt>playcount</tt>, <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>file</tt>
     *          , <tt>albumid</tt>, <tt>lastplayed</tt>, <tt>disc</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>albumartistid</tt>, <tt>albumreleasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.SongFields}.
     */
    public GetSongs(ListModel.Limits limits, String... properties) {
      super();
      addParameter("limits", limits);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all songs from specified album, artist or genre.
     * 
     * @param sort
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>artist</tt>, <tt>albumartist</tt>, <tt>genre</tt>, <tt>year</tt>, <tt>rating</tt>, <tt>album</tt>,
     *          <tt>track</tt>, <tt>duration</tt>, <tt>comment</tt>, <tt>lyrics</tt>, <tt>musicbrainztrackid</tt>, <tt>musicbrainzartistid</tt>,
     *          <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>, <tt>playcount</tt>, <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>file</tt>
     *          , <tt>albumid</tt>, <tt>lastplayed</tt>, <tt>disc</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>albumartistid</tt>, <tt>albumreleasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.SongFields}.
     */
    public GetSongs(ListModel.Sort sort, String... properties) {
      super();
      addParameter("sort", sort);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all songs from specified album, artist or genre.
     * 
     * @param limits
     * @param sort
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>artist</tt>, <tt>albumartist</tt>, <tt>genre</tt>, <tt>year</tt>, <tt>rating</tt>, <tt>album</tt>,
     *          <tt>track</tt>, <tt>duration</tt>, <tt>comment</tt>, <tt>lyrics</tt>, <tt>musicbrainztrackid</tt>, <tt>musicbrainzartistid</tt>,
     *          <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>, <tt>playcount</tt>, <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>file</tt>
     *          , <tt>albumid</tt>, <tt>lastplayed</tt>, <tt>disc</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>albumartistid</tt>, <tt>albumreleasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.SongFields}.
     */
    public GetSongs(ListModel.Limits limits, ListModel.Sort sort, String... properties) {
      super();
      addParameter("limits", limits);
      addParameter("sort", sort);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all songs from specified album, artist or genre.
     * 
     * @param filter
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>artist</tt>, <tt>albumartist</tt>, <tt>genre</tt>, <tt>year</tt>, <tt>rating</tt>, <tt>album</tt>,
     *          <tt>track</tt>, <tt>duration</tt>, <tt>comment</tt>, <tt>lyrics</tt>, <tt>musicbrainztrackid</tt>, <tt>musicbrainzartistid</tt>,
     *          <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>, <tt>playcount</tt>, <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>file</tt>
     *          , <tt>albumid</tt>, <tt>lastplayed</tt>, <tt>disc</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>albumartistid</tt>, <tt>albumreleasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.SongFields}.
     */
    public GetSongs(FilterGenreId filter, String... properties) {
      super();
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all songs from specified album, artist or genre.
     * 
     * @param limits
     * @param filter
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>artist</tt>, <tt>albumartist</tt>, <tt>genre</tt>, <tt>year</tt>, <tt>rating</tt>, <tt>album</tt>,
     *          <tt>track</tt>, <tt>duration</tt>, <tt>comment</tt>, <tt>lyrics</tt>, <tt>musicbrainztrackid</tt>, <tt>musicbrainzartistid</tt>,
     *          <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>, <tt>playcount</tt>, <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>file</tt>
     *          , <tt>albumid</tt>, <tt>lastplayed</tt>, <tt>disc</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>albumartistid</tt>, <tt>albumreleasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.SongFields}.
     */
    public GetSongs(ListModel.Limits limits, FilterGenreId filter, String... properties) {
      super();
      addParameter("limits", limits);
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all songs from specified album, artist or genre.
     * 
     * @param sort
     * @param filter
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>artist</tt>, <tt>albumartist</tt>, <tt>genre</tt>, <tt>year</tt>, <tt>rating</tt>, <tt>album</tt>,
     *          <tt>track</tt>, <tt>duration</tt>, <tt>comment</tt>, <tt>lyrics</tt>, <tt>musicbrainztrackid</tt>, <tt>musicbrainzartistid</tt>,
     *          <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>, <tt>playcount</tt>, <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>file</tt>
     *          , <tt>albumid</tt>, <tt>lastplayed</tt>, <tt>disc</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>albumartistid</tt>, <tt>albumreleasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.SongFields}.
     */
    public GetSongs(ListModel.Sort sort, FilterGenreId filter, String... properties) {
      super();
      addParameter("sort", sort);
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all songs from specified album, artist or genre.
     * 
     * @param limits
     * @param sort
     * @param filter
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>artist</tt>, <tt>albumartist</tt>, <tt>genre</tt>, <tt>year</tt>, <tt>rating</tt>, <tt>album</tt>,
     *          <tt>track</tt>, <tt>duration</tt>, <tt>comment</tt>, <tt>lyrics</tt>, <tt>musicbrainztrackid</tt>, <tt>musicbrainzartistid</tt>,
     *          <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>, <tt>playcount</tt>, <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>file</tt>
     *          , <tt>albumid</tt>, <tt>lastplayed</tt>, <tt>disc</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>albumartistid</tt>, <tt>albumreleasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.SongFields}.
     */
    public GetSongs(ListModel.Limits limits, ListModel.Sort sort, FilterGenreId filter, String... properties) {
      super();
      addParameter("limits", limits);
      addParameter("sort", sort);
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all songs from specified album, artist or genre.
     * 
     * @param includesingles
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>artist</tt>, <tt>albumartist</tt>, <tt>genre</tt>, <tt>year</tt>, <tt>rating</tt>, <tt>album</tt>,
     *          <tt>track</tt>, <tt>duration</tt>, <tt>comment</tt>, <tt>lyrics</tt>, <tt>musicbrainztrackid</tt>, <tt>musicbrainzartistid</tt>,
     *          <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>, <tt>playcount</tt>, <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>file</tt>
     *          , <tt>albumid</tt>, <tt>lastplayed</tt>, <tt>disc</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>albumartistid</tt>, <tt>albumreleasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.SongFields}.
     */
    public GetSongs(Boolean includesingles, String... properties) {
      super();
      addParameter("includesingles", includesingles);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all songs from specified album, artist or genre.
     * 
     * @param limits
     * @param includesingles
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>artist</tt>, <tt>albumartist</tt>, <tt>genre</tt>, <tt>year</tt>, <tt>rating</tt>, <tt>album</tt>,
     *          <tt>track</tt>, <tt>duration</tt>, <tt>comment</tt>, <tt>lyrics</tt>, <tt>musicbrainztrackid</tt>, <tt>musicbrainzartistid</tt>,
     *          <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>, <tt>playcount</tt>, <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>file</tt>
     *          , <tt>albumid</tt>, <tt>lastplayed</tt>, <tt>disc</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>albumartistid</tt>, <tt>albumreleasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.SongFields}.
     */
    public GetSongs(ListModel.Limits limits, Boolean includesingles, String... properties) {
      super();
      addParameter("limits", limits);
      addParameter("includesingles", includesingles);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all songs from specified album, artist or genre.
     * 
     * @param sort
     * @param includesingles
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>artist</tt>, <tt>albumartist</tt>, <tt>genre</tt>, <tt>year</tt>, <tt>rating</tt>, <tt>album</tt>,
     *          <tt>track</tt>, <tt>duration</tt>, <tt>comment</tt>, <tt>lyrics</tt>, <tt>musicbrainztrackid</tt>, <tt>musicbrainzartistid</tt>,
     *          <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>, <tt>playcount</tt>, <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>file</tt>
     *          , <tt>albumid</tt>, <tt>lastplayed</tt>, <tt>disc</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>albumartistid</tt>, <tt>albumreleasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.SongFields}.
     */
    public GetSongs(ListModel.Sort sort, Boolean includesingles, String... properties) {
      super();
      addParameter("sort", sort);
      addParameter("includesingles", includesingles);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all songs from specified album, artist or genre.
     * 
     * @param limits
     * @param sort
     * @param includesingles
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>artist</tt>, <tt>albumartist</tt>, <tt>genre</tt>, <tt>year</tt>, <tt>rating</tt>, <tt>album</tt>,
     *          <tt>track</tt>, <tt>duration</tt>, <tt>comment</tt>, <tt>lyrics</tt>, <tt>musicbrainztrackid</tt>, <tt>musicbrainzartistid</tt>,
     *          <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>, <tt>playcount</tt>, <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>file</tt>
     *          , <tt>albumid</tt>, <tt>lastplayed</tt>, <tt>disc</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>albumartistid</tt>, <tt>albumreleasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.SongFields}.
     */
    public GetSongs(ListModel.Limits limits, ListModel.Sort sort, Boolean includesingles, String... properties) {
      super();
      addParameter("limits", limits);
      addParameter("sort", sort);
      addParameter("includesingles", includesingles);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all songs from specified album, artist or genre.
     * 
     * @param filter
     * @param includesingles
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>artist</tt>, <tt>albumartist</tt>, <tt>genre</tt>, <tt>year</tt>, <tt>rating</tt>, <tt>album</tt>,
     *          <tt>track</tt>, <tt>duration</tt>, <tt>comment</tt>, <tt>lyrics</tt>, <tt>musicbrainztrackid</tt>, <tt>musicbrainzartistid</tt>,
     *          <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>, <tt>playcount</tt>, <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>file</tt>
     *          , <tt>albumid</tt>, <tt>lastplayed</tt>, <tt>disc</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>albumartistid</tt>, <tt>albumreleasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.SongFields}.
     */
    public GetSongs(FilterGenreId filter, Boolean includesingles, String... properties) {
      super();
      addParameter("filter", filter);
      addParameter("includesingles", includesingles);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all songs from specified album, artist or genre.
     * 
     * @param limits
     * @param filter
     * @param includesingles
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>artist</tt>, <tt>albumartist</tt>, <tt>genre</tt>, <tt>year</tt>, <tt>rating</tt>, <tt>album</tt>,
     *          <tt>track</tt>, <tt>duration</tt>, <tt>comment</tt>, <tt>lyrics</tt>, <tt>musicbrainztrackid</tt>, <tt>musicbrainzartistid</tt>,
     *          <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>, <tt>playcount</tt>, <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>file</tt>
     *          , <tt>albumid</tt>, <tt>lastplayed</tt>, <tt>disc</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>albumartistid</tt>, <tt>albumreleasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.SongFields}.
     */
    public GetSongs(ListModel.Limits limits, FilterGenreId filter, Boolean includesingles, String... properties) {
      super();
      addParameter("limits", limits);
      addParameter("filter", filter);
      addParameter("includesingles", includesingles);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all songs from specified album, artist or genre.
     * 
     * @param filter
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>artist</tt>, <tt>albumartist</tt>, <tt>genre</tt>, <tt>year</tt>, <tt>rating</tt>, <tt>album</tt>,
     *          <tt>track</tt>, <tt>duration</tt>, <tt>comment</tt>, <tt>lyrics</tt>, <tt>musicbrainztrackid</tt>, <tt>musicbrainzartistid</tt>,
     *          <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>, <tt>playcount</tt>, <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>file</tt>
     *          , <tt>albumid</tt>, <tt>lastplayed</tt>, <tt>disc</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>albumartistid</tt>, <tt>albumreleasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.SongFields}.
     */
    public GetSongs(FilterGenre filter, String... properties) {
      super();
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all songs from specified album, artist or genre.
     * 
     * @param limits
     * @param filter
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>artist</tt>, <tt>albumartist</tt>, <tt>genre</tt>, <tt>year</tt>, <tt>rating</tt>, <tt>album</tt>,
     *          <tt>track</tt>, <tt>duration</tt>, <tt>comment</tt>, <tt>lyrics</tt>, <tt>musicbrainztrackid</tt>, <tt>musicbrainzartistid</tt>,
     *          <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>, <tt>playcount</tt>, <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>file</tt>
     *          , <tt>albumid</tt>, <tt>lastplayed</tt>, <tt>disc</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>albumartistid</tt>, <tt>albumreleasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.SongFields}.
     */
    public GetSongs(ListModel.Limits limits, FilterGenre filter, String... properties) {
      super();
      addParameter("limits", limits);
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all songs from specified album, artist or genre.
     * 
     * @param sort
     * @param filter
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>artist</tt>, <tt>albumartist</tt>, <tt>genre</tt>, <tt>year</tt>, <tt>rating</tt>, <tt>album</tt>,
     *          <tt>track</tt>, <tt>duration</tt>, <tt>comment</tt>, <tt>lyrics</tt>, <tt>musicbrainztrackid</tt>, <tt>musicbrainzartistid</tt>,
     *          <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>, <tt>playcount</tt>, <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>file</tt>
     *          , <tt>albumid</tt>, <tt>lastplayed</tt>, <tt>disc</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>albumartistid</tt>, <tt>albumreleasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.SongFields}.
     */
    public GetSongs(ListModel.Sort sort, FilterGenre filter, String... properties) {
      super();
      addParameter("sort", sort);
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all songs from specified album, artist or genre.
     * 
     * @param limits
     * @param sort
     * @param filter
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>artist</tt>, <tt>albumartist</tt>, <tt>genre</tt>, <tt>year</tt>, <tt>rating</tt>, <tt>album</tt>,
     *          <tt>track</tt>, <tt>duration</tt>, <tt>comment</tt>, <tt>lyrics</tt>, <tt>musicbrainztrackid</tt>, <tt>musicbrainzartistid</tt>,
     *          <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>, <tt>playcount</tt>, <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>file</tt>
     *          , <tt>albumid</tt>, <tt>lastplayed</tt>, <tt>disc</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>albumartistid</tt>, <tt>albumreleasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.SongFields}.
     */
    public GetSongs(ListModel.Limits limits, ListModel.Sort sort, FilterGenre filter, String... properties) {
      super();
      addParameter("limits", limits);
      addParameter("sort", sort);
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all songs from specified album, artist or genre.
     * 
     * @param filter
     * @param includesingles
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>artist</tt>, <tt>albumartist</tt>, <tt>genre</tt>, <tt>year</tt>, <tt>rating</tt>, <tt>album</tt>,
     *          <tt>track</tt>, <tt>duration</tt>, <tt>comment</tt>, <tt>lyrics</tt>, <tt>musicbrainztrackid</tt>, <tt>musicbrainzartistid</tt>,
     *          <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>, <tt>playcount</tt>, <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>file</tt>
     *          , <tt>albumid</tt>, <tt>lastplayed</tt>, <tt>disc</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>albumartistid</tt>, <tt>albumreleasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.SongFields}.
     */
    public GetSongs(FilterGenre filter, Boolean includesingles, String... properties) {
      super();
      addParameter("filter", filter);
      addParameter("includesingles", includesingles);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all songs from specified album, artist or genre.
     * 
     * @param limits
     * @param filter
     * @param includesingles
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>artist</tt>, <tt>albumartist</tt>, <tt>genre</tt>, <tt>year</tt>, <tt>rating</tt>, <tt>album</tt>,
     *          <tt>track</tt>, <tt>duration</tt>, <tt>comment</tt>, <tt>lyrics</tt>, <tt>musicbrainztrackid</tt>, <tt>musicbrainzartistid</tt>,
     *          <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>, <tt>playcount</tt>, <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>file</tt>
     *          , <tt>albumid</tt>, <tt>lastplayed</tt>, <tt>disc</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>albumartistid</tt>, <tt>albumreleasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.SongFields}.
     */
    public GetSongs(ListModel.Limits limits, FilterGenre filter, Boolean includesingles, String... properties) {
      super();
      addParameter("limits", limits);
      addParameter("filter", filter);
      addParameter("includesingles", includesingles);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all songs from specified album, artist or genre.
     * 
     * @param filter
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>artist</tt>, <tt>albumartist</tt>, <tt>genre</tt>, <tt>year</tt>, <tt>rating</tt>, <tt>album</tt>,
     *          <tt>track</tt>, <tt>duration</tt>, <tt>comment</tt>, <tt>lyrics</tt>, <tt>musicbrainztrackid</tt>, <tt>musicbrainzartistid</tt>,
     *          <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>, <tt>playcount</tt>, <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>file</tt>
     *          , <tt>albumid</tt>, <tt>lastplayed</tt>, <tt>disc</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>albumartistid</tt>, <tt>albumreleasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.SongFields}.
     */
    public GetSongs(FilterArtistId filter, String... properties) {
      super();
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all songs from specified album, artist or genre.
     * 
     * @param limits
     * @param filter
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>artist</tt>, <tt>albumartist</tt>, <tt>genre</tt>, <tt>year</tt>, <tt>rating</tt>, <tt>album</tt>,
     *          <tt>track</tt>, <tt>duration</tt>, <tt>comment</tt>, <tt>lyrics</tt>, <tt>musicbrainztrackid</tt>, <tt>musicbrainzartistid</tt>,
     *          <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>, <tt>playcount</tt>, <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>file</tt>
     *          , <tt>albumid</tt>, <tt>lastplayed</tt>, <tt>disc</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>albumartistid</tt>, <tt>albumreleasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.SongFields}.
     */
    public GetSongs(ListModel.Limits limits, FilterArtistId filter, String... properties) {
      super();
      addParameter("limits", limits);
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all songs from specified album, artist or genre.
     * 
     * @param sort
     * @param filter
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>artist</tt>, <tt>albumartist</tt>, <tt>genre</tt>, <tt>year</tt>, <tt>rating</tt>, <tt>album</tt>,
     *          <tt>track</tt>, <tt>duration</tt>, <tt>comment</tt>, <tt>lyrics</tt>, <tt>musicbrainztrackid</tt>, <tt>musicbrainzartistid</tt>,
     *          <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>, <tt>playcount</tt>, <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>file</tt>
     *          , <tt>albumid</tt>, <tt>lastplayed</tt>, <tt>disc</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>albumartistid</tt>, <tt>albumreleasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.SongFields}.
     */
    public GetSongs(ListModel.Sort sort, FilterArtistId filter, String... properties) {
      super();
      addParameter("sort", sort);
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all songs from specified album, artist or genre.
     * 
     * @param limits
     * @param sort
     * @param filter
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>artist</tt>, <tt>albumartist</tt>, <tt>genre</tt>, <tt>year</tt>, <tt>rating</tt>, <tt>album</tt>,
     *          <tt>track</tt>, <tt>duration</tt>, <tt>comment</tt>, <tt>lyrics</tt>, <tt>musicbrainztrackid</tt>, <tt>musicbrainzartistid</tt>,
     *          <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>, <tt>playcount</tt>, <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>file</tt>
     *          , <tt>albumid</tt>, <tt>lastplayed</tt>, <tt>disc</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>albumartistid</tt>, <tt>albumreleasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.SongFields}.
     */
    public GetSongs(ListModel.Limits limits, ListModel.Sort sort, FilterArtistId filter, String... properties) {
      super();
      addParameter("limits", limits);
      addParameter("sort", sort);
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all songs from specified album, artist or genre.
     * 
     * @param filter
     * @param includesingles
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>artist</tt>, <tt>albumartist</tt>, <tt>genre</tt>, <tt>year</tt>, <tt>rating</tt>, <tt>album</tt>,
     *          <tt>track</tt>, <tt>duration</tt>, <tt>comment</tt>, <tt>lyrics</tt>, <tt>musicbrainztrackid</tt>, <tt>musicbrainzartistid</tt>,
     *          <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>, <tt>playcount</tt>, <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>file</tt>
     *          , <tt>albumid</tt>, <tt>lastplayed</tt>, <tt>disc</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>albumartistid</tt>, <tt>albumreleasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.SongFields}.
     */
    public GetSongs(FilterArtistId filter, Boolean includesingles, String... properties) {
      super();
      addParameter("filter", filter);
      addParameter("includesingles", includesingles);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all songs from specified album, artist or genre.
     * 
     * @param limits
     * @param filter
     * @param includesingles
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>artist</tt>, <tt>albumartist</tt>, <tt>genre</tt>, <tt>year</tt>, <tt>rating</tt>, <tt>album</tt>,
     *          <tt>track</tt>, <tt>duration</tt>, <tt>comment</tt>, <tt>lyrics</tt>, <tt>musicbrainztrackid</tt>, <tt>musicbrainzartistid</tt>,
     *          <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>, <tt>playcount</tt>, <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>file</tt>
     *          , <tt>albumid</tt>, <tt>lastplayed</tt>, <tt>disc</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>albumartistid</tt>, <tt>albumreleasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.SongFields}.
     */
    public GetSongs(ListModel.Limits limits, FilterArtistId filter, Boolean includesingles, String... properties) {
      super();
      addParameter("limits", limits);
      addParameter("filter", filter);
      addParameter("includesingles", includesingles);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all songs from specified album, artist or genre.
     * 
     * @param filter
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>artist</tt>, <tt>albumartist</tt>, <tt>genre</tt>, <tt>year</tt>, <tt>rating</tt>, <tt>album</tt>,
     *          <tt>track</tt>, <tt>duration</tt>, <tt>comment</tt>, <tt>lyrics</tt>, <tt>musicbrainztrackid</tt>, <tt>musicbrainzartistid</tt>,
     *          <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>, <tt>playcount</tt>, <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>file</tt>
     *          , <tt>albumid</tt>, <tt>lastplayed</tt>, <tt>disc</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>albumartistid</tt>, <tt>albumreleasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.SongFields}.
     */
    public GetSongs(FilterArtist filter, String... properties) {
      super();
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all songs from specified album, artist or genre.
     * 
     * @param limits
     * @param filter
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>artist</tt>, <tt>albumartist</tt>, <tt>genre</tt>, <tt>year</tt>, <tt>rating</tt>, <tt>album</tt>,
     *          <tt>track</tt>, <tt>duration</tt>, <tt>comment</tt>, <tt>lyrics</tt>, <tt>musicbrainztrackid</tt>, <tt>musicbrainzartistid</tt>,
     *          <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>, <tt>playcount</tt>, <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>file</tt>
     *          , <tt>albumid</tt>, <tt>lastplayed</tt>, <tt>disc</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>albumartistid</tt>, <tt>albumreleasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.SongFields}.
     */
    public GetSongs(ListModel.Limits limits, FilterArtist filter, String... properties) {
      super();
      addParameter("limits", limits);
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all songs from specified album, artist or genre.
     * 
     * @param sort
     * @param filter
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>artist</tt>, <tt>albumartist</tt>, <tt>genre</tt>, <tt>year</tt>, <tt>rating</tt>, <tt>album</tt>,
     *          <tt>track</tt>, <tt>duration</tt>, <tt>comment</tt>, <tt>lyrics</tt>, <tt>musicbrainztrackid</tt>, <tt>musicbrainzartistid</tt>,
     *          <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>, <tt>playcount</tt>, <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>file</tt>
     *          , <tt>albumid</tt>, <tt>lastplayed</tt>, <tt>disc</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>albumartistid</tt>, <tt>albumreleasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.SongFields}.
     */
    public GetSongs(ListModel.Sort sort, FilterArtist filter, String... properties) {
      super();
      addParameter("sort", sort);
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all songs from specified album, artist or genre.
     * 
     * @param limits
     * @param sort
     * @param filter
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>artist</tt>, <tt>albumartist</tt>, <tt>genre</tt>, <tt>year</tt>, <tt>rating</tt>, <tt>album</tt>,
     *          <tt>track</tt>, <tt>duration</tt>, <tt>comment</tt>, <tt>lyrics</tt>, <tt>musicbrainztrackid</tt>, <tt>musicbrainzartistid</tt>,
     *          <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>, <tt>playcount</tt>, <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>file</tt>
     *          , <tt>albumid</tt>, <tt>lastplayed</tt>, <tt>disc</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>albumartistid</tt>, <tt>albumreleasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.SongFields}.
     */
    public GetSongs(ListModel.Limits limits, ListModel.Sort sort, FilterArtist filter, String... properties) {
      super();
      addParameter("limits", limits);
      addParameter("sort", sort);
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all songs from specified album, artist or genre.
     * 
     * @param filter
     * @param includesingles
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>artist</tt>, <tt>albumartist</tt>, <tt>genre</tt>, <tt>year</tt>, <tt>rating</tt>, <tt>album</tt>,
     *          <tt>track</tt>, <tt>duration</tt>, <tt>comment</tt>, <tt>lyrics</tt>, <tt>musicbrainztrackid</tt>, <tt>musicbrainzartistid</tt>,
     *          <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>, <tt>playcount</tt>, <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>file</tt>
     *          , <tt>albumid</tt>, <tt>lastplayed</tt>, <tt>disc</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>albumartistid</tt>, <tt>albumreleasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.SongFields}.
     */
    public GetSongs(FilterArtist filter, Boolean includesingles, String... properties) {
      super();
      addParameter("filter", filter);
      addParameter("includesingles", includesingles);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all songs from specified album, artist or genre.
     * 
     * @param limits
     * @param filter
     * @param includesingles
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>artist</tt>, <tt>albumartist</tt>, <tt>genre</tt>, <tt>year</tt>, <tt>rating</tt>, <tt>album</tt>,
     *          <tt>track</tt>, <tt>duration</tt>, <tt>comment</tt>, <tt>lyrics</tt>, <tt>musicbrainztrackid</tt>, <tt>musicbrainzartistid</tt>,
     *          <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>, <tt>playcount</tt>, <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>file</tt>
     *          , <tt>albumid</tt>, <tt>lastplayed</tt>, <tt>disc</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>albumartistid</tt>, <tt>albumreleasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.SongFields}.
     */
    public GetSongs(ListModel.Limits limits, FilterArtist filter, Boolean includesingles, String... properties) {
      super();
      addParameter("limits", limits);
      addParameter("filter", filter);
      addParameter("includesingles", includesingles);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all songs from specified album, artist or genre.
     * 
     * @param filter
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>artist</tt>, <tt>albumartist</tt>, <tt>genre</tt>, <tt>year</tt>, <tt>rating</tt>, <tt>album</tt>,
     *          <tt>track</tt>, <tt>duration</tt>, <tt>comment</tt>, <tt>lyrics</tt>, <tt>musicbrainztrackid</tt>, <tt>musicbrainzartistid</tt>,
     *          <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>, <tt>playcount</tt>, <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>file</tt>
     *          , <tt>albumid</tt>, <tt>lastplayed</tt>, <tt>disc</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>albumartistid</tt>, <tt>albumreleasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.SongFields}.
     */
    public GetSongs(FilterAlbumId filter, String... properties) {
      super();
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all songs from specified album, artist or genre.
     * 
     * @param limits
     * @param filter
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>artist</tt>, <tt>albumartist</tt>, <tt>genre</tt>, <tt>year</tt>, <tt>rating</tt>, <tt>album</tt>,
     *          <tt>track</tt>, <tt>duration</tt>, <tt>comment</tt>, <tt>lyrics</tt>, <tt>musicbrainztrackid</tt>, <tt>musicbrainzartistid</tt>,
     *          <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>, <tt>playcount</tt>, <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>file</tt>
     *          , <tt>albumid</tt>, <tt>lastplayed</tt>, <tt>disc</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>albumartistid</tt>, <tt>albumreleasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.SongFields}.
     */
    public GetSongs(ListModel.Limits limits, FilterAlbumId filter, String... properties) {
      super();
      addParameter("limits", limits);
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all songs from specified album, artist or genre.
     * 
     * @param sort
     * @param filter
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>artist</tt>, <tt>albumartist</tt>, <tt>genre</tt>, <tt>year</tt>, <tt>rating</tt>, <tt>album</tt>,
     *          <tt>track</tt>, <tt>duration</tt>, <tt>comment</tt>, <tt>lyrics</tt>, <tt>musicbrainztrackid</tt>, <tt>musicbrainzartistid</tt>,
     *          <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>, <tt>playcount</tt>, <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>file</tt>
     *          , <tt>albumid</tt>, <tt>lastplayed</tt>, <tt>disc</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>albumartistid</tt>, <tt>albumreleasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.SongFields}.
     */
    public GetSongs(ListModel.Sort sort, FilterAlbumId filter, String... properties) {
      super();
      addParameter("sort", sort);
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all songs from specified album, artist or genre.
     * 
     * @param limits
     * @param sort
     * @param filter
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>artist</tt>, <tt>albumartist</tt>, <tt>genre</tt>, <tt>year</tt>, <tt>rating</tt>, <tt>album</tt>,
     *          <tt>track</tt>, <tt>duration</tt>, <tt>comment</tt>, <tt>lyrics</tt>, <tt>musicbrainztrackid</tt>, <tt>musicbrainzartistid</tt>,
     *          <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>, <tt>playcount</tt>, <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>file</tt>
     *          , <tt>albumid</tt>, <tt>lastplayed</tt>, <tt>disc</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>albumartistid</tt>, <tt>albumreleasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.SongFields}.
     */
    public GetSongs(ListModel.Limits limits, ListModel.Sort sort, FilterAlbumId filter, String... properties) {
      super();
      addParameter("limits", limits);
      addParameter("sort", sort);
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all songs from specified album, artist or genre.
     * 
     * @param filter
     * @param includesingles
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>artist</tt>, <tt>albumartist</tt>, <tt>genre</tt>, <tt>year</tt>, <tt>rating</tt>, <tt>album</tt>,
     *          <tt>track</tt>, <tt>duration</tt>, <tt>comment</tt>, <tt>lyrics</tt>, <tt>musicbrainztrackid</tt>, <tt>musicbrainzartistid</tt>,
     *          <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>, <tt>playcount</tt>, <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>file</tt>
     *          , <tt>albumid</tt>, <tt>lastplayed</tt>, <tt>disc</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>albumartistid</tt>, <tt>albumreleasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.SongFields}.
     */
    public GetSongs(FilterAlbumId filter, Boolean includesingles, String... properties) {
      super();
      addParameter("filter", filter);
      addParameter("includesingles", includesingles);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all songs from specified album, artist or genre.
     * 
     * @param limits
     * @param filter
     * @param includesingles
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>artist</tt>, <tt>albumartist</tt>, <tt>genre</tt>, <tt>year</tt>, <tt>rating</tt>, <tt>album</tt>,
     *          <tt>track</tt>, <tt>duration</tt>, <tt>comment</tt>, <tt>lyrics</tt>, <tt>musicbrainztrackid</tt>, <tt>musicbrainzartistid</tt>,
     *          <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>, <tt>playcount</tt>, <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>file</tt>
     *          , <tt>albumid</tt>, <tt>lastplayed</tt>, <tt>disc</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>albumartistid</tt>, <tt>albumreleasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.SongFields}.
     */
    public GetSongs(ListModel.Limits limits, FilterAlbumId filter, Boolean includesingles, String... properties) {
      super();
      addParameter("limits", limits);
      addParameter("filter", filter);
      addParameter("includesingles", includesingles);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all songs from specified album, artist or genre.
     * 
     * @param filter
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>artist</tt>, <tt>albumartist</tt>, <tt>genre</tt>, <tt>year</tt>, <tt>rating</tt>, <tt>album</tt>,
     *          <tt>track</tt>, <tt>duration</tt>, <tt>comment</tt>, <tt>lyrics</tt>, <tt>musicbrainztrackid</tt>, <tt>musicbrainzartistid</tt>,
     *          <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>, <tt>playcount</tt>, <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>file</tt>
     *          , <tt>albumid</tt>, <tt>lastplayed</tt>, <tt>disc</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>albumartistid</tt>, <tt>albumreleasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.SongFields}.
     */
    public GetSongs(FilterAlbum filter, String... properties) {
      super();
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all songs from specified album, artist or genre.
     * 
     * @param limits
     * @param filter
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>artist</tt>, <tt>albumartist</tt>, <tt>genre</tt>, <tt>year</tt>, <tt>rating</tt>, <tt>album</tt>,
     *          <tt>track</tt>, <tt>duration</tt>, <tt>comment</tt>, <tt>lyrics</tt>, <tt>musicbrainztrackid</tt>, <tt>musicbrainzartistid</tt>,
     *          <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>, <tt>playcount</tt>, <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>file</tt>
     *          , <tt>albumid</tt>, <tt>lastplayed</tt>, <tt>disc</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>albumartistid</tt>, <tt>albumreleasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.SongFields}.
     */
    public GetSongs(ListModel.Limits limits, FilterAlbum filter, String... properties) {
      super();
      addParameter("limits", limits);
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all songs from specified album, artist or genre.
     * 
     * @param sort
     * @param filter
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>artist</tt>, <tt>albumartist</tt>, <tt>genre</tt>, <tt>year</tt>, <tt>rating</tt>, <tt>album</tt>,
     *          <tt>track</tt>, <tt>duration</tt>, <tt>comment</tt>, <tt>lyrics</tt>, <tt>musicbrainztrackid</tt>, <tt>musicbrainzartistid</tt>,
     *          <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>, <tt>playcount</tt>, <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>file</tt>
     *          , <tt>albumid</tt>, <tt>lastplayed</tt>, <tt>disc</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>albumartistid</tt>, <tt>albumreleasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.SongFields}.
     */
    public GetSongs(ListModel.Sort sort, FilterAlbum filter, String... properties) {
      super();
      addParameter("sort", sort);
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all songs from specified album, artist or genre.
     * 
     * @param limits
     * @param sort
     * @param filter
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>artist</tt>, <tt>albumartist</tt>, <tt>genre</tt>, <tt>year</tt>, <tt>rating</tt>, <tt>album</tt>,
     *          <tt>track</tt>, <tt>duration</tt>, <tt>comment</tt>, <tt>lyrics</tt>, <tt>musicbrainztrackid</tt>, <tt>musicbrainzartistid</tt>,
     *          <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>, <tt>playcount</tt>, <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>file</tt>
     *          , <tt>albumid</tt>, <tt>lastplayed</tt>, <tt>disc</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>albumartistid</tt>, <tt>albumreleasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.SongFields}.
     */
    public GetSongs(ListModel.Limits limits, ListModel.Sort sort, FilterAlbum filter, String... properties) {
      super();
      addParameter("limits", limits);
      addParameter("sort", sort);
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all songs from specified album, artist or genre.
     * 
     * @param filter
     * @param includesingles
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>artist</tt>, <tt>albumartist</tt>, <tt>genre</tt>, <tt>year</tt>, <tt>rating</tt>, <tt>album</tt>,
     *          <tt>track</tt>, <tt>duration</tt>, <tt>comment</tt>, <tt>lyrics</tt>, <tt>musicbrainztrackid</tt>, <tt>musicbrainzartistid</tt>,
     *          <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>, <tt>playcount</tt>, <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>file</tt>
     *          , <tt>albumid</tt>, <tt>lastplayed</tt>, <tt>disc</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>albumartistid</tt>, <tt>albumreleasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.SongFields}.
     */
    public GetSongs(FilterAlbum filter, Boolean includesingles, String... properties) {
      super();
      addParameter("filter", filter);
      addParameter("includesingles", includesingles);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all songs from specified album, artist or genre.
     * 
     * @param limits
     * @param filter
     * @param includesingles
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>artist</tt>, <tt>albumartist</tt>, <tt>genre</tt>, <tt>year</tt>, <tt>rating</tt>, <tt>album</tt>,
     *          <tt>track</tt>, <tt>duration</tt>, <tt>comment</tt>, <tt>lyrics</tt>, <tt>musicbrainztrackid</tt>, <tt>musicbrainzartistid</tt>,
     *          <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>, <tt>playcount</tt>, <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>file</tt>
     *          , <tt>albumid</tt>, <tt>lastplayed</tt>, <tt>disc</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>albumartistid</tt>, <tt>albumreleasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.SongFields}.
     */
    public GetSongs(ListModel.Limits limits, FilterAlbum filter, Boolean includesingles, String... properties) {
      super();
      addParameter("limits", limits);
      addParameter("filter", filter);
      addParameter("includesingles", includesingles);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all songs from specified album, artist or genre.
     * 
     * @param filter
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>artist</tt>, <tt>albumartist</tt>, <tt>genre</tt>, <tt>year</tt>, <tt>rating</tt>, <tt>album</tt>,
     *          <tt>track</tt>, <tt>duration</tt>, <tt>comment</tt>, <tt>lyrics</tt>, <tt>musicbrainztrackid</tt>, <tt>musicbrainzartistid</tt>,
     *          <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>, <tt>playcount</tt>, <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>file</tt>
     *          , <tt>albumid</tt>, <tt>lastplayed</tt>, <tt>disc</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>albumartistid</tt>, <tt>albumreleasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.SongFields}.
     */
    public GetSongs(ListModel.SongFilter filter, String... properties) {
      super();
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all songs from specified album, artist or genre.
     * 
     * @param limits
     * @param filter
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>artist</tt>, <tt>albumartist</tt>, <tt>genre</tt>, <tt>year</tt>, <tt>rating</tt>, <tt>album</tt>,
     *          <tt>track</tt>, <tt>duration</tt>, <tt>comment</tt>, <tt>lyrics</tt>, <tt>musicbrainztrackid</tt>, <tt>musicbrainzartistid</tt>,
     *          <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>, <tt>playcount</tt>, <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>file</tt>
     *          , <tt>albumid</tt>, <tt>lastplayed</tt>, <tt>disc</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>albumartistid</tt>, <tt>albumreleasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.SongFields}.
     */
    public GetSongs(ListModel.Limits limits, ListModel.SongFilter filter, String... properties) {
      super();
      addParameter("limits", limits);
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all songs from specified album, artist or genre.
     * 
     * @param sort
     * @param filter
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>artist</tt>, <tt>albumartist</tt>, <tt>genre</tt>, <tt>year</tt>, <tt>rating</tt>, <tt>album</tt>,
     *          <tt>track</tt>, <tt>duration</tt>, <tt>comment</tt>, <tt>lyrics</tt>, <tt>musicbrainztrackid</tt>, <tt>musicbrainzartistid</tt>,
     *          <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>, <tt>playcount</tt>, <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>file</tt>
     *          , <tt>albumid</tt>, <tt>lastplayed</tt>, <tt>disc</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>albumartistid</tt>, <tt>albumreleasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.SongFields}.
     */
    public GetSongs(ListModel.Sort sort, ListModel.SongFilter filter, String... properties) {
      super();
      addParameter("sort", sort);
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all songs from specified album, artist or genre.
     * 
     * @param limits
     * @param sort
     * @param filter
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>artist</tt>, <tt>albumartist</tt>, <tt>genre</tt>, <tt>year</tt>, <tt>rating</tt>, <tt>album</tt>,
     *          <tt>track</tt>, <tt>duration</tt>, <tt>comment</tt>, <tt>lyrics</tt>, <tt>musicbrainztrackid</tt>, <tt>musicbrainzartistid</tt>,
     *          <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>, <tt>playcount</tt>, <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>file</tt>
     *          , <tt>albumid</tt>, <tt>lastplayed</tt>, <tt>disc</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>albumartistid</tt>, <tt>albumreleasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.SongFields}.
     */
    public GetSongs(ListModel.Limits limits, ListModel.Sort sort, ListModel.SongFilter filter, String... properties) {
      super();
      addParameter("limits", limits);
      addParameter("sort", sort);
      addParameter("filter", filter);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all songs from specified album, artist or genre.
     * 
     * @param filter
     * @param includesingles
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>artist</tt>, <tt>albumartist</tt>, <tt>genre</tt>, <tt>year</tt>, <tt>rating</tt>, <tt>album</tt>,
     *          <tt>track</tt>, <tt>duration</tt>, <tt>comment</tt>, <tt>lyrics</tt>, <tt>musicbrainztrackid</tt>, <tt>musicbrainzartistid</tt>,
     *          <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>, <tt>playcount</tt>, <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>file</tt>
     *          , <tt>albumid</tt>, <tt>lastplayed</tt>, <tt>disc</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>albumartistid</tt>, <tt>albumreleasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.SongFields}.
     */
    public GetSongs(ListModel.SongFilter filter, Boolean includesingles, String... properties) {
      super();
      addParameter("filter", filter);
      addParameter("includesingles", includesingles);
      addParameter("properties", properties);
    }

    /**
     * Retrieve all songs from specified album, artist or genre.
     * 
     * @param limits
     * @param filter
     * @param includesingles
     * @param properties
     *          One or more of: <tt>title</tt>, <tt>artist</tt>, <tt>albumartist</tt>, <tt>genre</tt>, <tt>year</tt>, <tt>rating</tt>, <tt>album</tt>,
     *          <tt>track</tt>, <tt>duration</tt>, <tt>comment</tt>, <tt>lyrics</tt>, <tt>musicbrainztrackid</tt>, <tt>musicbrainzartistid</tt>,
     *          <tt>musicbrainzalbumid</tt>, <tt>musicbrainzalbumartistid</tt>, <tt>playcount</tt>, <tt>fanart</tt>, <tt>thumbnail</tt>, <tt>file</tt>
     *          , <tt>albumid</tt>, <tt>lastplayed</tt>, <tt>disc</tt>, <tt>genreid</tt>, <tt>artistid</tt>, <tt>displayartist</tt>,
     *          <tt>albumartistid</tt>, <tt>albumreleasetype</tt>, <tt>dateadded</tt>. See constants at {@link AudioModel.SongFields}.
     */
    public GetSongs(ListModel.Limits limits, ListModel.SongFilter filter, Boolean includesingles, String... properties) {
      super();
      addParameter("limits", limits);
      addParameter("filter", filter);
      addParameter("includesingles", includesingles);
      addParameter("properties", properties);
    }

    @Override
    protected ArrayList<AudioModel.SongDetail> parseMany(JsonNode node) {
      final ArrayNode songs = parseResults(node, RESULT);
      if (songs != null) {
        final ArrayList<AudioModel.SongDetail> ret = new ArrayList<AudioModel.SongDetail>(songs.size());
        for (int i = 0; i < songs.size(); i++) {
          final ObjectNode item = (ObjectNode) songs.get(i);
          ret.add(new AudioModel.SongDetail(item));
        }
        return ret;
      }
      else {
        return new ArrayList<AudioModel.SongDetail>(0);
      }
    }

    @Override
    public String getName() {
      return API_TYPE;
    }

    @Override
    protected boolean returnsList() {
      return true;
    }

    /**
     * Note: This class is used as parameter only.<br/>
     * <i>This class was generated automatically from XBMC's JSON-RPC introspect.</i>
     */
    public static class FilterGenreId extends AbstractModel {

      // field names
      public static final String GENREID = "genreid";

      // class members
      public final Integer       genreid;

      /**
       * @param genreid
       */
      public FilterGenreId(Integer genreid) {
        this.genreid = genreid;
      }

      @Override
      public JsonNode toJsonNode() {
        final ObjectNode node = OM.createObjectNode();
        node.put(GENREID, genreid);
        return node;
      }
    }

    /**
     * Note: This class is used as parameter only.<br/>
     * <i>This class was generated automatically from XBMC's JSON-RPC introspect.</i>
     */
    public static class FilterGenre extends AbstractModel {

      // field names
      public static final String GENRE = "genre";

      // class members
      public final String        genre;

      /**
       * @param genre
       */
      public FilterGenre(String genre) {
        this.genre = genre;
      }

      @Override
      public JsonNode toJsonNode() {
        final ObjectNode node = OM.createObjectNode();
        node.put(GENRE, genre);
        return node;
      }
    }

    /**
     * Note: This class is used as parameter only.<br/>
     * <i>This class was generated automatically from XBMC's JSON-RPC introspect.</i>
     */
    public static class FilterArtistId extends AbstractModel {

      // field names
      public static final String ARTISTID = "artistid";

      // class members
      public final Integer       artistid;

      /**
       * @param artistid
       */
      public FilterArtistId(Integer artistid) {
        this.artistid = artistid;
      }

      @Override
      public JsonNode toJsonNode() {
        final ObjectNode node = OM.createObjectNode();
        node.put(ARTISTID, artistid);
        return node;
      }
    }

    /**
     * Note: This class is used as parameter only.<br/>
     * <i>This class was generated automatically from XBMC's JSON-RPC introspect.</i>
     */
    public static class FilterArtist extends AbstractModel {

      // field names
      public static final String ARTIST = "artist";

      // class members
      public final String        artist;

      /**
       * @param artist
       */
      public FilterArtist(String artist) {
        this.artist = artist;
      }

      @Override
      public JsonNode toJsonNode() {
        final ObjectNode node = OM.createObjectNode();
        node.put(ARTIST, artist);
        return node;
      }
    }

    /**
     * Note: This class is used as parameter only.<br/>
     * <i>This class was generated automatically from XBMC's JSON-RPC introspect.</i>
     */
    public static class FilterAlbumId extends AbstractModel {

      // field names
      public static final String ALBUMID = "albumid";

      // class members
      public final Integer       albumid;

      /**
       * @param albumid
       */
      public FilterAlbumId(Integer albumid) {
        this.albumid = albumid;
      }

      @Override
      public JsonNode toJsonNode() {
        final ObjectNode node = OM.createObjectNode();
        node.put(ALBUMID, albumid);
        return node;
      }
    }

    /**
     * Note: This class is used as parameter only.<br/>
     * <i>This class was generated automatically from XBMC's JSON-RPC introspect.</i>
     */
    public static class FilterAlbum extends AbstractModel {

      // field names
      public static final String ALBUM = "album";

      // class members
      public final String        album;

      /**
       * @param album
       */
      public FilterAlbum(String album) {
        this.album = album;
      }

      @Override
      public JsonNode toJsonNode() {
        final ObjectNode node = OM.createObjectNode();
        node.put(ALBUM, album);
        return node;
      }
    }
  }

  /**
   * Scans the audio sources for new library items.
   * <p/>
   * This class represents the API method <tt>AudioLibrary.Scan</tt>
   * <p/>
   * <i>This class was generated automatically from XBMC's JSON-RPC introspect.</i>
   */
  public static class Scan extends AbstractCall<String> {
    public final static String API_TYPE = "AudioLibrary.Scan";

    /**
     * Scans the audio sources for new library items.
     * 
     * @param directory
     */
    public Scan(String directory) {
      super();
      addParameter("directory", directory);
    }

    @Override
    protected String parseOne(JsonNode node) {
      return node.getTextValue();
    }

    @Override
    public String getName() {
      return API_TYPE;
    }

    @Override
    protected boolean returnsList() {
      return false;
    }
  }

  /**
   * Update the given album with the given details.
   * <p/>
   * This class represents the API method <tt>AudioLibrary.SetAlbumDetails</tt>
   * <p/>
   * <i>This class was generated automatically from XBMC's JSON-RPC introspect.</i>
   */
  public static class SetAlbumDetails extends AbstractCall<String> {
    public final static String API_TYPE = "AudioLibrary.SetAlbumDetails";

    /**
     * Update the given album with the given details.
     * 
     * @param albumid
     * @param title
     * @param artist
     * @param description
     * @param genre
     * @param theme
     * @param mood
     * @param style
     * @param type
     * @param albumlabel
     * @param rating
     * @param year
     */
    public SetAlbumDetails(Integer albumid, String title, String[] artist, String description, String[] genre, String[] theme, String[] mood,
        String[] style, String type, String albumlabel, Integer rating, Integer year) {
      super();
      addParameter("albumid", albumid);
      addParameter("title", title);
      addParameter("artist", artist);
      addParameter("description", description);
      addParameter("genre", genre);
      addParameter("theme", theme);
      addParameter("mood", mood);
      addParameter("style", style);
      addParameter("type", type);
      addParameter("albumlabel", albumlabel);
      addParameter("rating", rating);
      addParameter("year", year);
    }

    @Override
    protected String parseOne(JsonNode node) {
      return node.getTextValue();
    }

    @Override
    public String getName() {
      return API_TYPE;
    }

    @Override
    protected boolean returnsList() {
      return false;
    }
  }

  /**
   * Update the given artist with the given details.
   * <p/>
   * This class represents the API method <tt>AudioLibrary.SetArtistDetails</tt>
   * <p/>
   * <i>This class was generated automatically from XBMC's JSON-RPC introspect.</i>
   */
  public static class SetArtistDetails extends AbstractCall<String> {
    public final static String API_TYPE = "AudioLibrary.SetArtistDetails";

    /**
     * Update the given artist with the given details.
     * 
     * @param artistid
     * @param artist
     * @param instrument
     * @param style
     * @param mood
     * @param born
     * @param formed
     * @param description
     * @param genre
     * @param died
     * @param disbanded
     * @param yearsactive
     */
    public SetArtistDetails(Integer artistid, String artist, String[] instrument, String[] style, String[] mood, String born, String formed,
        String description, String[] genre, String died, String disbanded, String... yearsactive) {
      super();
      addParameter("artistid", artistid);
      addParameter("artist", artist);
      addParameter("instrument", instrument);
      addParameter("style", style);
      addParameter("mood", mood);
      addParameter("born", born);
      addParameter("formed", formed);
      addParameter("description", description);
      addParameter("genre", genre);
      addParameter("died", died);
      addParameter("disbanded", disbanded);
      addParameter("yearsactive", yearsactive);
    }

    @Override
    protected String parseOne(JsonNode node) {
      return node.getTextValue();
    }

    @Override
    public String getName() {
      return API_TYPE;
    }

    @Override
    protected boolean returnsList() {
      return false;
    }
  }

  /**
   * Update the given song with the given details.
   * <p/>
   * This class represents the API method <tt>AudioLibrary.SetSongDetails</tt>
   * <p/>
   * <i>This class was generated automatically from XBMC's JSON-RPC introspect.</i>
   */
  public static class SetSongDetails extends AbstractCall<String> {
    public final static String API_TYPE = "AudioLibrary.SetSongDetails";

    /**
     * Update the given song with the given details.
     * 
     * @param songid
     * @param title
     * @param artist
     * @param albumartist
     * @param genre
     * @param year
     * @param rating
     * @param album
     * @param track
     * @param disc
     * @param duration
     * @param comment
     * @param musicbrainztrackid
     * @param musicbrainzartistid
     * @param musicbrainzalbumid
     * @param musicbrainzalbumartistid
     * @param playcount
     * @param lastplayed
     */
    public SetSongDetails(Integer songid, String title, String[] artist, String[] albumartist, String[] genre, Integer year, Integer rating,
        String album, Integer track, Integer disc, Integer duration, String comment, String musicbrainztrackid, String musicbrainzartistid,
        String musicbrainzalbumid, String musicbrainzalbumartistid, Integer playcount, String lastplayed) {
      super();
      addParameter("songid", songid);
      addParameter("title", title);
      addParameter("artist", artist);
      addParameter("albumartist", albumartist);
      addParameter("genre", genre);
      addParameter("year", year);
      addParameter("rating", rating);
      addParameter("album", album);
      addParameter("track", track);
      addParameter("disc", disc);
      addParameter("duration", duration);
      addParameter("comment", comment);
      addParameter("musicbrainztrackid", musicbrainztrackid);
      addParameter("musicbrainzartistid", musicbrainzartistid);
      addParameter("musicbrainzalbumid", musicbrainzalbumid);
      addParameter("musicbrainzalbumartistid", musicbrainzalbumartistid);
      addParameter("playcount", playcount);
      addParameter("lastplayed", lastplayed);
    }

    @Override
    protected String parseOne(JsonNode node) {
      return node.getTextValue();
    }

    @Override
    public String getName() {
      return API_TYPE;
    }

    @Override
    protected boolean returnsList() {
      return false;
    }
  }
}
