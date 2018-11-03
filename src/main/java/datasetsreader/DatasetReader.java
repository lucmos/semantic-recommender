package datasetsreader;
import babelnet.BabelnetInterface;
import constants.DatasetInfo;
import constants.DatasetName;
import io.Config;
import io.TsvFileReader;
import model.twitter.*;
import utils.Chrono;

import java.util.List;
import java.util.Set;

/**
 * This class use a TsvFileReader to read a dataset and describes it building and managing a Dataset object
 */
public class DatasetReader {
    private TwitterFactory twitterFactory;
    private Dataset dataset;

    public DatasetReader(DatasetName name) {
        this(new Dataset(name, Config.getInstance().dimension()));
    }

    public DatasetReader(Dataset dataset) {
        this.dataset = dataset;
        this.twitterFactory = new TwitterFactory(dataset);
    }


    /**
     * From a couple userId-fFriendId creates two users and add them in the dataset object
     *
     * @param user_to_friend the couple that should be added to the dataset, where the user follows the friend
     * @param dataset        the object that stores all the informations
     */
    public static  void addRow_friendBased_dataset(List<String> user_to_friend, Dataset dataset, TwitterFactory twitterFactory) {
        assert user_to_friend.size() == 2;

        UserModel user = twitterFactory.getUser(user_to_friend.get(0));
        UserModel followed = twitterFactory.getUser(user_to_friend.get(1));

        user.addFollowOut(followed);

        dataset.addUser(user);
        dataset.addUser(followed);
    }

    /**
     * From a couple userId-wikiPage creates a user and its wikepedia page
     *
     * @param user_to_wikipage the couple of a user and the wikipedia page which represents it
     * @param dataset          the object that stores all the informations
     */
    public static  void addRow_friendBased_interest(List<String> user_to_wikipage, Dataset dataset, TwitterFactory twitterFactory) {
        assert user_to_wikipage.size() == 2;

        UserModel user = twitterFactory.getUser(user_to_wikipage.get(0));
        WikiPageModel wikiPage = twitterFactory.getWikiPage(user_to_wikipage.get(1));

        user.addWikiPageAboutUser(wikiPage);

        dataset.addWikiPage(wikiPage);
        dataset.addUser(user);
    }

    /**
     * Creates a user that makes a tweet about a specified interest. Note that the interest must already been created
     *
     * @param user_tweet_interest_interestUrl An array of 4 strings which represent: the user id, the tweet id, the interest id and
     *                                        the url of the tweet
     * @param dataset                         the object that stores all the informations
     */
    public static  void addRow_messageBased_dataset(List<String> user_tweet_interest_interestUrl, Dataset dataset, TwitterFactory twitterFactory) {
        assert user_tweet_interest_interestUrl.size() == 4;

        UserModel user = twitterFactory.getUser(user_tweet_interest_interestUrl.get(0));

        InterestModel interest = twitterFactory.getInterest(user_tweet_interest_interestUrl.get(2));

        TweetModel tweet = twitterFactory.getTweet(user_tweet_interest_interestUrl.get(1));
        tweet.setInterestId(interest);
        tweet.setInterestUrl(user_tweet_interest_interestUrl.get(3));

        user.addTweet(tweet);

        dataset.addUser(user);
        dataset.addInterest(interest);
        dataset.addTweet(tweet);
    }

    /**
     * Creates a new interest with the related wikipedia page in the specified dataset
     *
     * @param interest_platform_wikipage An array of 3 strings which represent: the interest id, the type of the platform and
     *                                   wikipedia page id
     * @param dataset                    the object that stores all the informations
     */
    public static  void addRow_messageBased_interest(List<String> interest_platform_wikipage, Dataset dataset, TwitterFactory twitterFactory) {
        assert interest_platform_wikipage.size() == 3;

        WikiPageModel page = twitterFactory.getWikiPage(interest_platform_wikipage.get(2));

        InterestModel interest = twitterFactory.getInterest(interest_platform_wikipage.get(0));
        interest.setPlatform(InterestModel.PlatformType.fromString(interest_platform_wikipage.get(1)));
        interest.setWikiPageId(page);

        dataset.addWikiPage(page);
        dataset.addInterest(interest);
    }

    /**
     * Creates a new user and add it in the specified dataset
     *
     * @param user    the id of the user to add
     * @param dataset the object that stores all the informations
     */
    public static  void addRow_S21(List<String> user, Dataset dataset, TwitterFactory twitterFactory) {
        assert user.size() == 1;

        UserModel u = twitterFactory.getUser(user.get(0));

        dataset.addUser(u);
    }

    /**
     * Creates a new user and a new wikipedia page that the user appreciates.
     * Both of them are added in the specified dataset
     *
     * @param user_to_wikipage the couple of the user id and of the wikipedia page id
     * @param dataset          the object that stores all the informations
     */
    public static  void addRow_S22_S23(List<String> user_to_wikipage, Dataset dataset, TwitterFactory twitterFactory) {
        assert user_to_wikipage.size() == 2;

        UserModel user = twitterFactory.getUser(user_to_wikipage.get(0));
        WikiPageModel wikiPage = twitterFactory.getWikiPage(user_to_wikipage.get(1));

        user.addWikiPagesOfLikedItemsIds(wikiPage);

        dataset.addUser(user);
        dataset.addWikiPage(wikiPage);
    }

