package executors;

import constants.DatasetName;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.*;

import java.io.IOException;

@Deprecated
public class ProgramExecutor
{
    ReadingPhaseExecutor readingEx;
    TwitterExtractionExecutor twitterExtractionEx;

    public ProgramExecutor()
    {
        this.readingEx = new ReadingPhaseExecutor();
        this.twitterExtractionEx = new TwitterExtractionExecutor();
    }
    public ReadingPhaseExecutor getReadingEx(){return readingEx;}
    public TwitterExtractionExecutor getTwitterExtractionEx() {return twitterExtractionEx;}


    public static void main(String args[]) throws TwitterException, IOException
    {
        ProgramExecutor programEx = new ProgramExecutor();
//        long start = System.currentTimeMillis();
////        for (DatasetName name : DatasetName.values()) {
////            programEx.getReadingEx().execute(name);
////        }
//        Dataset ds21 = programEx.getReadingEx().execute(DatasetName.S21, Dimension.COMPLETE);

        programEx.getTwitterExtractionEx().execute();

//        long now = System.currentTimeMillis();
////        System.out.println("In: " + (now - start)/ 1000.0);
//        System.out.println();
//        System.out.println("Id is: " + ds21.getUsers().get(10).getIdString());
//
//        ConfigurationBuilder cfg = new ConfigurationBuilder();
//        cfg.setOAuthAccessToken("973247752401547264-lSfo9oTH7hVnSCoY2UgSlYfEyZLzMiD");
//        cfg.setOAuthAccessTokenSecret("qgVEBsbSGueCRNqw9LpHgy1DOddAAvJhqAD9vJo1tP3PE");
//        cfg.setOAuthConsumerKey("kYx4lTa9T0VXOdrCO8bvg5qIO");
//        cfg.setOAuthConsumerSecret("06XC6YyjP6acZ96rf3ATHe91t7HR4e3efZ0xhXMNpIMlDIyFH3");
//
//        cfg.setTweetModeExtended(true);
//
//        StatusListener listener = new StatusListener(){
//            public void onStatus(Status status) {
//                System.out.println(status.getUser().getName() + " : "
//                        + status.getText());
//            }
//            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {}
//            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {}
//            public void onScrubGeo(long l, long l1) {}
//            public void onStallWarning(StallWarning stallWarning) {}
//            public void onException(Exception ex) { ex.printStackTrace(); }
//        };
//        TwitterStream twitterStream = new TwitterStreamFactory(cfg.build()).getInstance();
//        twitterStream.addListener(listener);
////// sample() method internally creates a thread which manipulates TwitterStream
////// and calls these adequate listener methods continuously.
////        twitterStream.sample();
////
////        // filter() method internally creates a thread which manipulates TwitterStream
////// and calls these adequate listener methods continuously.
////        FilterQuery fq = new FilterQuery();
////        fq.language("it");
////        fq.track(new String[]{"ciao"});
////        twitterStream.filter(fq);
        }
}
