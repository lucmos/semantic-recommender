package executors;

import constants.DatasetName;
import io.Cache;
import io.Utils;
import model.clusters.Clusters;
import model.twitter.Dataset;


@Deprecated
public class ReadingPhaseExecutor {

    public static void main(String[] args) throws Utils.CacheNotPresent {


        Dataset d = Cache.DatasetCache.read(DatasetName.WIKIMID);
//        System.out.println(d.report());

        Clusters c = Cache.ClustersWikiMidCache.read();
        System.out.println(c.report(d));
//        ClusterFactory cf = new ClusterFactory(c);
////        System.out.println(ClustersMeter.cosineSimilarity(map.get((UserModel) c.getUsersToCluster().keySet().toArray()[0]), map.get((UserModel) c.getUsersToCluster().keySet().toArray()[1])));
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
