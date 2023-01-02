package com.example.projet;

public class annonce {

    String name;

    public annonce(String name, String prix, String etat, String image, String text, String idUser) {

        this.name = name;
        this.prix = prix;
        this.etat = etat;
        this.image = image;
        this.text = text;
        this.idUser = idUser;
    }

    public annonce() {

    }

    String prix;
    String etat;
    String image;
    String text;

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    String idUser;





    public String getName() {
        return name;
    }

    public String getPrix() {
        return prix;
    }

    public String getEtat() {
        return etat;
    }

    public String getImage() {
        return image;
    }

    public String getText() {
        return text;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setPrix(String prix) {
        this.prix = prix;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setText(String text) {
        this.text = text;
    }


}
