package constants;

/**
 * This enum gathers the paths of the objects that are generated.
 */
public enum PathConstants {

    WIKIPAGE_TO_CATDOM("dataset/bin/wikipage_to_catdom.bin");

    private String path;

    PathConstants(String path) {
        this.path = path;
    }

    /**
     * Return the path of a given object
     *
     * @return the path
     */
    public String getPath() {
        return this.path;
    }
}
