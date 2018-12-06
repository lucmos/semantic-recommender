package constants;

import io.Config;

public enum TwitterRespPath {

    FRIENDS_RESP("responses/friends_request_%s.json"),
    TWEETS_RESP("responses/tweets_request_%s.json"),
    TWEETS_RESP_DISAMBIGUATED("responses/tweets_request_disambiguated_%s.json");

    private String path;

    TwitterRespPath(String path) {
        this.path = path;
    }

    /**
     * Return the path of a given object
     *
     * @return the path
     */
    public String getPath(String datasetName) {
        return String.format(this.path, datasetName);
    }
}
