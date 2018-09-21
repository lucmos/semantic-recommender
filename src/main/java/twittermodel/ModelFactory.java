package twittermodel;

import utils.OneToOneHash;

import java.util.HashMap;

public class ModelFactory {
    private ModelFactory() {

    }

    private static HashMap<Integer, UserModel> users = new HashMap<>();
    private static HashMap<Integer, TweetModel> tweets = new HashMap<>();
    private static HashMap<String, WikiPage> wikiPages = new HashMap<>();
    private static HashMap<String, Interest> interests = new HashMap<>();

    public static UserModel getUser(int id) {
        if (!ModelFactory.users.containsKey(id)) {
            ModelFactory.users.put(id, new UserModel(id));
        }
        return ModelFactory.users.get(id);
    }

    public static TweetModel getTweet(int id, Interest interest, String interestSourceUrl) {
        if (!ModelFactory.tweets.containsKey(id)) {
            ModelFactory.tweets.put(id, new TweetModel(id, interest, interestSourceUrl));
        }
        return ModelFactory.tweets.get(id);
    }

    public static Interest getInterest(String id, Interest.PlatformType platform, WikiPage wikiPage) {
        if (!ModelFactory.interests.containsKey(id)) {
            ModelFactory.interests.put(id, new Interest(id, platform, wikiPage));
        }
        return ModelFactory.interests.get(id);
    }

    public static WikiPage getWikiPage(String name) {
        if (!ModelFactory.wikiPages.containsKey(name)) {
            ModelFactory.wikiPages.put(name, new WikiPage(name));
        }
        return ModelFactory.wikiPages.get(name);
    }
}
