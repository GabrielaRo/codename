/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author gabriela
 */
@Entity(name = "ContactMessage")
@Table(name = "CONTACT_MESSAGE")
public class ContactMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Email
    @NotNull
    @NotEmpty
    private String contactEmail;

    @NotNull
    @NotEmpty
    private String contactName;

    @NotNull
    @NotEmpty
    private String contactSubject;

    @NotNull
    @NotEmpty
    private String contactMessageText;

    private Date contactMessageDate;

    private Boolean contactMessageReplied = false;

    private String contactMessageType;

    public ContactMessage() {
    }

    public ContactMessage(String contactEmail, String contactName, String contactSubject, String contactMessageText, String contactMessageType) {
        this.contactEmail = contactEmail;
        this.contactMessageDate = new Date();
        this.contactName = contactName;
        this.contactSubject = contactSubject;
        this.contactMessageText = contactMessageText;
        this.contactMessageType = contactMessageType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactSubject() {
        return contactSubject;
    }

    public void setContactSubject(String contactSubject) {
        this.contactSubject = contactSubject;
    }

    public String getContactMessageText() {
        return contactMessageText;
    }

    public void setContactMessageText(String contactMessageText) {
        this.contactMessageText = contactMessageText;
    }

    public Date getContactMessageDate() {
        return contactMessageDate;
    }

    public void setContactMessageDate(Date contactMessageDate) {
        this.contactMessageDate = contactMessageDate;
    }

    public String getContactMessageType() {
        return contactMessageType;
    }

    public void setContactMessageType(String contactMessageType) {
        this.contactMessageType = contactMessageType;
    }

    public Boolean getContactMessageReplied() {
        return contactMessageReplied;
    }

    public void setContactMessageReplied(Boolean contactMessageReplied) {
        this.contactMessageReplied = contactMessageReplied;
    }

    @Override
    public String toString() {
        return "ContactMessage{" + "id=" + id + ", contactEmail=" + contactEmail + ", contactName=" + contactName + ", contactSubject=" + contactSubject + ", contactMessageText=" + contactMessageText + ", contactMessageDate=" + contactMessageDate + ", contactMessageReplied=" + contactMessageReplied + ", contactMessageType=" + contactMessageType + '}';
    }

}
