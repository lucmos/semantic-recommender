package executors;

import constants.DatasetName;
import constants.Dimension;
import datasetsreader.Dataset;
import io.Cache;

public class Test {
    public static void main(String[] args) {
        Dataset d = Cache.readFromCache(DatasetName.S22, Dimension.SMALL);

        d.getPages().get(0);
    }
}
