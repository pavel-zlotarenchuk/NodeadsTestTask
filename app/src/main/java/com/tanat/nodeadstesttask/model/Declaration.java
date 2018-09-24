
package com.tanat.nodeadstesttask.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Declaration {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("firstname")
    @Expose
    private String firstname;
    @SerializedName("lastname")
    @Expose
    private String lastname;
    @SerializedName("placeOfWork")
    @Expose
    private String placeOfWork;
    @SerializedName("position")
    @Expose
    private String position;
    @SerializedName("linkPDF")
    @Expose
    private String linkPDF;

    private String comment;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPlaceOfWork() {
        return placeOfWork;
    }

    public void setPlaceOfWork(String placeOfWork) {
        this.placeOfWork = placeOfWork;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getLinkPDF() {
        return linkPDF;
    }

    public void setLinkPDF(String linkPDF) {
        this.linkPDF = linkPDF;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Declaration(String id, String firstname, String lastname, String placeOfWork, String position, String linkPDF, String comment) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.placeOfWork = placeOfWork;
        this.position = position;
        this.linkPDF = linkPDF;
        this.comment = comment;
    }
}
