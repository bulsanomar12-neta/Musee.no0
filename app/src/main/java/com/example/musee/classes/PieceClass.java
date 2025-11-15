package com.example.musee.classes;

public class PieceClass {
    private String id;
    private String category;
    private String artistName;
    private String hours;
    private String size;
    private String information;
    private String photo;




    public PieceClass(String s, String id, String category, String artistName, String hours, String information, String photo){
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

    public String getSize(){return size;}



    public String getPhoto(){return photo;}

}
