package executors;

import constants.DatasetInfo;
import constants.DatasetName;
import constants.Dimension;
import datasetsreader.Dataset;
import datasetsreader.DatasetReader;
import io.CacheManager;

import javax.rmi.CORBA.Util;
import javax.xml.crypto.Data;

public class ReadingPhaseExecutor {
    public static void main(String[] args) {

        long start = System.currentTimeMillis();
        for (DatasetName name : DatasetName.values()) {
            System.out.println("Reading... " + name);

//            Dataset d = DatasetReader.readDataset(name, Dimension.COMPLETE);
//            CacheManager.writeToCache(name, d);

          Dataset d = CacheManager.readFromCache(name, Dimension.COMPLETE);

            System.out.println(d);
            long now = System.currentTimeMillis();
            System.out.println("In: " + (now - start)/ 1000.0);
            start = now;

            System.out.println();
        }
    }
}
