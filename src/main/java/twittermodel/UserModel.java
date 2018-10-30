package twittermodel;


import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class UserModel extends ObjectModel {
    /**
     * The list of user that this user is following
     */
    private Set<String> followOutIds;

    /**
     * The list of user that follow this user
     */
    private Set<String> followInIds;

    /**
     * The list of tweetsIds that this user posted
     */
    private Set<String> tweetsIds;

    /**
     * The list of wikiPagesAboutUserIds associated to this user
     * (taken from the dataset)
     */
    private Set<String> wikiPagesAboutUserIds;


    /**
     * The list of the wiki pages of the items that the user likes.
     *
     * Used only for the datsets S2*
     */
    private Set<String> wikiPagesOfLikedItemsIds;


    public UserModel(String id) {
        super(id);
        this.followOutIds = new HashSet<>();
        this.followInIds = new HashSet<>();
        this.tweetsIds = new HashSet<>();
        this.wikiPagesAboutUserIds = new HashSet<>();
        this.wikiPagesOfLikedItemsIds = new HashSet<>();
    }

    /**
     * Adds a user to the list of the follower, called followInIds.
     * This user will have its list of followOutIds automatically updated.
     *
     * @param outFriend the user to follow
     */
    public void addFollowOut(UserModel outFriend) {
        assert outFriend != null;

        if (!outFriend.getFollowInIds().contains(getIdString())) {
            assert !this.followOutIds.contains(outFriend.getIdString());

            outFriend.followInIds.add(getIdString());
            this.followOutIds.add(outFriend.getIdString());
        }
    }


    /**
     * Adds a tweet to the list of the tweetsIds posted by this user
     *
     * @param tweet the tweet to add
     */
    public void addTweet(TweetModel tweet) {
        assert tweet != null;

        tweet.setAuthorId(this);
        this.tweetsIds.add(tweet.getIdString());
    }

    /**
     * Adds a wikiPage to the list of wikipedia pages related to this user
     *
     * @param wikiPage the wikipedia page to add
     */
    public void addWikiPageAboutUser(WikiPageModel wikiPage) {
        assert wikiPage != null;

        this.wikiPagesAboutUserIds.add(wikiPage.getIdString());
    }

    public void addWikiPagesOfLikedItemsIds(WikiPageModel wikiPage) {
        assert wikiPage != null;

        this.wikiPagesAboutUserIds.add(wikiPage.getIdString());
    }

    public Set<String> getFollowOutIds() {
        assert followOutIds != null;

        return followOutIds;
    }

    public Set<String> getFollowInIds() {
        assert followInIds != null;

        return followInIds;
    }

    public Set<String> getTweetsIds() {
        assert tweetsIds != null;

        return tweetsIds;
    }

    public Set<String> getWikiPagesAboutUserIds() {
        assert wikiPagesAboutUserIds != null;

        return wikiPagesAboutUserIds;
    }

    public Set<String> getWikiPagesOfLikedItemsIds() {
        assert wikiPagesOfLikedItemsIds != null;

        return wikiPagesOfLikedItemsIds;
    }

    public TweetModel getTweetModel(Map<String, TweetModel> tweets, String tweetId) {
        assert tweets.containsKey(tweetId);

        TweetModel tweet = tweets.get(tweetId);
        assert tweet.getIdString().equals(tweetId);

        return tweet;
    }

    @Override
    public String toString() {
        return String.format("(user: %s)", getIdString());
    }
}
