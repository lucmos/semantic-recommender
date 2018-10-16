package twittermodel;

import java.util.HashMap;

/**
 * Creates twitter objects.
 * If an object with a given identifier already exists, it is simply returned, otherwise it is created.
 */
public class ModelFactory {
    private ModelFactory() {

    }

    private static HashMap<Long, UserModel> users = new HashMap<>();
    private static HashMap<Long, TweetModel> tweets = new HashMap<>();
    private static HashMap<String, WikiPageModel> wikiPages = new HashMap<>();
    private static HashMap<String, InterestModel> interests = new HashMap<>();

    public static String report() {
        return "The users are " + users.size() + "\n" +
                "The tweets are " + tweets.size() + "\n" +
                "The wikipedia pages are " + wikiPages.size() + "\n" +
                "The interests are " + interests.size() + "\n";
    }

    /**
     * Returns a UserModel with the given id
     *
     * @param id the id of the user
     * @return the specified UserModel
     */
    public static UserModel getUser(long id) {
        assert id > 0;

        if (!ModelFactory.users.containsKey(id)) {
            ModelFactory.users.put(id, new UserModel(id));
        }
        UserModel res = ModelFactory.users.get(id);

        assert res != null;
        return res;
    }

    /**
     * Returns a TweetModel with the given id
     *
     * @param id the id of the tweet
     * @return the specified TweetModel
     */
    public static TweetModel getTweet(long id, InterestModel interest, String interestSourceUrl) {
        assert id > 0;
        assert interest != null;
        assert interestSourceUrl != null && !interestSourceUrl.equals("");

        if (!ModelFactory.tweets.containsKey(id)) {
            ModelFactory.tweets.put(id, new TweetModel(id, interest, interestSourceUrl));
        }
        TweetModel res = ModelFactory.tweets.get(id);

        assert res != null;
        return res;
    }

    /**
     * Returns a TweetModel that already exists with the given id
     *
     * @param id the id of the tweet
     * @return the specified TweetModel
     */
    public static TweetModel getTweet(long id) {
        assert id > 0;

        TweetModel res;
        try {
            res = ModelFactory.tweets.get(id);
        } catch (NullPointerException e) {
            throw new RuntimeException("This tweet id doesn't exist!", e);
        }

        assert res != null;
        return res;
    }

    /**
     * Returns a InterestModel with the given id
     *
     * @param id the id of the interest
     * @return the specified InterestModel
     */
    public static InterestModel getInterest(String id, InterestModel.PlatformType platform, WikiPageModel wikiPage) {
        assert id != null && !id.equals("");
        assert platform != null;
        assert wikiPage != null;

        if (!ModelFactory.interests.containsKey(id)) {
            ModelFactory.interests.put(id, new InterestModel(id, platform, wikiPage));
        }
        InterestModel res = ModelFactory.interests.get(id);

        assert res != null;
        return res;
    }

    /**
     * Returns a InterestModel that already exists with the given id
     *
     * @param id the id of the interest
     * @return the specified InterestModel
     */
    public static InterestModel getInterest(String id) {
        assert id != null && !id.equals("");
        assert ModelFactory.interests.containsKey(id);

        InterestModel res;
        try {
            res = ModelFactory.interests.get(id);
        } catch (NullPointerException e) {
            throw new RuntimeException("This interest id doesn't exist!", e);
        }

        assert res != null;
        return res;
    }

    /**
     * Returns a WikiPageModel with the given name
     *
     * @param name the name of the wikipedia page (not the url)
     * @return the specified WikiPageModel
     */
    public static WikiPageModel getWikiPage(String name) {
        assert name != null && !name.equals("");

        if (!ModelFactory.wikiPages.containsKey(name)) {
            ModelFactory.wikiPages.put(name, new WikiPageModel(name));
        }
        WikiPageModel res = ModelFactory.wikiPages.get(name);

        assert res != null;
        return res;
    }
}
