package twittermodel;

import constants.DatasetName;
import io.Utils;
import properties.Config;
import utils.IndexedSerializable;
import utils.OneToOneHash;

import javax.rmi.CORBA.Util;
import java.io.Serializable;

/**
 * A generic twitter object.
 * It always has an integer identifier
 * It may have a literal identifier
 */
public abstract class ObjectModel implements IndexedSerializable {
    /**
     * The integer identifier. It is globally unique inside the same class
     * and automatically generated.
     */
    private int seqId;

    /**
     * The literal identifier present in the dataset. If present, it is unique.
     */
    private String idString;


    ObjectModel(int seqId, String idString){
        this.setId(seqId);
        this.setIdString(idString);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ObjectModel)) return false;
        return seqId == ((ObjectModel) obj).seqId;
    }

    private void setId(int seqId) {
        assert seqId >= 0;

        this.seqId = seqId;
    }

    public int getId() {
        assert seqId >= 0;

        return seqId;
    }

    public String getIdString() {
        assert idString != null && !idString.equals("");

        return idString;
    }

    private void setIdString(String idString) {
        assert idString != null && !idString.equals("");

        this.idString = idString;
    }
}
