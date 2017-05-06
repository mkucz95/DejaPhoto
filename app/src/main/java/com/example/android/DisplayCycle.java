package com.example.android;

import android.media.Image;
import android.os.Environment;

import java.io.File;

/**
 * Created by Justin on 5/4/17.
 */

public class DisplayCycle {
    private ImageNode first; //first node to keep track of circle
    private ImageNode last; //last node in list used to wrap around
    private ImageNode head; //tracks current position in cycle
    private int cycleLength = 0;

    public void removeNode(ImageNode image){ //rearrange pointers
        ImageNode prev = image.getPrev();
        ImageNode next = image.getNext();
        prev.setNext(next);
        next.setPrev(prev);
    }

    //TODO not correct implementation
    public void addToCycle(String picPath){//add new node at the end of list
        if(first == null && last == null){
            ImageNode newNode = new ImageNode(picPath, last, first);
            this.first = newNode;
            this.last = newNode;
        }
        else {
            ImageNode newNode = new ImageNode(picPath, last, first); //create new node object
            last.setNext(newNode);
            first.setPrev(newNode);
            last = newNode;
        }

        this.cycleLength++;
    }

    public void DisplayCycle(){ //empty constructor
        this.first = null;
        this.last = null;
        this.head = null;

        buildDisplayCycle(false);
    }

    public void DisplayCycle(boolean flag){ //empty constructor
        this.first = null;
        this.last=null;
        this.head = null;

        buildDisplayCycle(flag);
    }

    public String getImage(boolean next){
        String imgPath;
        if(next){ //return next image in the cycle
            imgPath = (String) head.getNext().getData();
        }
        else{//return previous image
            imgPath = (String) head.getPrev().getData();
        }
        return imgPath;
    }

    public String updateCycle(){
        //get sharedpreferences to see what the dejavu mode settings are
        //TODO
        return (String) last.getData();
    }

    private void buildDisplayCycle(boolean flag) {
         if(flag) { //create display cycle from Camera Roll
             File dcimDirectory = new File(Environment.getExternalStorageDirectory(), "DCIM"); //get path to DCIM folder
             File cameraDirectory = new File (dcimDirectory.getAbsolutePath()+"/Camera"); //TODO


             File[] dcimPhotos = cameraDirectory.listFiles();
             if(dcimPhotos != null) { //DCIM contains photos
                 for (File currPicture : dcimPhotos) { //add each photo's path to cycle as a node
                     addToCycle(currPicture.getAbsolutePath());
                 }
             }
             else{
                 addToCycle("DEFAULTPICTURE");
             }
         }

         //todo NOTE: THIS FEATURE HAS BEEN POSTPONED. FIRST MILESTONE TO JUST CAMERA ALBUM
        /* else { //create display cycle from DejaPhoto album folder
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
         }*/
    }
}

