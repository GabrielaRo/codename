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
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Latitude;
import org.hibernate.search.annotations.Longitude;
import org.hibernate.search.annotations.Spatial;
import org.hibernate.search.annotations.SpatialMode;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author grogdj
 */
@Entity(name = "User")
@Table(name = "USERS")
@Indexed
@Spatial(spatialMode = SpatialMode.GRID)
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(min = 6, max = 20, message = "An user's password must contain between 5 and 20 characters")
    @NotNull
    @NotEmpty
    private String password;

    @Column(unique = true)
    @NotNull
    @NotEmpty
    @Email
    private String email;

    private boolean isFirstLogin = true;

    private String bio;

    private String longBio;

    private String originallyFrom;

    private String location;

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

    @Latitude
    private Double latitude;

    @Longitude
    private Double longitude;

    @IndexedEmbedded
    @ElementCollection
    private List<String> interests;

    @IndexedEmbedded
    @ElementCollection
    private List<String> categories;

    @IndexedEmbedded
    @ElementCollection
    private List<String> lookingFor;

    public User() {
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAvatarFileName() {
        return avatarFileName;
    }

    public void setAvatarFileName(String avatarFileName) {
        this.avatarFileName = avatarFileName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public byte[] getAvatarContent() {
        return avatarContent;
    }

    public void setAvatarContent(byte[] avatarContent) {
        this.avatarContent = avatarContent;
    }

    public String getCoverFileName() {
        return coverFileName;
    }

    public void setCoverFileName(String coverFileName) {
        this.coverFileName = coverFileName;
    }

    public byte[] getCoverContent() {
        return coverContent;
    }

    public void setCoverContent(byte[] coverContent) {
        this.coverContent = coverContent;
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

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }

    public boolean isIsFirstLogin() {
        return isFirstLogin;
    }

    public void setIsFirstLogin(boolean isFirstLogin) {
        this.isFirstLogin = isFirstLogin;
    }

    public String getLongBio() {
        return longBio;
    }

    public void setLongBio(String longBio) {
        this.longBio = longBio;
    }

    public String getOriginallyFrom() {
        return originallyFrom;
    }

    public void setOriginallyFrom(String originallyFrom) {
        this.originallyFrom = originallyFrom;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public List<String> getLookingFor() {
        return lookingFor;
    }

    public void setLookingFor(List<String> lookingFor) {
        this.lookingFor = lookingFor;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", email=" + email + ", isFirstLogin=" + isFirstLogin + ", bio=" + bio + ", longBio=" + longBio + ", originallyFrom=" + originallyFrom + ", location=" + location + ", avatarFileName=" + avatarFileName + ", title=" + title + ", coverFileName=" + coverFileName + ", firstname=" + firstname + ", lastname=" + lastname + ", latitude=" + latitude + ", longitude=" + longitude + ", interests=" + interests + ", categories=" + categories + ", lookingFor=" + lookingFor + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 83 * hash + (this.password != null ? this.password.hashCode() : 0);
        hash = 83 * hash + (this.email != null ? this.email.hashCode() : 0);
        hash = 83 * hash + (this.isFirstLogin ? 1 : 0);
        hash = 83 * hash + (this.bio != null ? this.bio.hashCode() : 0);
        hash = 83 * hash + (this.longBio != null ? this.longBio.hashCode() : 0);
        hash = 83 * hash + (this.originallyFrom != null ? this.originallyFrom.hashCode() : 0);
        hash = 83 * hash + (this.location != null ? this.location.hashCode() : 0);
        hash = 83 * hash + (this.avatarFileName != null ? this.avatarFileName.hashCode() : 0);
        hash = 83 * hash + (this.title != null ? this.title.hashCode() : 0);
        hash = 83 * hash + Arrays.hashCode(this.avatarContent);
        hash = 83 * hash + (this.coverFileName != null ? this.coverFileName.hashCode() : 0);
        hash = 83 * hash + Arrays.hashCode(this.coverContent);
        hash = 83 * hash + (this.firstname != null ? this.firstname.hashCode() : 0);
        hash = 83 * hash + (this.lastname != null ? this.lastname.hashCode() : 0);
        hash = 83 * hash + (this.latitude != null ? this.latitude.hashCode() : 0);
        hash = 83 * hash + (this.longitude != null ? this.longitude.hashCode() : 0);
        hash = 83 * hash + (this.interests != null ? this.interests.hashCode() : 0);
        hash = 83 * hash + (this.categories != null ? this.categories.hashCode() : 0);
        hash = 83 * hash + (this.lookingFor != null ? this.lookingFor.hashCode() : 0);
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
        final User other = (User) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if ((this.password == null) ? (other.password != null) : !this.password.equals(other.password)) {
            return false;
        }
        if ((this.email == null) ? (other.email != null) : !this.email.equals(other.email)) {
            return false;
        }
        if (this.isFirstLogin != other.isFirstLogin) {
            return false;
        }
        if ((this.bio == null) ? (other.bio != null) : !this.bio.equals(other.bio)) {
            return false;
        }
        if ((this.longBio == null) ? (other.longBio != null) : !this.longBio.equals(other.longBio)) {
            return false;
        }
        if ((this.originallyFrom == null) ? (other.originallyFrom != null) : !this.originallyFrom.equals(other.originallyFrom)) {
            return false;
        }
        if ((this.location == null) ? (other.location != null) : !this.location.equals(other.location)) {
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
        if (this.latitude != other.latitude && (this.latitude == null || !this.latitude.equals(other.latitude))) {
            return false;
        }
        if (this.longitude != other.longitude && (this.longitude == null || !this.longitude.equals(other.longitude))) {
            return false;
        }
        if (this.interests != other.interests && (this.interests == null || !this.interests.equals(other.interests))) {
            return false;
        }
        if (this.categories != other.categories && (this.categories == null || !this.categories.equals(other.categories))) {
            return false;
        }
        if (this.lookingFor != other.lookingFor && (this.lookingFor == null || !this.lookingFor.equals(other.lookingFor))) {
            return false;
        }
        return true;
    }

}
