package model;

import java.io.*;
import java.util.ArrayList;

/**
 * @author Varun Venkateshan
 * @author Yashwant Balaji
 */
public class Serialize implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String storeDir = "data";
    public static final String storeFile = "users.dat";

    // Reading from Storage File
    public static ArrayList<User> readApp() throws IOException, ClassNotFoundException {
        ObjectInputStream objInputStrem = new ObjectInputStream(
                new FileInputStream(storeDir + File.separator + storeFile));
        ArrayList<User> users = (ArrayList<User>) objInputStrem.readObject();
        return users;
    }

    // Writing to Storage File
    public static void writeApp(ArrayList<User> users) throws IOException {
        ObjectOutputStream objOutputStrem = new ObjectOutputStream(
                new FileOutputStream(storeDir + File.separator + storeFile));
        objOutputStrem.writeObject(users);
        objOutputStrem.close();
    }

}
