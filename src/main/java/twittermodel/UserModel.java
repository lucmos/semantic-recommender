package twittermodel;

import java.util.ArrayList;

public class UserModel extends ObjectModel {
    /**
     * The list of user that this user is following
     */
    private ArrayList<Integer> followOutIds;

    /**
     * The list of user that follow this user
     */
    private ArrayList<Integer> followInIds;

    /**
     * The list of tweetsIds that this user posted
     */
    private ArrayList<Integer> tweetsIds;

    /**
     * The list of wikiPagesAboutUserIds associated to this user
     * (taken from the dataset)
     */
    private ArrayList<Integer> wikiPagesAboutUserIds;


    /**
     * The list of the wiki pages of the items that the user likes.
     *
     * Used only for the datsets S2*
     */
    private ArrayList<Integer> wikiPagesOfLikedItemsIds;


    public UserModel(String id) {
        super(id);
        this.followOutIds = new ArrayList<>();
        this.followInIds = new ArrayList<>();
        this.tweetsIds = new ArrayList<>();
        this.wikiPagesAboutUserIds = new ArrayList<>();
        this.wikiPagesOfLikedItemsIds = new ArrayList<>();
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
     * Adds a wikiPage to the list of wikipedia pages related to this user
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

    public ArrayList<Integer> getFollowOutIds() {
        assert followOutIds != null;

        return followOutIds;
    }

    public ArrayList<Integer> getFollowInIds() {
        assert followInIds != null;

        return followInIds;
    }

    public ArrayList<Integer> getTweetsIds() {
        assert tweetsIds != null;

        return tweetsIds;
    }

    public ArrayList<Integer> getWikiPagesAboutUserIds() {
        assert wikiPagesAboutUserIds != null;

        return wikiPagesAboutUserIds;
    }

    public ArrayList<Integer> getWikiPagesOfLikedItemsIds() {
        assert wikiPagesOfLikedItemsIds != null;

        return wikiPagesOfLikedItemsIds;
    }

    @Override
    public String toString() {
        return String.format("(user: %d)", getId());
    }
}
