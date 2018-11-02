package model.clusters;

import model.ObjectCollection;
import model.twitter.Dataset;
import model.twitter.UserModel;
import utils.Counter;
import utils.Statistics;

import java.util.*;
import java.util.stream.Collectors;

public class Clusters extends ObjectCollection {

    HashMap<Integer, Cluster> clusters_index;

    private HashMap<Integer, Integer> usersToCluster;

    public Clusters() {
        clusters_index = new HashMap<>();
        usersToCluster = new HashMap<>();
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

    public int numberOfClusters() {
        return clusters_index.size();
    }

    public int numberOfUsers() {
        return usersToCluster.size();
    }

    @Override
    public String toString() {
        return String.format("(clusters: %s {users: %s})", numberOfClusters(), numberOfUsers());
    }

    public String report(Dataset dataset) {
        return report(dataset, new ArrayList<>(clusters_index.values()).get(0), 10);
    }

    public String report(Dataset dataset, ClusterFactory cf, String cluster, int k) {
        return report(dataset, cf.getCluster(cluster), k);
    }

    public String report(Dataset dataset, Cluster cluster, int k) {
        return String.format("%s\n\n%s\n\n%s", clustersStats(), clustersDistribution(k), clusterInspection(dataset, cluster));
    }

    public String clustersStats() {
        double[] cluster_sizes = clusters_index.values().stream().mapToDouble(Cluster::size).toArray();
        Statistics stat = new Statistics(cluster_sizes);

        return "[CLUSTER STATS]\n" +
                stat.report(
                        "number of users",
                        "number of clusters",
                        "greatest cluster size",
                        "smallest cluster size",
                        "mean cluster size",
                        "median cluster size",
                        "cluster size variance",
                        "cluster size stddev"
                        );
    }

    public String clustersDistribution(int k) {
        String clustDistr = Counter.fromCollection(usersToCluster.values().stream()
                .map(x -> clusters_index.get(x).getIdString()).collect(Collectors.toList()))
                .getDistribution(k);
        return String.format("[CLUSTERS DISTRIBUTION]\n %s", clustDistr);
    }

    public String clusterInspection(Dataset dataset, Cluster cluster) {
        StringBuilder s = new StringBuilder();
        s.append(String.format("[CLUSTER INSPECTION] -> %s\n", cluster));
        Set<Integer> users = cluster.getUserIds();
        for (Integer userId : users) {
            List<String> list = dataset.getUsers().get(userId).getTweetsIds().stream()
                    .map(tweetId ->
                            dataset.getTweets()
                                    .get(tweetId)
                                    .getWikiPageModel(dataset.getInterests(), dataset.getWikiPages())
                                    .toString())
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            s.append(String.format("\t%s:\t\t%s\n", userId, list));
        }
        return s.toString();
    }
}

