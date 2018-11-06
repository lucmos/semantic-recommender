package clusterization;

import io.Config;
import model.*;
import model.twitter.*;
import utils.Counter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ClustersUtils {
    private ClustersUtils() {
    }

    public static Set<String> getCategories(Dataset dataset, WikiPageModel page) {
        switch (Config.getInstance().clusterOver()) {
            case CATEGORIES:
                return page.getCategoriesModel(dataset).stream().map(x -> x.getName(dataset)).collect(Collectors.toSet());

            case DOMAINS:
                return page.getDomainsModel(dataset).stream().map(x -> x.getName(dataset)).collect(Collectors.toSet()); // TODO: 02/11/18 guarda tipi hash set qui, per sistemare il counter!

            default:
                throw new RuntimeException("Invalid clusterization.");
        }
    }

    public static Set<String> getCategories(Dataset dataset) {
        switch (Config.getInstance().clusterOver()) {
            case CATEGORIES:
                return dataset.getBabelCategories().values().stream().map(x -> x.getName(dataset)).collect(Collectors.toSet());

            case DOMAINS:
                return dataset.getBabelDomains().values().stream().map(x -> x.getName(dataset)).collect(Collectors.toSet()); // TODO: 02/11/18 guarda tipi hash set qui, per sistemare il counter!

            default:
                throw new RuntimeException("Invalid clusterization.");
        }
    }

    public static Counter<String> getUserToCatCounter(Dataset datasaet, UserModel user) {
        Counter<String> stringCounter = new Counter<>();

        switch (Config.getInstance().clusterOver()) {
            case CATEGORIES:
                Counter<BabelCategoryModel> catCounter = user.getTweetsCategories(datasaet);

                for (BabelCategoryModel b : catCounter.getMap().keySet()) {
                    stringCounter.increment(b.getName(datasaet));
                }
                return stringCounter;

            case DOMAINS:
                Counter<BabelDomainModel> domCounter = user.getTweetsDomains(datasaet);
                for (BabelDomainModel b : domCounter.getMap().keySet()) {
                    stringCounter.increment(b.getName(datasaet));
                }
                return stringCounter;

            default:
                throw new RuntimeException("Invalid clusterization.");
        }
    }

    /**
     * Associa ad ogni utente il counter delle categorie che gli piacciono
     */
    public static Map<UserModel, Counter<String>> getUserToCatCounter(Dataset dataset) {
        HashMap<UserModel, Counter<String>> userTocatCounter = new HashMap<>();

        for (UserModel user : dataset.getUsers().values()) {
            for (int tweetID : user.getTweetsIds()) {

                TweetModel tweet = user.getTweetModel(dataset, tweetID);
                WikiPageModel page = tweet.getWikiPageModel(dataset);

                Set<String> possibleClusters;

                switch (Config.getInstance().clusterOver()) {
                    case CATEGORIES: possibleClusters = page.getCategoriesModel(dataset).stream()
                                .map(x -> x.getName(dataset)).collect(Collectors.toSet());
                        break;

                    case DOMAINS: possibleClusters = page.getDomainsModel(dataset).stream()
                            .map(x -> x.getName(dataset)).collect(Collectors.toSet());
                        break;

                    default:
                        throw new RuntimeException("Invalid clusterization.");
                }

                if (possibleClusters.isEmpty()) {continue;} // TODO: 02/11/18 decidi se skippare l'utente o mettergli roba vuota
//                assert !possibleClusters.isEmpty();

                userTocatCounter.putIfAbsent(user, new Counter<>());
                for (String c : possibleClusters) {
                    userTocatCounter.get(user).increment(c);
                }
            }
        }

        return userTocatCounter;
    }
}
