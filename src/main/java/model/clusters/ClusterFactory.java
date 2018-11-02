package model.clusters;

import model.clusters.Cluster;
import model.clusters.Clusters;
import model.twitter.UserModel;

public class ClusterFactory {

    private final Clusters clusters;

    public ClusterFactory(Clusters clusters) {
        this.clusters = clusters;
    }

    public Clusters getClusters() {
        return clusters;
    }

    public Cluster getCluster(String id) {
        assert id != null && !id.equals("");

        if (!clusters.exixstObj(id)) {
            int i = clusters.getNextId(id);
            clusters.clusters_index.put(i, new Cluster(i, id));
        }

        Cluster c = clusters.clusters_index.get(clusters.getIntegerId(id));

        assert c != null;
        return c;
    }

    public void addUser(UserModel user, Cluster cluster) {
        clusters.addUser(user, cluster);
    }
}
