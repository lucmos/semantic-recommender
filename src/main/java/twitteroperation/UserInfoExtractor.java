package twitteroperation;

import constants.DatasetName;
import io.Cache;
import io.Utils;
import model.twitter.Dataset;
import model.twitter.UserModel;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.util.ArrayList;
import java.util.Map;


public class UserInfoExtractor
{

//    public void getUserFollowing(int userId) throws TwitterException {
//
//        Twitter twitter = new TwitterFactory().getInstance();
////        long cursor = -1;
//        IDs ids = twitter.getOutgoingFriendships(userId);
//        for (long i : ids.getIDs()){
//            System.out.println(userId +": "+ i );
//        }
//
//    }

    /**
     * This method look for all the following of the users of the dataset. If these users
     * already exist a following relation is added.
     * @param dataset the method look for following of the users in this dataset
     * @throws TwitterException
     */
    public void getUsersFollowing(Dataset dataset) throws TwitterException {
        try{
            Map<Integer, UserModel> users = dataset.getUsers();
            Twitter twitter = new TwitterFactory().getInstance();
            for (Map.Entry<Integer, UserModel> entry: users.entrySet()) {
                long userId = Integer.parseInt(entry.getValue().getName(dataset.getIdMapping()));
                System.out.println(userId);
                IDs ids = twitter.getFriendsIDs(userId,-1);
                System.out.println(ids + " "+ids.getIDs());
    //            for (long i : ids.getIDs()) {
    //                System.out.println(userId + ": " + i);
    //                //fare "se i sta nella lista di quelli conosciuti, allora aggiungilo come following per lo user corrispondente")
                }
        }
        catch (TwitterException e){
            e.printStackTrace();
            System.out.println("Failed to get outgoing friendships: "+ e.getMessage());
            System.exit((-1));
        }
    }

    public static void main(String[] args) throws Utils.CacheNotPresent, TwitterException {
        UserInfoExtractor userInfoExtractor = new UserInfoExtractor();

        Dataset d = Cache.DatasetCache.read(DatasetName.S22);
        Map<Integer, UserModel> users = d.getUsers();

        ConfigurationBuilder cfg = new ConfigurationBuilder();

        cfg.setOAuthAccessToken("973247752401547264-lSfo9oTH7hVnSCoY2UgSlYfEyZLzMiD");
        cfg.setOAuthAccessTokenSecret("qgVEBsbSGueCRNqw9LpHgy1DOddAAvJhqAD9vJo1tP3PE");
        cfg.setOAuthConsumerKey("kYx4lTa9T0VXOdrCO8bvg5qIO");
        cfg.setOAuthConsumerSecret("06XC6YyjP6acZ96rf3ATHe91t7HR4e3efZ0xhXMNpIMlDIyFH3");

        cfg.setTweetModeExtended(true);

        userInfoExtractor.getUsersFollowing(d);

//        for (Map.Entry<Integer, UserModel> entry: users.entrySet()){
////            userInfoExtractor.getUserFollowing(Integer.parseInt(entry.getValue().getIdString()));
//        }



//        StatusListener listener = new StatusListener()
//        {
//            public void onStatus(Status status) {
//                System.out.println(status.getUser().getName() + " : "
//                        + status.getText());
//            }
//            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {}
//            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {}
//
//            public void onScrubGeo(long l, long l1) {}
//
//            public void onStallWarning(StallWarning stallWarning) {}
//
//            public void onException(Exception ex) { ex.printStackTrace(); }
//        };
//
//
//        TwitterStream twitterStream = new TwitterStreamFactory(cfg.build()).getInstance();
//        twitterStream.addListener(listener);
//        FilterQuery fq = new FilterQuery();
//        fq.language("ita");
//        twitterStream.filter(fq);
//        twitterStream.sample();
//
    }
}
