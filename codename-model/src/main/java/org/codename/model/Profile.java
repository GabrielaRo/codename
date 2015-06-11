/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author grogdj
 */
@Entity(name = "Profile")
@Table(name = "PROFILE")
public class Profile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    private String introduction;

    private String postcode;

    private String avatarFileName;
    
    private String title;

    @Lob
    @Column(name = "AVATAR_CONTENT")
    private byte[] avatarContent;

    private String coverFileName;
    @Lob
    @Column(name = "COVER_CONTENT")
    private byte[] coverContent;

    private String firstname;

    private String lastname;

   // @Latitude
    private Double lat;

   // @Longitude
    private Double lon;

    @ManyToMany
    private List<Interest> interests;

    public Profile(User user) {
        this.user = user;
    }

    public Profile() {
    }

    public Profile(User user, String introduction, String postcode, String firstname, String lastname, String title) {
        this(user);
        this.introduction = introduction;
        this.postcode = postcode;
        this.firstname = firstname;
        this.lastname = lastname;
        this.title = title;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
 
    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
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

    public List<Interest> getInterests() {
        return interests;
    }

    public void setInterests(List<Interest> interests) {
        this.interests = interests;
    }

    public void addInterest(Interest interest) {
        if (this.interests != null) {
            this.interests.add(interest);
        }
    }

    public String getAvatarFileName() {
        return avatarFileName;
    }

    public void setAvatarFileName(String avatarFileName) {
        this.avatarFileName = avatarFileName;
    }

    public byte[] getAvatarContent() {
        return avatarContent;
    }

    public void setAvatarContent(byte[] avatarContent) {
        this.avatarContent = avatarContent;
    }

    public byte[] getCoverContent() {
        return coverContent;
    }

    public void setCoverContent(byte[] coverContent) {
        this.coverContent = coverContent;
    }

    public User getUser() {
        return user;
    }

    public String getCoverFileName() {
        return coverFileName;
    }

    public void setCoverFileName(String coverFileName) {
        this.coverFileName = coverFileName;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    @Override
    public String toString() {
        return "Profile{" + "introduction=" + introduction + ", postcode=" + postcode + ", avatarFileName=" + avatarFileName + ", title=" + title + ", avatarContent=" + avatarContent + ", coverFileName=" + coverFileName + ", coverContent=" + coverContent + ", firstname=" + firstname + ", lastname=" + lastname + ", lat=" + lat + ", lon=" + lon + ", interests=" + interests + '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + (this.user != null ? this.user.hashCode() : 0);
        hash = 59 * hash + (this.introduction != null ? this.introduction.hashCode() : 0);
        hash = 59 * hash + (this.postcode != null ? this.postcode.hashCode() : 0);
        hash = 59 * hash + (this.avatarFileName != null ? this.avatarFileName.hashCode() : 0);
        hash = 59 * hash + (this.title != null ? this.title.hashCode() : 0);
        hash = 59 * hash + Arrays.hashCode(this.avatarContent);
        hash = 59 * hash + (this.coverFileName != null ? this.coverFileName.hashCode() : 0);
        hash = 59 * hash + Arrays.hashCode(this.coverContent);
        hash = 59 * hash + (this.firstname != null ? this.firstname.hashCode() : 0);
        hash = 59 * hash + (this.lastname != null ? this.lastname.hashCode() : 0);
        hash = 59 * hash + (this.lat != null ? this.lat.hashCode() : 0);
        hash = 59 * hash + (this.lon != null ? this.lon.hashCode() : 0);
        hash = 59 * hash + (this.interests != null ? this.interests.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Profile other = (Profile) obj;
        if (this.user != other.user && (this.user == null || !this.user.equals(other.user))) {
            return false;
        }
        if ((this.introduction == null) ? (other.introduction != null) : !this.introduction.equals(other.introduction)) {
            return false;
        }
        if ((this.postcode == null) ? (other.postcode != null) : !this.postcode.equals(other.postcode)) {
            return false;
        }
        if ((this.avatarFileName == null) ? (other.avatarFileName != null) : !this.avatarFileName.equals(other.avatarFileName)) {
            return false;
        }
        if ((this.title == null) ? (other.title != null) : !this.title.equals(other.title)) {
            return false;
        }
        if (!Arrays.equals(this.avatarContent, other.avatarContent)) {
            return false;
        }
        if ((this.coverFileName == null) ? (other.coverFileName != null) : !this.coverFileName.equals(other.coverFileName)) {
            return false;
        }
        if (!Arrays.equals(this.coverContent, other.coverContent)) {
            return false;
        }
        if ((this.firstname == null) ? (other.firstname != null) : !this.firstname.equals(other.firstname)) {
            return false;
        }
        if ((this.lastname == null) ? (other.lastname != null) : !this.lastname.equals(other.lastname)) {
            return false;
        }
        if (this.lat != other.lat && (this.lat == null || !this.lat.equals(other.lat))) {
            return false;
        }
        if (this.lon != other.lon && (this.lon == null || !this.lon.equals(other.lon))) {
            return false;
        }
        if (this.interests != other.interests && (this.interests == null || !this.interests.equals(other.interests))) {
            return false;
        }
        return true;
    }
    
    

}
