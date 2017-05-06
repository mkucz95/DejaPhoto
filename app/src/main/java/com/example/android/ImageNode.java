package com.example.android;

/**
 * Created by Justin on 5/4/17.
 */

public class ImageNode<String>{
    private String data;
    private  ImageNode<String> next;
    private  ImageNode<String> previous;


   public ImageNode(){ //default constructor. all fields are set to null
        next=null;
        previous = null;
        data=null;
    }

    public ImageNode(String data){
        this.data = data;
        this.next = null;
        this.previous = null;
    }

    public ImageNode(String data, ImageNode<String> next, ImageNode<String> previous ) {
        this.data = data;
        this.next = next;
        this.previous = previous;
    } //constructor for imageNode with data passed in

    public String getData(){
        return this.data;
    }

    //Settter, getter methods for next node
    public void setNext(ImageNode<String> node){
        this.next = node;
    }

    public ImageNode getNext(){
        return this.next;
    }

    public void setPrev(ImageNode<String> node){
        this.previous = node;
    }

    public ImageNode getPrev(){
        return this.previous;
    }
}
