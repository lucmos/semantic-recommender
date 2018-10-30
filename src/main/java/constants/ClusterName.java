package constants;

import properties.Config;

public enum ClusterName {

    CLUSTERS_CAT("results/clusters_cat_%s_%s.json"),
    CLUSTERS_DOM("results/clusters_dom_%s_%s.json");

    private String path;

    ClusterName(String path) {
        this.path = path;
    }

    /**
     * Return the path of a given object
     *
     * @return the path
     */
    public String getPath(Config.Dimension dim, Config.ClusterType type) {
        return String.format(this.path, dim.getName(), type.getName());
    }
}
