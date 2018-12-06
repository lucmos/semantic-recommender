package twitteroperation;

import utils.Counter;

import java.util.HashMap;
import java.util.List;

public class TweetRespDisambiguated extends TweetsResp {

    /**
     * Tweeter respons of the request
     */
    private HashMap<String, Counter<String>> users2synsets;
    private HashMap<String, Counter<String>> users2categories;
    private HashMap<String, Counter<String>> users2domains;

    public TweetRespDisambiguated() {
        users2categories = new HashMap<>();
        users2synsets = new HashMap<>();
        users2domains = new HashMap<>();
    }

    public HashMap<String, Counter<String>> getUsers2categories() {
        return users2categories;
    }

    public HashMap<String, Counter<String>> getUsers2domains() {
        return users2domains;
    }

    @Override
    public String toString() {
        int synNUm = users2synsets.values().stream().map(Counter::total).reduce(Integer::sum).get();
        int catNum = users2categories.values().stream().map(Counter::total).reduce(Integer::sum).get();
        int domNum = users2domains.values().stream().map(Counter::total).reduce(Integer::sum).get();
        return String.format("(#users: %s, #synsets: %s, #categories: %s, #domains: %s)",
                users2synsets.size(), synNUm, catNum, domNum);
    }

    public TweetRespDisambiguated(TweetsResp resp) {
        this();

        this.originDataset = resp.originDataset;

        this.results = resp.results;

        this.notExistingUserId = resp.notExistingUserId;
        this.privateUserId = resp.privateUserId;
        this.suspendedUserId = resp.suspendedUserId;
    }

    public void addUserSynsets(String userId, Counter<String> synsets) {
        assert !users2synsets.containsKey(userId);
        users2synsets.put(userId, synsets);
    }

    public void addUserCategories(String userId, Counter<String> categories) {
        assert !users2categories.containsKey(userId);
        users2categories.put(userId, categories);
    }

    public void addUserDomains(String usereId, Counter<String> domains) {
        assert !users2domains.containsKey(usereId);
        users2domains.put(usereId, domains);
    }
}
