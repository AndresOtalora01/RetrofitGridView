package com.example.retrofitgridview;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Format implements Serializable {

    @SerializedName("application/x-mobipocket-ebook")
    private String ebook;

    @SerializedName("text/plain; charset=utf-8")
    private String textPlain;

    @SerializedName("text/html; charset=utf-8")
    private String textHtml;

    @SerializedName("application/rdf+xml")
    private String textXml;

    @SerializedName("application/epub+zip")
    private String epubZip;

    @SerializedName("image/jpeg")
    private String image;

    @SerializedName("application/zip")
    private String zip;



    public String getEbook() {
        return ebook;
    }

    public void setEbook(String ebook) {
        this.ebook = ebook;
    }

    public String getTextPlain() {
        return textPlain;
    }

    public void setTextPlain(String textPlain) {
        this.textPlain = textPlain;
    }

    public String getTextHtml() {
        return textHtml;
    }

    public void setTextHtml(String textHtml) {
        this.textHtml = textHtml;
    }

    public String getTextXml() {
        return textXml;
    }

    public void setTextXml(String textXml) {
        this.textXml = textXml;
    }

    public String getEpubZip() {
        return epubZip;
    }

    public void setEpubZip(String epubZip) {
        this.epubZip = epubZip;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }



    @Override
    public String toString() {
        return "Format{" +
                "ebook='" + ebook + '\'' +
                ", textPlain='" + textPlain + '\'' +
                ", textHtml='" + textHtml + '\'' +
                ", textXtml='" + textXml + '\'' +
                ", epubZip='" + epubZip + '\'' +
                ", zip='" + zip + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
