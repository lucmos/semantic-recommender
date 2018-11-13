package clusterization;

import io.Config;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
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

@Deprecated
public class _ClusterGenerator {

    private final Config config;
    private final Dataset dataset;


    public _ClusterGenerator(Dataset dataset) {
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

        HashMap<UserModel, Counter<String>> userToCounterTFIDF = new HashMap<>();

//      numero documenti/utenti
        double documents = userToCounter.size();

//      calcolo del document frequency (ogni termine/categoria in quanti documenti/utenti è?)
        Counter<String> documentFreq = new Counter<>();
        for (UserModel u : userToCounter.keySet()) {
            for (String term : userToCounter.get(u).getMap().keySet()){
                documentFreq.increment(term);
            }
        }

//      calcolo idf
        HashMap<String, Double> idfCache = new HashMap<>();
        cat.forEach(i -> idfCache.put(i, Math.log(documents / documentFreq.countDouble(i))));

//      Associa ad ogni utente le categorie che gli piacciono associate ad una misura di importanza basata sul tf.idf
        for (Map.Entry<UserModel, Counter<String>> entry : userToCounter.entrySet()) {
            UserModel user = entry.getKey();

            Counter<String> userCatCounter = entry.getValue();
            Counter<String> userImpCounter = new Counter<>();

            for (Object2DoubleOpenHashMap.Entry<String> e : userCatCounter.getMap().object2DoubleEntrySet()) {
                String categ = e.getKey();
                double tf = userCatCounter.countDouble(categ) / userCatCounter.totalDouble();
                double idf = idfCache.get(categ);
//                System.out.println(user);
//                System.out.println(categ);
//                System.out.println(tf + " " +  userCatCounter.countDouble(categ) + " " +  userCatCounter.totalDouble());
//                System.out.println(idf);
//                System.out.println(tf * idf);
//                System.out.println();
                userImpCounter.set(categ, tf * idf);
            }

            userToCounterTFIDF.put(user, userImpCounter);
        }
        return userToCounterTFIDF;
    }
}
