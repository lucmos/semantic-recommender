package twitteroperation;

import constants.DatasetName;
import io.Cache;
import io.Utils;
import javafx.scene.control.SpinnerValueFactory;
import model.twitter.Dataset;
import model.twitter.UserModel;
import twitter4j.*;
import utils.Chrono;

import java.util.*;
import java.util.concurrent.TimeUnit;


public class UserInfoExtractor
{
    /**
     * This method look for all the following of the users of the dataset. If these users
     * already exist a following relation is added.
     * @param dataset the method look for following of the users in this dataset
     * @throws TwitterException
     */
    public FollowerResp getUsersFollowing(Dataset dataset, Dataset clusterizedDataset) throws InterruptedException {
        Chrono c = new Chrono("Downloading user friendships....");
        int counter = 0;
        FollowerResp resp = new FollowerResp(dataset.getName());
        Map<Integer, UserModel> users = dataset.getUsers();
        Twitter twitter = new TwitterFactory().getInstance();
        for (Map.Entry<Integer, UserModel> entry: users.entrySet()) {
            try {
                String stringUserId = entry.getValue().getName(dataset);
                long userId = Integer.parseInt(stringUserId);
                User user = twitter.showUser(userId);
                if(user.getStatus() == null){
                    entry.getValue().setIsPrivate(true);
                    resp.addPrivateUserId(stringUserId);
                }
                else {
                    IDs ids = twitter.getFriendsIDs(userId, -1);
                    HashSet<String> friends = new HashSet<String>();
                    for (long i : ids.getIDs()) {
                        if (clusterizedDataset.exixstObj(i + "")) {
                            friends.add(i+"");
//                            UserModel followed = clusterizedDataset.getUser(i + "");
//                            TwitterObjectFactory tof = new TwitterObjectFactory(clusterizedDataset);
//                            UserModel following = tof.getUser(userId + "", dataset.getName());
//                            following.addFollowOut(followed);
                        }
                    }
                    resp.addResult(stringUserId, friends);
                }
            } catch (TwitterException e) {
                if(e.exceededRateLimitation()){
                    if (counter==1)return resp;
                    counter+=1;
                    System.out.println("I'm so asleep.... I think I'll relax for next 15 minutes! (zzzzzz.....)");
                    TimeUnit.MINUTES.sleep(3);
                }
                else{
                manageRequestError(e, entry.getValue(), resp, dataset);
            }}
        }
        c.millis();
        return resp;
    }

    public TweetsResp getUsersTwetter(Dataset dataset) throws TwitterException {
        Chrono c = new Chrono("Downloading user tweets....");
        TweetsResp resp = new TweetsResp(dataset.getName());
        Map<Integer, UserModel> users = dataset.getUsers();
        Twitter twitter = new TwitterFactory().getInstance();
        for (Map.Entry<Integer, UserModel> entry : users.entrySet()) {
            try {
                String stringUserId = entry.getValue().getName(dataset);
                long userId = Integer.parseInt(stringUserId);
                User user = twitter.showUser(userId);
                if(user.getStatus() == null){
                    entry.getValue().setIsPrivate(true);
                    resp.addPrivateUserId(stringUserId);
                }
                else{
                    List<Status> statuses = twitter.getUserTimeline(userId);
                    resp.addResult(stringUserId, statuses);
                }
            } catch (TwitterException e) {
                manageRequestError(e, entry.getValue(), resp, dataset);
            }
        }
        c.millis();
        return resp;
    }

    private void manageRequestError(TwitterException e, UserModel user, Resp resp, Dataset dataset){
        if (e.resourceNotFound()) {
            user.setNotExists(true);
            resp.addNotExistingId(user.getName(dataset));
        } else {
            e.printStackTrace();
            System.out.println("Failed to get user tweets: " + e.getMessage());
            System.exit((-1));
        }
    }

    public static void main(String[] args) throws Utils.CacheNotPresent, TwitterException, InterruptedException {
        UserInfoExtractor userInfoExtractor = new UserInfoExtractor();

        Dataset d22 = Cache.DatasetCache.read(DatasetName.S21);
        Dataset wikimid = Cache.DatasetCache.read(DatasetName.WIKIMID);
        Map<Integer, UserModel> users = d22.getUsers();

//        ConfigurationBuilder cfg = new ConfigurationBuilder();
//
//        cfg.setOAuthAccessToken("973247752401547264-lSfo9oTH7hVnSCoY2UgSlYfEyZLzMiD");
//        cfg.setOAuthAccessTokenSecret("qgVEBsbSGueCRNqw9LpHgy1DOddAAvJhqAD9vJo1tP3PE");
//        cfg.setOAuthConsumerKey("kYx4lTa9T0VXOdrCO8bvg5qIO");
//        cfg.setOAuthConsumerSecret("06XC6YyjP6acZ96rf3ATHe91t7HR4e3efZ0xhXMNpIMlDIyFH3");
//
//        cfg.setTweetModeExtended(true);
//
        FollowerResp fresp = userInfoExtractor.getUsersFollowing(d22, wikimid);
        fresp.toString();
        System.out.println(fresp);

        System.out.println();
        TweetsResp resp = userInfoExtractor.getUsersTwetter(d22);
        resp.toString();
        System.out.println("!!!!!!!!!!!!!!!!!!!");
        System.out.println(resp);

    }
}
