package clusterization;

import constants.DatasetName;
import io.Cache;
import io.IndexedSerializable;
import io.Utils;
import model.twitter.Dataset;
import model.twitter.UserModel;
import utils.Chrono;
import utils.Counter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class JavaExport implements IndexedSerializable {

    Map<String, Counter<String>> user2catdom_counter;
    Set<String> all_catdom;

    public JavaExport(Dataset dataset) {
        Chrono c = new Chrono(String.format("Computing export: %s, %s...", dataset.getName(), dataset.getDimension()));
        all_catdom = ClustersUtils.getCategories(dataset);

        Map<UserModel, Counter<String>> map = ClustersUtils.getUserToCatCounter(dataset);

        user2catdom_counter = new HashMap<>();
        for (UserModel user : map.keySet()) {
            user2catdom_counter.put(user.getName(dataset), map.get(user));
        }
        c.millis(Chrono.defaultFinalMessage + String.format(", catdom: %s", all_catdom.size()));
    }

    public static void main(String[] args) throws Utils.CacheNotPresent {
        new JavaExport(Cache.DatasetCache.read(DatasetName.WIKIMID));
    }
}
