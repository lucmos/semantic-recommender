package datasetsreader;


import constants.DatasetName;
import properties.PropReader;
import twittermodel.*;
import utils.IndexedSerializable;
import java.util.*;

public class Dataset implements IndexedSerializable {
    private DatasetName name;
    private PropReader.Dimension dimension;

    private HashMap<Integer, InterestModel> interests;
    private HashMap<Integer, TweetModel> tweets;
    private HashMap<Integer, UserModel> users;
    private HashMap<Integer, WikiPageModel> pages;

    @Override
    public String toString() {
        return String.format("(name: %s, users: %s, tweets: %s, interest: %s, pages: %s)",
                name, users.size(), tweets.size(), interests.size(), pages.size());

//        "The users are " + users.size() + "\n" +
//                "The tweets are " + tweets.size() + "\n" +
//                "The wikipedia pages are " + pages.size() + "\n" +
//                "The interests are " + interests.size() + "\n";
    }

    public Dataset(DatasetName name, PropReader.Dimension limit) {
        this.name = name;
        this.dimension = limit;

        interests = new HashMap<>();
        tweets = new HashMap<>();
        users = new HashMap<>();
        pages = new HashMap<>();
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

    public Map<Integer, WikiPageModel> getPages() {
        return pages;
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
        pages.putIfAbsent(pg.getId(), pg);
    }

    public PropReader.Dimension getDimension() {
        return dimension;
    }

    public void setDimension(PropReader.Dimension dimension) {
        this.dimension = dimension;
    }
}
