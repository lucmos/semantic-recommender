package clusterization;

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
//        
    }


    public Config getConfig() {
        return config;
    }

    public Dataset getDataset() {
        return dataset;
    }
}
