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
        Dataset d = Cache.DatasetCache.read(DatasetName.WIKIMID);
        WikiPageMapping wmap = Cache.WikiMappingCache.read();

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
        HashMap<UserModel, Counter<String>> userTocatCounter = new HashMap<>();

//      Associa ad ogni utente il conteggio delle categorie che gli piacciono
        for (UserModel user : dataset.getUsers().values()) {
            for (String tweetID : user.getTweetsIds()) {
                TweetModel tweet = user.getTweetModel(dataset.getTweets(), tweetID);
                WikiPageModel page = tweet.getWikiPageModel(dataset.getInterests(), dataset.getPages());
                if (page == null) continue;

                Set<String> possibleClusters = this.wikiMap.getStrings(page, synToClusters);
                if (possibleClusters == null) {
                    continue;
                }

                if (!userTocatCounter.containsKey(user)) {
                    userTocatCounter.put(user, new Counter<>());
                }

                for (String s : possibleClusters) {

//                    userTocatCounter.get(user).set(s, importance.get(s));
                    userTocatCounter.get(user).increment(possibleClusters);
                }
            }
        }

//        Counter<String> catdistr = Counter.fromMultiMap(wikiMap.getSynsetToCategories());
        HashMap<UserModel, Counter<String>> usersTocatImportance = new HashMap<>();

        HashMap<String, Double> cache = new HashMap<>();
        double documents = synToClusters.size();

//        Associa ad ogni utente le categire che gli piacciono associate ad una misura di importanza basata sul tf.idf
        for (Map.Entry<UserModel, Counter<String>> entry : userTocatCounter.entrySet()) {
            UserModel user = entry.getKey();

            Counter<String> userCatCounter = entry.getValue();
            Counter<String> userImpCounter = new Counter<>();

            for (Map.Entry<String, Double> e : userCatCounter.getMap().entrySet()) {
                String cat = e.getKey();

                double cf = userCatCounter.importance(cat) / userCatCounter.size();
                double iuf;

                if (cache.containsKey(cat)) {
                    iuf = cache.get(cat);
                }
                else{
                    double uf = synToClusters.values().stream().parallel().filter(x -> x.contains(cat)).count();
                    iuf = Math.log(documents / uf);
                    cache.put(cat, iuf);
                }
//                System.out.println(String.format("%s\n%s\n%s\n%s\n", cf, documntes, uf, iuf));
                userImpCounter.set(cat, cf * iuf);
            }
            usersTocatImportance.put(user, userImpCounter);
        }

//        Per ogni utente prende la categoria più importante
        Clusters clusters = new Clusters();
        for (Map.Entry<UserModel, Counter<String>> entry : usersTocatImportance.entrySet()) {
            clusters.addUser(entry.getKey(), entry.getValue().mostCommon());
        }
        return clusters;
    }
}
