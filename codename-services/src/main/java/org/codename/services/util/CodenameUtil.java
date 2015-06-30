/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.services.util;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joda.time.DateTime;


/**
 *
 * @author grogdj
 */
public class CodenameUtil {
    private static final JWSHeader JWT_HEADER = new JWSHeader(JWSAlgorithm.HS256);
	private static final String TOKEN_SECRET = "aliceinwonderland";
	public static final String AUTH_HEADER_KEY = "Authorization";
    public static String hash(String value){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return new String(md.digest(value.getBytes()));
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(CodenameUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }
    
    public static String createToken(String host, String sub) throws JOSEException {
		JWTClaimsSet claim = new JWTClaimsSet();
		claim.setSubject(sub);
		claim.setIssuer(host);
		claim.setIssueTime(DateTime.now().toDate());
		claim.setExpirationTime(DateTime.now().plusDays(14).toDate());
		
		JWSSigner signer = new MACSigner(TOKEN_SECRET);
		SignedJWT jwt = new SignedJWT(JWT_HEADER, claim);
		jwt.sign(signer);
		
		return jwt.serialize();
	}
}
