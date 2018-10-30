package clusters;

import datasetsreader.Dataset;
import twittermodel.TweetModel;
import twittermodel.UserModel;
import utils.IndexedSerializable;

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

    public String statsCoherence(Dataset dataset, String cluster) {
        StringBuilder s = new StringBuilder(String.format("Cluster: %s\n", cluster));
        Set<String> users = clusterToUsers.get(cluster);

        for (String userId : users) {
            List<String> list = dataset.getUsers().get(userId).getTweetsIds().stream()
                    .map(tweetId ->{
                                TweetModel tweet = dataset.getTweets().get(tweetId);
                                if (tweet.getInterestModel(dataset.getInterests()).isValid()) {
                                    return tweet.getWikiPageModel(dataset.getInterests(), dataset.getPages()).toString();
                                    }
                                    else{
                                    return null;
                                }})
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            s.append(String.format("\t%s:\t\t%s\n", userId, list));
        }
        return s.toString();
    }
}
