package com.example.musee.classes;

public class PieceClass {
    private String id;
    private String category;
    private String artistName;
    private String hours;
    private String size;
    private String information;
    private  String price;
    private String photo;

    public PieceClass()
    {}

    public PieceClass(String id, String category, String artistName, String hours,String size, String information,String price, String photo){
        this.id = id;
        this.category = category;
        this.artistName = artistName;
        this.hours = hours;
        this.size = size;
        this.information = information;
        this.price = price;
        this.photo = photo;
    }

    public String getId(){return id;}
    public String getCategory(){return category;}
    public String getArtistName(){return artistName;}
    public String getHours(){return hours;}
    public String getSize(){return size;}
    public  String getInformation(){return information;}
    public String getPrice() {return price;}
    public String getPhoto(){return photo;}




    public String toString() {
        return "Piece{" +
                "id='" + id + '\'' +
                ", category='" + category + '\'' +
                ", artistName='" + artistName + '\'' +
                ", hours='" + hours + '\'' +
                ", size='" + size + '\'' +
                ", information='" + information + '\'' +
                ", price='" + price + '\'' +
                ", photo='" + photo + '\'' +
                '}';
    }
}
