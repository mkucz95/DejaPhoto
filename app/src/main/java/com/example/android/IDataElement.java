package com.example.android;

import com.google.firebase.database.DatabaseReference;

public interface IDataElement{

    public boolean checkExist(String check);

    public boolean addElement();
    
    public DatabaseReference getRef(String[] info);
}
