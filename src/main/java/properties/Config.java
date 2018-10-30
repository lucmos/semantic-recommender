package properties;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    public enum Dimension
    {
        SMALL("small", 100000),
        COMPLETE("complete", -1);

        private String name;
        private int size;

        Dimension(String name, int size) {
            this.name = name;
            this.size = size;
        }

        public String getName() {
            return name;
        }

        public int getSize() {
            return size;
        }

        public static Dimension fromString(String text) {
            for (Dimension b : Dimension.values()) {
                if (b.name.equalsIgnoreCase(text)) {
                    return b;
                }
            }
            throw new RuntimeException("Invalid dimension: " + text);
        }
    }

    public enum ClusterType {
        MOST_COMMON("most_common"),
        TF_IDF("tf_idf");

        private String name;

        ClusterType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public static ClusterType fromString(String text) {
            for (ClusterType b : ClusterType.values()) {
                if (b.name.equalsIgnoreCase(text)) {
                    return b;
                }
            }
            throw new RuntimeException("Invalid cluster type: " + text);
        }
    }

    private static final String CONFIG_FILE = "config/wsie.properties";
    private static final String DIMENSION = "dimension";
    private static final String CLUSTERS = "clusters";

    static {
        Config instance = Config.getInstance();
        System.out.println(String.format("[CONFIGURATION]\n" +
                "\t%s:\t%s\n" +
                "\t%s:\t%s\n",
                DIMENSION.toUpperCase(), instance.dimension(),
                CLUSTERS.toUpperCase(), instance.clusterType));
    }

    private static Config instance;
    public static Config getInstance() {

        if (instance == null) {
            instance = new Config();
        }
        return instance;
    }

    private Dimension dimension;
    private ClusterType clusterType;

    private Config() {

        try (InputStream input = new FileInputStream(CONFIG_FILE))
        {
            Properties properties = new Properties();
            properties.load(input);
            this.dimension = Dimension.fromString(properties.getProperty(DIMENSION));
            this.clusterType = ClusterType.fromString(properties.getProperty(CLUSTERS));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Dimension dimension() {
        return dimension;
    }

    public ClusterType clusterType() {
        return clusterType;}
}
