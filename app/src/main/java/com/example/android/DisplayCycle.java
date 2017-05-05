package com.example.android;

import android.media.Image;

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

    public void addToCycle(Image newImage){//add new node at the end of list
        ImageNode newNode = new ImageNode(newImage, last, first);
        last.setNext(newNode);
        first.setPrev(newNode);
        last = newNode;
    }


    public void DisplayCycle(){ //empty constructor
        this.head = null;
        this.first = null;
    }

    public DisplayCycle(Image newImage){ //called when new display cycle object is called
        this.head = new ImageNode(newImage);
        this.first = this.head;
        this.last.setNext(first);

    }
}

