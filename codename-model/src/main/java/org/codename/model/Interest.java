/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.model;

import javax.persistence.Column;
import javax.persistence.Entity;

import javax.persistence.Id;

import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author grogdj
 */
@Entity(name = "Interest")
@Table(name = "INTERESTS")
public class Interest {

    @Id
    @Size(min = 3, max = 20, message = "A interest  must contain between 3 and 20 characters")
    @NotNull
    @NotEmpty
    @Column(unique = true)
    private String name;

    private String imageURL;

    public Interest() {
    }

    public Interest(String name) {
        this.name = name;
    }

    public Interest(String name, String imageURL) {
        this.name = name;
        this.imageURL = imageURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 29 * hash + (this.imageURL != null ? this.imageURL.hashCode() : 0);
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
        final Interest other = (Interest) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        if ((this.imageURL == null) ? (other.imageURL != null) : !this.imageURL.equals(other.imageURL)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Interest{" + "name=" + name + ", imageURL=" + imageURL + '}';
    }
    
    

}
