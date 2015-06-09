/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.services.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author grogdj
 */
public class CodenameUtil {
    public static String hash(String value){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return new String(md.digest(value.getBytes()));
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(CodenameUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }
}
