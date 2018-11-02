package constants;

import io.Config;

public enum ClustersPath {

    CLUSTERS("results/clusters_%s_%s_%s.json");

    private String path;

    ClustersPath(String path) {
        this.path = path;
    }

    /**
     * Return the path of a given object
     *
     * @return the path
     */
    public String getPath(Config.ClusterOver over, Config.ClusterMethod method, Config.Dimension dim) {
        return String.format(this.path, over.getName(), method.getName(), dim.getName());
    }
}
