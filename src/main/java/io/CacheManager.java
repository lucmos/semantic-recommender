package io;

import constants.DatasetConstants;
import datasetsreader.Dataset;
import datasetsreader.DatasetReader;

public class CacheManager {

    public static void WriteToCache(DatasetConstants datasetConstants) {
        Dataset dataset = new Dataset(datasetConstants.getName());
//        DatasetReader.readDataset(); // TODO: 16/10/18 finisci 
    }
}
