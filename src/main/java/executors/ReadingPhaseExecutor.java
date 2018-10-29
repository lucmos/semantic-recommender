package executors;

import constants.DatasetName;
import constants.Dimension;
import datasetsreader.Dataset;
import io.Cache;
import io.Utils;

@Deprecated
public class ReadingPhaseExecutor {

    public static void main(String[] args) throws Utils.CacheNotPresent {

        long start = System.currentTimeMillis();
        for (DatasetName name : DatasetName.values()) {
            System.out.println("Reading... " + name);

//            Dataset d = DatasetReader.readDataset(name, Dimension.COMPLETE);
//            Cache.writeToCache(name, d);

            Dataset d = Cache.DatasetCache.readFromCache(name, Dimension.COMPLETE);

            System.out.println(d);
            long now = System.currentTimeMillis();
            System.out.println("In: " + (now - start) / 1000.0);
            start = now;

            System.out.println();
        }
    }
}
