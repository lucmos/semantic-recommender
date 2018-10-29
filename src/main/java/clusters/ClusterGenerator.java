package clusters;

import babelnet.WikiPageMapping;
import constants.DatasetName;
import constants.Dimension;
import constants.PathConstants;
import datasetsreader.Dataset;
import io.Cache;
import io.Utils;
import twittermodel.TweetModel;
import twittermodel.UserModel;
import twittermodel.WikiPageModel;
import utils.Chrono;
import utils.Counter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ClusterGenerator {

    public static void main(String[] args) throws Utils.CacheNotPresent {
        Dimension dim = Dimension.COMPLETE;
        Dataset d = Cache.DatasetCache.readFromCache(DatasetName.WIKIMID, dim);
        WikiPageMapping wmap = Cache.WikiMappingCache.readFromCache(dim);

        ClusterGenerator gen = new ClusterGenerator(d, wmap);
        gen.generateCategoryClusters(dim);
        gen.generateDomainClusters(dim);
    }


    private Dataset dataset;
    private WikiPageMapping wikiMap;

    public ClusterGenerator(Dataset dataset, WikiPageMapping wikiMap) {
        this.dataset = dataset;
        this.wikiMap = wikiMap;
    }


    public Clusters generateCategoryClusters(Dimension dim) {
        Chrono c = new Chrono(String.format("Generating category clusters %s...", dim));

        Clusters clusters = clusterize(this.wikiMap.getSynsetToCategories());
        System.out.println(clusters.getClusterToUsers().size() + " " + clusters.getUserToCluster().size());
        Utils.savePrettyJson(clusters, PathConstants.CLUSTERS_CAT.getPath(dim));

        c.millis();
        return clusters;
    }

    public Clusters generateDomainClusters(Dimension dim) {
        Chrono c = new Chrono(String.format("Generating domain clusters %s...", dim));

        Clusters clusters = clusterize(this.wikiMap.getSynsetToDomain());
        Utils.savePrettyJson(clusters, PathConstants.CLUSTERS_DOM.getPath(dim));

        c.millis();
        return clusters;
    }

    private Clusters clusterize(Map<String, Set<String>> synToClusters) {
        HashMap<UserModel, Counter<String>> clusterization = new HashMap<>();

        for (UserModel user : dataset.getUsers().values()) {
            for (Integer tweetID : user.getTweetsIds()) {
                TweetModel tweet = user.getTweetModel(dataset.getTweets(), tweetID);
                WikiPageModel page = tweet.getWikiPageModel(dataset.getInterests(), dataset.getPages());

                Set<String> possibleClusters = this.wikiMap.getStrings(page, synToClusters);
                if (possibleClusters == null) {
                    continue;
                }

                if (!clusterization.containsKey(user)) {
                    clusterization.put(user, new Counter<>());
                }
                clusterization.get(user).increment(possibleClusters);
            }
        }

        Clusters clusters = new Clusters();
        for (Map.Entry<UserModel, Counter<String>> entry : clusterization.entrySet()) {
            UserModel user = entry.getKey();
            String cluster = entry.getValue().mostCommon();
            clusters.addUser(user, cluster);
        }
        return clusters;
    }
}
