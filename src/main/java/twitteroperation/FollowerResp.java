package twitteroperation;

import constants.DatasetName;

import java.util.HashMap;
import java.util.HashSet;

public class FollowerResp extends Resp
{

    /**
     * Tweeter respons of the request
     */
    private HashMap<String, HashSet<String>> results;

//    public FollowerResp(){
//        super(null);
//    }

    public FollowerResp(DatasetName originDataset){
        super(originDataset);
        this.results = new HashMap<>();
    }

    public void addResult(String id, HashSet<String> value){this.results.put(id, value);}

    public HashMap<String, HashSet<String>> getResults(){return this.results;}

    public String toString(){
        return ("REPORT OF THE USER SEARCH:\n"+
                "Origin dataset = "+this.getOriginDataset()+"\n"+
                "# not existing users = " + this.getNotExistingUserId().size() +"\n"+
                "# private users = " + this.getPrivateUserId().size() +"\n"
                + "# voices in the report "+this.getResults().size());
    }


//    public void saveFollowerResp(boolean pretty, String datasetName) {
//        Utils.saveJson(this, TwitterRespPath.FRIENDS_RESP.getPath(datasetName), pretty);
//    }


}
