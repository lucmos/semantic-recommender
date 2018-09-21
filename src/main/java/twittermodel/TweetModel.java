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
    private Interest interest;

    /**
     * The source from which the interest has been found
     */
    private String interestSource;

    public TweetModel(int id, Interest interest, String interestSourceUrl) {
        super(id);
        this.setInterest(interest);
        this.setInterestSource(interestSourceUrl);
    }

    public void setAuthor(UserModel author) {
        this.author = author;
    }

    public void setInterestSource(String interestSource) {
        this.interestSource = interestSource;
    }

    public void setInterest(Interest interest) {
        this.interest = interest;
    }

    public UserModel getAuthor() {
        return author;
    }

    public String getInterestSource() {
        return interestSource;
    }

    public Interest getInterest() {
        return interest;
    }

    @Override
    public OneToOneHash<Integer, String> getIdMapping() {
        throw new UnsupportedOperationException();
    }
}
