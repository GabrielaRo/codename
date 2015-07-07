/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;
import org.hibernate.search.annotations.DateBridge;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Latitude;
import org.hibernate.search.annotations.Longitude;
import org.hibernate.search.annotations.Resolution;
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

    public static enum UserProvider {

        FHELLOW,
        GOOGLE
    };

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

    private String nickname;

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

    @IndexedEmbedded
    @ElementCollection
    private List<String> iAms;

    private boolean live;

    @DateBridge(resolution = Resolution.DAY)
    private Date birthday;

    private String gender;

    private String website;

    private String twitter;

    private String linkedin;

    private UserProvider provider = UserProvider.FHELLOW;

    private String providerId = "";

    public User() {
    }

    public User(String email, String password) {
        this.email = email;
        this.nickname = email.split("@")[0];
        this.password = password;
    }

    public User(String email, String password, UserProvider provider, String providerId) {
        this(email, password);
        this.provider = provider;
        this.providerId = providerId;
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

    public List<String> getiAms() {
        return iAms;
    }

    public void setiAms(List<String> iAms) {
        this.iAms = iAms;
    }

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public UserProvider getProvider() {
        return provider;
    }

    public void setProvider(UserProvider provider) {
        this.provider = provider;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

}
