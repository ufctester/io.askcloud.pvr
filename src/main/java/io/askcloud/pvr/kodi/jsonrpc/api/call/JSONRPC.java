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
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;

import io.askcloud.pvr.kodi.jsonrpc.api.AbstractCall;
import io.askcloud.pvr.kodi.jsonrpc.api.AbstractModel;

public final class JSONRPC {

  /**
   * Notify all other connected clients.
   * <p/>
   * This class represents the API method <tt>JSONRPC.NotifyAll</tt>
   * <p/>
   * <i>This class was generated automatically from XBMC's JSON-RPC introspect.</i>
   */
  public static class NotifyAll extends AbstractCall<String> {
    public final static String API_TYPE = "JSONRPC.NotifyAll";

    /**
     * Notify all other connected clients.
     * 
     * @param sender
     * @param message
     * @param data
     */
    public NotifyAll(String sender, String message, String data) {
      super();
      addParameter("sender", sender);
      addParameter("message", message);
      addParameter("data", data);
    }

    /**
     * Notify all other connected clients.
     * 
     * @param sender
     * @param message
     */
    public NotifyAll(String sender, String message) {
      super();
      addParameter("sender", sender);
      addParameter("message", message);
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
   * Retrieve the clients permissions.
   * <p/>
   * This class represents the API method <tt>JSONRPC.Permission</tt>
   * <p/>
   * <i>This class was generated automatically from XBMC's JSON-RPC introspect.</i>
   */
  public static class Permission extends AbstractCall<Permission.PermissionResult> {
    public final static String API_TYPE = "JSONRPC.Permission";

    /**
     * Retrieve the clients permissions.
     */
    public Permission() {
      super();
    }

    @Override
    protected PermissionResult parseOne(JsonNode node) {
      return new PermissionResult(node);
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
     * Note: This class is used as result only.<br/>
     * <i>This class was generated automatically from XBMC's JSON-RPC introspect.</i>
     */
    public static class PermissionResult extends AbstractModel {

      // field names
      public static final String CONTROLGUI      = "controlgui";
      public static final String CONTROLNOTIFY   = "controlnotify";
      public static final String CONTROLPLAYBACK = "controlplayback";
      public static final String CONTROLPOWER    = "controlpower";
      public static final String CONTROLPVR      = "controlpvr";
      public static final String CONTROLSYSTEM   = "controlsystem";
      public static final String EXECUTEADDON    = "executeaddon";
      public static final String MANAGEADDON     = "manageaddon";
      public static final String NAVIGATE        = "navigate";
      public static final String READDATA        = "readdata";
      public static final String REMOVEDATA      = "removedata";
      public static final String UPDATEDATA      = "updatedata";
      public static final String WRITEFILE       = "writefile";

      // class members
      public final Boolean       controlgui;
      public final Boolean       controlnotify;
      public final Boolean       controlplayback;
      public final Boolean       controlpower;
      public final Boolean       controlpvr;
      public final Boolean       controlsystem;
      public final Boolean       executeaddon;
      public final Boolean       manageaddon;
      public final Boolean       navigate;
      public final Boolean       readdata;
      public final Boolean       removedata;
      public final Boolean       updatedata;
      public final Boolean       writefile;

      /**
       * @param controlgui
       * @param controlnotify
       * @param controlplayback
       * @param controlpower
       * @param controlpvr
       * @param controlsystem
       * @param executeaddon
       * @param manageaddon
       * @param navigate
       * @param readdata
       * @param removedata
       * @param updatedata
       * @param writefile
       */
      public PermissionResult(Boolean controlgui, Boolean controlnotify, Boolean controlplayback, Boolean controlpower, Boolean controlpvr,
          Boolean controlsystem, Boolean executeaddon, Boolean manageaddon, Boolean navigate, Boolean readdata, Boolean removedata,
          Boolean updatedata, Boolean writefile) {
        this.controlgui = controlgui;
        this.controlnotify = controlnotify;
        this.controlplayback = controlplayback;
        this.controlpower = controlpower;
        this.controlpvr = controlpvr;
        this.controlsystem = controlsystem;
        this.executeaddon = executeaddon;
        this.manageaddon = manageaddon;
        this.navigate = navigate;
        this.readdata = readdata;
        this.removedata = removedata;
        this.updatedata = updatedata;
        this.writefile = writefile;
      }

      /**
       * Construct from JSON object.
       * 
       * @param node
       *          JSON object representing a PermissionResult object
       */
      public PermissionResult(JsonNode node) {
        controlgui = node.get(CONTROLGUI).getBooleanValue(); // required value
        controlnotify = node.get(CONTROLNOTIFY).getBooleanValue(); // required value
        controlplayback = node.get(CONTROLPLAYBACK).getBooleanValue(); // required value
        controlpower = node.get(CONTROLPOWER).getBooleanValue(); // required value
        controlpvr = node.get(CONTROLPVR).getBooleanValue(); // required value
        controlsystem = node.get(CONTROLSYSTEM).getBooleanValue(); // required value
        executeaddon = node.get(EXECUTEADDON).getBooleanValue(); // required value
        manageaddon = node.get(MANAGEADDON).getBooleanValue(); // required value
        navigate = node.get(NAVIGATE).getBooleanValue(); // required value
        readdata = node.get(READDATA).getBooleanValue(); // required value
        removedata = node.get(REMOVEDATA).getBooleanValue(); // required value
        updatedata = node.get(UPDATEDATA).getBooleanValue(); // required value
        writefile = node.get(WRITEFILE).getBooleanValue(); // required value
      }

      @Override
      public JsonNode toJsonNode() {
        final ObjectNode node = OM.createObjectNode();
        node.put(CONTROLGUI, controlgui);
        node.put(CONTROLNOTIFY, controlnotify);
        node.put(CONTROLPLAYBACK, controlplayback);
        node.put(CONTROLPOWER, controlpower);
        node.put(CONTROLPVR, controlpvr);
        node.put(CONTROLSYSTEM, controlsystem);
        node.put(EXECUTEADDON, executeaddon);
        node.put(MANAGEADDON, manageaddon);
        node.put(NAVIGATE, navigate);
        node.put(READDATA, readdata);
        node.put(REMOVEDATA, removedata);
        node.put(UPDATEDATA, updatedata);
        node.put(WRITEFILE, writefile);
        return node;
      }

      /**
       * Extracts a list of {@link PermissionResult} objects from a JSON array.
       * 
       * @param node
       *          ObjectNode containing the list of objects.
       * @param key
       *          Key pointing to the node where the list is stored.
       */
      static List<PermissionResult> getJSONRPCPermissionResultList(JsonNode node, String key) {
        if (node.has(key)) {
          final ArrayNode a = (ArrayNode) node.get(key);
          final List<PermissionResult> l = new ArrayList<PermissionResult>(a.size());
          for (int i = 0; i < a.size(); i++) {
            l.add(new PermissionResult((JsonNode) a.get(i)));
          }
          return l;
        }
        return new ArrayList<PermissionResult>(0);
      }
    }
  }

  /**
   * Ping responder.
   * <p/>
   * This class represents the API method <tt>JSONRPC.Ping</tt>
   * <p/>
   * <i>This class was generated automatically from XBMC's JSON-RPC introspect.</i>
   */
  public static class Ping extends AbstractCall<String> {
    public final static String API_TYPE = "JSONRPC.Ping";

    /**
     * Ping responder.
     */
    public Ping() {
      super();
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
   * Retrieve the JSON-RPC protocol version.
   * <p/>
   * This class represents the API method <tt>JSONRPC.Version</tt>
   * <p/>
   * <i>This class was generated automatically from XBMC's JSON-RPC introspect.</i>
   */
  public static class Version extends AbstractCall<Version.VersionResult> {
    public final static String API_TYPE = "JSONRPC.Version";

    /**
     * Retrieve the JSON-RPC protocol version.
     */
    public Version() {
      super();
    }

    @Override
    protected VersionResult parseOne(JsonNode node) {
      return new VersionResult(node);
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
     * Note: This class is used as result only.<br/>
     * <i>This class was generated automatically from XBMC's JSON-RPC introspect.</i>
     */
    public static class VersionResult extends AbstractModel {

      // field names
      public static final String MAJOR = "major";
      public static final String MINOR = "minor";
      public static final String PATCH = "patch";

      // class members
      public final Integer       major;
      public final Integer       minor;
      public final Integer       patch;

      /**
       * @param major
       * @param minor
       * @param patch
       */
      public VersionResult(Integer major, Integer minor, Integer patch) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
      }

      @Override
      public String toString() {
        return major + "." + minor + "." + patch;
      }

      /**
       * Returns a comparable integer by multiplying and adding the version parts. <br>
       * Example: <tt>2.19.625</tt> becomes <tt>20190625</tt>
       * 
       * @return
       */
      public int toInt() {
        return patch + minor * 10000 + major * 10000000;
      }

      /**
       * Construct from JSON object.
       * 
       * @param node
       *          JSON object representing a VersionResult object
       */
      public VersionResult(JsonNode node) {
        major = node.get(MAJOR).getIntValue(); // required value
        minor = node.get(MINOR).getIntValue(); // required value
        patch = node.get(PATCH).getIntValue(); // required value
      }

      @Override
      public JsonNode toJsonNode() {
        final ObjectNode node = OM.createObjectNode();
        node.put(MAJOR, major);
        node.put(MINOR, minor);
        node.put(PATCH, patch);
        return node;
      }

      /**
       * Extracts a list of {@link VersionResult} objects from a JSON array.
       * 
       * @param node
       *          ObjectNode containing the list of objects.
       * @param key
       *          Key pointing to the node where the list is stored.
       */
      static List<VersionResult> getJSONRPCVersionResultList(JsonNode node, String key) {
        if (node.has(key)) {
          final ArrayNode a = (ArrayNode) node.get(key);
          final List<VersionResult> l = new ArrayList<VersionResult>(a.size());
          for (int i = 0; i < a.size(); i++) {
            l.add(new VersionResult((JsonNode) a.get(i)));
          }
          return l;
        }
        return new ArrayList<VersionResult>(0);
      }
    }
  }
}
