package clusterization;

import io.Config;
import model.clusters.ClusterFactory;
import model.twitter.Dataset;
import model.twitter.UserModel;
import model.clusters.Clusters;
import model.twitter.WikiPageModel;
import utils.Chrono;
import utils.Counter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ClusterGenerator {

    private final Config config;
    private final Dataset dataset;


    public ClusterGenerator(Dataset dataset) {
        this.config = Config.getInstance();
        this.dataset = dataset;
    }

    public Clusters generate() {

        ClusterFactory clusterFactory = new ClusterFactory(new Clusters());
        clusterize(clusterFactory);

        return clusterFactory.getClusters();
    }

    private void clusterize(ClusterFactory clusterFactory) {
        Chrono c = new Chrono("Generating " + config.clusterOver() + " clusters...");

        Map<UserModel, Counter<String>> userTocatCounter = ClustersUtils.getUserToCatCounter(dataset);

        switch (config.clusterMethod()) {
            case MOST_COMMON:
                mostCommonCategoryClusterization(clusterFactory, userTocatCounter);
                c.millis();
                return;

            case TF_IDF:
                mostCommonCategoryClusterization(clusterFactory, getUserToCounterTFIDF(userTocatCounter));
                c.millis();
                return;
        }
        throw new RuntimeException("Error while creating clusters_index.");
    }

    /**
     * Genera i clusters_index prendendo per ogni utente prende la categoria più comunte/importante
     */
    private void mostCommonCategoryClusterization(ClusterFactory clusterFactory, Map<UserModel, Counter<String>> userToCounter) {
        for (Map.Entry<UserModel, Counter<String>> entry : userToCounter.entrySet()) {
            clusterFactory.addUser(entry.getKey(), clusterFactory.getCluster(entry.getValue().mostCommon()));
        }
    }

    /**
     * Genera un counter in cui per ogni utentet associa il tf.idf di ogni sua categoria
     * @param userToCounter
     * @return
     */
    private Map<UserModel, Counter<String>> getUserToCounterTFIDF(Map<UserModel, Counter<String>> userToCounter) {

        Set<String> cat = ClustersUtils.getCategories(dataset);
        Counter<String> catCounter = Counter.fromCollection(cat);

        HashMap<UserModel, Counter<String>> userToCounterTFIDF = new HashMap<>();

//      numero documenti
        double documents = cat.size();

//      calcolo del document frequency (ogni categoria in quanti documenti è?)
        Counter<String> documentFreq = new Counter<>();
        for (WikiPageModel page : dataset.getWikiPages().values()) {
            for (String term : ClustersUtils.getCategories(dataset, page)) {
                documentFreq.increment(term);
            }
        }

//      calcolo idf
        HashMap<String, Double> idfCache = new HashMap<>();
        cat.forEach(i -> idfCache.put(i, Math.log(documents / documentFreq.countDouble(i))));

//        Associa ad ogni utente le categire che gli piacciono associate ad una misura di importanza basata sul tf.idf
        for (Map.Entry<UserModel, Counter<String>> entry : userToCounter.entrySet()) {
            UserModel user = entry.getKey();

            Counter<String> userCatCounter = entry.getValue();
            Counter<String> userImpCounter = new Counter<>();

            for (Map.Entry<String, Double> e : userCatCounter.getMap().entrySet()) {
                String categ = e.getKey();
                double tf = userCatCounter.countDouble(categ) / userCatCounter.totalDouble();
                double idf = idfCache.get(categ);
                userImpCounter.set(categ, tf * idf);
//                System.out.println(BabelInfo.format("t: %s, tot: %s\n tf: %s\n idf: %s \n tf*idf: %s",
//                        userCatCounter.count(cat), userCatCounter.totalDouble(),
//                        tf, idf, tf * idf));
            }

            userToCounterTFIDF.put(user, userImpCounter);
        }
        return userToCounterTFIDF;
    }
}
