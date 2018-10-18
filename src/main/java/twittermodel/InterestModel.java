package twittermodel;

import java.io.Serializable;

/**
 * Models an "interest"
 */
public class InterestModel extends TwitterObjectModel {

    /**
     * Describes the possible types of platforms
     */
    public enum PlatformType implements Serializable {
        GOOD_READS("GoodReads"),
        IMDB("IMDb"),
        SPOTIFY("Spotify");

        private String text;

        PlatformType(String text)
        {
            assert text != null && !text.equals("");
            this.text = text;
        }

        public String getText() {
            return this.text;
        }

        /**
         * Given a String returns the correspondent PlatformType object
         * @param text
         * @return
         */
        public static PlatformType fromString(String text) {
            assert text != null && !text.equals("");

            for (PlatformType b : PlatformType.values()) {
                if (b.text.equalsIgnoreCase(text)) {
                    return b;
                }
            }
            throw new RuntimeException("This interest id doesn't exist!");
        }
    }

    /**
     * The platform where the interested originated
     */
    private PlatformType platform;

    /**
     * A wikiPageId that describes the interest
     */
    private long wikiPageId;


    public InterestModel(String idString) {
        super(idString);
    }

    public void setPlatform(PlatformType platform) {
        assert platform != null;

        this.platform = platform;
    }

    public void setWikiPageId(WikiPageModel wikiPageId)
    {
        assert wikiPageId != null;

        this.wikiPageId = wikiPageId.getId();
    }

    public PlatformType getPlatform() {
        assert platform != null;

        return platform;
    }

    public long getWikiPageId() {
        // TODO: 16/10/18 4 interest hanno wikiPageId null
//        assert wikiPageId != null;

        return wikiPageId;
    }

    @Override
    public String toString(){
        return String.format("(interest: %s {wikipage: %s})", getId(), getWikiPageId());
    }
}
