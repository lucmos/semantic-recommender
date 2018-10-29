package constants;

/**
 * This enum gathers the paths of the objects that are generated.
 */
public enum PathConstants {

    WIKIPAGE_TO_BABELNET("datasets/bin/wikipage_to_babelnet_%s.bin"),
    CLUSTERS_CAT("results/clusters_cat_%s.json"),
    CLUSTERS_DOM("results/clusters_dom_%s.json");

    private String path;

    PathConstants(String path) {
        this.path = path;
    }

    /**
     * Return the path of a given object
     *
     * @return the path
     */
    public String getPath(Dimension dim) {
        return String.format(this.path, dim.getName());
    }
}
