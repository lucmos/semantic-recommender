package executors;

import io.Config;
import io.Utils;
import twitter4j.TwitterException;
import twitteroperation.UserInfoExtractor;

public class DownloadTweetInformations {

    public static void download()  throws Utils.CacheNotPresent, InterruptedException, TwitterException {
        UserInfoExtractor._download_friends();
        UserInfoExtractor._download_tweets();
    }
    public static void main(String[] args) throws InterruptedException, Utils.CacheNotPresent, TwitterException {
        download();
    }
}
