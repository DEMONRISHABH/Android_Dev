package com.mastercoding.helloworld2.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.mastercoding.helloworld2.db.entity.Contact;

@Database(entities = {Contact.class},version = 1)
public abstract class ContactsAppDatabase extends RoomDatabase {

    // Linking the DAO with our Database
    public abstract ContactDAO getContactDAO();



}
