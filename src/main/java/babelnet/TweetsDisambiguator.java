package babelnet;

import constants.DatasetName;
import constants.TwitterRespPath;
import io.Utils;
import twitteroperation.TweetRespDisambiguated;
import twitteroperation.TweetsResp;
import utils.Chrono;

import java.util.List;

public class TweetsDisambiguator {

    private static DatasetName[] _dataset_to_process() {
        return new DatasetName[]{DatasetName.S21, DatasetName.S22};
    }

    private static String merge_tweets(List<String> tweets) {
        StringBuilder s = new StringBuilder();
        for (String tweet : tweets) {
            s.append(tweet);
            s.append(".\n");
        }
        return s.toString();
    }


    public static void main(String[] args) throws Utils.CacheNotPresent {
        DatasetName[] datasets = _dataset_to_process();

        for (DatasetName dname : datasets) {
            Chrono c = new Chrono(String.format("Disambiguating dataset: %s...", dname));
            String path = TwitterRespPath.TWEETS_RESP.getPath(dname.name());

            TweetsResp resp = Utils.restoreJson(path, TweetsResp.class);
            assert dname.equals(resp.getOriginDataset());

            TweetRespDisambiguated resp_disambiguated = new TweetRespDisambiguated(resp);

            resp.getResults().forEach((x, y) -> {
                String merged_tweets = merge_tweets(y);

                List<String> synsets = BabelfyInterface.disambiguate(merged_tweets);
                List<String> categories = BabelnetInterface.getCategories(synsets);
                List<String> domains = BabelnetInterface.getDomains(synsets);

                resp_disambiguated.addUserSynsets(x, synsets);
                resp_disambiguated.addUserCategories(x, categories);
                resp_disambiguated.addUserDomains(x, domains);
            });

            Utils.saveJson(resp_disambiguated, TwitterRespPath.TWEETS_RESP_DISAMBIGUATED.getPath(dname.name()), true);
            c.millis();
        }
    }
}
