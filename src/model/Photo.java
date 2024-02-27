package model;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

/**
 * @author Varun Venkateshan
 * @author Yashwant Balaji
 */
public class Photo implements Serializable {

    //Image File
    public File image;

    //Calendar used to keep the date of the photo
    public Calendar calendar;
    
    //Date of the photo
    public Date date;
    
    //Keeps a list of tags for the image
    public ArrayList<Tag> tags;

    //Photo Caption
    public String caption;

    //Constructors
    public Photo() {
        this.calendar = new GregorianCalendar();
        this.calendar.set(Calendar.MILLISECOND, 0);
        this.date = this.calendar.getTime();
        image = null;
        this.tags = new ArrayList<Tag>();
    }
    public Photo(File pic) {
        this.image = pic;
        this.tags = new ArrayList<Tag>();
        this.calendar = new GregorianCalendar();
        this.calendar.set(Calendar.MILLISECOND, 0);
        this.date = this.calendar.getTime();
    }

    //Getter for photo date
    public String getDate() {
        return date.toString();
    }

    //Getter for photo Date as Date Object
    public Date getActualDate() { return this.date; }

    //Getter for photo caption
    public String getCaption() {
        return this.caption;
    }

    //Getter for all Tags for a photo
    public ArrayList<Tag> getTags() {
        return this.tags;
    }

    //Getter for all tags
    public String getDisplayTags() {
        StringBuilder allTags = new StringBuilder();
        for (Tag t: this.tags) {
            allTags.append(t.getTag());
            allTags.append("  ");
        }
        return allTags.toString();
    }

    //Getter for the file object of image
    public File getFile() {
        return this.image;
    }

    //Setter for photo caption
    public void setCaption(String caption) throws IllegalArgumentException {
        if (caption == null || caption.trim().equals("")) {
            throw new IllegalArgumentException("Caption cannot be empty!");
        }
        this.caption = caption.trim();
    }

    //Add Tag to Photo
    public void addTag(Tag tag) throws IllegalArgumentException {
        String key = tag.getKey().trim();
        String value = tag.getValue().trim();
        for (Tag t: this.tags) {
            if (t.getKey().equalsIgnoreCase(key) && t.getValue().equalsIgnoreCase(value)) {
                throw new IllegalArgumentException("Cannot add the same tag to this photo!");
            }
            if(!t.getX() && t.getKey().equals(key)){
                throw new IllegalArgumentException("Key cannot have multiple different values");
            }
        }
        this.tags.add(tag);
        System.out.println("Successfully added Tag");
        for (Tag t: this.tags) {
            System.out.println(t.getKey() + " " + t.getValue());
        }
    }

    //Delete Tag from photo
    public void deleteTag(Tag tag) throws IllegalArgumentException {
        Iterator<Tag> iter = tags.iterator();
        String key = tag.getKey();
        String value = tag.getValue();
        while (iter.hasNext()) {
            Tag t = iter.next();
            if (t.getKey().equalsIgnoreCase(key) && t.getValue().equalsIgnoreCase(value)) {
                tags.remove(t);
                System.out.println(t.getKey() + " " + t.getValue());
                return;
            }
        }
        throw new IllegalArgumentException("Cannot find this tag!");

    }

    //Checks to see if images are the same
    public boolean sameImage(Photo photo) {
        try {
            if (Files.size(photo.image.toPath()) != Files.size(this.image.toPath())) {
                System.out.println("Files are not the same");
                return false;
            }
            byte[] first = Files.readAllBytes(photo.image.toPath());
            byte[] second = Files.readAllBytes(this.image.toPath());
            if (Arrays.equals(first,second)) {
                System.out.println("Files are the same");
            } else System.out.println("Files are not the same");
            return Arrays.equals(first, second);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Files are not the same");
        return false;
    }
}
