package com.example.android;

/**
 * Created by Justin on 5/4/17.
 */

public class ImageNode<T> {

    private Photo data;
    private  ImageNode<Photo> next;
    private  ImageNode<Photo> previous;


    public Photo getData() {
        return data;
    }

    public ImageNode(Photo data){
        this.data = data;
        this.next = null;
        this.previous = null;
    }

    public ImageNode(Photo data, ImageNode<Photo> next, ImageNode<Photo> previous ) {
        this.data = data;
        this.next = next;
        this.previous = previous;
    } //constructor for imageNode with data passed in

    public String getPath(){

        return data.getImagePath();
    }

    //Settter, getter methods for next node
    public void setNext(ImageNode<Photo> node){this.next = node;}
    public ImageNode getNext(){return this.next;}

    public void setPrev(ImageNode<Photo> node){
        this.previous = node;
    }

    public ImageNode getPrev(){
        return this.previous;
    }
}
