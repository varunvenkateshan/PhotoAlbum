package model;

import java.io.Serializable;

/**
 * @author Varun Venkateshan
 * @author Yashwant Balaji
 */
public class Tag implements Serializable {

    //Tag Key
    public String key;
    //Tag Value
    public String value;
    //Boolean for multiple values
    public boolean multiValues;

    //Constructor
    public Tag(String key, String value, boolean multiValues) {
        this.key = key;
        this.value = value;
        this.multiValues = multiValues;
    }

    //Getter for Key and Value pair
    public String getTag() {
        return "( " + this.key + ", " + this.value + " )";
    }

    //Getter for Key
    public String getKey() {
        return this.key;
    }

    //Getter for Value
    public String getValue() {
        return this.value;
    }

    //Getter for Multiple Values boolean
    public boolean getX() {return this.multiValues;}

}
