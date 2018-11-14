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
import java.util.stream.Collectors;

public class JavaExport implements IndexedSerializable {
    Set<String> all_users;
    Set<String> all_pages;
    Set<String> all_catdom;

    Map<String, Set<String>> pages2catdom;
    Map<String, Set<String>> user2personalPage_catdom;

    Map<String, Set<String>> user2likedItems_wikipage;
    Map<String, Set<String>> user2followOut;

    Map<String, Map<String, Double>> user2tweet_catdom_counter;

    public JavaExport(Dataset dataset) {
        Chrono c = new Chrono(String.format("Computing export: %s, %s...", dataset.getName(), dataset.getDimension()));

        all_users = dataset.getUsers().values().stream().map(x -> x.getName(dataset)).collect(Collectors.toSet());
        all_pages = dataset.getWikiPages().values().stream().map(x -> x.getName(dataset)).collect(Collectors.toSet());
        all_catdom = ClustersUtils.getCategories(dataset);

        this.fill_pages2catdom(dataset);
        this.fill_user2personal(dataset);
        this.fill_user2tweet(dataset);
        this.fill_user2items(dataset);
        this.fill_user2user(dataset);

        c.millis(Chrono.defaultFinalMessage + String.format(", catdom: %s", all_catdom.size()));
    }

    private void fill_pages2catdom(Dataset dataset) {
        pages2catdom = new HashMap<>();
        dataset.getWikiPages().values().forEach(x ->
        {
            Set<String> s = ClustersUtils.getCategories(dataset, x);
            if (!s.isEmpty()) {
                pages2catdom.put(x.getName(dataset), s);
            }
        });
    }

    private void fill_user2personal(Dataset dataset) {
        user2personalPage_catdom = new HashMap<>();
        dataset.getUsers().values().forEach(x -> {
            if (x.isFamous()) {
                user2personalPage_catdom.put(x.getName(dataset),
                        ClustersUtils.getCategories(dataset, x.getWikiPageAboutUserModel(dataset)));
            }
        });
    }

    private void fill_user2items(Dataset dataset){
        user2likedItems_wikipage = new HashMap<>();
        dataset.getUsers().values().forEach(x -> {
            if (!x.getWikiPagesOfLikedItemsIds().isEmpty()) {
                user2likedItems_wikipage.put(x.getName(dataset), x.getWikiPagesOfLikedItemdsModel(dataset).stream().map(y -> y.getName(dataset)).collect(Collectors.toSet()));
            }
        });
    }

    private void fill_user2user(Dataset dataset) {
        user2followOut = new HashMap<>();
        dataset.getUsers().values().forEach(x ->{
            if (!x.getFollowOutIds().isEmpty()) {
                user2followOut.put(x.getName(dataset), x.getFollowOutUserModels(dataset).stream().map(y -> y.getName(dataset)).collect(Collectors.toSet()));
            }
        });
    }

    private void fill_user2tweet(Dataset dataset) {
        Map<UserModel, Counter<String>> map = ClustersUtils.getUserToCatCounter(dataset);

        user2tweet_catdom_counter = new HashMap<>();
        for (UserModel user : map.keySet()) {
            Counter<String> s = map.get(user);
            if (!s.getMap().isEmpty()) {
                user2tweet_catdom_counter.put(user.getName(dataset), s.getMap());
            }
        }
    }

    public static void main(String[] args) throws Utils.CacheNotPresent {
        new JavaExport(Cache.DatasetCache.read(DatasetName.WIKIMID));
    }
}
