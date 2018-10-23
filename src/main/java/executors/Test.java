package executors;

import constants.DatasetName;
import constants.Dimension;
import datasetsreader.Dataset;
import io.Cache;
import utils.Chrono;

public class Test {
    public static void main(String[] args) {
        Test.ric(5);

        //        Chrono c = new Chrono("Test a... ");
//        Chrono d = new Chrono("Test b... ");
//        d.millis();
//        d = new Chrono("Test c... ");
//        d.millis();
//        d = new Chrono("Test d... ");
//        Chrono d2 = new Chrono("Test e... ");
//        Chrono d3 = new Chrono("Test e... ");
//        Chrono d4 = new Chrono("Test e... ");
//        d3.millis();
//        d3.millis();
//        d2.millis();
//        d.millis();
//
//        c.millis();
    }

    public static void ric(int a) {
        if (a <= 0) {
            return;
        }

        Chrono b = new Chrono(String.format("Ricorsione livello %s...", a));
        ric(a - 1);
        b.millis();

        Chrono d = new Chrono(String.format("Ricorsione livello %s...", a));
        ric(a - 2);
        d.millis();
    }
}
