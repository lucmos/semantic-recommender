package twitteroperation;
import constants.DatasetName;
import java.util.HashMap;
import java.util.HashSet;

public class FollowerResp extends Resp
{
    /**
     * Tweeter respons of the follower request of the users read in a dataset with a UserInfoExtractor
     */
    private HashMap<String, HashSet<String>> results;

    /**
     * The constructor
     * @param originDataset the dataset from which have been made the Follower request
     */
    public FollowerResp(DatasetName originDataset){
        super(originDataset);
        this.results = new HashMap<>();
    }

    /**
     * Add a result to the response
     * @param id the id of the user that is the follower
     * @param value the results, so the following of the users
     */
    public void addFriends(String id, HashSet<String> value){this.results.put(id, value);}

    /**
     * Return the results
     * @return the hasmap that maps users with the lists of their following
     */
    public HashMap<String, HashSet<String>> getUser2Friends(){return this.results;}

    /**
     * Makes a String that represents the object
     * @return the string
     */
    public String toString(){
        return ("REPORT OF THE USER SEARCH:\n"+
                "Origin dataset = "+this.getOriginDataset()+"\n"+
                "# not existing users = " + this.getNotExistingUserId().size() +"\n"+
                "# private users = " + this.getPrivateUserId().size() +"\n"
                + "# voices in the report "+this.getUser2Friends().size());
    }
}
