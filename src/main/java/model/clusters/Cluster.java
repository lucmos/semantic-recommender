package model.clusters;

import model.ObjectModel;
import model.twitter.UserModel;

import java.util.HashSet;
import java.util.Set;

public class Cluster extends ObjectModel {

    private Set<Integer> userIds;

    Cluster(int seqId, String idString) {
        super(seqId, idString);

        this.userIds = new HashSet<>();
    }

    @Override
    public String toString() {
        return String.format(("(cluster: %s {users: %s})"), getIdString(), userIds.size());
    }

    void addUser(UserModel userModel) {
        assert userIds.contains(userModel.getId());

        userIds.add(userModel.getId());
    }

    public int size() {
        return userIds.size();
    }

    public Set<Integer> getUserIds() {
        return userIds;
    }
}
