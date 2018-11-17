package twitteroperation;

import constants.DatasetName;

import java.util.HashMap;
import java.util.List;

public class TweetsResp extends Resp{

    /**
     * Tweeter respons of the request
     */
    HashMap<String, List<String>> results;

    public TweetsResp(){
        this(null);
    }

    public TweetsResp(DatasetName originDataset){
        super(originDataset);
        this.results = new HashMap<String, List<String>>();
    }

    public void addResult(String id, List<String> value){this.results.put(id, value);}

    public HashMap<String, List<String>> getResults(){return this.results;}

    public String toString(){
        return ("REPORT OF THE TWEET SEARCH:\n"+
                "Origin dataset = "+this.getOriginDataset()+"\n"+
                "# not existing users = " + this.getNotExistingUserId().size() +"\n"+
                "# private users = " + this.getPrivateUserId().size() +"\n"
                + "# voices in the report "+this.getResults().size());
    }

//    public void saveTweetResp(boolean pretty, String datasetName){
//        Utils.saveJson(this, TwitterRespPath.TWEETS_RESP.getPath(datasetName), pretty);
//    }

}