    public  Dataset readDataset() {
        fillDataset(dataset.getName());
        return dataset;
    }

    /**
     * Reads datas from a dataset file using a TsvFileReader
     *
     * @param name    the dataset to read
     */
    public  void fillDataset(DatasetName name) {
        assert name != null;
        assert dataset != null;

        List<List<String>> s;
        Chrono c;
        switch (name) {
            case WIKIMID:
                s = TsvFileReader.readText(DatasetInfo.WIKIMID_MESSAGE_BASED_INTEREST_INFO.getPath());

                c = new Chrono("Building objects...");
                s.forEach(p -> addRow_messageBased_interest(p, dataset, twitterFactory));
                c.millis();

                s = TsvFileReader.readText(DatasetInfo.WIKIMID_MESSAGE_BASED_DATASET.getPath());
                c = new Chrono("Building objects...");
                s.forEach(p -> addRow_messageBased_dataset(p, dataset, twitterFactory));
                c.millis();

                s = TsvFileReader.readText(DatasetInfo.WIKIMID_FRIEND_BASED_DATASET.getPath());
                c = new Chrono("Building objects...");
                s.forEach(p -> addRow_friendBased_dataset(p, dataset, twitterFactory));
                c.millis();

                s = TsvFileReader.readText(DatasetInfo.WIKIMID_FRIEND_BASED_INTEREST_INFO.getPath());
                c = new Chrono("Building objects...");
                s.forEach(p -> addRow_friendBased_interest(p, dataset, twitterFactory));
                c.millis();

//                clean_WikiMID(dataset);
                break;

            case S21:
                s = TsvFileReader.readText(DatasetInfo.S21_DATASET.getPath());

                c = new Chrono("Building objects...");
                s.forEach(p -> addRow_S21(p, dataset, twitterFactory));
                c.millis();

                break;
            case S22:
                s = TsvFileReader.readText(DatasetInfo.S22_DATASET.getPath());

                c = new Chrono("Building objects...");
                s.forEach(p -> addRow_S22_S23(p, dataset, twitterFactory));
                c.millis();

                break;
            case S23:
                s = TsvFileReader.readText(DatasetInfo.S23_DATASET.getPath());

                c = new Chrono("Building objects...");
                s.forEach(p -> addRow_S22_S23(p, dataset, twitterFactory));
                c.millis();
                break;
        }
        updateBabelnetInformations();
    }

    private void updateBabelnetInformations() {
        Chrono c = new Chrono("Updating babelnet informations...");
        dataset.getWikiPages().values().forEach(page -> {
            Set<String> categories = BabelnetInterface.getCategories(page);

            for (String cat : categories) {
                BabelCategoryModel catModel = twitterFactory.getCategory(cat);
                page.addCategory(catModel);
                dataset.addCategory(catModel);
            }

            Set<String> domains = BabelnetInterface.getDomains(page);
            for (String dom : domains) {
                BabelDomainModel domainModel = twitterFactory.getDomain(dom);
                page.addBabelDomain(domainModel);
                dataset.addDomain(domainModel);
            }
        });

//        for (WikiPageModel page : dataset.getWikiPages().values()) {
//            Set<String> categories = BabelnetInterface.getCategories(page);
//
//            for (String cat : categories) {
//                BabelCategoryModel catModel = twitterFactory.getCategory(cat);
//                page.addCategory(catModel);
//                dataset.addCategory(catModel);
//            }
//
//            Set<String> domains = BabelnetInterface.getDomains(page);
//            for (String dom : domains) {
//                BabelDomainModel domainModel = twitterFactory.getDomain(dom);
//                page.addBabelDomain(domainModel);
//                dataset.addDomain(domainModel);
//            }
//        }
        c.millis();
    }

//    private  void clean_WikiMID(Dataset dataset) {
//        // TODO: 30/10/18 Inutile. 4 interesti con wikipage non usati, non 4 interest senza wikipage... [...]
//        Chrono c = new Chrono("Cleaning wikimid...");
//        Map<UserModel, Map<TweetModel, InterestModel>> toRemove = new HashMap<>();
//
//        for (UserModel user : dataset.getUsers().values()) {
//            for (String tweetId : user.getTweetsIds()) {
//                TweetModel tweet = user.getTweetModel(dataset.getTweets(), tweetId);
//                InterestModel interest = tweet.getInterestModel(dataset.getInterests());
//
//                if (!interest.isValid()) {
//                    toRemove.putIfAbsent(user, new HashMap<>());
//
//                    Map<TweetModel, InterestModel> m = toRemove.get(user);
//                    m.put(tweet, interest);
//                }
//            }
//        }
//
//        toRemove.forEach((user, y) -> y.forEach((tweet, interest) -> {
//            dataset.removeInterest(interest);
//            dataset.removeTweet(tweet);
//            user.removeTweet(tweet);
//        }));
//
//        c.millis("done (in %s %s)" + String.format(" - [interests deleted: %s]", toRemove.values()));
//    }
}
