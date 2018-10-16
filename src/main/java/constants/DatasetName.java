package constants;

public enum DatasetName {
    WIKIMID("datasets/bin/wikimid.bin"),
    S21("datasets/bin/S21.bin"),
    S22("datasets/bin/S22_preferences.bin"),
    S23("datasets/bin/S23.bin");

    private String binPath;

    DatasetName(String binPath) {
        this.binPath = binPath;
    }

    public String getBinPath() {
        return binPath;
    }
}




