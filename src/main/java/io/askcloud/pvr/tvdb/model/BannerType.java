package io.askcloud.pvr.tvdb.model;

/**
 * Describes the list of banner types that are returned in the "BannerType2" field from TheTVDB
 *
 * @author Stuart.Boston
 *
 */
public enum BannerType {

    GRAPHICAL("graphical"),
    SEASON("season"),
    SEASONWIDE("seasonwide"),
    BLANK("blank"),
    TEXT("text"),
    FANART_HD("1920x1080"),
    FANART_SD("1280x720"),
    POSTER("680x1000"),
    ARTWORK("artwork");

    private final String type;

    private BannerType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    /**
     * Set the banner type from a string. If the banner type isn't found, but the type contains an "x" as in 1920x1080 then the type
     * will be set to ARTWORK
     *
     * @param type
     * @return
     */
    public static BannerType fromString(String type) {
        if (type != null) {
            for (BannerType bannerType : BannerType.values()) {
                if (type.equalsIgnoreCase(bannerType.type)) {
                    return bannerType;
                }
            }

            // If we've not found the type, then try a generic ARTWORK for the 1920x1080, 1280x720 or 680x1000 values
            if (type.toLowerCase().contains("x")) {
                return BannerType.ARTWORK;
            }
        }
        throw new IllegalArgumentException("BannerType is empty/null");
    }

}
