package properties;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropReader {
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
            return null;
        }
    }

    static {
        PropReader instance = PropReader.getInstance();
        System.out.println(String.format("WORKING WITH DIMENSION: %s", instance.dimension()));
    }

    public static final String CONFIG_FILE = "config/wsie.properties";

    public static final String DIMENSION = "dimension";

    private static PropReader instance;
    public static PropReader getInstance() {
        if (instance == null) {
            instance = new PropReader();
        }
        return instance;
    }

    private Properties properties = new Properties();
    private Dimension dimension;
    private PropReader() {
        try (InputStream input = new FileInputStream(CONFIG_FILE))
        {
            properties.load(input);
            this.dimension = loadDimension();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Dimension dimension() {
        return dimension;
    }

    private Dimension loadDimension() {
        String dim = properties.getProperty(DIMENSION);

        Dimension d = Dimension.fromString(dim);
        if (d == null) {
            throw new RuntimeException("Invalid dimension: " + dim);
        }
        return d;
    }
}
