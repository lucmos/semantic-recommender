package twittermodel;

import utils.OneToOneHash;

import java.util.ArrayList;

public class UserModel extends TwitterObjectModel
{
    private ArrayList<UserModel> followOut;
    private ArrayList<UserModel> followIn;
    private ArrayList<TweetModel> tweets;
    private ArrayList<WikiPage> wikiPages;

    public UserModel(int id) {
        super(id);
        this.followOut = new ArrayList<>();
        this.followIn = new ArrayList<>();
        this.tweets = new ArrayList<>();
        this.wikiPages = new ArrayList<>();
    }

    public void addFollowOut(UserModel userID) {
        this.followOut.add(userID);
        if (!userID.getFollowIn().contains(this)) {
            userID.addFollowIn(this);
        }
    }

    public void addFollowIn(UserModel userID) {
        this.followOut.add(userID);
        if (!userID.getFollowOut().contains(this)) {
            userID.addFollowOut(this);
        }
    }

    public void addTweet(TweetModel tweetID) {
        tweetID.setAuthor(this);
        this.tweets.add(tweetID);
    }

    public void addWikiPage(WikiPage wikiPage) {
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

    public ArrayList<WikiPage> getWikiPages() {
        return wikiPages;
    }

    @Override
    public OneToOneHash<Integer, String> getIdMapping() {
        throw new UnsupportedOperationException();
    }
}
