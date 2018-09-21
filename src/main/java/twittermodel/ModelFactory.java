package twittermodel;

import utils.OneToOneHash;

import java.util.HashMap;

/**
 * Creates twitter objects.
 * If an object with a given identifier already exists, it is simply returned, otherwise it is created.
 */
public class ModelFactory {
    private ModelFactory() {

    }

    private static HashMap<Integer, UserModel> users = new HashMap<>();
    private static HashMap<Integer, TweetModel> tweets = new HashMap<>();
    private static HashMap<String, WikiPage> wikiPages = new HashMap<>();
    private static HashMap<String, Interest> interests = new HashMap<>();

    /**
     * Returns a UserModel with the given id
     * @param id the id of the user
     * @return the specified UserModel
     */
    public static UserModel getUser(int id) {
        if (!ModelFactory.users.containsKey(id)) {
            ModelFactory.users.put(id, new UserModel(id));
        }
        return ModelFactory.users.get(id);
    }

    /**
     * Returns a TweetModel with the given id
     * @param id the id of the tweet
     * @return the specified TweetModel
     */
    public static TweetModel getTweet(int id, Interest interest, String interestSourceUrl) {
        if (!ModelFactory.tweets.containsKey(id)) {
            ModelFactory.tweets.put(id, new TweetModel(id, interest, interestSourceUrl));
        }
        return ModelFactory.tweets.get(id);
    }

    /**
     * Returns a Interest with the given id
     * @param id the id of the interest
     * @return the specified Interest
     */
    public static Interest getInterest(String id, Interest.PlatformType platform, WikiPage wikiPage) {
        if (!ModelFactory.interests.containsKey(id)) {
            ModelFactory.interests.put(id, new Interest(id, platform, wikiPage));
        }
        return ModelFactory.interests.get(id);
    }

    /**
     * Returns a WikiPage with the given name
     * @param name the name of the wikipedia page (not the url)
     * @return the specified WikiPage
     */
    public static WikiPage getWikiPage(String name) {
        if (!ModelFactory.wikiPages.containsKey(name)) {
            ModelFactory.wikiPages.put(name, new WikiPage(name));
        }
        return ModelFactory.wikiPages.get(name);
    }
}
