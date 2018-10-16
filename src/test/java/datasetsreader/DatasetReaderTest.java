package datasetsreader;

import org.junit.Test;
import twittermodel.ModelFactory;
import twittermodel.UserModel;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class DatasetReaderTest {

    @Test
    public void readDataset() {
    }

    @Test
    public void addUserFollowed() {
        Dataset d = new Dataset(null);
        String id1 = "1";
        String id2 = "2";

        ArrayList<String> couple1 = new ArrayList<>();
        couple1.add(id1);
        couple1.add(id2);

        DatasetReader.addRow_friendBased_dataset(couple1, d);
        assertEquals(2, d.getUsers().size());

        UserModel u1 = ModelFactory.getUser(id1);
        UserModel u2 = ModelFactory.getUser(id2);

        assertEquals(1, u1.getFollowOut().size());
        assertTrue(u1.getFollowOut().contains(u2));
        assertTrue(u1.getFollowIn().isEmpty());

        assertTrue(u2.getFollowIn().contains(u1));
        assertTrue(u2.getFollowOut().isEmpty());

        DatasetReader.addRow_friendBased_dataset(couple1, d);
        assertEquals(2, d.getUsers().size());

        UserModel u11 = ModelFactory.getUser(id1);
        UserModel u22 = ModelFactory.getUser(id2);

        assertEquals(1, u11.getFollowOut().size());
        assertTrue(u11.getFollowOut().contains(u2));
        assertTrue(u11.getFollowIn().isEmpty());

        assertEquals(1, u22.getFollowIn().size());
        assertTrue(u22.getFollowIn().contains(u1));
        assertTrue(u22.getFollowOut().isEmpty());
    }

    @Test
    public void addUserCorrespondingInterest() {
    }

    @Test
    public void addUserTweetInterestURL() {
    }

    @Test
    public void addInterestPlatformPage() {
    }

    @Test
    public void addUser() {
    }

    @Test
    public void addUserFollowedPage() {
    }

}