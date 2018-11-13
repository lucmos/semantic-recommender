package clusterization;


import de.lmu.ifi.dbs.elki.data.NumberVector;
import de.lmu.ifi.dbs.elki.math.linearalgebra.Vector;
import model.twitter.Dataset;
import model.twitter.UserModel;

import java.util.Collection;

public class UserClusterable implements NumberVector {

    private final UserModel userModel;
    private Dataset dataset;

    public UserClusterable(Dataset dataset, UserModel userModel) {
        this.userModel = userModel;
        this.dataset = dataset;
    }

    @Override
    public int getDimensionality() {
        return dataset.getBabelCategories().size(); // TODO: 13/11/18 gestisci config domains 
    }

    @Override
    public double getMin(int dimension) {
        return 0;
    }

    @Override
    public double getMax(int dimension) {
        return 0;
    }

    @Override
    public Number getValue(int dimension) {
        return null;
    }

    @Override
    public double doubleValue(int dimension) {
        return 0;
    }

    @Override
    public float floatValue(int dimension) {
        return 0;
    }

    @Override
    public int intValue(int dimension) {
        return 0;
    }

    @Override
    public long longValue(int dimension) {
        return 0;
    }

    @Override
    public short shortValue(int dimension) {
        return 0;
    }

    @Override
    public byte byteValue(int dimension) {
        return 0;
    }

    @Override
    public Vector getColumnVector() {
        return null;
    }

//    @Override
//    public double distanceFrom(UserClusterable p) {
//        return dataset.distance(userModel.getId(), p.userModel.getId());
//    }
//
//    @Override
//    public UserClusterable centroidOf(Collection<UserClusterable> users) {
//        float min = Integer.MAX_VALUE;
//        UserClusterable u = null;
//
//        for (UserClusterable f: users) {
//            float mean = meanDistances(f, users);
//            if (mean < min) {
//                min = mean;
//                u = f;
//            }
//        }
//
//        assert u != null;
//        return u;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if(! (o instanceof UserClusterable)) return false;
//        return userModel.equals(o);
//    }
//
//    public float meanDistances(UserClusterable u, Collection<UserClusterable> users) {
//        float sum = 0;
//        for (UserClusterable f : users) {
//            if(f.equals(u)) continue;
//            sum += u.distanceFrom(f);
//        }
//        return sum / users.size();
//    }
//
//    @Override
//    public double[] getPoint() {
//        return new double[0];
//    }
}
