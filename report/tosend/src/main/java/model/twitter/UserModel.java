package model.twitter;


import constants.DatasetName;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import model.ObjectModel;
import utils.Counter;

import java.util.Set;
import java.util.stream.Collectors;


public class UserModel extends ObjectModel {
    /**
     * The list of user that this user is following
     */
    private IntOpenHashSet followOutIds;

    /**
     * The list of user that follow this user
     */
    private IntOpenHashSet followInIds;

    /**
     * The list of tweetsIds that this user posted
     */
    private IntOpenHashSet tweetsIds;

    /**
     * The wikipage associated to this user
     * (taken from the dataset)
     */
    private int wikiPageAboutUserId;

    /**
     * The origin dataset of the user
     */
    private DatasetName datasetName;

    /**
     * True if the user information are private;
     */
    private boolean isPrivate;

    /**
     * True if the user has been deleted by Twitter;
     */
    private boolean notExists;

    private boolean isSuspended;

    /**
     * The list of the wiki wikiPages of the items that the user likes.
     *
     * Used only for the datsets S2*
     */
    private IntOpenHashSet wikiPagesOfLikedItemsIds;

    private IntOpenHashSet wikiPagesOfMayLikeItemsIds;

    private Counter<Integer> babelCategoriesInDisambiguatedTweets;

    private Counter<Integer> babelDomainsInDisambiguatedTweets;

    private boolean famous;

    UserModel(int seqId, DatasetName datasetName) {
        super(seqId);
        this.followOutIds = new IntOpenHashSet();
        this.followInIds = new IntOpenHashSet();
        this.tweetsIds = new IntOpenHashSet();
        this.wikiPagesOfLikedItemsIds = new IntOpenHashSet();
        this.wikiPagesOfMayLikeItemsIds = new IntOpenHashSet();

        this.babelCategoriesInDisambiguatedTweets = new Counter<>();
        this.babelDomainsInDisambiguatedTweets = new Counter<>();

        this.datasetName = datasetName;
        this.isPrivate = false;
        this.notExists = false;
    }

//    public void setBabelCategoriesInDisambiguatedTweets(Counter<Integer> babelCategoriesInDisambiguatedTweets) {
//        this.babelCategoriesInDisambiguatedTweets = babelCategoriesInDisambiguatedTweets;
//    }
//
//    public void setBabelDomainsInDisambiguatedTweets(Counter<Integer> babelDomainsInDisambiguatedTweets) {
//        this.babelDomainsInDisambiguatedTweets = babelDomainsInDisambiguatedTweets;
//    }


    public Counter<Integer> getBabelCategoriesInDisambiguatedTweets() {
        return babelCategoriesInDisambiguatedTweets;
    }

    public Counter<Integer> getBabelDomainsInDisambiguatedTweets() {
        return babelDomainsInDisambiguatedTweets;
    }

    public void addCategoryToDisambiguatedTweets(BabelCategoryModel cat) {
        babelCategoriesInDisambiguatedTweets.increment(cat.getId());
    }

    public void addCategoryToDisambiguatedTweets(BabelCategoryModel cat, int times) {
        babelCategoriesInDisambiguatedTweets.add(cat.getId(), times);
    }

    public void addDomainToDisambiguatedTweets(BabelDomainModel dom) {
        babelDomainsInDisambiguatedTweets.increment(dom.getId());
    }

    public void addDomainToDisambiguatedTweets(BabelDomainModel dom, int times) {
        babelDomainsInDisambiguatedTweets.add(dom.getId(), times);
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
        assert !famous;

        wikiPageAboutUserId = wikiPage.getId();
        famous = true;
    }

    public void addWikiPagesOfLikedItemsIds(WikiPageModel wikiPage) {
        assert wikiPage != null;

        this.wikiPagesOfLikedItemsIds.add(wikiPage.getId());
    }

    public void addWikiPagesOfMayLikeItemsIds(WikiPageModel wikiPageModel) {
        assert wikiPageModel != null;

        this.wikiPagesOfMayLikeItemsIds.add(wikiPageModel.getId());
    }

