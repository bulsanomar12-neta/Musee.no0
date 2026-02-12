package com.example.musee.classes;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class FirebaseServices {
    private static  FirebaseServices instance;
    private FirebaseAuth auth;
    private FirebaseFirestore fire;
    private FirebaseStorage storage;
    private Uri selectedImageURL;
    private boolean userChangeFlag;
    private ArrayList<User> users;




    public FirebaseServices(){
        auth = FirebaseAuth.getInstance();
        fire = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        users = new ArrayList<>();
        selectedImageURL = null;
    }
    public Uri getSelectedImageURL() {
        return selectedImageURL;
    }

    public void setSelectedImageURL(Uri selectedImageURL) {
        this.selectedImageURL = selectedImageURL;
    }

    public FirebaseStorage getStorage() {
        return storage;
    }

    public FirebaseFirestore getFire() {
        return fire;
    }

    public FirebaseAuth getAuth() {
        return auth;
    }

    public static FirebaseServices getInstance(){
        if (instance == null) {
            instance = new FirebaseServices();
        }
        return instance ;
    }
    /*
    public void setSelectedImageURL(Uri selectedImageURL) {
        this.selectedImageURL = selectedImageURL;
    }
     */
    public void setUserChangeFlag(boolean userChangeFlag) {
        this.userChangeFlag = userChangeFlag;
    }

    public ArrayList<User> getUsers() {
        return users;
    }
    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public boolean updateUser(User user)
    {
        final boolean[] flag = {false};
        // Reference to the collection
        String collectionName = "users";
        String firstNameFieldName = "firstName";
        String firstNameValue = user.getFirstName();
        String lastNameFieldName = "lastName";
        String lastNameValue = user.getLastName();
        String usernameFieldName = "userName";
        String usernameValue = user.getUserName();
        String addressFieldName = "address";
        String addressValue = user.getAddress();
        String phoneFieldName = "phoneNum";
        String phoneValue = user.getPhoneNum();
        //String photoFieldName = "photo";
        //String photoValue = user.getPhoto();

        // Create a query for documents based on a specific field
        Query query = fire.collection(collectionName).
                whereEqualTo(usernameFieldName, usernameValue);

        // Execute the query
        query.get()
                .addOnSuccessListener((QuerySnapshot querySnapshot) -> {
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        // Get a reference to the document
                        DocumentReference documentRef = document.getReference();

                        // Update specific fields of the document
                        documentRef.update(
                                        firstNameFieldName, firstNameValue,
                                        lastNameFieldName, lastNameValue,
                                        usernameFieldName, usernameValue,
                                        addressFieldName, addressValue,
                                        phoneFieldName, phoneValue
                                        //photoFieldName, photoValue
                                )
                                .addOnSuccessListener(aVoid -> {

                                    flag[0] = true;
                                })
                                .addOnFailureListener(e -> {
                                    System.err.println("Error updating document: " + e);
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    System.err.println("Error getting documents: " + e);
                });
        return flag[0];
    }

}
