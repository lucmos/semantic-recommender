package clusterization;

import constants.DatasetName;
import de.lmu.ifi.dbs.elki.data.SparseDoubleVector;
import io.Cache;
import io.Utils;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntConsumer;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import model.twitter.Dataset;
import model.twitter.UserModel;
import org.ejml.data.DMatrixSparseTriplet;
import utils.Chrono;

import java.util.Comparator;
import java.util.function.IntToDoubleFunction;
import java.util.function.IntUnaryOperator;
import java.util.stream.IntStream;

@Deprecated
public class UserDistanceMatrix implements DistanceMatrix {

    public static void main(String[] args) throws Utils.CacheNotPresent {
        Dataset d = Cache.DatasetCache.read(DatasetName.WIKIMID);
        UserDistanceMatrix u = new UserDistanceMatrix(d);
        System.out.println(u);
    }

    private ClustersMeter meter;
    private Dataset dataset;

    private Int2IntOpenHashMap user2Index = new Int2IntOpenHashMap();
    private Int2IntOpenHashMap index2User = new Int2IntOpenHashMap();

    private DMatrixSparseTriplet matrix;

    public UserDistanceMatrix(Dataset dataset) {
        this.dataset = dataset;
        this.meter = new ClustersMeter(dataset);
        compute();
    }

    private  void put(int i, int j, double v) {
        matrix.set(i, j, v);
    }

    private synchronized double get(int i, int j) {
        return matrix.get(i, j);
    }


    private void compute() {
        Chrono c = new Chrono("Sorting users...");
        ObjectArrayList<UserModel> sorted_users = new ObjectArrayList<>(dataset.getUsers().values());
        sorted_users.sort(Comparator.naturalOrder());

        for (int i = 0; i < sorted_users.size(); i++) {
            user2Index.put(sorted_users.get(i).getId(), i);
            index2User.put(i, sorted_users.get(i).getId());
        }
        c.millis();

        c = new Chrono("Computing users distance matrix...");
        int n = sorted_users.size();
        matrix = new DMatrixSparseTriplet(n, n, 500000);

        IntStream.range(0, n).forEach((IntConsumer) i -> {
            Chrono c1 = new Chrono(String.format("Computing row %s", i));

            IntStream.range(i+1, n).parallel().forEach(j -> {
                UserModel u1 = sorted_users.get(i);
                UserModel u2 = sorted_users.get(j);

                double dis = meter.distance(u1, u2);
//                put(i, j, dis);
            });

            c1.millis();
        });


//            for (int j = i + 1; j < n; j++) {
//                UserModel u1 = sorted_users.get(i);
//                UserModel u2 = sorted_users.get(j);
//
//                double dis = meter.distance(u1, u2);
//
//                if (dis == 0) continue;
////                m.set(i, j, dis);
//            }

//            m.replaceRow(i, null);

        c.millis();
    }

    public double distance(int u1, int u2) {
        return distance(dataset.getUser(u1), dataset.getUser(u2));
    }

    public double distance(UserModel u1, UserModel u2) {
        if (u1.equals(u2)) {
            return 0;
        }

        int i1 = getIndex(u1);
        int i2 = getIndex(u2);
        return (i1 < i2) ? matrix.get(i1, i2) : matrix.get(i2, i1);
    }

    public int getIndex(int u) {
        return getIndex(dataset.getUser(u));
    }

    public int getIndex(UserModel userModel) {
        return user2Index.get(userModel.getId());
    }

    public int getUserIdFromIndex(int index) {
        return index2User.get(index);
    }

    public UserModel getUserFromIndex(int index) {
        return dataset.getUser(getUserIdFromIndex(index));
    }

    public DMatrixSparseTriplet getMatrix() {
        return matrix;
    }

    public Int2IntOpenHashMap getUser2Index() {
        return user2Index;
    }

    public Int2IntOpenHashMap getIndex2User() {
        return index2User;
    }
}
