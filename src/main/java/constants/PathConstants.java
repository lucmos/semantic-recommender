package constants;

/**
 * This enum gathers the paths of the objects that are generated.
 */
public enum PathConstants {

    SAVED_OBJECT("saved/object.sav");

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
