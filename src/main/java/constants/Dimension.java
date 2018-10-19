package constants;

public enum Dimension
{
    SMALL("small", 100000),
    COMPLETE("complete", -1);

    private String name;
    private int dim;

    Dimension(String name, int dim) {
        this.name = name;
        this.dim = dim;
    }

    public String getName() {
        return name;
    }

    public int getDim() {
        return dim;
    }
}