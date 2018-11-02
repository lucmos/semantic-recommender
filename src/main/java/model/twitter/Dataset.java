package model.twitter;


import constants.DatasetName;
import io.Config;
import model.ObjectCollection;
import model.ObjectModel;
import model.clusters.Cluster;
import utils.Counter;
import utils.IndexedSerializable;
import utils.Statistics;

import java.util.*;

public class Dataset extends ObjectCollection {
    private DatasetName name;
    private Config.Dimension dimension;

    HashMap<Integer, InterestModel> interests;
    HashMap<Integer, TweetModel> tweets;
    HashMap<Integer, UserModel> users;
    HashMap<Integer, WikiPageModel> wikiPages;
    HashMap<Integer, BabelCategoryModel> babelCategories;
    HashMap<Integer, BabelDomainModel> babelDomains;

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
        babelCategories = new HashMap<>();
        babelDomains = new HashMap<>();

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

    public BabelCategoryModel getCategory(Integer id) {
        assert babelCategories.containsKey(id);

        return babelCategories.get(id);
    }

    public BabelDomainModel getDomain(Integer id) {
        assert babelDomains.containsKey(id);

        return babelDomains.get(id);
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

    public HashMap<Integer, BabelCategoryModel> getBabelCategories() {
        return babelCategories;
    }

    public HashMap<Integer, BabelDomainModel> getBabelDomains() {
        return babelDomains;
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

    public void addWikiPage(WikiPageModel pg) {
        wikiPages.putIfAbsent(pg.getId(), pg);
    }

    public void addCategory(BabelCategoryModel categoryModel) {
        babelCategories.putIfAbsent(categoryModel.getId(), categoryModel);
    }

    public void addDomain(BabelDomainModel domainModel) {
        babelDomains.putIfAbsent(domainModel.getId(), domainModel);
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


    public String tweeetStats() {
        double[] tweets_sizes = users.values().stream().mapToDouble(x -> x.getTweetsIds().size()).toArray();
        Statistics stat = new Statistics(tweets_sizes);

        return stat.report(
                        "TWEETS STATS",
                        "total number of tweets",
                        "total number of users",
                        "greatest user per #tweets",
                        "#greatest users per #tweets",
                        "smallest user per #tweets",
                        "#smallest user per #tweets",
                        "median user per #tweets",
                        "#median users per #tweets",
                        "mean user per #tweets",
                        "#tweets per user variance",
                        "#tweets per user stddev"
                );
    }

    public String friendStats() {
        double[] in_sizes = users.values().stream().mapToDouble(x -> {
            HashSet<Integer> s = new HashSet<>(x.getFollowInIds());
            s.addAll(x.getFollowOutIds());
            return s.size();
        }).toArray();
        Statistics stat = new Statistics(in_sizes);

        return stat.report("FRIEND (IN/OUT) STATS",
                "total number of friends",
                "total number of users",
                "greatest user per #friends",
                "#greatest users per #friends",
                "smallest user per #friends",
                "#smallest user per #friends",
                "median user per #friends",
                "#median users per #friends",
                "mean user per #friends",
                "#friends per user variance",
                "#friends per user stddev");
    }

    public String followInStats() {
        double[] in_sizes = users.values().stream().mapToDouble(x -> x.getFollowInIds().size()).toArray();
        Statistics stat = new Statistics(in_sizes);

        return stat.report("FOLLOW-IN STATS",
                "total number of followIn",
                "total number of users",
                "greatest user per #followIn",
                "#greatest users per #followIn",
                "smallest user per #followIn",
                "#smallest user per #followIn",
                "median user per #followIn",
                "#median users per #followIn",
                "mean user per #followIn",
                "#followIn per user variance",
                "#followIn per user stddev");
    }

    public String followOutStats() {
        double[] in_sizes = users.values().stream().mapToDouble(x -> x.getFollowOutIds().size()).toArray();
        Statistics stat = new Statistics(in_sizes);

        return stat.report("FOLLOW-OUT STATS",
                "total number of followOut",
                "total number of users",
                "greatest user per #followOut",
                "#greatest users per #followOut",
                "smallest user per #followOut",
                "#smallest user per #followOut",
                "median user per #followOut",
                "#median users per #followOut",
                "mean user per #followOut",
                "#followOut per user variance",
                "#followOut per user stddev");
    }

    public String categoriesStats() {

        StringBuilder s = new StringBuilder();

        double[] catUsers = users.values().stream().mapToDouble(x ->
                x.getTweetsCategories(tweets, interests, wikiPages, babelCategories).size()).toArray();
        Statistics stat = new Statistics(catUsers);
        s.append(stat.report(
                "[CATEGORIES STATS PER USER]",
                "total numer of categories",
                "total number of users",
                "greatest user per #categories",
                "#greatest users per #categories",
                "smallest user per #categories",
                "#smallest user per #categories",
                "median user per #categories",
                "#median users per #categories",
                "mean user per #categories",
                "#categories per user variance",
                "#categories per user stddev"));

        s.append("\n");

        double[] catTweet = tweets.values().stream().mapToDouble(x ->
                x.getWikiPageModel(interests, wikiPages).getBabelCategories().size()).toArray();
        stat = new Statistics(catTweet);
        s.append(stat.report(
                "[CATEGORIES STATS PER TWEET]",
                "total numer of categories",
                "total number of tweets",
                "greatest tweet per #categories",
                "#greatest tweets per #categories",
                "smallest tweet per #categories",
                "#smallest tweet per #categories",
                "median tweet per #categories",
                "#median tweets per #categories",
                "mean tweet per #categories",
                "#categories per tweet variance",
                "#categories per tweet stddev"));
        return s.toString();
    }

    public String domainsStats() {

        StringBuilder s = new StringBuilder();

        double[] domUsers = users.values().stream().mapToDouble(x ->
                x.getTweetsDomains(tweets, interests, wikiPages, getBabelDomains()).size()).toArray();
        Statistics stat = new Statistics(domUsers);
        s.append(stat.report(
                "[DOMAINS STATS PER USER]",
                "total numer of domains",
                "total number of users",
                "greatest user per #domains",
                "#greatest users per #domains",
                "smallest user per #domains",
                "#smallest user per #domains",
                "median user per #domains",
                "#median users per #domains",
                "mean user per #domains",
                "#domains per user variance",
                "#domains per user stddev"));

        s.append("\n");

        double[] domTweet = tweets.values().stream().mapToDouble(x ->
                x.getWikiPageModel(interests, wikiPages).getBabelDomains().size()).toArray();
        stat = new Statistics(domTweet);
        s.append(stat.report(
                "[DOMAINS STATS PER TWEET]",
                "total numer of domains",
                "total number of tweets",
                "greatest tweet per #domains",
                "#greatest tweets per #domains",
                "smallest tweet per #domains",
                "#smallest tweet per #domains",
                "median tweet per #domains",
                "#median tweets per #domains",
                "mean tweet per #domains",
                "#domains per tweet variance",
                "#domains per tweet stddev"));
        return s.toString();
    }

//    public String categoriesDistribution() {
//        Counter<String> catCounter = Counter.fromCollection(
//                users.values().stream().map(x -> x.getTweetsModel(tweets)).map(y -> y.).map(y -> y.).to)
//    }

    // TODO: 31/10/18 STATISTICHE CATEGORIE E DOMINI, fallo funzionare con la nuova struttura a oggetti.
//    public String stats() {
//        return stats(50);
//    }



//    public String stats(int k) {
//        return String.format("\n[WIKIPAGES MAPPING STATS]\n" +
//                "\tsynsets found: %s\n", wikiToSynset.size()) +
//                _stats(synsetToDomain, "domains", k).append(_stats(synsetToCategories, "categories", k));
//    }

    private StringBuilder _stats(Map<String, Set<String>> map, String name, int k) {
        StringBuilder s = new StringBuilder(String.format("\n[OCCURRENCES OF THE ~ %s ~ ACROSS SYNSETS]\n", name.toUpperCase()));

        Counter<String> elements = Counter.fromMultiMap(map);
        Statistics stat = new Statistics(elements);

        s.append(stat.report());

        s.append(String.format("\n[%s DISTRIBUTION]\n%s\n", name.toUpperCase(), elements.getDistribution(k)));
        return s;
    }
}
