package model.clusters;

import model.ObjectCollection;
import model.twitter.UserModel;

import java.util.*;

public class Clusters extends ObjectCollection {

    HashMap<Integer, Cluster> clusters_index;

    private HashMap<Integer, Integer> usersToCluster;

    public Clusters() {
        clusters_index = new HashMap<>();
        usersToCluster = new HashMap<>();
    }

    @Override
    public String toString() {
        return String.format("(clusters_index: %s)", clusters_index.size());
    }

    public Cluster getCluster(Integer clusterId) {
        assert clusters_index.containsKey(clusterId);

        return clusters_index.get(clusterId);
    }

    public Cluster getCluster(UserModel userModel) {
        assert usersToCluster.containsKey(userModel.getId());

        return getCluster(usersToCluster.get(userModel.getId()));
    }

    public Map<Integer, Cluster> getClusters() {
        return clusters_index;
    }


    public HashMap<Integer, Integer> getUsersToCluster() {
        return usersToCluster;
    }

    void addUser(UserModel user, Cluster cluster) {
        assert !usersToCluster.containsKey(user.getId());

        usersToCluster.put(user.getId(), cluster.getId());
        cluster.addUser(user);
    }
    //  // TODO: 02/11/18 problema con id? se tolgo e rimuovo
//    void removeUser(UserModel user, Cluster cluster) {
//        assert usersToCluster.containsKey(user.getId());
//
//        usersToCluster.remove(user.getId());
//        cluster.removeUser(user);
//    }
//
//    void removeUser(UserModel userModel) {
//        Cluster c = usersToCluster.get(userModel.getId());
//        removeUser(userModel, c);

//    }



//
//    public String getCluster(Integer user) {
//        return usersToCluster.get(user);
//    }
//
//    public String getCluster(UserModel userModel) {
//        return getCluster(userModel.getId());
//    }
//
//    public Map<String, Set<Integer>> getClusterToUsers() {
//        return clusterToUsers;
//    }
//
//    public Map<Integer, String> getUsersToCluster() {
//        return usersToCluster;
//    }
//
//    public int numberOfClusters() {
//        return clusterToUsers.size();
//    }
//
//    public int numberOfUsers() {
//        return usersToCluster.size();
//    }
//
//    @Override
//    public String toString() {
//        return String.format("(clusters_index: %s {users: %s})", numberOfClusters(), numberOfUsers());
//    }
//
//    public String report(Dataset dataset, String cluster, int k) {
//        return clustersStats() + clustersDistribution(k) + clusterInspection(dataset, cluster);
//    }
//
//    public String clustersStats() {
//        double[] cluster_sizes = clusterToUsers.values().stream().mapToDouble(Set::size).toArray();
//        Statistics stat = new Statistics(cluster_sizes);
//
//        return "\n[CLUSTER STATS]\n" +
//                String.format("\t#Users: %s\n", numberOfUsers()) +
//                stat.report();
//    }
//
//    public String clustersDistribution(int k) {
//        String clustDistr = Counter.fromMap(usersToCluster).getDistribution(k);
//        return String.format("\n[CLUSTERS DISTRIBUTION]\n %s\n", clustDistr);
//    }
//
//    public String clusterInspection(Dataset dataset, String cluster) {
//        StringBuilder s = new StringBuilder();
//        s.append(String.format("\n[CLUSTER INSPECTION] -> %s\n", cluster));
//        Set<Integer> users = clusterToUsers.get(cluster);
//        for (Integer userId : users) {
//            List<String> list = dataset.getUsers().get(userId).getTweetsIds().stream()
//                    .map(tweetId ->
//                            dataset.getTweets()
//                                    .get(tweetId)
//                                    .getWikiPageModel(dataset.getInterests(), dataset.getWikiPages())
//                                    .toString())
//                    .filter(Objects::nonNull)
//                    .collect(Collectors.toList());
//            s.append(String.format("\t%s:\t\t%s\n", userId, list));
//        }
//        return s.toString();
//    }
}

