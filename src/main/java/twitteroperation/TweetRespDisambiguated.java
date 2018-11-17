package twitteroperation;

import java.util.HashMap;
import java.util.List;

public class TweetRespDisambiguated extends TweetsResp {

    /**
     * Tweeter respons of the request
     */
    private HashMap<String, List<String>> users2synsets;
    private HashMap<String, List<String>> users2categories;
    private HashMap<String, List<String>> users2domains;

    public TweetRespDisambiguated() {
        users2categories = new HashMap<>();
        users2synsets = new HashMap<>();
        users2domains = new HashMap<>();
    }

    public TweetRespDisambiguated(TweetsResp resp) {
        this();

        this.originDataset = resp.originDataset;

        this.results = resp.results;

        this.notExistingUserId = resp.notExistingUserId;
        this.privateUserId = resp.privateUserId;
        this.suspendedUserId = resp.suspendedUserId;
    }

    public void addUserSynsets(String userId, List<String> synsets) {
        assert !users2synsets.containsKey(userId);
        users2synsets.put(userId, synsets);
    }

    public void addUserCategories(String userId, List<String> categories) {
        assert !users2categories.containsKey(userId);
        users2categories.put(userId, categories);
    }

    public void addUserDomains(String usereId, List<String> domains) {
        assert !users2domains.containsKey(usereId);
        users2domains.put(usereId, domains);
    }
}
