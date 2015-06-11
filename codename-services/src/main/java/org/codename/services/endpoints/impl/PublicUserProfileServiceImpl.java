/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.services.endpoints.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.validation.constraints.NotNull;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import org.codename.model.Interest;
import org.codename.model.Profile;
import org.codename.services.api.InterestsService;
import org.codename.services.api.ProfilesService;
import org.codename.services.endpoints.api.PublicUserProfileEndpointService;
import org.codename.services.exceptions.ServiceException;



/**
 *
 * @author grogdj
 */
@Stateless
public class PublicUserProfileServiceImpl implements PublicUserProfileEndpointService {

    @Inject
    private ProfilesService profileService;
  
    
    private final static String serverUrl = "localhost";
    
    private final static Logger log = Logger.getLogger(PublicUserProfileServiceImpl.class.getName());

    public PublicUserProfileServiceImpl() {

    }

    @Override
    public Response getAll() throws ServiceException {
        List<Profile> profiles = profileService.getAll();
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        
        for(Profile p : profiles){
            
            JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder();
            jsonObjBuilder.add("userId", (p.getUser() == null) ? "" : p.getUser().getId().toString());
            jsonObjBuilder.add("bio", (p.getIntroduction() == null) ? "" : p.getIntroduction());
            jsonObjBuilder.add("location", (p.getPostcode() == null) ? "" : p.getPostcode());
            jsonObjBuilder.add("firstname", (p.getFirstname()== null) ? "" : p.getFirstname());
            jsonObjBuilder.add("lastname", (p.getLastname()== null) ? "" : p.getLastname());
            jsonObjBuilder.add("title", (p.getTitle()== null) ? "" : p.getTitle());
            JsonArrayBuilder jsonArrayBuilder2 = Json.createArrayBuilder();
            for(Interest i : p.getInterests()){
                jsonArrayBuilder2.add(i.getName());
            }
            jsonObjBuilder.add("interests", jsonArrayBuilder2);
            jsonArrayBuilder.add(jsonObjBuilder);
            
        }
        return Response.ok(jsonArrayBuilder.build().toString()).build();
    }
    
    

    @Override
    public Response get(@PathParam("id") Long user_id) throws ServiceException {
        Profile p = profileService.getById(user_id);
        if (p == null) {
            throw new ServiceException("Profile for " + user_id + " doesn't exists");
        }
        JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder();
        jsonObjBuilder.add("userId", (p.getUser() == null) ? "" : p.getUser().getId().toString());
        jsonObjBuilder.add("bio", (p.getIntroduction() == null) ? "" : p.getIntroduction());
        jsonObjBuilder.add("location", (p.getPostcode() == null) ? "" : p.getPostcode());
        jsonObjBuilder.add("username", (p.getFirstname() == null) ? "" : p.getFirstname());
        jsonObjBuilder.add("lastname", (p.getLastname()== null) ? "" : p.getLastname());
        jsonObjBuilder.add("title", (p.getTitle()== null) ? "" : p.getTitle());
        JsonArrayBuilder jsonArrayBuilder2 = Json.createArrayBuilder();
            for(Interest i : p.getInterests()){
                jsonArrayBuilder2.add(i.getName());
            }
            jsonObjBuilder.add("interests", jsonArrayBuilder2);
        JsonObject jsonObj = jsonObjBuilder.build();
        return Response.ok(jsonObj.toString()).build();
    }

    @Override
    public Response getAvatar(@NotNull @PathParam("id") Long user_id) throws ServiceException {

        byte[] tmp = profileService.getAvatar(user_id);
        final byte[] avatar;
        if (tmp != null && tmp.length > 0) {
            log.info("avatar found");
            avatar = tmp;
            return Response.ok().entity(new StreamingOutput() {
                @Override
                public void write(OutputStream output)
                        throws IOException, WebApplicationException {
                    output.write(avatar);
                    output.flush();
                }
            }).build();
        } else {
            try {
                log.info("avatar not found");
                return Response.temporaryRedirect(new URI("http://"+serverUrl+"/static/img/public-images/default-avatar.png")).build();
            } catch (URISyntaxException ex) {
                Logger.getLogger(PublicUserProfileServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        return Response.serverError().build();

    }
    
    @Override
    public Response getCover(@NotNull @PathParam("id") Long user_id) throws ServiceException {

        byte[] tmp = profileService.getCover(user_id);
        final byte[] avatar;
        if (tmp != null && tmp.length > 0) {
            log.info("avatar found");
            avatar = tmp;
            return Response.ok().entity(new StreamingOutput() {
                @Override
                public void write(OutputStream output)
                        throws IOException, WebApplicationException {
                    output.write(avatar);
                    output.flush();
                }
            }).build();
        } else {
            try {
                log.info("avatar not found");
                return Response.temporaryRedirect(new URI("http://"+serverUrl+"/static/img/public-images/default-cover.png")).build();
            } catch (URISyntaxException ex) {
                Logger.getLogger(PublicUserProfileServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        return Response.serverError().build();

    }

}
