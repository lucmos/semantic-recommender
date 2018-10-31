package twittermodel;


import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class UserModel extends ObjectModel {
    /**
     * The list of user that this user is following
     */
    private Set<Integer> followOutIds;

    /**
     * The list of user that follow this user
     */
    private Set<Integer> followInIds;

    /**
     * The list of tweetsIds that this user posted
     */
    private Set<Integer> tweetsIds;

    /**
     * The list of wikiPagesAboutUserIds associated to this user
     * (taken from the dataset)
     */
    private Set<Integer> wikiPagesAboutUserIds;


    /**
     * The list of the wiki wikiPages of the items that the user likes.
     *
     * Used only for the datsets S2*
     */
    private Set<Integer> wikiPagesOfLikedItemsIds;


    UserModel(int seqId, String id) {
        super(seqId, id);
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

        if (!outFriend.getFollowInIds().contains(getId())) {
            assert !this.followOutIds.contains(outFriend.getId());

            outFriend.followInIds.add(getId());
            this.followOutIds.add(outFriend.getId());
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
        this.tweetsIds.add(tweet.getId());
    }

    /**
     * Adds a tweet to the list of the tweetsIds posted by this user
     *
     * @param tweet the tweet to add
     */
    public void removeTweet(TweetModel tweet) {
        assert tweetsIds.contains(tweet.getId());

        this.tweetsIds.remove(tweet.getId());
    }

    /**
     * Adds a wikiPage to the list of wikipedia wikiPages related to this user
     *
     * @param wikiPage the wikipedia page to add
     */
    public void addWikiPageAboutUser(WikiPageModel wikiPage) {
        assert wikiPage != null;

        this.wikiPagesAboutUserIds.add(wikiPage.getId());
    }

    public void addWikiPagesOfLikedItemsIds(WikiPageModel wikiPage) {
        assert wikiPage != null;

        this.wikiPagesAboutUserIds.add(wikiPage.getId());
    }

    public Set<Integer> getFollowOutIds() {
        assert followOutIds != null;

        return followOutIds;
    }

    public Set<Integer> getFollowInIds() {
        assert followInIds != null;

        return followInIds;
    }

    public Set<Integer> getTweetsIds() {
        assert tweetsIds != null;

        return tweetsIds;
    }

    public Set<Integer> getWikiPagesAboutUserIds() {
        assert wikiPagesAboutUserIds != null;

        return wikiPagesAboutUserIds;
    }

    public Set<Integer> getWikiPagesOfLikedItemsIds() {
        assert wikiPagesOfLikedItemsIds != null;

        return wikiPagesOfLikedItemsIds;
    }

    public TweetModel getTweetModel(Map<Integer, TweetModel> tweets, Integer tweetId) {
        assert tweets.containsKey(tweetId);

        TweetModel tweet = tweets.get(tweetId);
        assert tweet.getId() == tweetId;

        return tweet;
    }

    @Override
    public String toString() {
        return String.format("(user: #%s {id: %s, tweets: %s, out: %s, in: %s})", getId(), getIdString(), tweetsIds.size(), followOutIds.size(), followInIds.size());
    }
}
