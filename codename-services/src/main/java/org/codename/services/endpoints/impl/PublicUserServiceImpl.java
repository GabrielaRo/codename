/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.services.endpoints.impl;

import com.nimbusds.jose.JOSEException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import org.codename.model.User;
import org.codename.services.api.UsersService;
import org.codename.services.endpoints.api.PublicUserEndpointService;
import static org.codename.services.endpoints.impl.UsersHelper.createFullJsonUser;
import static org.codename.services.endpoints.impl.UsersHelper.createPublicJsonUser;
import org.codename.services.exceptions.ServiceException;
import org.codename.services.filters.auth.GrogAuthenticator;
import org.codename.services.util.CodenameUtil;

/**
 *
 * @author grogdj
 */
@Stateless
public class PublicUserServiceImpl implements PublicUserEndpointService {

    @Inject
    private UsersService usersService;

    private final static String serverUrl = "localhost:8080/codename-server/";

    private final static Logger log = Logger.getLogger(PublicUserServiceImpl.class.getName());
    
    // Very bad idea! 
    @Inject
    private GrogAuthenticator authenticator;

    public PublicUserServiceImpl() {

    }

    @Override
    public Response getAll() throws ServiceException {
        List<User> users = usersService.getAll();
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();

        for (User u : users) {

            JsonObjectBuilder jsonObjBuilder = createPublicJsonUser(u);
            jsonArrayBuilder.add(jsonObjBuilder);

        }
        return Response.ok(jsonArrayBuilder.build().toString()).build();
    }

    @Override
    public Response get(@PathParam("id") Long user_id) throws ServiceException {
        User u = usersService.getById(user_id);
        if (u == null) {
            throw new ServiceException("User  " + user_id + " doesn't exists");
        }
        JsonObjectBuilder jsonObjBuilder = createPublicJsonUser(u);
        return Response.ok(jsonObjBuilder.build().toString()).build();
    }

    @Override
    public Response getByNickName(String nickname) throws ServiceException {
        User u = usersService.getByNickName(nickname);
        if (u == null) {
            throw new ServiceException("User  with nickname: " + nickname + " doesn't exists");
        }
        JsonObjectBuilder jsonObjBuilder = createPublicJsonUser(u);
        return Response.ok(jsonObjBuilder.build().toString()).build();
    }

    @Override
    public Response getAvatar(@NotNull @PathParam("id") Long user_id) throws ServiceException {

        byte[] tmp = usersService.getAvatar(user_id);
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
                return Response.temporaryRedirect(new URI("http://" + serverUrl + "/static/img/public-images/default-avatar.png")).build();
            } catch (URISyntaxException ex) {
                Logger.getLogger(PublicUserServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        return Response.serverError().build();

    }

    @Override
    public Response getCover(@NotNull @PathParam("id") Long user_id) throws ServiceException {

        byte[] tmp = usersService.getCover(user_id);
        final byte[] avatar;
        if (tmp != null && tmp.length > 0) {
            log.info("cover found");
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
                log.info("cover not found");
                return Response.temporaryRedirect(new URI("http://" + serverUrl + "/static/img/public-images/default-cover.png")).build();
            } catch (URISyntaxException ex) {
                Logger.getLogger(PublicUserServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        return Response.serverError().build();

    }

    public Response getExternal(HttpServletRequest request) throws ServiceException {
        try {
            User authUser = getAuthUser(request);
            if (authUser == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            
            String authToken = authenticator.loginWithExternalToken("webkey:" + authUser.getEmail(), 
                    authUser.getEmail(), 
                    CodenameUtil.getSubject(request.getHeader(CodenameUtil.AUTH_HEADER_KEY)));
            JsonObjectBuilder jsonObjBuilder = createFullJsonUser(authUser);
            jsonObjBuilder.add("email", authUser.getEmail());

            jsonObjBuilder.add("auth_token", authToken);
            jsonObjBuilder.add("userId", authUser.getId());
            jsonObjBuilder.add("firstLogin", authUser.isIsFirstLogin());
            return Response.ok().entity(jsonObjBuilder.build()).build();
        } catch (ParseException ex) {
            throw new ServiceException(ex.getMessage());
        } catch (JOSEException ex) {
            throw new ServiceException(ex.getMessage());
        }
    }

    /*
     * Helper methods
     */
    
    private User getAuthUser(HttpServletRequest request) throws ParseException, JOSEException {
        String subject = CodenameUtil.getSubject(request.getHeader(CodenameUtil.AUTH_HEADER_KEY));
        return usersService.getByProviderId(subject);
    }
}
