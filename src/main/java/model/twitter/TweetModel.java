package model.twitter;

import model.ObjectModel;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Map;

/**
 * Models a "tweet"
 */
public class TweetModel extends ObjectModel {

    /**
     * The authorId of the tweet.
     * If this object if associated to an user, this field is written.
     */
    private int authorId;

    /**
     * The interested this tweet is talking about
     */
    private int interestId;

//    /**
//     * The source from which the interestId has been found
//     */
//    private String interestUrl; // TODO: 06/11/18 ignoring interestURL

    TweetModel(int seqId) {
        super(seqId);
    }

    public void setAuthorId(UserModel authorId) {
        assert authorId != null;

        this.authorId = authorId.getId();
    }

    public void setInterestUrl(String interestSource) {
//        assert interestSource != null && !interestSource.equals("");
//
//        this.interestUrl = interestSource;
    }// TODO: 06/11/18 ignoring interestURL

    public void setInterestId(InterestModel interest) {
        assert interest != null;

        this.interestId = interest.getId();
    }

    public int getAuthorId() {
        assert authorId > 0;

        return authorId;
    }

    public String getInterestUrl() {
//        assert interestUrl != null;
//
//        return interestUrl;// TODO: 06/11/18 ignoring interestURL
        throw new NotImplementedException();
    }

    public int getInterestId() {
        assert interestId > 0;

        return interestId;
    }

    public InterestModel getInterestModel(Map<Integer, InterestModel> interests) {
        assert interests.containsKey(getInterestId());

        InterestModel interest = interests.get(getInterestId());
        assert interest.getId() == getInterestId();

        return interest;
    }

    public WikiPageModel getWikiPageModel(Map<Integer, InterestModel> interests, Map<Integer, WikiPageModel> pages) {
        WikiPageModel p = getInterestModel(interests).getWikiPageModel(pages);

        assert p != null;
        return p;
    }

    @Override
    public String toString(){
        return String.format("(tweet: #%s {interestId: %s})", getId(), interestId);
    }
}
