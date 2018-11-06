package model.clusters;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import model.NamedObjectModel;
import model.twitter.UserModel;

public class Cluster extends NamedObjectModel {

    private IntOpenHashSet userIds;

    Cluster(int seqId, String idString) {
        super(seqId, idString);

        this.userIds = new IntOpenHashSet();
    }

    @Override
    public String toString() {
        return String.format(("(cluster: %s {users: %s})"), getName(), userIds.size());
    }

    void addUser(UserModel userModel) {
        assert userIds.contains(userModel.getId());

        userIds.add(userModel.getId());
    }

    public int size() {
        return userIds.size();
    }

    public IntOpenHashSet getUserIds() {
        return userIds;
    }
}
