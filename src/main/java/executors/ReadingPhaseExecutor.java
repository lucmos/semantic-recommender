package executors;

import babelnet.WikiPageMapping;
import clusters.ClusterGenerator;
import clusters.Clusters;
import constants.DatasetName;
import datasetsreader.Dataset;

import io.Cache;
import io.Utils;


@Deprecated
public class ReadingPhaseExecutor {

    public static void main(String[] args) throws Utils.CacheNotPresent {

//        for (DatasetName name : DatasetName.values()) {
//           Chrono c = new Chrono("Reading... " + name);
//
////            Dataset d = DatasetReader.readDataset(name, Dimension.COMPLETE);
////            Cache.write(name, d);
//
////            Dataset d = Cache.DatasetCache.read(name, Dimension.SMALL);
//            c.millis();
//        }
        //

        Dataset d = Cache.DatasetCache.read(DatasetName.WIKIMID);
        Clusters c = Cache.ClustersWikiMidCache.readCategories();
        System.out.println(c.stats(d, "BNCAT:EN:Swedish_DJs", 10));

        System.out.println(
        );
        System.out.println(c.getCluster("37962832"));
//        WikiPageMapping w = Cache.WikiMappingCache.read();
//        System.out.println(w.stats());
        //
        System.out.println(c.clusterInspection(d, "BNCAT:EN:Swedish_people_of_Italian_descent"));




//        WikiPageMapping d = WikiPageMapping.getInstance();
//        Clusters clu = new ClusterGenerator(d, w).loadCategoryClusters();
//        clu.stats(d, "")
//        System.out.println(clu.stats(d, "BNCAT:EN:Films_directed_by_Tony_Kaye_(director)"));
//        System.out.println(d);
//        System.out.println(w.stats(200));
//        System.out.println(d.stats(200));

//        d = Cache.WikiMappingCache.read(Dimension.COMPLETE);
//        System.out.println(d);
//        System.out.println(d.stats());
    }
}
