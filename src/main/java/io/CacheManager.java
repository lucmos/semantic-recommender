package io;

import constants.DatasetName;
import datasetsreader.Dataset;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CacheManager {

    // TODO: 19/10/18 In utilizzo il dataset ridotto!!!
    public static String current_revision = "19-10_10-46";

    public static void writeToCache(DatasetName datasetName, Dataset dataset) {
        System.out.println("Writing");
        System.out.println(datasetName.getBinPath(Utils.timestamp));
        Utils.save(dataset, datasetName.getBinPath(Utils.timestamp));
    }

    public static Dataset readFromCache(DatasetName datasetName) {
        File file = new File(datasetName.getBinPath(current_revision));
        if (!file.exists()) {
            throw new RuntimeException("File cache not present: " + file.getPath());
        }
        return Utils.restore(datasetName.getBinPath(current_revision));
    }
}
