package com.example.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import java.io.File;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Justin on 5/4/17.
 */

public class DisplayCycle {
    private ImageNode first; //first node to keep track of circle
    private ImageNode last; //last node in list used to wrap around
    private ImageNode head; //tracks current position in cycle
    private int cycleLength = 0;

    //TODO not correct implementation
    public void addToCycle(String picPath){//add new node at the end of list
        if(first == null && last == null){
            ImageNode newNode = new ImageNode(new Photo(picPath), last, first);
            this.first = newNode;
            this.last = newNode;
        }
        else {
            ImageNode newNode = new ImageNode(new Photo(picPath), last, first); //create new node object
            last.setNext(newNode);
            first.setPrev(newNode);
            last = newNode;
        }
        cycleLength++;
        head = last; //our first image is the last one in the list
    }

    public DisplayCycle(boolean flag){ //empty constructor
        this.first = null;
        this.last=null;
        this.head = null;

        buildDisplayCycle(flag);
    }

    public String getImage(boolean next){
        String imgPath;
        if(next){ //return next image in the cycle
            imgPath = (String) head.getNext().getPath();
        }
        else{//return previous image
            imgPath = (String) head.getPrev().getPath();
        }
        return imgPath;
    }

    public String updateCycle(boolean flag){
        //get sharedpreferences to see what the dejavu mode settings are
        //TODO implement this method that reranks based on button presses.
        //iteration 2
        if(flag){
            //increment karma for current picture
            //rerank pictures
            head.getData().setKarma(true);  //this picture now has karma
            head = rerank(this); //TODO not sure if this is done right

            return head.getPath();
        }else{
            //remove current picture from display cycle
            //move head
           head =  deleteNode(head);
            if(head==null){ //if there are now no images in the display cycle
                return "DEFAULTPICTURE";
            }
            return head.getPath();
        }
    }

    public ImageNode deleteNode(ImageNode current){
       if(cycleLength==1){
           return null;
       }
       else {
           ImageNode temp = current;
           ImageNode next = current.getNext();
           ImageNode previous = current.getPrev();

           temp = previous;
           temp.setNext(next);

           return temp;
       }

       //delete from storage if not in camera folder
    }

    public ImageNode rerank(DisplayCycle displayCycle){
        //TODO
        //get shared preferences about settings (location, time, day)
        //sort displaycycle by the settings
        return displayCycle.last;
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

