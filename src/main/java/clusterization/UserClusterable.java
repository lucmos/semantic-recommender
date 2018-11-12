package clusterization;


import model.twitter.UserModel;
import org.apache.commons.math3.stat.clustering.Clusterable;

import java.util.Collection;

public class UserClusterable implements Clusterable<UserClusterable> {

    private final UserModel userModel;
    private DistanceMatrix distanceMatrix;

    public UserClusterable(UserModel userModel, DistanceMatrix distanceMatrix) {
        this.userModel = userModel;
        this.distanceMatrix = distanceMatrix;
    }

    @Override
    public double distanceFrom(UserClusterable p) {
        return distanceMatrix.distance(userModel.getId(), p.userModel.getId());
    }

    @Override
    public UserClusterable centroidOf(Collection<UserClusterable> users) {
        float min = Integer.MAX_VALUE;
        UserClusterable u = null;

        for (UserClusterable f: users) {
            float mean = meanDistances(f, users);
            if (mean < min) {
                min = mean;
                u = f;
            }
        }

        assert u != null;
        return u;
    }

    @Override
    public boolean equals(Object o) {
        if(! (o instanceof UserClusterable)) return false;
        return userModel.equals(o);
    }

    public float meanDistances(UserClusterable u, Collection<UserClusterable> users) {
        float sum = 0;
        for (UserClusterable f : users) {
            if(f.equals(u)) continue;
            sum += u.distanceFrom(f);
        }
        return sum / users.size();
    }
}
