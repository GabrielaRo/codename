/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.services.endpoints.chat.impl;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.codename.core.chat.layer.impl.LayerIdentityTokenGenerator;
import org.codename.core.exceptions.ServiceException;
import org.codename.services.endpoints.chat.api.LayerIdentityEndpointService;

/**
 *
 * @author grogdj
 */
@Stateless
public class LayerIdentityEndpointServiceImpl implements LayerIdentityEndpointService {

    private final static Logger log = Logger.getLogger(LayerIdentityEndpointServiceImpl.class.getName());

    @Inject
    private LayerIdentityTokenGenerator tokenGenerator;


    public LayerIdentityEndpointServiceImpl() {

    }

    @Override
    public Response getIdentityToken(String nonce, String nickname) throws ServiceException {
        try {
            String token = tokenGenerator.getToken(nonce, nickname);
            JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder();
            System.out.println("This is the NONCE in the backend: "+nonce);
            jsonObjBuilder.add("identity_token", token);
            return Response.ok(jsonObjBuilder.build().toString(), MediaType.APPLICATION_JSON).build();
        } catch (SignatureException ex) {
            Logger.getLogger(LayerIdentityEndpointServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(LayerIdentityEndpointServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(LayerIdentityEndpointServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(LayerIdentityEndpointServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(LayerIdentityEndpointServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.serverError().build();
    }

    
}
