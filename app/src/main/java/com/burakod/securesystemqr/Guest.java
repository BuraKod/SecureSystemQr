package com.burakod.securesystemqr;

public class Guest {



    public String fullName ;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getPlaceBlok() {
        return placeBlok;
    }

    public void setPlaceBlok(String placeBlok) {
        this.placeBlok = placeBlok;
    }

    public String getPlaceDaireNo() {
        return placeDaireNo;
    }

    public void setPlaceDaireNo(String placeDaireNo) {
        this.placeDaireNo = placeDaireNo;
    }

    public String getQrCodeLink() {
        return qrCodeLink;
    }

    public void setQrCodeLink(String qrCodeLink) {
        this.qrCodeLink = qrCodeLink;
    }

    public String place;

    public String placeBlok;

    public String placeDaireNo;

    public String qrCodeLink;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String code;


    public Guest(String fullName , String place , String placeBlok , String placeDaireNo,String code,String qrCodeLink){

        this.fullName = fullName ;
        this.place = place ;
        this.placeBlok = placeBlok;
        this.placeDaireNo = placeDaireNo;
        this.code = code;
        this.qrCodeLink = qrCodeLink;
    }



}
