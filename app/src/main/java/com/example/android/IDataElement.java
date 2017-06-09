package com.example.android;

import com.google.firebase.database.DatabaseReference;

public interface IDataElement{

    public boolean checkExist(String check);

    public void addElement();
    
    public DatabaseReference getRef();
}
