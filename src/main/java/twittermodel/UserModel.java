package twittermodel;

import utils.OneToOneHash;

import java.util.ArrayList;

public class UserModel extends TwitterObjectModel {
    /**
     * The list of user that this user is following
     */
    private ArrayList<UserModel> followOut;

    /**
     * The list of user that follow this user
     */
    private ArrayList<UserModel> followIn;

    /**
     * The list of tweets that this user posted
     */
    private ArrayList<TweetModel> tweets;

    /**
     * The list of wikiPagesAboutUser associated to this user
     * (taken from the dataset)
     */
    private ArrayList<WikiPageModel> wikiPagesAboutUser;
    private ArrayList<WikiPageModel> wikiPageLiked;

    public UserModel(long id) {
        super(id);
        this.followOut = new ArrayList<>();
        this.followIn = new ArrayList<>();
        this.tweets = new ArrayList<>();
        this.wikiPagesAboutUser = new ArrayList<>();
        this.wikiPageLiked = new ArrayList<>();
    }

    /**
     * Adds a user to the list of the follower, called followIn.
     * This user will have its list of followOut automatically updated.
     *
     * @param userID the user to follow
     */
    public void addFollowOut(UserModel userID) {
        assert userID != null;

        if (!userID.getFollowIn().contains(this)) {
            userID.followIn.add(this);
            this.followOut.add(userID);
        }
    }


    /**
     * Adds a tweet to the list of the tweets posted by this user
     *
     * @param tweetID the tweet to add
     */
    public void addTweet(TweetModel tweetID) {
        assert tweetID != null;

        tweetID.setAuthor(this);
        addWikiPageLiked(tweetID.getInterest().getWikiPage());
        this.tweets.add(tweetID);
    }

    /**
     * Adds a wikiPage to the list of wikipedia pages related to this user
     *
     * @param wikiPage the wikipedia page to add
     */
    public void addWikiPageAbout(WikiPageModel wikiPage) {
        assert wikiPage != null;

        this.wikiPagesAboutUser.add(wikiPage);
    }

    public void addWikiPageLiked(WikiPageModel wikiPage) {
        assert wikiPage != null;

        this.wikiPageLiked.add(wikiPage);
    }

    public ArrayList<UserModel> getFollowOut() {
        return followOut;
    }

    public ArrayList<UserModel> getFollowIn() {
        return followIn;
    }

    public ArrayList<TweetModel> getTweets() {
        return tweets;
    }

    public ArrayList<WikiPageModel> getWikiPagesAboutUser() {
        return wikiPagesAboutUser;
    }

    public ArrayList<WikiPageModel> getWikiPageLiked() {
        return wikiPageLiked;
    }

    @Override
    public OneToOneHash<Integer, String> getIdMapping() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return "User " + getId() + " that has " + followIn + " follower, \n" + "follows " + followOut + " and has done " + tweets + " tweets.\n"
                + "Gli piacciono le pagine wikipedia" + wikiPageLiked + "\n" + "Parlano di lui " + wikiPagesAboutUser + " pagine di wikipedia";
    }
}
