package com.bravo.johny.democustomlistview;

public class Place {

    public String mNameOfPlace;
    public int mZipCode;
    String mNameOfImage;
    String mPopup;

    public Place (String startNameOfPlace, int startZipCode,String startNameOfImage, String startPopup ){
        this.mNameOfPlace = startNameOfPlace;
        this.mZipCode = startZipCode;
        this.mNameOfImage = startNameOfImage;
        this.mPopup = startPopup;
    }
}
