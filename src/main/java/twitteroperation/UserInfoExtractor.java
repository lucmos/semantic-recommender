package twitteroperation;

import constants.DatasetName;
import io.Cache;
import io.Utils;
import model.twitter.Dataset;
import model.twitter.TwitterObjectFactory;
import model.twitter.UserModel;
import twitter4j.*;
import twitter4j.api.TweetsResources;
import twitter4j.conf.ConfigurationBuilder;

import java.util.ArrayList;
import java.util.Map;


public class UserInfoExtractor
{
    /**
     * This method look for all the following of the users of the dataset. If these users
     * already exist a following relation is added.
     * @param dataset the method look for following of the users in this dataset
     * @throws TwitterException
     */
    public void getUsersFollowing(Dataset dataset, Dataset clusterizedDataset){
        try{
            Map<Integer, UserModel> users = dataset.getUsers();
            Twitter twitter = new TwitterFactory().getInstance();
            for (Map.Entry<Integer, UserModel> entry: users.entrySet()) {
                System.out.println(entry.getKey());
                long userId = Integer.parseInt(entry.getValue().getName(dataset.getIdMapping()));
                IDs ids = twitter.getFriendsIDs(userId,-1);
                System.out.println(userId+" follows "+ ids.getIDs().length+ " users");
                int c=0;
                for (long i : ids.getIDs()) {
                    if (clusterizedDataset.exixstObj(i+"")) {
                        System.out.println("Utente numero "+c);
                        c++;
                        UserModel followed = clusterizedDataset.getUser(i+"");
                        System.out.println(followed);
                        TwitterObjectFactory tof = new TwitterObjectFactory(clusterizedDataset);
                        UserModel following = tof.getUser(userId+"", dataset.getName());
                        System.out.println(following);
                        following.addFollowOut(followed);
                    }
                }
            }}
        catch (TwitterException e){
            e.printStackTrace();
            System.out.println("Failed to get outgoing friendships: "+ e.getMessage());
            System.exit((-1));
        }
    }

    public void getUsersTweetter(Dataset dataset) throws TwitterException {
//        try{
            Map<Integer, UserModel> users = dataset.getUsers();
            Twitter twitter = new TwitterFactory().getInstance();
            for (Map.Entry<Integer, UserModel> entry: users.entrySet()) {
                System.out.println(entry.getKey());
                long userId = Integer.parseInt(entry.getValue().getName(dataset.getIdMapping()));   //userId è lo user di s22
//                IDs ids = twitter.getUserListStatuses(userId,-1);
//                https://github.com/Twitter4J/Twitter4J/blob/master/twitter4j-examples/src/main/java/twitter4j/examples/timeline/GetUserTimeline.java
//                System.out.println(ids);
//                int c=0;
//                for (long i : ids.getIDs()) {//i sono tutti i suoi following
//                    if (clusterizedDataset.exixstObj(i+"")) {
//                        System.out.println(c);
//                        c++;
////                        UserModel followed = clusterizedDataset.getUser(i+"");
////                        TwitterObjectFactory tof = new TwitterObjectFactory(clusterizedDataset);
////                        UserModel following = tof.getUser(userId+"");
////                        following.addFollowOut(followed);
//                    }
//                }
            }
//        catch (TwitterException e){
//            e.printStackTrace();
//            System.out.println("Failed to get outgoing friendships: "+ e.getMessage());
//            System.exit((-1));
//        }
    }
    public static void main(String[] args) throws Utils.CacheNotPresent, TwitterException {
        UserInfoExtractor userInfoExtractor = new UserInfoExtractor();

        Dataset d22 = Cache.DatasetCache.read(DatasetName.S22);
        Dataset wikimid = Cache.DatasetCache.read(DatasetName.WIKIMID);
        Map<Integer, UserModel> users = d22.getUsers();

//        ConfigurationBuilder cfg = new ConfigurationBuilder();

//        cfg.setOAuthAccessToken("973247752401547264-lSfo9oTH7hVnSCoY2UgSlYfEyZLzMiD");
//        cfg.setOAuthAccessTokenSecret("qgVEBsbSGueCRNqw9LpHgy1DOddAAvJhqAD9vJo1tP3PE");
//        cfg.setOAuthConsumerKey("kYx4lTa9T0VXOdrCO8bvg5qIO");
//        cfg.setOAuthConsumerSecret("06XC6YyjP6acZ96rf3ATHe91t7HR4e3efZ0xhXMNpIMlDIyFH3");

//        cfg.setTweetModeExtended(true);

        userInfoExtractor.getUsersFollowing(d22, wikimid);
//        userInfoExtractor.getUsersTweetter(d22);


    }
}
