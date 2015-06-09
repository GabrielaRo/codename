/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.services.endpoints.impl;

import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.validation.constraints.NotNull;
import javax.ws.rs.FormParam;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import org.codename.model.User;
import org.codename.services.api.ProfilesService;
import org.codename.services.api.UsersService;
import org.codename.services.endpoints.api.AuthenticationEndpointService;
import org.codename.services.exceptions.ServiceException;
import org.codename.services.filters.auth.GrogAuthenticator;
import org.codename.services.filters.auth.GrogHTTPHeaderNames;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author grogdj
 */
@Stateless
public class AuthenticationServiceImpl implements AuthenticationEndpointService {

    @Inject
    UsersService userService;
    
    @Inject
    ProfilesService profileService;

    @Inject
    GrogAuthenticator authenticator;
    
    private final static Logger log =  Logger.getLogger(AuthenticationServiceImpl.class.getName() );

    public AuthenticationServiceImpl() {

    }

    @Override
    public Response registerUser(@NotNull @Email @NotEmpty @FormParam("email") String email, 
            @NotNull @NotEmpty @FormParam("password") String password) throws ServiceException{
        userService.newUser(new User(email, password));
        return Response.ok().build();
    }

    @Override
    public Response login(
            @Context HttpHeaders httpHeaders,
            @NotNull @Email @NotEmpty @FormParam("email") String email,
            @NotNull @NotEmpty @FormParam("password") String password) throws ServiceException {

        String serviceKey = httpHeaders.getHeaderString(GrogHTTPHeaderNames.SERVICE_KEY);

        String authToken = authenticator.login(serviceKey, email, password);
        User authUser = userService.getByEmail(email);
        boolean firstLogin = !profileService.exist(authUser.getId());
        JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder();
        jsonObjBuilder.add("email", email);
        jsonObjBuilder.add("service_key", serviceKey);
        jsonObjBuilder.add("auth_token", authToken);
        jsonObjBuilder.add("user_id", authUser.getId());
        jsonObjBuilder.add("firstLogin", firstLogin);

        JsonObject jsonObj = jsonObjBuilder.build();

        return getNoCacheResponseBuilder(Response.Status.OK).entity(jsonObj.toString()).build();

    }

    @Override
    public Response logout(
            @Context HttpHeaders httpHeaders) throws ServiceException {

        String serviceKey = httpHeaders.getHeaderString(GrogHTTPHeaderNames.SERVICE_KEY);

        String authToken = httpHeaders.getHeaderString(GrogHTTPHeaderNames.AUTH_TOKEN);

        authenticator.logout(serviceKey, authToken);

        return getNoCacheResponseBuilder(Response.Status.NO_CONTENT).build();

    }

    private Response.ResponseBuilder getNoCacheResponseBuilder(Response.Status status) {

        CacheControl cc = new CacheControl();

        cc.setNoCache(true);

        cc.setMaxAge(-1);

        cc.setMustRevalidate(true);

        return Response.status(status).cacheControl(cc);

    }

    public UsersService getUserService() {
        return userService;
    }

    public void setUserService(UsersService userService) {
        this.userService = userService;
    }

    public ProfilesService getProfileService() {
        return profileService;
    }

    public void setProfileService(ProfilesService profileService) {
        this.profileService = profileService;
    }

    public GrogAuthenticator getAuthenticator() {
        return authenticator;
    }

    public void setAuthenticator(GrogAuthenticator authenticator) {
        this.authenticator = authenticator;
    }

    
}
