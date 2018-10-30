package datasetsreader;


import constants.DatasetName;
import properties.PropReader;
import twittermodel.*;
import utils.IndexedSerializable;
import java.util.*;

public class Dataset implements IndexedSerializable {
    private DatasetName name;
    private PropReader.Dimension dimension;

    private HashMap<String, InterestModel> interests;
    private HashMap<String, TweetModel> tweets;
    private HashMap<String, UserModel> users;
    private HashMap<String, WikiPageModel> pages;

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

    public Map<String, InterestModel> getInterests() { // TODO: 30/10/18 usa solo interest validi? metodo isValid()
        return interests;
    }

    public Map<String, TweetModel> getTweets() {
        return tweets;
    }

    public Map<String, UserModel> getUsers() {
        return users;
    }

    public Map<String, WikiPageModel> getPages() {
        return pages;
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
        pages.putIfAbsent(pg.getIdString(), pg);
    }

    public PropReader.Dimension getDimension() {
        return dimension;
    }

    public void setDimension(PropReader.Dimension dimension) {
        this.dimension = dimension;
    }
}
