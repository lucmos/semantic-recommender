package clusters;

import twittermodel.UserModel;
import utils.IndexedSerializable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
}
