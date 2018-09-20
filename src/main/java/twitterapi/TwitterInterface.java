package twitterapi;


import twitter4j.*;
import java.util.List;

/**
 * Layer between the application and the twitter API.
 * All the API calls must be performed here.
 */
public class TwitterInterface {

    private static TwitterInterface instance = null;

    /**
     * Implements the singleton pattern
     *
     * @return an instance of this class
     */
    public static TwitterInterface getSingleton() {
        if (TwitterInterface.instance == null) {
            TwitterInterface.instance = new TwitterInterface();
        }
        return TwitterInterface.instance;
    }

    private Twitter twitterFactory;

    /**
     * Builds the required twitter4j objects, once.
     */
    private TwitterInterface() {
        this.twitterFactory = TwitterFactory.getSingleton();
    }

    /**
     * Request the information of a specific user
     *
     * @param id the twitter id of the user
     * @return the user information
     */
    public User getUser(long id) {
        try {
            return this.twitterFactory.showUser(id);
        } catch (TwitterException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to retrieve user");
        }
    }

    /**
     * Request the information of a batch of users
     *
     * @param ids an array of user's id
     * @return the users information
     */
    public ResponseList<User> getUsers(long[] ids) {
        try {
            return this.twitterFactory.lookupUsers(ids);
        } catch (TwitterException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to retrieve users");
        }
    }


    public static void main(String[] args) {
        TwitterInterface tw = TwitterInterface.getSingleton();
        System.out.println(tw.getUsers(new long[] {101935414,105320169}));
    }
}
