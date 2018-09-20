package constants;

public enum DatasetConstants {
    WIKIMID_FRIEND_BASED_DATASET("EN/friend-based_dataset.tsv"),
    WIKIMID_FRIEND_BASED_INTEREST_INFO("EN/friend-based_interest_info.tsv"),
    WIKIMID_MESSAGE_BASED_DATASET("/home/luca/IdeaProjects/WSIEProject/datasets/EN/message-based_dataset.tsv"),
    WIKIMID_MESSAGE_BASED_INTEREST_INFO("/home/luca/IdeaProjects/WSIEProject/datasets/EN/message-based_interest_info.tsv"),

    S21_DATASET("S21.tsv"),
    S22_DATASET("S22_preferences.tsv"),
    S23_DATASET("S23.tsv");


    private String path;

    DatasetConstants(String path) {
        this.path = path;
    }

    public String getPath() {
        return this.path;
    }
}
