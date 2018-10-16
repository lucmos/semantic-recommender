package twittermodel;

import utils.OneToOneHash;

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
    private String interestSource;

    public TweetModel(long id, InterestModel interest, String interestSourceUrl) {
        super(id);

        this.setInterest(interest);
        this.setInterestSource(interestSourceUrl);
    }

    public void setAuthor(UserModel author) {
        assert author != null;

        this.author = author;
    }

    public void setInterestSource(String interestSource) {
        assert interestSource != null && !interestSource.equals("");

        this.interestSource = interestSource;
    }

    public void setInterest(InterestModel interest) {
        assert interest != null;

        this.interest = interest;
    }

    public UserModel getAuthor() {
        assert author != null;

        return author;
    }

    public String getInterestSource() {
        assert interestSource != null;

        return interestSource;
    }

    public InterestModel getInterest() {
        assert interest != null;

        return interest;
    }

    @Override
    public OneToOneHash<Integer, String> getIdMapping() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString(){
//        return "Tweet, with interest: " + interest + " , interest source: "+ interestSource+ ", \nauthor: " + author.toString();
        return String.format("(tweet: %d {interest: %s, interestSource: %s})", getId(), interest, interestSource);
    }
}
