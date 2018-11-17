package executors;

import clusterization.JavaExport;
import io.Cache;
import io.Utils;
import twitter4j.TwitterException;

public class Execute {
    public static void main(String[] args) throws Utils.CacheNotPresent, TwitterException, InterruptedException {
        Cache.DatasetCache.regenCache();
        DownloadTweetInformations.download(); // Downloading friends takes 2 days.
        TweetsDisambiguator.disambiguate();
        DatasetMerger.unionAndSave();
        Cache.JavaExportCache.export();
    }
}
