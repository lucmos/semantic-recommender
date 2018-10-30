package clusters;

import babelnet.WikiPageMapping;
import constants.DatasetName;
import constants.PathConstants;
import datasetsreader.Dataset;
import io.Cache;
import io.Utils;
import properties.PropReader;
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
        Dataset d = Cache.DatasetCache.readFromCache(DatasetName.WIKIMID);
        WikiPageMapping wmap = Cache.WikiMappingCache.readFromCache();

        ClusterGenerator gen = new ClusterGenerator(d, wmap);
        gen.generateCategoryClusters();
        gen.generateDomainClusters();
    }


    private Dataset dataset;
    private WikiPageMapping wikiMap;

    public ClusterGenerator(Dataset dataset, WikiPageMapping wikiMap) {
        this.dataset = dataset;
        this.wikiMap = wikiMap;
    }

    public Clusters loadCategoryClusters() throws Utils.CacheNotPresent {
        return Cache.ClustersCache.readFromCache(PathConstants.CLUSTERS_CAT);
    }

    public Clusters loadDomainsClusters() throws Utils.CacheNotPresent {
        return Cache.ClustersCache.readFromCache(PathConstants.CLUSTERS_DOM);
    }

    public Clusters generateCategoryClusters() {
        Chrono c = new Chrono("Generating category clusters...");

        Clusters clusters = clusterize(this.wikiMap.getSynsetToCategories());
        System.out.println(clusters.getClusterToUsers().size() + " " + clusters.getUserToCluster().size());
        Utils.savePrettyJson(clusters, PathConstants.CLUSTERS_CAT.getPath(PropReader.getInstance().dimension()));

        c.millis();
        return clusters;
    }

    public Clusters generateDomainClusters() {
        Chrono c = new Chrono("Generating domain clusters...");

        Clusters clusters = clusterize(this.wikiMap.getSynsetToDomain());
        Utils.savePrettyJson(clusters, PathConstants.CLUSTERS_DOM.getPath(PropReader.getInstance().dimension()));

        c.millis();
        return clusters;
    }

    private Clusters clusterize(Map<String, Set<String>> synToClusters) {
        HashMap<UserModel, Counter<String>> clusterization = new HashMap<>();

        for (UserModel user : dataset.getUsers().values()) {
            for (String tweetID : user.getTweetsIds()) {
                TweetModel tweet = user.getTweetModel(dataset.getTweets(), tweetID);
                WikiPageModel page = tweet.getWikiPageModel(dataset.getInterests(), dataset.getPages());
                if (page == null) continue;

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
