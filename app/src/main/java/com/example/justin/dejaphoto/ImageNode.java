package com.example.justin.dejaphoto;

/**
 * Created by Justin on 5/4/17.
 */

public class ImageNode<Image>{
    private Image data;
    private  ImageNode<Image> next;
    private  ImageNode<Image> previous;


    ImageNode(){ //default constructor. all fields are set to null
        next=null;
        previous = null;
        data=null;
    }

    public ImageNode(Image data){
        this.data = data;
        this.next = null;
        this.previous = null;
    }

    public ImageNode(Image data, ImageNode<Image> next, ImageNode<Image> previous ) {
        this.data = data;
        this.next = next;
        this.previous = previous;
    } //constructor for imageNode with data passed in

    Image getData(){
        return this.data;
    }

    //Settter, getter methods for next node
    public void setNext(ImageNode<Image> node){
        this.next = node;
    }

    public ImageNode getNext(){
        return this.next;
    }

    public void setPrev(ImageNode<Image> node){
        this.previous = node;
    }

    public ImageNode getPrev(){
        return this.previous;
    }
}
