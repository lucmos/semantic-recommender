package twittermodel;

import java.io.Serializable;

public abstract class TwitterObjectModel implements Serializable
{
    private int id;

    public void setId(int id)
    {
        this.id=id;
    }

    public int getId() {
        return id;
    }
}
