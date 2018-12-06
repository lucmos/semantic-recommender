package constants;

import io.Config;

public enum DatasetName {
    UNION("datasets/bin/union_%s.bin"),
    WIKIMID("datasets/bin/wikimid_%s.bin"),
    S21("datasets/bin/S21_%s.bin"),
    S22("datasets/bin/S22_preferences_%s.bin"),
    S23("datasets/bin/S23_%s.bin");

    private String binPath;
    DatasetName(String binPath) {
        this.binPath = binPath;
    }

    public String getBinPath(Config.Dimension dim) {
        return String.format(binPath, dim.getName());
    }
}




