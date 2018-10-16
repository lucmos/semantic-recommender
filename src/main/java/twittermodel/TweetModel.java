package twittermodel;

/**
 * Models a "tweet"
 */
public class TweetModel extends TwitterObjectModel {

    /**
     * The author of the tweet.
     * If this object if associated to an user, this field is written.
     */
    private UserModel author;

    /**
     * The interested this tweet is talking about
     */
    private InterestModel interest;

    /**
     * The source from which the interest has been found
     */
    private String interestUrl;

    public TweetModel(String id) {
        super(id);
    }

    public void setAuthor(UserModel author) {
        assert author != null;

        this.author = author;
    }

    public void setInterestUrl(String interestSource) {
        assert interestSource != null && !interestSource.equals("");

        this.interestUrl = interestSource;
    }

    public void setInterest(InterestModel interest) {
        assert interest != null;

        this.interest = interest;
    }

    public UserModel getAuthor() {
        assert author != null;

        return author;
    }

    public String getInterestUrl() {
        assert interestUrl != null;

        return interestUrl;
    }

    public InterestModel getInterest() {
        assert interest != null;

        return interest;
    }

    @Override
    public String toString(){
//        return "Tweet, with interest: " + interest + " , interest source: "+ interestUrl+ ", \nauthor: " + author.toString();
        return String.format("(tweet: %d {interest: %s, interestUrl: %s})", getId(), interest, interestUrl);
    }
}
