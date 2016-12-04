package io.askcloud.pvr.tvdb.model;

/**
 * Describes the list of possible banner types stored in the "BannerType" field returned from TheTVDB
 *
 * @author Stuart.Boston
 *
 */
public enum BannerListType {

    SERIES,
    SEASON,
    POSTER,
    FANART;

    public static BannerListType fromString(String type) {
        if (type != null) {
            try {
                return BannerListType.valueOf(type.trim().toUpperCase());
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException("BannerListType " + type + " does not exist", ex);
            }
        }
        throw new IllegalArgumentException("BannerListType is null");
    }

}
