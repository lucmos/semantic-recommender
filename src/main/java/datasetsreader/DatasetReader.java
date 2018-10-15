package datasetsreader;
import twittermodel.*;
import io.TsvFileReader;

import java.util.ArrayList;

/**
 * This class use a TsvFileReader to read a dataset and describes it building and managing a Dataset object
 */
public class DatasetReader
{
    /**Reads datas from a dataset file using a TsvFileReader
     * @param path the path of the file
     * @return the readed data
     */
    public ArrayList<ArrayList<String>> readDataset(String path)
    {
        TsvFileReader fileReader = new TsvFileReader();
//        todo: remove magic number
        ArrayList<String> datasetLines = fileReader.readText(path, 100000);
        return fileReader.splitByChar(datasetLines);
    }

    public enum DatasetType
    {
        WIKI_MID_FRIENDBASED_DATASET,
        WIKI_MID_FRIENDBASED_INTEREST,
        WIKI_MID_MESSAGEBASED_DATASET,
        WIKI_MID_MESSAGEBASED_INTEREST,
        S21,
        S22_S23;
    }

    /**
     * From a couple userId-fFriendId creates two users and add them in the dataset object
     * @param userFriend the couple that should be added to the dataset, where the user follows the friend
     * @param dataset the object that stores all the informations
     */
    private void addUserFollowed(ArrayList<String> userFriend, Dataset dataset)
    {
        assert userFriend.size()==2;
        UserModel user = ModelFactory.getUser(Integer.parseInt(userFriend.get(0)));
        UserModel followed = ModelFactory.getUser(Integer.parseInt(userFriend.get(1)));
        followed.addFollowOut(user);
        dataset.addUser(user);
        dataset.addUser(followed);
    }

    /**
     * From a couple userId-wikiPage creates a user and its wikepedia page
     * @param userWikiPage the couple of a user and the wikipedia page which represents it
     * @param dataset the object that stores all the informations
     */
    private void addUserCorrespondingInterest(ArrayList<String> userWikiPage, Dataset dataset)
    {
        assert userWikiPage.size()==2;
        UserModel user = ModelFactory.getUser(Integer.parseInt(userWikiPage.get(0)));
        WikiPageModel wikiPage = ModelFactory.getWikiPage(userWikiPage.get(1));
        user.addWikiPageAbout(wikiPage);
        dataset.addPage(wikiPage);
        dataset.addUser(user);
    }


    /**
     * Creates a user that makes a tweet about a specified interest. Note that the interest must already been created
     * @param userTweetInterestURL An array of 4 strings which represent: the user id, the tweet id, the interest id and
     *                             the url of the tweet
     * @param dataset the object that stores all the informations
     */
    private void addUserTweetInterestURL(ArrayList<String> userTweetInterestURL, Dataset dataset)
    {
        assert userTweetInterestURL.size()==4;
        UserModel user = ModelFactory.getUser(Integer.parseInt(userTweetInterestURL.get(0)));
        InterestModel interest = ModelFactory.getInterest(userTweetInterestURL.get(2));
        TweetModel tweet = ModelFactory.getTweet(Integer.parseInt(userTweetInterestURL.get(1)), interest, userTweetInterestURL.get(3));

        user.addTweet(tweet);

        dataset.addUser(user);
        dataset.addInterest(interest);
        dataset.addTweet(tweet);
    }

    /**
     * Creates a new interest with the related wikipedia page in the specified dataset
     * @param interestPlatformPage An array of 3 strings which represent: the interest id, the type of the platform and
     *                             wikipedia page id
     * @param dataset  the object that stores all the informations
     */
    private void  addInterestPlatformPage(ArrayList<String> interestPlatformPage, Dataset dataset)
    {
        assert interestPlatformPage.size()==3;
        WikiPageModel page = ModelFactory.getWikiPage(interestPlatformPage.get(2));
        InterestModel interest = ModelFactory.getInterest(interestPlatformPage.get(0), InterestModel.PlatformType.fromString(interestPlatformPage.get(1)), page);
        dataset.addPage(page);
        dataset.addInterest(interest);
    }

    /**
     * Creates a new user and add it in the specified dataset
     * @param user the id of the user to add
     * @param dataset the object that stores all the informations
     */
    private void addUser (ArrayList<String> user, Dataset dataset){
        assert user.size()==1;
        UserModel u = new UserModel(Integer.parseInt(user.get(0)));
        dataset.addUser(u);
    }

    /**
     * Creates a new user and a new wikipedia page that the user appreciates. Both of them are added in the specified dataset
     * @param userWikipage the couple of the user id and of the wikipedia page id
     * @param dataset the object that stores all the informations
     */
    private void addUserFollowedPage(ArrayList<String> userWikipage, Dataset dataset){
        assert userWikipage.size()==2;
        UserModel user = ModelFactory.getUser(Integer.parseInt(userWikipage.get(0)));
        WikiPageModel wikiPage = ModelFactory.getWikiPage(userWikipage.get(1));

        user.addWikiPageLiked(wikiPage);
        dataset.addUser(user);
        dataset.addPage(wikiPage);
    }


    /**
     * Stores a list of object taken from
     * @param dataset
     * @param readedData
     * @param dType
     */
    public void addDataInDataset(Dataset dataset, ArrayList<ArrayList<String>> readedData, DatasetType dType)
    {
        switch(dType){
            case WIKI_MID_FRIENDBASED_DATASET:
                for (ArrayList<String> userFriend : readedData)
                {
                    addUserFollowed(userFriend, dataset);
                }
                break;
            case WIKI_MID_FRIENDBASED_INTEREST:
                for (ArrayList<String> frienWikiPage : readedData)
                {
                    addUserCorrespondingInterest(frienWikiPage, dataset);
                }
                break;
            case WIKI_MID_MESSAGEBASED_DATASET:
                for (ArrayList<String> userTweetIntURL : readedData)
                {
                    addUserTweetInterestURL(userTweetIntURL, dataset);
                }
                break;
            case WIKI_MID_MESSAGEBASED_INTEREST:
                for (ArrayList<String> intPlatPage : readedData)
                {
                    addInterestPlatformPage(intPlatPage, dataset);
                }
                break;
            case S21:
                for (ArrayList<String> user : readedData)
                {
                    addUser(user, dataset);
                }
                break;
            case S22_S23:
                for (ArrayList<String> userPage : readedData)
                {
                    addUserFollowedPage(userPage, dataset);
                }
                break;
        }
    }
}
