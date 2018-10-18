package twittermodel;

/**
 * Models a "tweet"
 */
public class TweetModel extends TwitterObjectModel {

    /**
     * The authorId of the tweet.
     * If this object if associated to an user, this field is written.
     */
    private long authorId;

    /**
     * The interested this tweet is talking about
     */
    private long interestId;

    /**
     * The source from which the interestId has been found
     */
    private String interestUrl;

    public TweetModel(String id) {
        super(id);
    }

    public void setAuthorId(UserModel authorId) {
        assert authorId != null;

        this.authorId = authorId.getId();
    }

    public void setInterestUrl(String interestSource) {
        assert interestSource != null && !interestSource.equals("");

        this.interestUrl = interestSource;
    }

    public void setInterestId(InterestModel interestId) {
        assert interestId != null;

        this.interestId = interestId.getId();
    }

    public long getAuthorId() {
        assert authorId >= 0;

        return authorId;
    }

    public String getInterestUrl() {
        assert interestUrl != null;

        return interestUrl;
    }

    public long getInterestId() {
        assert interestId >= 0;

        return interestId;
    }

    @Override
    public String toString(){
        return String.format("(tweet: %d {interestId: %s, interestUrl: %s})", getId(), interestId, interestUrl);
    }
}
