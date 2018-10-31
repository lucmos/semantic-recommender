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

    HashMap<String, InterestModel> interests;
    HashMap<String, TweetModel> tweets;
    HashMap<String, UserModel> users;
    HashMap<String, WikiPageModel> wikiPages;

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

    public DatasetName getName() {
        return name;
    }


    public InterestModel getInterest(String id) { // TODO: 30/10/18 usa solo interest validi? metodo isValid()
        assert interests.containsKey(id);

        return interests.get(id);
    }

    public TweetModel getTweet(String id) {
        assert tweets.containsKey(id);

        return tweets.get(id);
    }

    public UserModel getUser(String id) {
        assert users.containsKey(id);

        return users.get(id);
    }

    public WikiPageModel getPage(String id) {
        assert wikiPages.containsKey(id);

        return wikiPages.get(id);
    }

    public Map<String, InterestModel> getInterests() { // TODO: 30/10/18 usa solo interest validi? metodo isValid()
        return interests;
    }

    public Map<String, TweetModel> getTweets() {
        return tweets;
    }

    public Map<String, UserModel> getUsers() {
        return users;
    }

    public Map<String, WikiPageModel> getWikiPages() {
        return wikiPages;
    }

    public void addInterest(InterestModel interest) {
        interests.putIfAbsent(interest.getIdString(), interest);
    }

    public void addTweet(TweetModel tw) {
        tweets.putIfAbsent(tw.getIdString(), tw);
    }

    public void addUser(UserModel usr) {
        users.putIfAbsent(usr.getIdString(), usr);
    }

    public void addPage(WikiPageModel pg) {
        wikiPages.putIfAbsent(pg.getIdString(), pg);
    }


    public void removeInterest(InterestModel interest) {
        assert interests.containsKey(interest.getIdString());
        interests.remove(interest.getIdString());
    }

    public void removeTweet(TweetModel tw) {
        assert tweets.containsKey(tw.getIdString());
        tweets.remove(tw.getIdString());
    }

    public void removeUser(UserModel usr) {
        assert users.containsKey(usr.getIdString());
        users.remove(usr.getIdString());
    }

    public void removePage(WikiPageModel pg) {
        assert wikiPages.containsKey(pg.getIdString());
        wikiPages.remove(pg.getIdString());
    }

    public Config.Dimension getDimension() {
        return dimension;
    }

    public void setDimension(Config.Dimension dimension) {
        this.dimension = dimension;
    }
}
