package twittermodel;

import utils.OneToOneHash;

import java.util.ArrayList;

public class UserModel extends TwitterObjectModel
{
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
     * The list of wikiPages associated to this user
     * (taken from the dataset)
     */
    private ArrayList<WikiPageModel> wikiPages;

    public UserModel(int id) {
        super(id);
        this.followOut = new ArrayList<>();
        this.followIn = new ArrayList<>();
        this.tweets = new ArrayList<>();
        this.wikiPages = new ArrayList<>();
    }

    /**
     * Adds a user to the list of the follower, called followIn.
     * This user will have its list of followOut automatically updated.
     * @param userID the user to follow
     */
    public void addFollowOut(UserModel userID) {
        if (!userID.getFollowIn().contains(this))
        {
            userID.followIn.add(this);
            this.followOut.add(userID);
        }
    }
//
//    /**
//     * Adds a user to the list of the follower,
//     * This user will have its list of followIn automatically updated.
//     * @param userID the user to follow
//     */
//    public void addFollower(UserModel userID) {
//        this.followOut.add(userID);
//        if (!userID.getFollowIn().contains(this)) {
//            userID.followIn.add(this);
//        }
//    }
    /**
     * Adds a tweet to the list of the tweets posted by this user
     * @param tweetID the tweet to add
     */
    public void addTweet(TweetModel tweetID) {
        tweetID.setAuthor(this);
        this.tweets.add(tweetID);
    }

    /**
     * Adds a wikiPage to the list of wikipedia pages related to this user
     * @param wikiPage the wikipedia page to add
     */
    public void addWikiPage(WikiPageModel wikiPage) {
        this.wikiPages.add(wikiPage);
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

    public ArrayList<WikiPageModel> getWikiPages() {
        return wikiPages;
    }

    @Override
    public OneToOneHash<Integer, String> getIdMapping() {
        throw new UnsupportedOperationException();
    }
}
