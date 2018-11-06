package model.clusters;

import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import model.ObjectCollection;
import model.twitter.Dataset;
import model.twitter.UserModel;
import utils.Counter;
import utils.Statistics;

import java.util.*;
import java.util.stream.Collectors;

public class Clusters extends ObjectCollection {

    Int2ObjectOpenHashMap<Cluster> clusters_index;

    private Int2IntOpenHashMap usersToCluster;

    public Clusters() {
        clusters_index = new Int2ObjectOpenHashMap<>();
        usersToCluster = new Int2IntOpenHashMap();
    }

    public Cluster getCluster(int clusterId) {
        assert clusters_index.containsKey(clusterId);

        return clusters_index.get(clusterId);
    }

    public Cluster getCluster(UserModel userModel) {
        assert usersToCluster.containsKey(userModel.getId());

        return getCluster(usersToCluster.get(userModel.getId()));
    }

    public Int2ObjectOpenHashMap<Cluster> getClusters() {
        return clusters_index;
    }


    public Int2IntOpenHashMap getUsersToCluster() {
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

        return stat.report(
                        "cluster stats",
                        "total #users",
                        "#clusters",
                        "greatest |cluster|",
                        "#greatest clusters",
                        "smallest |cluster|",
                        "#smallest clusters",
                        "median |cluster|",
                        "#median clusters",
                        "mean |cluster|",
                        "|cluster| variance",
                        "|cluster| stddev"
                        );
    }

    public String clustersDistribution(int k) {
        String clustDistr = Counter.fromCollection(usersToCluster.values().stream()
                .map(x -> clusters_index.get((int) x).getName(this)).collect(Collectors.toList()))
                .getDistribution(k);
        return String.format("[CLUSTERS SIZE DISTRIBUTION]\n %s", clustDistr);
    }

    public String clusterInspection(Dataset dataset, Cluster cluster) {
        StringBuilder s = new StringBuilder();
        s.append(String.format("[INSPECTION OF USERS IN CLUSTER] -> %s\n", cluster));
        IntOpenHashSet users = cluster.getUserIds();
        for (int userId : users) {
            List<String> list = dataset.getUsers().get(userId).getTweetsIds().stream()
                    .map(tweetId ->
                            dataset.getTweets()
                                    .get((int) tweetId)
                                    .getWikiPageModel(dataset)
                                    .toString())
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            s.append(String.format("\t%s:\t\t%s\n", userId, list));
        }
        return s.toString();
    }
}

