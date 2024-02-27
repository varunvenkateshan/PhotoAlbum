package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * This class is responsible for creating a User Instance which is populated in
 * the UsersList ArrayList
 *
 * @author Varun Venkateshan
 * @author Yashwant Balaji
 */
public class User implements Serializable {
    // Username
    public String username;
    // Album arrayList for user
    public ArrayList<Album> albums;
    public ArrayList<String> presetTags;

    // Constructor to create a user with a username input
    public User(String username) {
        this.username = username;
        this.albums = new ArrayList<Album>();
        this.presetTags = new ArrayList<String>();
        presetTags.add("batsman");
        presetTags.add("bowler");
        presetTags.add("teamIndia");
    }

    // Add Preset Tag to Photo
    public void addPreset(Tag tag) {
        for (int i = 0; i < presetTags.size(); i++) {

            if (presetTags.get(i).equals(tag.getKey())) {
                return;
            }
        }
        presetTags.add(tag.getKey());
    }

    // Prints Preset Tags
    public String printPreset() {
        StringBuilder allPresetTags = new StringBuilder();
        for (String i : presetTags) {
            allPresetTags.append(i);
            allPresetTags.append(", ");
        }
        return allPresetTags.toString();
    }

    // Delete Preset Tags
    public void deletePreset(Tag tag) {
        if (tag.getKey().equals("batsman") || tag.getKey().equals("bowler")) {
            return;
        }
        for (int i = 0; i < presetTags.size(); i++) {
            if (presetTags.get(i).equals(tag.getKey())) {
                presetTags.remove(i);
            }
        }
    }

    // Getter for username
    public String getUsername() {
        return this.username;
    }

    // Getter for Albums of User
    public ArrayList<Album> getAlbums() {
        return albums;
    }

    // Getter for Album with specified name
    public Album getAlbum(String name) {
        for (Album a : this.albums) {
            System.out.println("the name of the album is: " + a.getName());
            if (a.getName().equalsIgnoreCase(name)) {
                return a;
            }
        }
        return null;
    }

    // Getter for Album with name as input, returns Album obj
    public Album getAlbumWithName(String name) {
        for (Album a : this.albums) {
            if (a.getName().equals(name))
                return a;
        }
        return null;
    }

    // Returns size of Album
    public int numberOfAlbums() {
        return albums.size();
    }

    // Add Album
    public void addAlbum(Album album) throws IllegalArgumentException {
        if (album == null) {
            throw new IllegalArgumentException("Album cannot be null!");
        }
        if (album.getName().isEmpty()) {
            throw new IllegalArgumentException("Please enter a name for the Album");
        }
        for (Album a : albums) {
            if (a.getName().equalsIgnoreCase(album.getName()))
                throw new IllegalArgumentException("You cannot add an album with a name that already exists!");
        }
        albums.add(album);
    }

    // Album Editer
    public void editAlbum(String oldName, String newName) throws IllegalArgumentException {
        if (newName.trim().equals("") || newName.equals(null)) {
            throw new IllegalArgumentException("You cannot add an album with no name!");
        }
        for (Album a : albums) {
            if (a.getName().equalsIgnoreCase(newName) && !oldName.equalsIgnoreCase(newName)) {
                throw new IllegalArgumentException("You cannot add an album with the same name!");
            }
        }
        boolean noAlbumExists = true;
        for (Album a : albums) {
            if (a.getName().equals(oldName)) {
                a.setName(newName.trim());
                noAlbumExists = false;
            }
        }
        if (noAlbumExists) {
            throw new IllegalArgumentException(oldName + " does not exist!");
        }

    }

    // Delete Album
    public void deleteAlbum(String name) throws IllegalArgumentException {
        if (name == null || name.isEmpty())
            throw new IllegalArgumentException("Please enter an album to delete!");
        boolean noAlbumExists = true;
        Iterator<Album> iter = albums.iterator();
        while (iter.hasNext()) {
            Album a = iter.next();
            if (a.getName().equalsIgnoreCase(name)) {
                iter.remove();
                noAlbumExists = false;
            }
        }
        if (noAlbumExists) {
            throw new IllegalArgumentException("Album with that name does not exist!");
        }
    }

    // Copy Photo from Album
    public void copyPhoto(Photo photo, Album start, Album end) throws IllegalArgumentException {
        if (start == null || end == null)
            throw new IllegalArgumentException("Album does not exist!");
        boolean startAlbumDoesNotExist = true;
        boolean endAlbumDoesNotExist = true;
        Album startAlbum = null;
        Album endAlbum = null;
        for (Album a : this.albums) {
            if (a.getName().equals(start.getName())) {
                startAlbumDoesNotExist = false;
                startAlbum = a;
            }

            if (a.getName().equals((end.getName()))) {
                endAlbumDoesNotExist = false;
                endAlbum = a;
            }
        }
        if (startAlbumDoesNotExist || endAlbumDoesNotExist) {
            throw new IllegalArgumentException("Album does not exist!");
        }
        boolean photoExistsinAlbum = false;
        Photo photoInAlbum = null;
        for (Photo p : startAlbum.getPhotos()) {
            if (p.sameImage(photo)) {
                photoExistsinAlbum = true;
                photoInAlbum = p;

            }
        }
        if (!photoExistsinAlbum) {
            throw new IllegalArgumentException("This photo does not exist in this album!");
        }
        end.addPhoto(photoInAlbum);
    }

    // Moving a photo to a different album
    public void movePhoto(Photo photo, Album start, Album end) throws IllegalArgumentException {
        // makes sure either album is not null
        if (start == null || end == null)
            throw new IllegalArgumentException("Album does not exist!");
        boolean startAlbumDoesNotExist = true;
        boolean endAlbumDoesNotExist = true;
        Album startAlbum = null;
        Album endAlbum = null;
        for (Album a : this.albums) {
            if (a.getName().equals(start.getName())) {
                startAlbumDoesNotExist = false;
                startAlbum = a;
            }
            if (a.getName().equals((end.getName()))) {
                endAlbumDoesNotExist = false;
                endAlbum = a;
            }
        }
        if (startAlbumDoesNotExist || endAlbumDoesNotExist) {
            throw new IllegalArgumentException("Album does not exist!");
        }
        boolean photoExistsinAlbum = false;
        Photo photoInAlbum = null;
        for (Photo p : startAlbum.getPhotos()) {
            if (p.sameImage(photo)) {
                photoExistsinAlbum = true;
                photoInAlbum = p;

            }
        }
        if (!photoExistsinAlbum) {
            throw new IllegalArgumentException("This photo does not exist in this album!");
        }
        end.addPhoto(photoInAlbum);
        start.deletePhoto(photoInAlbum);
    }

}
