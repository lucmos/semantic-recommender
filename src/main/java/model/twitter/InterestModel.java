package model.twitter;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import model.ObjectModel;

import java.io.Serializable;
import java.util.Map;

/**
 * Models an "interest"
 */
public class InterestModel extends ObjectModel {
    /**
     * The platform where the interested originated
     */
    private int platform;

    /**
     * A wikiPageId that describes the interest
     */
    private int wikiPageId;


    InterestModel(int seqId) {
        super(seqId);
    }

    public void setPlatform(PlatformType platform) {
        assert platform != null;

        this.platform = platform.getId();
    }

    public void setWikiPageId(WikiPageModel wikiPageId) {
        assert wikiPageId != null;

        this.wikiPageId = wikiPageId.getId();
    }

    public PlatformType getPlatform() {
        assert platform > 0;

        return PlatformType.fromId(platform);
    }

    public int getWikiPageId() {
        assert wikiPageId > 0;

        return wikiPageId;
    }

    public WikiPageModel getWikiPageModel(Int2ObjectOpenHashMap<WikiPageModel> pages) {
        assert pages.containsKey(wikiPageId);

        WikiPageModel page = pages.get(wikiPageId);
        assert page.getId() == wikiPageId;

        return page;
    }

    @Override
    public String toString(){
        return String.format("(interest: %s {wikipage: %s})", getId(), getWikiPageId());
    }
}
