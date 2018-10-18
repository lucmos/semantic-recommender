package datasetsreader;

//import twittermodel.;

import constants.DatasetName;
import sun.security.x509.DNSName;
import twittermodel.*;
import utils.IndexedSerializable;

import javax.print.attribute.standard.PagesPerMinute;
import java.util.*;

public class Dataset implements IndexedSerializable {
    private DatasetName name;

    private HashMap<Long, InterestModel> interests;
    private HashMap<Long, TweetModel> tweets;
    private HashMap<Long, UserModel> users;
    private HashMap<Long, WikiPageModel> pages;

    @Override
    public String toString() {
        return String.format("(name: %s, users: %s, tweets: %s, interets: %s, pages: %s)",
                name, users.size(), tweets.size(), interests.size(), pages.size());

//        "The users are " + users.size() + "\n" +
//                "The tweets are " + tweets.size() + "\n" +
//                "The wikipedia pages are " + pages.size() + "\n" +
//                "The interests are " + interests.size() + "\n";
    }

    public Dataset(DatasetName name) {
        this.name = name;
        interests = new HashMap<>();
        tweets = new HashMap<>();
        users = new HashMap<>();
        pages = new HashMap<>();
    }

    public Map<Long, InterestModel> getInterests() {
        return interests;
    }

    public Map<Long, TweetModel> getTweets() {
        return tweets;
    }

    public Map<Long, UserModel> getUsers() {
        return users;
    }

    public Map<Long, WikiPageModel> getPages() {
        return pages;
    }

    public void addInterest(InterestModel interest) {
        if (! interests.containsKey(interest.getId())){
            interests.put(interest.getId(), interest);
        }
    }

    public void addTweet(TweetModel tw) {
        if (! tweets.containsKey(tw.getId())) {
            tweets.put(tw.getId(), tw);
        }
    }

    public void addUser(UserModel usr) {
        if (! users.containsKey(usr.getId())) {
            users.put(usr.getId(), usr);
        }
    }

    public void addPage(WikiPageModel pg) {
        if (!pages.containsKey(pg.getId())) {
            pages.put(pg.getId(), pg);
        }
    }
}
