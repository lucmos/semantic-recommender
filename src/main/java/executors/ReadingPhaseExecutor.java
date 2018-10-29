package executors;

import babelnet.WikiPageMapping;
import constants.DatasetName;
import datasetsreader.Dataset;

import datasetsreader.DatasetReader;
import io.Cache;
import io.Utils;


@Deprecated
public class ReadingPhaseExecutor {

    public static void main(String[] args) throws Utils.CacheNotPresent {

//        for (DatasetName name : DatasetName.values()) {
//           Chrono c = new Chrono("Reading... " + name);
//
////            Dataset d = DatasetReader.readDataset(name, Dimension.COMPLETE);
////            Cache.writeToCache(name, d);
//
////            Dataset d = Cache.DatasetCache.readFromCache(name, Dimension.SMALL);
//            c.millis();
//        }


        WikiPageMapping d = Cache.WikiMappingCache.readFromCache();
//        WikiPageMapping d = WikiPageMapping.getInstance();
        System.out.println(d);
        System.out.println(d.stats(200));

//        d = Cache.WikiMappingCache.readFromCache(Dimension.COMPLETE);
//        System.out.println(d);
//        System.out.println(d.stats());
    }
}
