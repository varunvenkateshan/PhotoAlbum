package model;

import java.io.Serializable;
import java.util.*;

/**
 * @author Varun Venkateshan
 * @author Yashwant Balaji
 */
public class Album implements Serializable {

    //Album Name
    public String albumName;
    
    //Photos ArrayList within Album
    public ArrayList<Photo> photos;

    //Constructor
    public Album(String albumName) {
        this.albumName = albumName;
        photos = new ArrayList<Photo>();
    }

    //Getter for Album Name
    public String getName() {
        return this.albumName;
    }

    //Setter for Album Name
    public void setName(String albumName) {
        this.albumName = albumName;
    }

    //Getter for photos in Album
    public ArrayList<Photo> getPhotos() {
        return this.photos;
    }
    
    //Getter for Size of Album
    public int numPhotos() {
        return photos.size();
    }

    //Boolean return for adding a photo, checking to see if phot exists in album already
    public boolean addPhotoBoolean(ArrayList<Photo> p, Photo photo){
        for (Photo i: p){
            if (i.sameImage(photo)){
                return false;
            }
        }
        return true;
    }

    public ArrayList<Date> getRangeOfPhotos() {
        ArrayList<Date> rangeOfDates = new ArrayList<Date>();
        ArrayList<Photo> copy = this.photos;
        if (copy.size() == 0) return null;
        if (copy.size() == 1) {
            rangeOfDates.add(copy.get(0).getActualDate());
            return rangeOfDates;
        }
        Collections.sort(copy, new Comparator<Photo>() {
            @Override
            public int compare(Photo p1, Photo p2) {
                return p1.getActualDate().compareTo(p2.getActualDate());
            }
        });

        rangeOfDates.add(copy.get(0).getActualDate());
        rangeOfDates.add(copy.get(copy.size()-1).getActualDate());
        return rangeOfDates;
    }

    //Add Photo
    public void addPhoto(Photo photo) throws IllegalArgumentException {
        for (Photo i: photos) {
            boolean samePhoto = false;
            if (i.sameImage(photo)) {
                samePhoto = true;
            }
            if (samePhoto) throw new IllegalArgumentException("The same photo cannot be added to the same album!");
        }
        photos.add(photo);
    }

    //Delete Photo
    public void deletePhoto(Photo photo) throws IllegalArgumentException {
        Iterator<Photo> iter = photos.iterator();
        while (iter.hasNext()) {
            Photo p = iter.next();
            boolean samePhoto = false;
            if (p.sameImage(photo)) {
                samePhoto = true;
            }
            if (samePhoto) {
                photos.remove(p);
                return;
            }
        }
        throw new IllegalArgumentException("Cannot find this photo in this album!)");
    }

}