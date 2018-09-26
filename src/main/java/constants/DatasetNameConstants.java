package constants;

public enum DatasetNameConstants
{
    WIKIMID("wikimid"), S21("s21"), S22("s22"), S23("s23");

    private String name;

    DatasetNameConstants(String name){
        this.name=name;
    }

    public String getName() {
        return name;
    }
}
