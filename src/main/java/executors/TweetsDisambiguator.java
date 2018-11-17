package executors;

import babelnet.BabelfyInterface;
import babelnet.BabelnetInterface;
import constants.DatasetName;
import constants.TwitterRespPath;
import io.Utils;
import twitteroperation.TweetRespDisambiguated;
import twitteroperation.TweetsResp;
import utils.Chrono;
import utils.Counter;

import java.util.Collection;
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

    public static void disambiguate() throws Utils.CacheNotPresent {
        DatasetName[] datasets = _dataset_to_process();

        for (DatasetName dname : datasets) {
            Chrono c = new Chrono(String.format("Disambiguating dataset: %s...", dname));
            String path = TwitterRespPath.TWEETS_RESP.getPath(dname.name());

            TweetsResp resp = Utils.restoreJson(path, TweetsResp.class);
            assert dname.equals(resp.getOriginDataset());

            TweetRespDisambiguated resp_disambiguated = new TweetRespDisambiguated(resp);

            resp.getResults().forEach((x, y) -> {
                System.out.println(String.format("Operating on user: %s", x));
                String merged_tweets = merge_tweets(y);

                Collection<String> synsets = BabelfyInterface.disambiguate(merged_tweets);
                Collection<String> categories = BabelnetInterface.getCategories(synsets);
                Collection<String> domains = BabelnetInterface.getDomains(synsets);

                resp_disambiguated.addUserSynsets(x, Counter.fromCollection(synsets));
                resp_disambiguated.addUserCategories(x, Counter.fromCollection(categories));
                resp_disambiguated.addUserDomains(x, Counter.fromCollection(domains));
            });

            Utils.saveJson(resp_disambiguated, TwitterRespPath.TWEETS_RESP_DISAMBIGUATED.getPath(dname.name()), true);
            c.millis();
        }
    }


    public static void main(String[] args) throws Utils.CacheNotPresent {
        disambiguate();
    }
}
