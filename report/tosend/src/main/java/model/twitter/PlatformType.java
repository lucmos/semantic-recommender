package model.twitter;

/**
 * Describes the possible types of platforms
 */
public enum PlatformType {
    GOOD_READS("GoodReads", 1),
    IMDB("IMDb", 2),
    SPOTIFY("Spotify", 3);

    private String text;
    private int id;

    PlatformType(String text, int id)
    {
        assert text != null && !text.equals("");
        this.text = text;
        this.id = id;
    }

    public String getText() {
        return this.text;
    }

    public int getId() {
        return this.id;
    }
    /**
     * Given a String returns the correspondent PlatformType object
     * @param text
     * @return
     */
    public static PlatformType fromString(String text) {
        assert text != null && !text.equals("");

        for (PlatformType b : PlatformType.values()) {
            if (b.text.equalsIgnoreCase(text)) {
                return b;
            }
        }
        throw new RuntimeException("This interest id name doesn't exist!");
    }

    /**
     * Given a String returns the correspondent PlatformType object
     * @param id
     * @return
     */
    public static PlatformType fromId(int id) {
        assert id > 0;

        for (PlatformType b : PlatformType.values()) {
            if (b.getId() == id) return b;
        }
        throw new RuntimeException("This interest seqId doesn't exist!");
    }
}