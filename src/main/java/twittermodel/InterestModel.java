package twittermodel;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Models an "interest"
 */
public class InterestModel extends ObjectModel {

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
    private String wikiPageId;


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

        this.wikiPageId = wikiPageId.getIdString();
    }

    public PlatformType getPlatform() {
        assert platform != null;

        return platform;
    }

    public String getWikiPageId() {
        // TODO: 16/10/18 4 interest hanno wikiPageId null
//        assert wikiPageId != null;

        return wikiPageId;
    }

    public WikiPageModel getWikiPageModel(Map<String, WikiPageModel> pages) {
        assert pages.containsKey(wikiPageId);

        WikiPageModel page = pages.get(wikiPageId);
        assert page.getIdString().equals(wikiPageId);

        return page;
    }

    @Override
    public String toString(){
        return String.format("(interest: %s {wikipage: %s})", getIdString(), getWikiPageId());
    }

    public boolean isValid() {
        // TODO: 30/10/18 4 interesti hanno un id, ma non sono associati a nessuna wikipage...
        return wikiPageId != null;
    }

}
