package executors;

import babelnet.BabelnetInterface;
import constants.DatasetName;
import io.Cache;
import io.Utils;
import model.twitter.Dataset;
import model.twitter.WikiPageModel;

public class TEST {
    public static void main(String[] args) throws Utils.CacheNotPresent {
        Dataset d = Cache.DatasetCache.read(DatasetName.S22);

        System.out.println(d.report());

        int total = 0;
        int counter = 0;
        for (WikiPageModel m : d.getWikiPages().values()) {
            String s = BabelnetInterface.getSynset(m);
            if (s == null) {
                counter++;
            }
            else{
                total++;
            }
        }
        System.out.println(counter
        );
        System.out.println(String.format("Synset torvato: %s", total));
    }
}
