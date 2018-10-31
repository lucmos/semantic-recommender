package twittermodel;

import java.util.HashMap;
import java.util.Map;

/**
 * Models a "tweet"
 */
public class TweetModel extends ObjectModel {

    /**
     * The authorId of the tweet.
     * If this object if associated to an user, this field is written.
     */
    private String authorId;

    /**
     * The interested this tweet is talking about
     */
    private String interestId;

    /**
     * The source from which the interestId has been found
     */
    private String interestUrl;

    TweetModel(int seqId, String id) {
        super(seqId, id);
    }

    public void setAuthorId(UserModel authorId) {
        assert authorId != null;

        this.authorId = authorId.getIdString();
    }

    public void setInterestUrl(String interestSource) {
        assert interestSource != null && !interestSource.equals("");

        this.interestUrl = interestSource;
    }

    public void setInterestId(InterestModel interest) {
        assert interest != null;

        this.interestId = interest.getIdString();
    }

    public String getAuthorId() {
        assert authorId != null && !authorId.equals("");

        return authorId;
    }

    public String getInterestUrl() {
        assert interestUrl != null;

        return interestUrl;
    }

    public String getInterestId() {
        assert interestId != null && !interestId.equals("");

        return interestId;
    }

    public InterestModel getInterestModel(Map<String, InterestModel> interests) {
        assert interests.containsKey(getInterestId());

        InterestModel interest = interests.get(getInterestId());
        assert interest.getIdString().equals(getInterestId());

        return interest;
    }

    public WikiPageModel getWikiPageModel(Map<String, InterestModel> interests, Map<String, WikiPageModel> pages) {
        InterestModel i = getInterestModel(interests);
        WikiPageModel p = i.getWikiPageModel(pages);

        assert p != null;
        return p;
    }

    @Override
    public String toString(){
        return String.format("(tweet: #%s {id: %s, interestId: %s, interestUrl: %s})", getId(), getIdString(), interestId, interestUrl);
    }
}
