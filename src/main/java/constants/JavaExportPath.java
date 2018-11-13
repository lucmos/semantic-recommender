package constants;

import io.Config;

public enum JavaExportPath {

    EXPORT_PATH("results/java_export/export_%s_%s_%s.json");

    private String path;

    JavaExportPath(String path) {
        this.path = path;
    }

    /**
     * Return the path of a given object
     *
     * @return the path
     */
    public String getPath(DatasetName name, Config.ClusterOver over, Config.Dimension dim) {
        return String.format(this.path, name.name(), over.getName(), dim.getName());
    }
}
