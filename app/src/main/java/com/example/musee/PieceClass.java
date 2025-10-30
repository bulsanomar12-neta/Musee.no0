package com.example.musee;

public class PieceClass {
    private String id;
    private String category;
    private String artistName;
    private String hours;
    private String information;
    private String photo;

    public PieceClass(String id, String category, String artist, String hours, String information, String imageUrl) {} // constructor فارغ ضروري للـ Firestore
    public PieceClass(String id,String category,String artistName,String hours,String information){
        this.id = id;
        this.category = category;
        this.artistName = artistName;
        this.hours = hours;
        this.information = information;
        this.photo = photo;
    }

    public String getId(){return id;}
    public String getCategory(){return category;}
    public String getArtistName(){return artistName;}
    public String getHours(){return hours;}
    public  String getInformation(){return information;}

    public String getPhoto(){return photo;}

}
