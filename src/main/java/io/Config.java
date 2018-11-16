package io;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

    private static final String CONFIG_FILE = "config/wsie.properties";

    private static final String DIMENSION_PROPERTY = "dimension";
    private static final String CLUSTER_OVER_PROPERTY = "cluster_over";

    private Dimension dimension;
    private ClusterOver clusterOver;

    static {
        Config instance = Config.getInstance();
        System.out.println(String.format("[CONFIGURATION]\n" +
                        "\t%s:\t%s\n" +
                        "\t%s:\t%s\n",
                DIMENSION_PROPERTY.toUpperCase(), instance.dimension(),
                CLUSTER_OVER_PROPERTY.toUpperCase(), instance.clusterOver()));
    }

    private static Config instance;
    public static Config getInstance() {

        if (instance == null) {
            instance = new Config();
        }
        return instance;
    }

    private Config() {
        load();
    }

    public Dimension dimension() {
        return dimension;
    }


    public ClusterOver clusterOver() {
        return clusterOver;
    }


    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }

    public void setClusterOver(ClusterOver clusterOver) {
        this.clusterOver = clusterOver;
    }


    public void load() {

        try (InputStream input = new FileInputStream(CONFIG_FILE))
        {
            Properties properties = new Properties();
            properties.load(input);

            this.dimension = Dimension.fromString(properties.getProperty(DIMENSION_PROPERTY));
            this.clusterOver = ClusterOver.fromString(properties.getProperty(CLUSTER_OVER_PROPERTY));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public enum Dimension
    {
        SMALL("small", 1000),
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

    public enum ClusterOver {
        CATEGORIES("categories"),
        DOMAINS("domains");

        private String name;

        ClusterOver(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public static ClusterOver fromString(String text) {
            for (ClusterOver b : ClusterOver.values()) {
                if (b.name.equalsIgnoreCase(text)) {
                    return b;
                }
            }
            throw new RuntimeException("Invalid cluster type: " + text);
        }
    }
}
