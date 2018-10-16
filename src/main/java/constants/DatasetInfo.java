package constants;

/**
 * This enum gathers the relative paths of the datasets
 */
public enum DatasetInfo {

    WIKIMID_FRIEND_BASED_DATASET(DatasetName.WIKIMID,
            "datasets/EN/friend-based_dataset.tsv",
            DatasetType.FRIENDBASED_DATASET),

    WIKIMID_FRIEND_BASED_INTEREST_INFO(DatasetName.WIKIMID,
            "datasets/EN/friend-based_interest_info.tsv",
            DatasetType.FRIENDBASED_INTEREST),

    WIKIMID_MESSAGE_BASED_DATASET(DatasetName.WIKIMID,
            "datasets/EN/message-based_dataset.tsv",
            DatasetType.MESSAGEBASED_DATASET),

    WIKIMID_MESSAGE_BASED_INTEREST_INFO(DatasetName.WIKIMID,
            "datasets/EN/message-based_interest_info.tsv",
            DatasetType.MESSAGEBASED_INTEREST),

    S21_DATASET(DatasetName.S21,
            "datasets/S21.tsv",
            DatasetType.S21),
    S22_DATASET(DatasetName.S22,
            "datasets/S22_preferences.tsv",
            DatasetType.S22_S23),
    S23_DATASET(DatasetName.S23,
            "datasets/S23.tsv",
            DatasetType.S22_S23);

    private DatasetName name;
    private DatasetType type;
    private String path;

    DatasetInfo(DatasetName name, String path, DatasetType type) {
        this.name = name;
        this.path = path;
        this.type = type;
    }

    /**
     * Return the path of a given object
     *
     * @return the path
     */
    public String getPath() {
        return this.path;
    }

    public DatasetName getName() {
        return name;
    }

    public DatasetType getType() {return type;}
}
