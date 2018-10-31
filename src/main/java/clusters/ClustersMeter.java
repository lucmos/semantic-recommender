package clusters;

import com.google.common.util.concurrent.AtomicDouble;
import twittermodel.Dataset;
import twittermodel.UserModel;
import utils.Counter;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class ClustersMeter {

    private final Clusters clusters;
    private final Dataset dataset;
    private final Map<UserModel, Counter<String>> userToCat;

    public ClustersMeter(Dataset dataset, Clusters clusters, Map<Integer, Set<String>> pageToCat) {
        this.dataset = dataset;
        this.clusters = clusters;
        this.userToCat = ClustersUtils.getUserToCatCounter(dataset, pageToCat);
    }

    public float usersJaccardSimilarity(Integer u1, Integer u2) {
        return usersJaccardSimilarity(dataset.getUser(u1), dataset.getUser(u2));
    }

    public float usersJaccardSimilarity(UserModel u1, UserModel u2) {
//        assert userToCat.containsKey(u1);
//        assert userToCat.containsKey(u2);

        Set<String> catU1 = userToCat.get(u1).getMap().keySet();
        Set<String> catU2 = userToCat.get(u2).getMap().keySet();

        int u = unionSize(catU1, catU2);
        return u == 0 ? 0 : intersectionSize(catU1, catU2) / (float) u;
    }

    public float clustersJaccardSimilarity(String c1, String c2) {
        Set<Integer> cluster1 = clusters.getUsers(c1);
        Set<Integer> cluster2 = clusters.getUsers(c2);

        AtomicDouble sim = new AtomicDouble();
        AtomicInteger i = new AtomicInteger();

        cluster1.stream().parallel().forEach(x1 -> cluster2.forEach(x2 -> {
            if (!x1.equals(x2)) { // se sono esattamente gli stessi no!  accade se c1=c2
                sim.addAndGet(usersJaccardSimilarity(x1, x2));
                i.incrementAndGet();
            }
        }));
        return sim.floatValue() / i.intValue();
    }

    public static <T> int unionSize(Set<T> s1, Set<T> s2) {
        int s1size = s1.size();
        int s2size = s2.size();
        int u;

        if (s1size > s2size) {
            u = s1size;
            for (T e2 : s2) if (!s1.contains(e2)) u++;
        } else {
            u = s2size;
            for (T e1 : s1) if (!s2.contains(e1)) u++;
        }
        return u;
    }

    public static <T> int intersectionSize(Set<T> s1, Set<T> s2) {
        int i = 0;
        for (T e1 : s1) {
            if (s2.contains(e1)) i++;
        }
        return i;
    }


    public static <T> Set<T> union(Set<T> s1, Set<T> s2) {
        Set<T> u = new HashSet<>(s1);
        u.addAll(s2);
        return u;
    }

    public static <T> Set<T> intersection(Set<T> s1, Set<T> s2) {
        Set<T> u = new HashSet<>(s1);
        u.retainAll(s2);
        return u;
    }
}
