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

    public static final String CONFIG_FILE = "/home/luca/IdeaProjects/WSIEProject/config/wsie.properties";

    public static final String DIMENSION = "dimension";

    private static PropReader instance;
    public static PropReader getInstance() {
        if (instance == null) {
            instance = new PropReader();
        }
        return instance;
    }

    private Properties properties = new Properties();

    private PropReader() {
        try (InputStream input = new FileInputStream(CONFIG_FILE))
        {
            properties.load(input);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Dimension dimension() {
        String dim = properties.getProperty(DIMENSION);

        Dimension d = Dimension.fromString(dim);
        if (d == null) {
            throw new RuntimeException("Invalid dimension: " + dim);
        }
        return d;
    }
}
