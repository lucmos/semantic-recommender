package twitteroperation;

import constants.DatasetName;
import io.IndexedSerializable;

import java.util.HashSet;

public abstract class Resp implements IndexedSerializable {

    /**
     * List of idStrings of private users
     */
    HashSet<String>  privateUserId;
    /**
     * List of idStrings of not existing users
     */
    HashSet<String> notExistingUserId;

    /**
     * List of idStrings of not existing users
     */
    HashSet<String> suspendedUserId;
//    /**
//     * Tweeter respons of the request
//     */
//    private HashMap<String, Object> results;
    /**
     * Dataset from which users have been taken
     */
    DatasetName originDataset;

    public Resp(DatasetName originDataset){
        this.originDataset = originDataset;
        this.privateUserId = new HashSet<String>();
        this.notExistingUserId = new HashSet<String>();
        this.suspendedUserId = new HashSet<>();
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

    public void addNotExistingUserId(String newNotExistingUserId){this.notExistingUserId.add(newNotExistingUserId);}

    public HashSet<String> getNotExistingUserId() {
        return notExistingUserId;
    }

    public void addSuspendedUserId(String user) {
        this.suspendedUserId.add(user);
    }

    public HashSet<String> getSuspendedUserId() {
        return suspendedUserId;
    }


    //    public void addResult(String id, Object value){this.results.put(id, value);}
//
//    public HashMap<String, Object> getResults() {
//        return results;
//    }
}