    public IntOpenHashSet getFollowOutIds() {
        assert followOutIds != null;

        return followOutIds;
    }

    public IntOpenHashSet getFollowInIds() {
        assert followInIds != null;

        return followInIds;
    }

    public IntOpenHashSet getTweetsIds() {
        assert tweetsIds != null;

        return tweetsIds;
    }

    public DatasetName getDatasetName() {
        return datasetName;
    }

    public int getWikiPageAboutUserId() {
        return this.wikiPageAboutUserId;
    }

    public IntOpenHashSet getWikiPagesOfLikedItemsIds() {
        assert wikiPagesOfLikedItemsIds != null;

        return wikiPagesOfLikedItemsIds;
    }

    public Set<WikiPageModel> getWikiPagesOfLikedItemdsModel(Dataset dataset) {
        assert wikiPagesOfLikedItemsIds != null;

        return wikiPagesOfLikedItemsIds.stream().map(x -> dataset.wikiPages.get(x)).collect(Collectors.toSet());
    }

    public Set<UserModel> getFollowOutUserModels(Dataset dataset) {
        assert followOutIds != null;

        Set<UserModel> fu = followOutIds.stream().map(x -> dataset.users.get((int) x)).collect(Collectors.toSet());

        assert fu.size() == followOutIds.size();
        return fu;
    }

    public WikiPageModel getWikiPageAboutUserModel(Dataset dataset) {
        assert dataset.wikiPages != null;

        WikiPageModel page = dataset.wikiPages.get(this.wikiPageAboutUserId);

        assert page != null;
        return page;
    }

    public boolean isFamous() {
        return famous;
    }

    public TweetModel getTweetModel(Dataset dataset, int tweetId) {
        assert dataset.tweets.containsKey(tweetId);

        TweetModel tweet = dataset.tweets.get(tweetId);
        assert tweet.getId() == tweetId;

        return tweet;
    }

    public Set<TweetModel> getTweetsModel(Dataset dataset) {
        return tweetsIds.stream().map(x -> getTweetModel(dataset, x)).collect(Collectors.toSet());
    }

    public Counter<BabelCategoryModel> getTweetsCategories(Dataset dataset) {
        Counter<BabelCategoryModel> cats = new Counter<>();

        Set<TweetModel> tweets = getTweetsModel(dataset);
        for (TweetModel t : tweets) {
            WikiPageModel w = t.getWikiPageModel(dataset);
            for (BabelCategoryModel cat : w.getCategoriesModel(dataset)) {
                cats.increment(cat);
            }
        }

        for (int cat : babelCategoriesInDisambiguatedTweets.getMap().keySet()) {
            BabelCategoryModel c = dataset.getCategory(cat);
            cats.add(c, babelCategoriesInDisambiguatedTweets.count(cat));
        }

        return cats;
    }

    public Counter<BabelDomainModel> getTweetsDomains(Dataset dataset) {
        Counter<BabelDomainModel> cats = new Counter<>();

        Set<TweetModel> tweets = getTweetsModel(dataset);
        for (TweetModel t : tweets) {
            WikiPageModel w = t.getWikiPageModel(dataset);
            for (BabelDomainModel cat : w.getDomainsModel(dataset)) {
                cats.increment(cat);
            }
        }

        for (int dom : babelDomainsInDisambiguatedTweets.getMap().keySet()) {
            BabelDomainModel d = dataset.getDomain(dom);
            cats.add(d, babelDomainsInDisambiguatedTweets.count(dom));
        }

        return cats;
    }

    @Override
    public String toString() {
        return String.format("(user: #%s {tweets: %s, out: %s, in: %s})", getId(), tweetsIds.size(), followOutIds.size(), followInIds.size());
    }

    public void setIsPrivate(boolean isPrivate){this.isPrivate = isPrivate;}

    public  void setNotExists(boolean notExists){this.notExists = notExists;}

    public void setIsSuspended(boolean suspended) {
        isSuspended = suspended;
    }
}
