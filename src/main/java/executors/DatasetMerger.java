package executors;

import constants.DatasetName;
import constants.TwitterRespPath;
import io.Cache;
import io.Utils;
import model.twitter.*;
import twitteroperation.FollowerResp;
import twitteroperation.TweetRespDisambiguated;
import utils.Counter;

import javax.xml.crypto.Data;
import java.util.HashSet;
import java.util.Map;

public class DatasetMerger {

    private static DatasetName[] _to_process() {
        return new DatasetName[]{DatasetName.S21, DatasetName.S22};
    }

    private static void integrate_disambiguated_tweets(TwitterObjectFactory factory, DatasetName dname) throws Utils.CacheNotPresent {
        TweetRespDisambiguated resp = Utils.restoreJson(TwitterRespPath.TWEETS_RESP_DISAMBIGUATED.getPath(dname.name()), TweetRespDisambiguated.class);
        assert resp.getOriginDataset().equals(dname);

        for (Map.Entry<String, Counter<String>> entry : resp.getUsers2categories().entrySet()) {
            UserModel u = factory.getUser(entry.getKey(), dname);
            for (String cat : entry.getValue().getMap().keySet()) {
                BabelCategoryModel catModel = factory.getCategory(cat);
                u.addCategoryToDisambiguatedTweets(catModel, entry.getValue().count(cat));
            }
        }

        for (Map.Entry<String, Counter<String>> entry : resp.getUsers2domains().entrySet()) {
            UserModel u = factory.getUser(entry.getKey(), dname);
            for (String dom : entry.getValue().getMap().keySet()) {
                BabelDomainModel catModel = factory.getDomain(dom);
                u.addDomainToDisambiguatedTweets(catModel, entry.getValue().count(dom));
            }
        }
    }

    private static void integrate_friends(TwitterObjectFactory factory, DatasetName dname) throws Utils.CacheNotPresent {
        FollowerResp resp = Utils.restoreJson(TwitterRespPath.FRIENDS_RESP.getPath(dname.name()), FollowerResp.class);
        assert resp.getOriginDataset().equals(dname);

        for (Map.Entry<String, HashSet<String>> entry : resp.getUser2Friends().entrySet()) {
            UserModel u = factory.getUser(entry.getKey(), dname);

            for (String f : entry.getValue()) {
                UserModel friend = factory.getUser(f, dname);
                u.addFollowOut(friend);
            }
        }
    }

    private static Dataset integrate_twitter_information(Dataset mainDataset, DatasetName[] infoDataset) throws Utils.CacheNotPresent {
        TwitterObjectFactory factory = new TwitterObjectFactory(mainDataset);

        for (DatasetName dname : infoDataset) {
            integrate_disambiguated_tweets(factory, dname);
            integrate_friends(factory, dname);
        }

        factory.updateBabelnetInformations();
        return factory.getDataset();
    }

    public static void unionAndSave() throws Utils.CacheNotPresent {
        Dataset wikimid = Cache.DatasetCache.read(DatasetName.WIKIMID);
        Dataset union = integrate_twitter_information(wikimid, _to_process());
        Cache.DatasetCache.write(DatasetName.UNION, union);
    }


    public static void main(String[] args) throws Utils.CacheNotPresent {
        unionAndSave();
    }
}
