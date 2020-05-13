package com.example.myplaces;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MyPlacesData {

    private ArrayList<MyPlaces> myPlaces;
    private HashMap<String, Integer> myPlacesKeyIndexMapping;
    private DatabaseReference database;
    private static final String FIREBASE_CHILD = "myplaces-82ed1";

    private MyPlacesData() {
        myPlaces = new ArrayList<>();
        myPlacesKeyIndexMapping = new HashMap<String, Integer>();
        database = FirebaseDatabase.getInstance().getReference();
        database.child(FIREBASE_CHILD).addChildEventListener(childEventListener);
        database.child(FIREBASE_CHILD).addListenerForSingleValueEvent(parentEventListener);


//        myPlaces.add(new MyPlaces("PLace A", "Place a", "41.2356", "21.36589"));
//        myPlaces.add(new MyPlaces("PLace B", "Place b", "42.2356", "21.36589"));
//        myPlaces.add(new MyPlaces("PLace C","Place c", "43.2356", "21.36589"));
//        myPlaces.add(new MyPlaces("PLace D","Place d", "44.2356", "21.36589"));
//        myPlaces.add(new MyPlaces("PLace E","Place e", "45.2356", "21.36589"));
    }

    ValueEventListener parentEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if(updateListener != null)
            {
                updateListener.onListUpdated();
            }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            String myPlaceKey = dataSnapshot.getKey();
            if (!myPlacesKeyIndexMapping.containsKey(myPlaceKey)) {
                MyPlaces myplace = dataSnapshot.getValue(MyPlaces.class);
                myplace.key = myPlaceKey;
                myPlaces.add(myplace);
                myPlacesKeyIndexMapping.put(myPlaceKey, myPlaces.size() - 1);
                if(updateListener != null)
                {
                    updateListener.onListUpdated();
                }
            }

        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            String myPlaceKey = dataSnapshot.getKey();
            MyPlaces myplace = dataSnapshot.getValue(MyPlaces.class);
            myplace.key = myPlaceKey;
            if (myPlacesKeyIndexMapping.containsKey(myPlaceKey)) {
                int index = myPlacesKeyIndexMapping.get(myPlaceKey);
                myPlaces.set(index, myplace);
            } else {
                myPlaces.add(myplace);
                myPlacesKeyIndexMapping.put(myPlaceKey, myPlaces.size() - 1);
            }
            if(updateListener != null)
            {
                updateListener.onListUpdated();
            }


        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            String myPlaceKey = dataSnapshot.getKey();
            if (!myPlacesKeyIndexMapping.containsKey(myPlaceKey)) {
                int index = myPlacesKeyIndexMapping.get(myPlaceKey);
                myPlaces.remove(index);
                recreateKeyIndexMapping();
                if(updateListener != null)
                {
                    updateListener.onListUpdated();
                }
            }

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };


    public static class SingletonHOlder {
        public static final MyPlacesData instance = new MyPlacesData();
    }

    public static MyPlacesData getInstance() {
        return SingletonHOlder.instance;
    }

    public ArrayList<MyPlaces> getMyPlaces() {
        return myPlaces;
    }

    public void addNewPlaces(MyPlaces place) {
        String key = database.push().getKey();
        myPlaces.add(place);
        myPlacesKeyIndexMapping.put(key, myPlaces.size() - 1);
        database.child(FIREBASE_CHILD).child(key).setValue(place);
        place.key = key;
    }

    public MyPlaces getIndex(int index) {
        return myPlaces.get(index);
    }

    public void deletePLace(int index) {
        database.child(FIREBASE_CHILD).child(myPlaces.get(index).key).removeValue();
        myPlaces.remove(index);
        recreateKeyIndexMapping();

    }

    public void updatePlace(int index, String nme, String desc, String lng, String lat) {
        MyPlaces myPlace = myPlaces.get(index);
        myPlace.name = nme;
        myPlace.description = desc;
        myPlace.longitude = lng;
        myPlace.latitude = lat;
        database.child(FIREBASE_CHILD).child(myPlace.key).setValue(myPlace);
    }

    private void recreateKeyIndexMapping() {
        myPlacesKeyIndexMapping.clear();
        for (int i = 0; i < myPlaces.size(); i++) {
            myPlacesKeyIndexMapping.put(myPlaces.get(i).key, i);
        }
    }

    ListUpdateEventListener updateListener;

    public void setEvrntListener(ListUpdateEventListener listener)
    {
        updateListener = listener;
    }

    public interface ListUpdateEventListener{
        void onListUpdated();
    }


}
