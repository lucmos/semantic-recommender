package datasetsreader;

//import twittermodel.;
import constants.DatasetName;
import sun.security.x509.DNSName;
import twittermodel.*;
import utils.IndexedSerializable;

import javax.print.attribute.standard.PagesPerMinute;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Dataset implements IndexedSerializable
{
    private DatasetName name;

    private Set<InterestModel> interests;
    private Set<TweetModel> tweets;
    private Set<UserModel> users;
    private Set<WikiPageModel> pages;

    public Dataset(DatasetName name)
    {
        this.name = name;
        interests = new HashSet<>();
        tweets = new HashSet<>();
        users = new HashSet<>();
        pages = new HashSet<>();
    }

    @Override
    public String toString(){
        return "Name: "+name;
    }


    public Set<InterestModel> getInterests() {
        return interests;
    }

    public Set<TweetModel> getTweets() {
        return tweets;
    }

    public Set<UserModel> getUsers() {
        return users;
    }

    public Set<WikiPageModel> getPages() {
        return pages;
    }

    public void addInterest(InterestModel interest){interests.add(interest);}
    public void addTweet(TweetModel tw){tweets.add(tw);}
    public void addUser(UserModel usr){users.add(usr);}
    public void addPage(WikiPageModel pg){pages.add(pg);}
}
