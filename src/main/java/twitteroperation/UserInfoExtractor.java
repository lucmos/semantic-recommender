package twitteroperation;

import constants.DatasetName;
import constants.TwitterRespPath;
import io.Cache;
import io.Config;
import io.Utils;
import model.twitter.Dataset;
import model.twitter.UserModel;
import twitter4j.*;
import utils.Chrono;

import javax.xml.crypto.Data;
import java.util.*;
import java.util.concurrent.TimeUnit;


public class UserInfoExtractor {

    public List<UserModel> getSortedUsers(Dataset dataset) {
        List<UserModel> l = new ArrayList<>(dataset.getUsers().values());
        Collections.sort(l);
        return l;
    }

    /**
     * This method look for all the following of the users of the dataset. If these users
     * already exist a following relation is added.
     *
     * @param dataset the method look for following of the users in this dataset
     * @throws TwitterException
     */
    public FollowerResp getUsersFollowing(Dataset dataset, Dataset clusterizedDataset) throws InterruptedException {
        Chrono c = new Chrono("Downloading user friendships....");
        FollowerResp resp = new FollowerResp(dataset.getName());
        Twitter twitter = new TwitterFactory().getInstance();

        List<UserModel> users = this.getSortedUsers(dataset);
        int current_user = 0; // keeps tack of current user

        while (current_user < users.size()) {
            UserModel u = users.get(current_user);
            System.out.println(String.format("Downloading friends of user %s...", current_user));

            try {
                String stringUserId = u.getName(dataset);
                long userId = Integer.parseInt(stringUserId);
                User user = twitter.showUser(userId);
                if (user.getStatus() == null) {
                    u.setIsPrivate(true);
                    resp.addPrivateUserId(stringUserId);
                } else {
                    IDs ids = twitter.getFriendsIDs(userId, -1);
                    HashSet<String> friends = new HashSet<String>();
                    for (long i : ids.getIDs()) {
                        if (clusterizedDataset.exixstObj(i + "")) {
                            friends.add(i + "");
//                            UserModel followed = clusterizedDataset.getUser(i + "");
//                            TwitterObjectFactory tof = new TwitterObjectFactory(clusterizedDataset);
//                            UserModel following = tof.getUser(userId + "", dataset.getName());
//                            following.addFollowOut(followed);
                        }
                    }
                    resp.addResult(stringUserId, friends);
                }

                current_user++;
            } catch (TwitterException e) {
                boolean cause = manageRequestError(e, u, resp, dataset);
                current_user += cause ? 1 : 0;
            }
        }
        c.millis();
        return resp;
    }

    public TweetsResp getUsersTwetter(Dataset dataset) throws TwitterException, InterruptedException {
        Chrono c = new Chrono("Downloading user tweets....");
        TweetsResp resp = new TweetsResp(dataset.getName());
        Twitter twitter = new TwitterFactory().getInstance();

        List<UserModel> users = this.getSortedUsers(dataset);
        int current_user = 0;

        while (current_user < users.size()) {
            UserModel u = users.get(current_user);
            System.out.println(String.format("Downloading tweets of user %s...", current_user));

            try {
                String stringUserId = u.getName(dataset);
                long userId = Integer.parseInt(stringUserId);
                User user = twitter.showUser(userId);
                if (user.getStatus() == null) {
                    u.setIsPrivate(true);
                    resp.addPrivateUserId(stringUserId);
                } else {
                    List<Status> statuses = twitter.getUserTimeline(userId);
                    List<String> texts = new ArrayList<>();
                    for (Status s : statuses) {
                        texts.add(s.getText());
                    }
                    resp.addResult(stringUserId, texts);
                }

                current_user++;
            } catch (TwitterException e) {
                boolean cause = manageRequestError(e, u, resp, dataset);
                current_user += cause ? 1 : 0;
            }
        }
        c.millis();
        return resp;
    }

    private boolean manageRequestError(TwitterException e, UserModel user, Resp resp, Dataset dataset) throws InterruptedException {
        if (e.exceededRateLimitation()) {
            System.out.println("I'm sure I am so asleep.... I think I'll relax for next 15 minutes! (zzzzzz.....)");
            TimeUnit.MINUTES.sleep(3);
            return false;
        } else if (e.resourceNotFound()) {
            System.out.println("A user hasn't been found");
            user.setNotExists(true);
            resp.addNotExistingUserId(user.getName(dataset));
            return true;
        } else if (e.getErrorCode() == 63) {
            System.out.println("The user is suspended!");
            user.setIsSuspended(true);
            resp.addSuspendedUserId(user.getName(dataset));
            return true;
        } else {
            System.out.println(String.format(
                    "\n\nERROR CODE: %s" +
                            "STATUS CODE: %s" +
                            "EXCEPTION CODE: %s\n\n", e.getErrorCode(), e.getStatusCode(), e.getExceptionCode()));
            e.printStackTrace();
            System.out.println("\n\nI'm may be asleep.... I think I'll relax for next 15 minutes! (zzzzzz.....)\n\n");
            TimeUnit.MINUTES.sleep(3);
            return false;
        }
    }

    private static DatasetName[] _dataset_to_process() {
        return new DatasetName[]{DatasetName.S21, DatasetName.S22,};
    }

    private static void _download_tweets() throws Utils.CacheNotPresent, TwitterException, InterruptedException {
        for (DatasetName dname : _dataset_to_process()) {
            Chrono c = new Chrono(String.format("Downloading tweets of: %s", dname));
            UserInfoExtractor userInfoExtractorTweet = new UserInfoExtractor();
            Dataset dataset = Cache.DatasetCache.read(dname);
            TweetsResp tweetResp22 = userInfoExtractorTweet.getUsersTwetter(dataset);
            System.out.println(tweetResp22);
            Utils.saveJson(tweetResp22, TwitterRespPath.TWEETS_RESP.getPath(dataset.getName().toString()), true);
            c.millis();
        }
    }

    private static void _download_friends() throws Utils.CacheNotPresent, InterruptedException {
        for (DatasetName dname : _dataset_to_process()) {
            Chrono c = new Chrono(String.format("Downloading friends of: %s", dname));
            UserInfoExtractor userInfoExtractor = new UserInfoExtractor();

            Dataset dataset = Cache.DatasetCache.read(dname);
            Dataset wikimid = Cache.DatasetCache.read(DatasetName.WIKIMID);

            FollowerResp friendResp22 = userInfoExtractor.getUsersFollowing(dataset, wikimid);
            System.out.println(friendResp22);
            Utils.saveJson(friendResp22, TwitterRespPath.FRIENDS_RESP.getPath(dataset.getName().toString()), true);
            c.millis();
        }
    }

    public static void main(String[] args) throws Utils.CacheNotPresent, TwitterException, InterruptedException {
        Config.getInstance();

//        UserInfoExtractor._download_friends();
        UserInfoExtractor._download_tweets();

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
