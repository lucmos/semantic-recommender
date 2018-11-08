package clusterization;

import com.google.common.util.concurrent.AtomicDouble;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import model.clusters.Cluster;
import model.clusters.ClusterFactory;
import model.twitter.Dataset;
import model.twitter.UserModel;
import utils.Counter;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class ClustersMeter {

    private final Dataset dataset;
    private final Map<UserModel, Counter<String>> userToCat;

    public ClustersMeter(Dataset dataset) {
        this.dataset = dataset;
        this.userToCat = ClustersUtils.getUserToCatCounter(dataset);
    }

    public float usersJaccardSimilarity(int u1, int u2) {
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

//    public float clustersJaccardSimilarity(Cluster c1, Cluster c2) {
//        IntOpenHashSet cluster1 = c1.getUserIds();
//        IntOpenHashSet cluster2 = c2.getUserIds();
//
//        AtomicDouble sim = new AtomicDouble();
//        AtomicInteger i = new AtomicInteger();
//
//        cluster1.parallelStream().forEach(x1 -> cluster2.forEach(x -> {}));
//
//
////                x2 -> {
////            if (!x1.equals(x2)) { // se sono esattamente gli stessi no!  accade se c1=c2
////                sim.addAndGet(usersJaccardSimilarity(x1, x2));
////                i.incrementAndGet();
////            }
////        }));
//        return sim.floatValue() / i.intValue();
//    }

    public static <T> float jaccardSimilarity(Set<T> s1, Set<T> s2) {
        int u = unionSize(s1, s2);
        return u == 0 ? 0 : intersectionSize(s1, s2) / (float) u;
    }

    public static <T> double cosineSimilarity(Counter<T> entry1counter,
                                   Counter<T> entry2counter) {

        //Set<T> entry1categories = entry1.getValue().getMap().keySet();
        Set<T> entry2categories = entry2counter.getMap().keySet();

        double numeratore = 0;

        double squaredSumsEntry1 = 0; // Già che lo scorro una volta, ne approfitto e calcolo parte della norm
        double squaredSumsEntry2 = 0;
        for (T category : entry1counter.getMap().keySet()) {

            double entry1count = entry1counter.count(category);
            squaredSumsEntry1 += Math.pow(entry1count, 2);

            if (entry2categories.contains(category))
                numeratore += entry1count * entry2counter.count(category);
        }

        for (T category : entry2counter.getMap().keySet()) {
            double entry2count = entry2counter.count(category);
            squaredSumsEntry2 += Math.pow(entry2count, 2);
        }

        double denominatore = Math.sqrt(squaredSumsEntry1) * Math.sqrt(squaredSumsEntry2);

        return numeratore / denominatore;
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
        int s1size = s1.size();
        int s2size = s2.size();
        int i = 0;

        if (s1size > s2size) {
            for (T e2 : s2) {
                if (s1.contains(e2)) i++;
            }
        } else {
            for (T e1 : s1) {
                if (s2.contains(e1)) i++;
            }
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
