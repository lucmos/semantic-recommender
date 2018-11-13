package twitteroperation;

import constants.DatasetName;
import twitter4j.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class TweetsResp extends Resp{

    /**
     * Tweeter respons of the request
     */
    private HashMap<String, List<Status>> results;

    public TweetsResp(DatasetName originDataset){
        super(originDataset);
        this.results = new HashMap<String, List<Status>>();
    }

    public void addResult(String id, List<Status> value){this.results.put(id, value);}

    public HashMap<String, List<Status>> getResults(){return this.results;}

    public String toString(){
        return ("REPORT OF THE TWEET SEARCH:\n"+
                "Origin dataset = "+this.getOriginDataset()+"\n"+
                "# not existing users = " + this.getNotExistingId().size() +"\n"+
                "# private users = " + this.getPrivateUserId().size() +"\n"
                + "# voices in the report "+this.getResults().size());
    }

}
