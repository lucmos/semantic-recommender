package io;

import constants.DatasetInfo;
import constants.DatasetName;
import datasetsreader.Dataset;
import datasetsreader.DatasetReader;

import javax.rmi.CORBA.Util;

public class CacheManager {

    public static void writeToCache(DatasetName datasetName) {
        Dataset dataset = DatasetReader.readDataset(datasetName);
        Utils.save(dataset, datasetName.getBinPath());
//        DatasetReader.readDataset(); // TODO: 16/10/18 finisci
    }

    public static Dataset readFromCache(DatasetName datasetName) {
        return Utils.restore(datasetName.getBinPath());
    }
}
