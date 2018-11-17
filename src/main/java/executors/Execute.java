package executors;

import clusterization.JavaExport;
import io.Cache;
import io.Utils;

public class Execute {
    public static void main(String[] args) throws Utils.CacheNotPresent {
//      Must be present the twitter downloaded information! See TwitterRespPath
        Cache.DatasetCache.regenCache();
        DatasetMerger.unionAndSave();
        Cache.JavaExportCache.export();
    }
}
