package twitteroperation;

import constants.DatasetName;
import constants.TwitterRespPath;
import io.Cache;
import io.Utils;
import model.twitter.Dataset;
import model.twitter.UserModel;
import twitter4j.*;
import utils.Chrono;

import javax.xml.crypto.Data;
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
//        int counter = 0;
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

    public TweetsResp getUsersTwetter(Dataset dataset) throws TwitterException, InterruptedException {
        Chrono c = new Chrono("Downloading user tweets....");
        int userCounter = 0;
        TweetsResp resp = new TweetsResp(dataset.getName());
        Map<Integer, UserModel> users = dataset.getUsers();
        Twitter twitter = new TwitterFactory().getInstance();
        for (Map.Entry<Integer, UserModel> entry : users.entrySet()) {
            System.out.println("\n\nThis is the "+userCounter+" user");
            userCounter++;

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
                    List<String> texts = new ArrayList<>();
//                    int abc = 0;
                    for (Status s : statuses) {
//                        System.out.print(abc);
//                        ++abc;
//                        System.out.println(s.getText());
                        texts.add(s.getText());
                    }
                    resp.addResult(stringUserId, texts);
                }
            } catch (TwitterException e) {
                manageRequestError(e, entry.getValue(), resp, dataset);
            }
        }
        c.millis();
        return resp;
    }

    private void manageRequestError(TwitterException e, UserModel user, Resp resp, Dataset dataset) throws InterruptedException {
        if (e.resourceNotFound()) {
            System.out.println("A user hasn't been found");
            user.setNotExists(true);
            resp.addNotExistingId(user.getName(dataset));
        } else if(e.getErrorCode()==403){
            System.out.println("Error 403: "+ e.getMessage());
            System.out.println("I'm so asleep.... I think I'll relax for next 15 minutes! (zzzzzz.....)");
            TimeUnit.MINUTES.sleep(3);
        } else{
            e.printStackTrace();
            System.out.println("Failed to get user tweets: " + e.getMessage());
            System.exit((-1));
        }
    }

    public static void main(String[] args) throws Utils.CacheNotPresent, TwitterException, InterruptedException {
        //FAI PER S22 ED S21 (DI DEFAULT E' S22)
        UserInfoExtractor userInfoExtractor = new UserInfoExtractor();

        Dataset d22 = Cache.DatasetCache.read(DatasetName.S22);
        Dataset wikimid = Cache.DatasetCache.read(DatasetName.WIKIMID);

        FollowerResp friendResp22 = userInfoExtractor.getUsersFollowing(d22, wikimid);
        System.out.println(friendResp22);
        Utils.saveJson(friendResp22, TwitterRespPath.FRIENDS_RESP.getPath(d22.getName().toString()), true);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////
        UserInfoExtractor userInfoExtractorTweet = new UserInfoExtractor();

        Dataset d22Tweet = Cache.DatasetCache.read(DatasetName.S22);

        TweetsResp tweetResp22 = userInfoExtractorTweet.getUsersTwetter(d22Tweet);
        System.out.println(tweetResp22);
        Utils.saveJson(tweetResp22, TwitterRespPath.TWEETS_RESP.getPath(d22Tweet.getName().toString()), true);


//        Map<Integer, UserModel>1 users = d22.getUsers();

//        System.out.println(d22.getName());

////
//        FollowerResp fresp = userInfoExtractor.getUsersFollowing(d22, wikimid);
//        fresp.toString();
//        System.out.println(fresp);
////        fresp.saveFollowerResp(true, d22.getName().toString());
//        Utils.saveJson(fresp, TwitterRespPath.FRIENDS_RESP.getPath(d22.getName().toString()), true);
//
//        FollowerResp respf = Utils.restoreJson(TwitterRespPath.FRIENDS_RESP.getPath(DatasetName.S22.name()), FollowerResp.class);
//        System.out.println(respf);



//        TweetsResp resp = userInfoExtractor.getUsersTwetter(d22);
//        resp.toString();
//        System.out.println("!!!!!!!!!!!!!!!!!!!");
//        System.out.println(resp);

//        resp.saveTweetResp(true, d22.getName().toString());
//        Utils.saveJson(resp, TwitterRespPath.TWEETS_RESP.getPath(d22.getName().toString()), true);

//
//        TweetsResp resp2 = Utils.restoreJson(TwitterRespPath.TWEETS_RESP.getPath(DatasetName.S22.name()), TweetsResp.class);
//        FollowerResp respfk = Utils.restoreJson(TwitterRespPath.FRIENDS_RESP.getPath(DatasetName.S22.name()), FollowerResp.class);

//        System.out.println(resp2);
//        System.out.println(respfk);
    }
}
