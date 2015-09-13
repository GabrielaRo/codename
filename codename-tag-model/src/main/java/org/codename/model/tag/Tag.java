/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.model.tag;

import java.awt.Color;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.codename.model.user.User;
import org.hibernate.search.annotations.Indexed;

/**
*
* @author gabriela
*/
@Entity(name = "Tags")
@Table(name = "TAGS")
@Indexed
public class Tag implements Serializable {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	@NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
	
    private String tagName;
    private Color color;
    private boolean isPublic;
    private Date timestamp;
    
    public Tag() {    	
    }
    
    public Tag(User user, String tagName, Color color, boolean isPublic) {
    	this.user = user;
    	this.tagName = tagName;
    	this.color = color;
    	this.isPublic = isPublic;
    	this.timestamp = new Date();
    }
    
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public String getTagName() {
		return tagName;
	}
	
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public boolean isPublic() {
		return isPublic;
	}
	
	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}
	
	public Date getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
    
	@Override
    public String toString() {
        return "Tags{" + "id=" + id + ", user=" + user + ", tagName=" + tagName + ", color=" + color + ", isPublic=" + isPublic + '}';
    }
}
