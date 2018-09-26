package constants;

/**
 * This enum gathers the relative paths of the datasets
 */
public enum DatasetConstants {
    WIKIMID_FRIEND_BASED_DATASET("EN/friend-based_dataset.tsv"),
    WIKIMID_FRIEND_BASED_INTEREST_INFO("EN/friend-based_interest_info.tsv"),
    WIKIMID_MESSAGE_BASED_DATASET("EN/message-based_dataset.tsv"),
    WIKIMID_MESSAGE_BASED_INTEREST_INFO("EN/message-based_interest_info.tsv"),

    S21_DATASET("S21.tsv"),
    S22_DATASET("S22_preferences.tsv"),
    S23_DATASET("S23.tsv");

    private String path;

    DatasetConstants(String path) {
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
