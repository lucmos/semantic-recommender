package constants;

import datasetsreader.Dataset;

/**
 * This enum gathers the relative paths of the datasets
 */
public enum DatasetConstants {

    WIKIMID_FRIEND_BASED_DATASET("wikimid",
            "datasets/EN/friend-based_dataset.tsv",
            "datasets/bin/friend-based_dataset.bin",
            DatasetType.FRIENDBASED_DATASET),

    WIKIMID_FRIEND_BASED_INTEREST_INFO("wikimid",
            "datasets/EN/friend-based_interest_info.tsv",
            "datasets/bin/friend-based_interest_info.bin",
            DatasetType.FRIENDBASED_INTEREST),

    WIKIMID_MESSAGE_BASED_DATASET("wikimid",
            "datasets/EN/message-based_dataset.tsv",
            "datasets/bin/message-based_dataset.bin",
            DatasetType.MESSAGEBASED_DATASET),

    WIKIMID_MESSAGE_BASED_INTEREST_INFO("wikimid",
            "datasets/EN/message-based_interest_info.tsv",
            "datasets/bin/message-based_interest_info.bin",
            DatasetType.MESSAGEBASED_INTEREST),

    S21_DATASET("s21",
            "datasets/S21.tsv",
            "datasets/bin/S21.bin",
            DatasetType.S21),
    S22_DATASET("s22",
            "datasets/S22_preferences.tsv",
            "datasets/bin/S22_preferences.bin",
            DatasetType.S22_S23),
    S23_DATASET("s23",
            "datasets/S23.tsv",
            "datasets/bin/S23.bin",
            DatasetType.S22_S23);

    private String name;
    private DatasetType type;
    private String path;
    private String binPath;

    DatasetConstants(String name, String path, String binPath, DatasetType type) {
        this.name = name;
        this.path = path;
        this.binPath = binPath;
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

    public String getName() {
        return name;
    }

    public DatasetType getType() {return type;}

    public String getBinPath() {return binPath;}
}
