package pij.ryan.durling.serializers;

import pij.ryan.durling.controllers.Contacts;
import pij.ryan.durling.controllers.Meetings;

public interface Serializers {

    /**
     * serialize meetings and contacts
     *
     * @param meetings
     * @param contacts
     */
    public void serialize(Meetings meetings, Contacts contacts);

    /**
     * @return an object array containing the deserialized meetings and contacts
     */
    public Object[] deserialize();

    /**
     * check to see if the data file exists
     *
     * @return true if it does and false otherwise
     */
    public boolean dataExists();

    /**
     * set the file name for the for persistence
     *
     * @param fileName
     */
    public void setFileName(String fileName);
}
