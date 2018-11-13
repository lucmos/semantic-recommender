package clusterization;

import de.lmu.ifi.dbs.elki.algorithm.clustering.kmeans.KMeans;
import de.lmu.ifi.dbs.elki.algorithm.clustering.kmeans.KMeansLloyd;
import de.lmu.ifi.dbs.elki.algorithm.clustering.kmeans.initialization.RandomlyGeneratedInitialMeans;
import de.lmu.ifi.dbs.elki.math.random.RandomFactory;
import io.Config;
import model.twitter.Dataset;

public class ApacheClusterGenerator {

    private final Config config;
    private final Dataset dataset;

    public ApacheClusterGenerator(Dataset dataset) {
        this.config = Config.getInstance();
        this.dataset = dataset;
    }

    public void clusterize() {
//        RandomlyGeneratedInitialMeans init = new RandomlyGeneratedInitialMeans(RandomFactory.DEFAULT);
//        KMeansLloyd<UserClusterable> a = new KMeansLloyd<>(2, 0, init);
//        a.run()
////
//        new Database
    }


    public Config getConfig() {
        return config;
    }

    public Dataset getDataset() {
        return dataset;
    }
}
