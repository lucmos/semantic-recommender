package twittermodel;


import constants.DatasetName;
import properties.Config;
import utils.IndexedSerializable;
import utils.OneToOneHash;

import java.util.*;

public class Dataset implements IndexedSerializable {
    private DatasetName name;
    private Config.Dimension dimension;

    /**
     * A mapping between an integer identifier and a string one
     */
    private OneToOneHash<Integer, String> idMap;

    HashMap<Integer, InterestModel> interests;
    HashMap<Integer, TweetModel> tweets;
    HashMap<Integer, UserModel> users;
    HashMap<Integer, WikiPageModel> wikiPages;

    @Override
    public String toString() {
        return String.format("(name: %s, users: %s, tweets: %s, interest: %s, wikiPages: %s)",
                name, users.size(), tweets.size(), interests.size(), wikiPages.size());
    }

    public Dataset(DatasetName name, Config.Dimension limit) {
        this.name = name;
        this.dimension = limit;

        interests = new HashMap<>();
        tweets = new HashMap<>();
        users = new HashMap<>();
        wikiPages = new HashMap<>();

        idMap = new OneToOneHash<>();
    }

    /**
     * Defines the mapping between the given literal identifier and the generated integer identifier
     *
     * @return the mapping, local to each class
     */
    private OneToOneHash<Integer, String> getIdMapping() {
        assert idMap != null;

        return idMap;
    }

    /**
     * Finds the next available integer identifier in the current class, and associates it to the literal id
     * @param idString the literal identifier
     * @return the integer identifier
     */
    public int getNextId(String idString) {
        assert idString != null && !idString.equals("");

        int index = idMap.size();

        assert !idMap.containsA(index);
        assert !idMap.containsB(idString);

        idMap.put(index, idString);

        return index;
    }

    public boolean exixstObj(String id) {
        return idMap.containsB(id);
    }

    public String getStringId(int i) {
        return idMap.getA(i);
    }

    public int getIntegerId(String s) {
        return idMap.getB(s);
    }

    public DatasetName getName() {
        return name;
    }


    public InterestModel getInterest(Integer id) {
        assert interests.containsKey(id);

        return interests.get(id);
    }

    public TweetModel getTweet(Integer id) {
        assert tweets.containsKey(id);

        return tweets.get(id);
    }

    public UserModel getUser(Integer id) {
        assert users.containsKey(id);

        return users.get(id);
    }

    public WikiPageModel getPage(Integer id) {
        assert wikiPages.containsKey(id);

        return wikiPages.get(id);
    }

    public Map<Integer, InterestModel> getInterests() {
        return interests;
    }

    public Map<Integer, TweetModel> getTweets() {
        return tweets;
    }

    public Map<Integer, UserModel> getUsers() {
        return users;
    }

    public Map<Integer, WikiPageModel> getWikiPages() {
        return wikiPages;
    }

    public void addInterest(InterestModel interest) {
        interests.putIfAbsent(interest.getId(), interest);
    }

    public void addTweet(TweetModel tw) {
        tweets.putIfAbsent(tw.getId(), tw);
    }

    public void addUser(UserModel usr) {
        users.putIfAbsent(usr.getId(), usr);
    }

    public void addPage(WikiPageModel pg) {
        wikiPages.putIfAbsent(pg.getId(), pg);
    }


    public void removeInterest(InterestModel interest) {
        assert interests.containsKey(interest.getId());
        interests.remove(interest.getId());
    }

    public void removeTweet(TweetModel tw) {
        assert tweets.containsKey(tw.getId());
        tweets.remove(tw.getId());
    }

    public void removeUser(UserModel usr) {
        assert users.containsKey(usr.getId());
        users.remove(usr.getId());
    }

    public void removePage(WikiPageModel pg) {
        assert wikiPages.containsKey(pg.getId());
        wikiPages.remove(pg.getId());
    }

    public Config.Dimension getDimension() {
        return dimension;
    }

    public void setDimension(Config.Dimension dimension) {
        this.dimension = dimension;
    }
}
