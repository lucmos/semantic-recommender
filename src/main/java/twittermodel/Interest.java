package twittermodel;

import utils.OneToOneHash;

import java.io.Serializable;

/**
 * Models an "interest"
 */
public class Interest extends TwitterObjectModel {

    /**
     * Describes the possible types of platforms
     */
    public enum PlatformType implements Serializable {
        GOOD_READS,
        IMDB,
        SPOTIFY
    }

    /**
     * The platform where the interested originated
     */
    private PlatformType platform;

    /**
     * A wikiPage that describes the interest
     */
    private WikiPage wikiPage;

    /**
     * A mapping between an integer identifier and a string one
     */
    private static OneToOneHash<Integer, String> idMap = new OneToOneHash<>();

    public Interest(String idString, PlatformType platform, WikiPage wikiPage) {
        super(idString);
        this.setPlatform(platform);
        this.setWikiPage(wikiPage);
    }

    private void setPlatform(PlatformType platform) {
        this.platform = platform;
    }

    private void setWikiPage(WikiPage wikiPage) {
        this.wikiPage = wikiPage;
    }

    private PlatformType getPlatform() {
        return platform;
    }

    @Override
    public OneToOneHash<Integer, String> getIdMapping() {
        return Interest.idMap;
    }

    public WikiPage getWikiPage() {
        return wikiPage;
    }
}
