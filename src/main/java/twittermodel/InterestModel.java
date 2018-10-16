package twittermodel;

import utils.OneToOneHash;

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
     * A wikiPage that describes the interest
     */
    private WikiPageModel wikiPage;

    /**
     * A mapping between an integer identifier and a string one
     */
    private static OneToOneHash<Integer, String> idMap = new OneToOneHash<>();

    public InterestModel(String idString, PlatformType platform, WikiPageModel wikiPage) {
        super(idString);
        this.setPlatform(platform);
        this.setWikiPage(wikiPage);
    }

    private void setPlatform(PlatformType platform) {
        assert platform != null;

        this.platform = platform;
    }

    private void setWikiPage(WikiPageModel wikiPage)
    {
        assert wikiPage != null;

        this.wikiPage = wikiPage;
    }

    private PlatformType getPlatform() {
        return platform;
    }

    @Override
    public OneToOneHash<Integer, String> getIdMapping() {
        return InterestModel.idMap;
    }

    public WikiPageModel getWikiPage() {
        return wikiPage;
    }

    @Override
    public String toString(){
        return "Interest " + idMap +", platform type: "+ platform + ", wikipage: " + wikiPage;
    }
}
