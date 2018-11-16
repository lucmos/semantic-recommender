package twitteroperation;

import constants.DatasetName;
import io.IndexedSerializable;
import model.twitter.Dataset;
import twitter4j.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public abstract class Resp implements IndexedSerializable {

    /**
     * List of idStrings of private users
     */
    private HashSet<String>  privateUserId;
    /**
     * List of idStrings of not existing users
     */
    private HashSet<String> notExistingId;
//    /**
//     * Tweeter respons of the request
//     */
//    private HashMap<String, Object> results;
    /**
     * Dataset from which users have been taken
     */
    private  DatasetName originDataset;

    public Resp(DatasetName originDataset){
        this.originDataset = originDataset;
        this.privateUserId = new HashSet<String>();
        this.notExistingId = new HashSet<String>();
//        this.results = new HashMap<String, Object>();
    }

    public Resp(){
        this(null);
    }


    public DatasetName getOriginDataset() {
        return originDataset;
    }

    public void addPrivateUserId(String newPrivateId){this.privateUserId.add(newPrivateId);}

    public HashSet<String> getPrivateUserId() {
        return privateUserId;
    }

    public void addNotExistingId(String newNotExistingId){this.notExistingId.add(newNotExistingId);}

    public HashSet<String> getNotExistingId() {
        return notExistingId;
    }


//    public void addResult(String id, Object value){this.results.put(id, value);}
//
//    public HashMap<String, Object> getResults() {
//        return results;
//    }
}
