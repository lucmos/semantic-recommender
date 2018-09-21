package twittermodel;

import utils.OneToOneHash;

import java.io.Serializable;

public class Interest extends TwitterObjectModel {

    public enum PlatformType implements Serializable {
        GOOD_READS,
        IMDB,
        SPOTIFY
    }

    private PlatformType platform;
    private WikiPage wikiPage;

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
