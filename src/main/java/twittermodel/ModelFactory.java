package twittermodel;

import java.util.HashMap;

/**
 * Creates twitter objects.
 * If an object with a given identifier already exists, it is simply returned, otherwise it is created.
 */
public class ModelFactory {

    private final Dataset dataset;

    public ModelFactory(Dataset dataset) {
        this.dataset = dataset;
    }

    /**
     * Returns a UserModel with the given id
     *
     * @param id the id of the user
     * @return the specified UserModel
     */
    public  UserModel getUser(String id) {
        assert id != null && !id.equals("");

        if (!dataset.users.containsKey(id)) {
            dataset.users.put(id, new UserModel(dataset.getNextId(id), id));
        }
        UserModel res = dataset.users.get(id);

        assert res != null;
        return res;
    }

    /**
     * Returns a TweetModel with the given id
     *
     * @param id the id of the tweet
     * @return the specified TweetModel
     */
    public  TweetModel getTweet(String id) {
        assert id != null && !id.equals("");

        if (!dataset.tweets.containsKey(id)) {
            dataset.tweets.put(id, new TweetModel(dataset.getNextId(id), id));
        }
        TweetModel res = dataset.tweets.get(id);

        assert res != null;
        return res;
    }

    /**
     * Returns a InterestModel with the given id
     *
     * @param id the id of the interest
     * @return the specified InterestModel
     */
    public  InterestModel getInterest(String id) {
        assert id != null && !id.equals("");

        if (!dataset.interests.containsKey(id)) {
            dataset.interests.put(id, new InterestModel(dataset.getNextId(id), id));
        }
        InterestModel res = dataset.interests.get(id);

        assert res != null;
        return res;
    }

    /**
     * Returns a WikiPageModel with the given name
     *
     * @param name the name of the wikipedia page (not the url)
     * @return the specified WikiPageModel
     */
    public  WikiPageModel getWikiPage(String name) {
        assert name != null && !name.equals("");

        if (!dataset.wikiPages.containsKey(name)) {
            dataset.wikiPages.put(name, new WikiPageModel(dataset.getNextId(name), name));
        }
        WikiPageModel res = dataset.wikiPages.get(name);

        assert res != null;
        return res;
    }
}
