package executors;

import clusterization.ClustersMeter;
import constants.DatasetName;
import io.Cache;
import io.Config;
import io.Utils;
import model.clusters.Clusters;
import model.twitter.Dataset;
import model.twitter.TwitterFactory;
import model.twitter.UserModel;
import utils.Chrono;
import utils.Counter;


@Deprecated
public class ReadingPhaseExecutor {

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
        TwitterFactory f = new TwitterFactory(d);

        UserModel u1 = f.getUser(2394493);
        UserModel u2 = f.getUser(1775295);
        UserModel u3 = f.getUser(937894);

        System.out.println();
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
    }
}
