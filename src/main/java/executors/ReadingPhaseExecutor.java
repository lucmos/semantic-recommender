package executors;

import clusterization.ClustersMeter;
import constants.DatasetName;
import io.Cache;
import io.Utils;
import model.twitter.BabelCategoryModel;
import model.twitter.Dataset;


@Deprecated
public class ReadingPhaseExecutor {

    //<<<<<<< Updated upstream
    public static void main(String[] args) throws Utils.CacheNotPresent {


//        Counter<Integer> s1 = new Counter<>();
//        Counter<Integer> s2 = new Counter<>();
//
//        for (int a = 0; a < 100000; a = a + 2) {
//            s1.increment(a);
//        }
//
//        for (int a = 1; a < 100; a++) {
//            s2.increment(a);
//        }

//        System.out.println(s1);
//        System.out.println(s2);
//        double a = 0;
//        int times = 1;
//        Chrono c = new Chrono(String.format("Computing similarity %s times", times));
//        for(int i = 0; i< times; i++)
//            a = ClustersMeter.cosineSimilarity(s1, s2);
//        c.millis();
//        System.out.println(a);
        Dataset d = Cache.DatasetCache.read(DatasetName.WIKIMID);
        System.out.println(d.report());
    }
}
//        TwitterFactory f = new TwitterFactory(d);
//
//        UserModel u1 = f.getUser(1591641);
//        UserModel u2 = f.getUser(718480);
//        UserModel u3 = f.getUser(1265112);
//
//        System.out.println();
//        Counter<BabelCategoryModel> c1 = u1.getTweetsCategories(d);
//        Counter<BabelCategoryModel> c2 = u2.getTweetsCategories(d);
//        Counter<BabelCategoryModel> c3 = u3.getTweetsCategories(d);
//
//        System.out.println(c1);
//        System.out.println(c2);
//        System.out.println(c3);
//
//        double a = ClustersMeter.cosineSimilarity(c1, c2);
//        double b = ClustersMeter.cosineSimilarity(c1, c3);
//        double c = ClustersMeter.cosineSimilarity(c2, c3);
//        System.out.println(a);
//        System.out.println(b);
//        System.out.println(c);
//        System.out.println(d.report());

//        Clusters c = Cache.ClustersWikiMidCache.read();
//        System.out.println(c.report(d));
//        ClusterFactory cf = new ClusterFactory(c);
//        System.out.println(ClustersMeter.cosineSimilarity(u1.getTweetsCategories());
//////
////
//        System.out.println(c.report(d, cf.getCluster("BNCAT:EN:Swedish_DJs"), 10));
//        System.out.println(d.tweeetStats());
        ////
////        System.out.println(
////        );
////        System.out.println(c.getCluster("37962832"));
//        BabelnetInterface w = Cache.WikiMappingCache.read();
////        System.out.println(w.clustersStats());
//        //
////        System.out.println(c.clusterInspection(d, "BNCAT:EN:Swedish_people_of_Italian_descent"));
//
//        ClustersMeter cm = new ClustersMeter(d, c, w.getPagesToCategories());
//
//        Chrono cr = new Chrono();
//        System.out.println(cm.clustersJaccardSimilarity("BNCAT:EN:Musical_groups_from_Detroit,_Michigan", "BNCAT:EN:Musical_groups_from_Detroit,_Michigan"));
//        System.out.println(cm.clustersJaccardSimilarity("BNCAT:EN:Musical_groups_from_Detroit,_Michigan", "BNCAT:EN:Song_recordings_produced_by_Muff_Winwood"));
//        System.out.println(cm.clustersJaccardSimilarity("BNCAT:EN:English_pop_music_groups", "BNCAT:EN:People_from_Mableton,_Georgia"));
//        System.out.println(cm.clustersJaccardSimilarity("BNCAT:EN:Living_people", "BNCAT:EN:People_from_Mableton,_Georgia"));
//        cr.millis();
//        BabelnetInterface d = BabelnetInterface.getInstance();
//        Clusters clu = new ClusterGenerator(d, w).loadCategoryClusters();
//        clu.clustersStats(d, "")
//        System.out.println(clu.clustersStats(d, "BNCAT:EN:Films_directed_by_Tony_Kaye_(director)"));
//        System.out.println(d);
//        System.out.println(w.clustersStats(200));
//        System.out.println(d.clustersStats(200));

//        d = Cache.WikiMappingCache.read(Dimension.COMPLETE);
//        System.out.println(d);
//        System.out.println(d.clustersStats());
//=======
//    public Dataset execute(DatasetName dn, Dimension dim)
//    {
//        System.out.println("Reading... " + dn);
//
//        Dataset d = CacheManager.readFromCache(dn, Dimension.SMALL);
//        DatasetReader dr = new DatasetReader();
//        Dataset d = dr.readDataset(dn, dim);
//
//        System.out.println(d);
//        System.out.println();
//        return d;
//>>>>>>> Stashed changes
//    }
//    public static void main(String[] args) {
//
//        long start = System.currentTimeMillis();
//        for (DatasetName name : DatasetName.values()) {
//            System.out.println("Reading... " + name);
//
////            Dataset d = DatasetReader.readDataset(name, Dimension.COMPLETE);
////            CacheManager.writeToCache(name, d);
//
//          Dataset d = CacheManager.readFromCache(name, Dimension.COMPLETE);
//
//            System.out.println(d);
//            long now = System.currentTimeMillis();
//            System.out.println("In: " + (now - start)/ 1000.0);
//            start = now;
//
//            System.out.println();
//        }
//    }
//}
