package clusters;

import datasetsreader.Dataset;
import twittermodel.TweetModel;
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

    public String stats(Dataset dataset) {
        return stats(dataset, clusterToUsers.keySet().stream().limit(1).findFirst().get(), 50);
    }

    public String stats(Dataset dataset, String cluster) {
        return stats(dataset, cluster, 50);
    }

    public String stats(Dataset dataset, String cluster, int k) {
        int userNum = numberOfUsers();

        double[] cluster_sizes = clusterToUsers.values().stream().mapToDouble(Set::size).toArray();
        Statistics stat = new Statistics(cluster_sizes);

        StringBuilder s = new StringBuilder("\n[CLUSTER STATS]\n");
        s.append(String.format("\t#Users: %s\n", userNum));
        s.append(stat.report());

        String clustDistr = Counter.fromMap(userToCluster).getDistribution(k);
        s.append(String.format("\n[CLUSTERS DISTRIBUTION]\n %s", clustDistr));

        s.append(clusterInspection(dataset, cluster));

        return s.toString();
    }// TODO: 30/10/18 fai report

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

