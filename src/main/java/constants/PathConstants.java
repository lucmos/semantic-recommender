package constants;

public enum PathConstants {

    SAVED_OBJECT("saved/object.sav");

    private String path;

    PathConstants(String path) {
        this.path = path;
    }

    public String getPath() {
        return this.path;
    }
}
