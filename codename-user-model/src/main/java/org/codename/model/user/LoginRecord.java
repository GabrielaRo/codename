/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.model.user;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author grogdj
 */
@Entity(name = "LoginRecord")
@Table(name = "LOGINRECORD")
public class LoginRecord {
    
    private User user;
    private Date logTime;
}
