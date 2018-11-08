package executors;

import constants.DatasetName;

import io.Cache;
import io.Utils;
import model.twitter.Dataset;


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


//        System.out.println(d.getBabelCategories().size());
//        System.out.println(d);
//        System.out.println(d.getWikiPages().get(0));
//
//        Collection<WikiPageModel> set = d.getWikiPages().values();
//        System.out.println(set.size());
//        System.out.println(new HashSet<>(set).size());
//
//        Collection<BabelCategoryModel> s2 = d.getBabelCategories().values();
//        System.out.println(s2.size());
//        System.out.println(new HashSet<>(s2).size());
//
//        Collection<BabelDomainModel> s3 = d.getBabelDomains().values();
//        System.out.println(s3.size());
//        System.out.println(new HashSet<>(s3).size());

        Dataset d = Cache.DatasetCache.read(DatasetName.S23);
        System.out.println(d.report());



//        System.out.println(d.getUsers().values().stream().filter(x -> x.isFamous(d.getWikiPages())).count());

//        TwitterObjectFactory m = new TwitterObjectFactory(d);
//
//        Map<UserModel, Counter<String>> map = ClustersUtils.getUserToCatCounter(d);
////
////        long a = d.getUsers().values().stream()
////                .filter(x -> x.getTweetsIds().stream()
////                        .mapToInt(y -> x.getTweetModel(d.getTweets(), y).getWikiPageModel(d.getInterests(), d.getWikiPages()).getBabelCategories().isEmpty() ? 0 : 1).sum() != 0)
////                .count();
////
////        long b = d.getUsers().values().stream()
////                .filter(x -> x.getTweetsIds().size() != 0)
////                .count();
//
//        System.out.println(String.format("NUMERO DI UTENTI CHE HANNO ALMENO UNA CATEGORIA: %s", a));
//        System.out.println(String.format("NUMERO DI UTENTI CHE HANNO ALMENO UN TWEET: %s", b));
//        System.out.println(d.getUsers().size());
//        System.out.println(map.size());

//        for (UserModel u : map.keySet()) {
//            assert d.getUsers().containsKey(u.getId());
//        }
//
//        int o = 0;
//        for (UserModel u : d.getUsers().values()) {
//            try {
//                o++;
////                assert map.containsKey(u);
//            } catch (Error e) {
//                System.out.println(u);
////                assert false;
//            }
//        }
//        System.out.println("Errore "+ o);


//        UserModel u1 = m.getUser(524289);
//        UserModel u2 = m.getUser(524311);
//        Clusters c = Cache.ClustersWikiMidCache.read();
//        System.out.println(c.report(d));
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
