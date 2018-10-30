package clusters;

import datasetsreader.Dataset;
import twittermodel.UserModel;
import utils.Counter;
import utils.IndexedSerializable;
import utils.Statistics;

import java.util.*;
import java.util.stream.Collectors;

public class Clusters implements IndexedSerializable {
    private HashMap<String, String> userToCluster = new HashMap<>();
    private HashMap<String, Set<String>> clusterToUsers = new HashMap<>();

    public void addUser(UserModel user, String cluster) {
        String id = user.getIdString();

        assert !userToCluster.containsKey(id);

        userToCluster.put(id, cluster);

        if (!clusterToUsers.containsKey(cluster)) {
            clusterToUsers.put(cluster, new HashSet<>());
        }
        clusterToUsers.get(cluster).add(id);
    }

    public Set<String> getUsers(String cluster) {
        return clusterToUsers.get(cluster);
    }

    public String getCluster(String user) {
        return userToCluster.get(user);
    }

    public String getCluster(UserModel userModel) {
        return getCluster(userModel.getIdString());
    }

    public Map<String, Set<String>> getClusterToUsers() {
        return clusterToUsers;
    }

    public Map<String, String> getUserToCluster() {
        return userToCluster;
    }

    public int numberOfClusters() {
        return clusterToUsers.size();
    }

    public int numberOfUsers() {
        return userToCluster.size();
    }

    @Override
    public String toString() {
        return String.format("(clusters: %s {users: %s})", numberOfClusters(), numberOfUsers());
    }

    public String report(Dataset dataset, String cluster, int k) {
        return clustersStats() + clustersDistribution(k) + clusterInspection(dataset, cluster);
    }

    public String clustersStats() {
        double[] cluster_sizes = clusterToUsers.values().stream().mapToDouble(Set::size).toArray();
        Statistics stat = new Statistics(cluster_sizes);

        return "\n[CLUSTER STATS]\n" +
                String.format("\t#Users: %s\n", numberOfUsers()) +
                stat.report();
    }

    public String clustersDistribution(int k) {
        String clustDistr = Counter.fromMap(userToCluster).getDistribution(k);
        return String.format("\n[CLUSTERS DISTRIBUTION]\n %s\n", clustDistr);
    }

    public String clusterInspection(Dataset dataset, String cluster) {
        StringBuilder s = new StringBuilder();
        s.append(String.format("\n[CLUSTER INSPECTION] -> %s\n", cluster));
        Set<String> users = clusterToUsers.get(cluster);
        for (String userId : users) {
            List<String> list = dataset.getUsers().get(userId).getTweetsIds().stream()
                    .map(tweetId ->
                            dataset.getTweets()
                                    .get(tweetId)
                                    .getWikiPageModel(dataset.getInterests(), dataset.getPages())
                                    .toString())
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            s.append(String.format("\t%s:\t\t%s\n", userId, list));
        }
        return s.toString();
    }
}

