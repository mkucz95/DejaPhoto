package com.example.android;

import android.media.Image;
import android.os.Environment;

import java.io.File;

/**
 * Created by Justin on 5/4/17.
 */

public class DisplayCycle {
    private ImageNode head; //first node of the linked list. also current node
    private ImageNode first; //first node to keep track of circle
    private ImageNode last; //last node in list used to wrap around

    public void removeNode(ImageNode image){ //rearrange pointers
        ImageNode prev = image.getPrev();
        ImageNode next = image.getNext();
        prev.setNext(next);
        next.setPrev(prev);
    }

    public void addToCycle(String picPath){//add new node at the end of list
        ImageNode newNode = new ImageNode(picPath, last, first);
        last.setNext(newNode);
        first.setPrev(newNode);
        last = newNode;
    }


    public void DisplayCycle(){ //empty constructor
        this.head = null;
        this.first = null;

        buildDisplayCycle(false);
    }

    public void DisplayCycle(boolean flag){ //empty constructor
        this.head = null;
        this.first = null;

        buildDisplayCycle(true);
    }

    private void buildDisplayCycle(boolean flag) {
         if(flag) { //create display cycle from Camera Roll
             File dcimDirectory = new File(Environment.getExternalStorageDirectory(), "DCIM"); //get path to DCIM folder
             File[] dcimPhotos = dcimDirectory.listFiles();
             if(dcimPhotos != null) { //DCIM contains photos
                 for (File currPicture : dcimPhotos) {
                     addToCycle(currPicture.getAbsolutePath());
                 }
             }
             else{
                 //No photos in DCIM, display default image
                 //TODO: addToCycle(DEFAULT_IMAGE);
             }
         }

         else { //create display cycle from DejaPhoto album folder
             File dejaPhotoDirectory = new File(Environment.getExternalStorageDirectory(), "DejaPhoto"); //get path to DejaPhoto folder
             if(dejaPhotoDirectory.exists()){
                 File[] dejaPhotos = dejaPhotoDirectory.listFiles();
                 if (dejaPhotos != null){ //DejaPhoto album exists, and contains photos
                     for (File currPicture : dejaPhotos) {
                         addToCycle(currPicture.getAbsolutePath());
                     }
                 }
                 else{ //DejaPhoto album exists, but empty. Display default image
                     //TODO: addToCycle(DEFAULT_IMAGE);
                 }
             }
             else{
                 //DejaPhoto Album does not exist. Create one and display default image
                 File newFolder = new File(Environment.getExternalStorageDirectory() + File.separator + "DejaPhoto");
                 newFolder.mkdirs();
                 //TODO: addToCycle(DEFAULT_IMAGE);
             }
         }
    }
}

