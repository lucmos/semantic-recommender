package clusters;

import babelnet.WikiPageMapping;
import constants.ClusterName;
import twittermodel.Dataset;
import io.Utils;
import properties.Config;
import twittermodel.UserModel;
import utils.Chrono;
import utils.Counter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ClusterGenerator {

    private final Config config;
    private final Dataset dataset;
    private final WikiPageMapping wikiMap;


    public ClusterGenerator(Dataset dataset, WikiPageMapping wikiMap) {
        this.config = Config.getInstance();
        this.dataset = dataset;
        this.wikiMap = wikiMap;
    }

    // TODO: 31/10/18 decommenta 
//    public Clusters generateCategoryClusters() {
//        Chrono c = new Chrono("Generating category clusters...");
//
//        Clusters clusters = clusterize(this.wikiMap.getPagesToCategories());
//        Utils.savePrettyJson(clusters, ClusterName.CLUSTERS_CAT.getPath(config.dimension(), config.clusterType()));
//
//        c.millis();
//        return clusters;
//    }
//
//    public Clusters generateDomainClusters() {
//        Chrono c = new Chrono("Generating domain clusters...");
//
//        Clusters clusters = clusterize(this.wikiMap.getPagesToDomain());
//        Utils.savePrettyJson(clusters, ClusterName.CLUSTERS_DOM.getPath(config.dimension(), config.clusterType()));
//
//        c.millis();
//        return clusters;
//    }

    private Clusters clusterize(Map<Integer, Set<String>> pageToCat) {
        Map<UserModel, Counter<String>> userTocatCounter = ClustersUtils.getUserToCatCounter(dataset, pageToCat);
        switch (config.clusterType()) {
            case MOST_COMMON:
                return generateCluster(userTocatCounter);

            case TF_IDF:
                return generateCluster(getUserToCounterTFIDF(pageToCat, userTocatCounter));


        }
        throw new RuntimeException("Error while creating clusters.");
    }

    /**
     * Genera i clusters prendendo per ogni utente prende la categoria più comunte/importante
     */
    private Clusters generateCluster(Map<UserModel, Counter<String>> userToCounter) {
        Clusters clusters = new Clusters();
        for (Map.Entry<UserModel, Counter<String>> entry : userToCounter.entrySet()) {
            clusters.addUser(entry.getKey(), entry.getValue().mostCommon());
        }
        return clusters;
    }

    /**
     * Genera un counter in cui per ogni utentet associa il tf.idf di ogni sua categoria
     * @param userToCounter
     * @return
     */
    private Map<UserModel, Counter<String>> getUserToCounterTFIDF(Map<Integer, Set<String>> pageToCat, Map<UserModel, Counter<String>> userToCounter) {

        Counter<String> categories = Counter.fromMultiMap(pageToCat);

        HashMap<UserModel, Counter<String>> userToCounterTFIDF = new HashMap<>();

//      numero documenti
        double documents = pageToCat.size();

//      calcolo del document frequency (ogni categoria in quanti documenti è?)
        Counter<String> documentFreq = new Counter<>();
        for (Set<String> doc : pageToCat.values()) {
            for (String term : doc) {
                documentFreq.increment(term);
            }
        }

//      calcolo idf
        HashMap<String, Double> idfCache = new HashMap<>();
        categories.getMap().keySet().forEach(cat -> idfCache.put(cat, Math.log(documents / documentFreq.countDouble(cat))));

//        Associa ad ogni utente le categire che gli piacciono associate ad una misura di importanza basata sul tf.idf
        for (Map.Entry<UserModel, Counter<String>> entry : userToCounter.entrySet()) {
            UserModel user = entry.getKey();

            Counter<String> userCatCounter = entry.getValue();
            Counter<String> userImpCounter = new Counter<>();

            for (Map.Entry<String, Double> e : userCatCounter.getMap().entrySet()) {
                String cat = e.getKey();
                double tf = userCatCounter.countDouble(cat) / userCatCounter.totalDouble();
                double idf = idfCache.get(cat);
                userImpCounter.set(cat, tf * idf);
//                System.out.println(String.format("t: %s, tot: %s\n tf: %s\n idf: %s \n tf*idf: %s",
//                        userCatCounter.count(cat), userCatCounter.totalDouble(),
//                        tf, idf, tf * idf));
            }

            userToCounterTFIDF.put(user, userImpCounter);
        }
        return userToCounterTFIDF;
    }
}
