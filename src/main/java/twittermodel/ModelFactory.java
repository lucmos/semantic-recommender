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

    public BabelCategoryModel getCategory(String id) {
        assert id != null && !id.equals("");

        if (!dataset.exixstObj(id)) {
            int i = dataset.getNextId(id);
            dataset.babelCategories.put(i, new BabelCategoryModel(i, id));
        }
        BabelCategoryModel res = dataset.babelCategories.get(dataset.getIntegerId(id));

        assert res != null;
        return res;
    }

    public BabelDomainModel getDoamin(String id) {
        assert id != null && !id.equals("");

        if (!dataset.exixstObj(id)) {
            int i = dataset.getNextId(id);
            dataset.babelDomains.put(i, new BabelDomainModel(i, id));
        }
        BabelDomainModel res = dataset.babelDomains.get(dataset.getIntegerId(id));

        assert res != null;
        return res;
    }

    /**
     * Returns a UserModel with the given id
     *
     * @param id the id of the user
     * @return the specified UserModel
     */
    public  UserModel getUser(String id) {
        assert id != null && !id.equals("");

        if (!dataset.exixstObj(id)) {
            int i = dataset.getNextId(id);
            dataset.users.put(i, new UserModel(i, id));
        }
        UserModel res = dataset.users.get(dataset.getIntegerId(id));

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

        if (!dataset.exixstObj(id)) {
            int i = dataset.getNextId(id);
            dataset.tweets.put(i, new TweetModel(i, id));
        }
        TweetModel res = dataset.tweets.get(dataset.getIntegerId(id));

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

        if (!dataset.exixstObj(id)) {
            int i = dataset.getNextId(id);
            dataset.interests.put(i, new InterestModel(i, id));
        }
        InterestModel res = dataset.interests.get(dataset.getIntegerId(id));

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

        if (!dataset.exixstObj(name)) {
            int i = dataset.getNextId(name);
            dataset.wikiPages.put(i, new WikiPageModel(i, name));
        }
        WikiPageModel res = dataset.wikiPages.get(dataset.getIntegerId(name));

        assert res != null;
        return res;
    }
}
