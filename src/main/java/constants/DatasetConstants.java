package constants;

/**
 * This enum gathers the relative paths of the datasets
 */
public enum DatasetConstants {
    WIKIMID_FRIEND_BASED_DATASET("datasets/EN/friend-based_dataset.tsv"),
    WIKIMID_FRIEND_BASED_INTEREST_INFO("datasets/EN/friend-based_interest_info.tsv"),
    WIKIMID_MESSAGE_BASED_DATASET("datasets/EN/message-based_dataset.tsv"),
    WIKIMID_MESSAGE_BASED_INTEREST_INFO("datasets/EN/message-based_interest_info.tsv"),

    S21_DATASET("datasets/S21.tsv"),
    S22_DATASET("datasets/S22_preferences.tsv"),
    S23_DATASET("datasets/S23.tsv");

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
