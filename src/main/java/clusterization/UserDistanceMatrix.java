package clusterization;

import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import model.twitter.Dataset;
import model.twitter.UserModel;
import utils.Chrono;

import java.util.Comparator;

public class UserDistanceMatrix implements DistanceMatrix {

    private ClustersMeter meter;
    private Dataset dataset;

    private Int2IntOpenHashMap user2Index = new Int2IntOpenHashMap();
    private Int2IntOpenHashMap index2User = new Int2IntOpenHashMap();

    private float[][] matrix;

    public UserDistanceMatrix(Dataset dataset) {
        this.dataset = dataset;
        this.meter = new ClustersMeter(dataset);
        matrix = compute();
    }

    private float[][] compute() {
        Chrono c = new Chrono("Computing users distance matrix...");
        ObjectArrayList<UserModel> sorted_users = new ObjectArrayList<>(dataset.getUsers().values());
        sorted_users.sort(Comparator.naturalOrder());

        for (int i = 0; i < sorted_users.size(); i++) {
            user2Index.put(sorted_users.get(i).getId(), i);
            index2User.put(i, sorted_users.get(i).getId());
        }

        int n = sorted_users.size();
        float[][] D = new float[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                UserModel u1 = getUserFromIndex(i);
                UserModel u2 = getUserFromIndex(j);

                float dis = meter.distance(u1, u2);
                D[i][j] = dis;
                D[j][i] = dis;
            }
        }
        c.millis();
        return D;
    }

    public float distance(int u1, int u2) {
        return distance(dataset.getUser(u1), dataset.getUser(u2));
    }

    public float distance(UserModel u1, UserModel u2) {
        return matrix[getIndex(u1)][getIndex(u2)];
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

    public float[][] getMatrix() {
        return matrix;
    }

    public Int2IntOpenHashMap getUser2Index() {
        return user2Index;
    }

    public Int2IntOpenHashMap getIndex2User() {
        return index2User;
    }
}
