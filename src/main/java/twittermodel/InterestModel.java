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


    public InterestModel(String idString) {
        super(idString);
    }

    public void setPlatform(PlatformType platform) {
        assert platform != null;

        this.platform = platform;
    }

    public void setWikiPage(WikiPageModel wikiPage)
    {
        assert wikiPage != null;

        this.wikiPage = wikiPage;
    }

    public PlatformType getPlatform() {
        assert platform != null;

        return platform;
    }

    public WikiPageModel getWikiPage() {
        // TODO: 16/10/18 4 interest hanno wikiPage null 
//        assert wikiPage != null;

        return wikiPage;
    }

    @Override
    public String toString(){
//        return "Interest " + idMap +", platform type: "+ platform + ", wikipage: " + wikiPage;
        return String.format("(interest: %s", getId());
    }
}
