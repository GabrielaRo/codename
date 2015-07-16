/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.services.endpoints.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import org.apache.commons.io.IOUtils;
import org.codename.model.User;

import org.codename.core.api.UsersService;
import org.codename.services.endpoints.api.PublicInitEndpointService;
import org.codename.core.exceptions.ServiceException;

//http://localhost:8080/codename-server/rest/public/app/init
@Stateless
public class PublicInitEndpointServiceImpl implements PublicInitEndpointService {

    @Inject
    private UsersService usersService;

    private static String serverUrl;

    public PublicInitEndpointServiceImpl() {
        serverUrl = System.getProperty("server.url");
        if(serverUrl == null || serverUrl.equals("")){
            serverUrl = "localhost:8080/codename-server/";
        }
    }
    
    

    public Response initApplication() throws ServiceException {
        try {
            createGrogDJ();

            createEze();
            
            createGenericUser("eldo@gmail.com", "asdasd", "2", "Kings Mall, 1 Kings Street, London W6 0PZ, United Kingdom",-0.22539040000003752, 51.4927488);
            
            createGenericUser("bot@gmail.com", "asdasd", "1", "13 Southfield Rd, London W4 1AG, UK", -0.2532988000000387, 51.5013335);

        } catch (Exception ex) {
            Logger.getLogger(PublicInitEndpointServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.ok().build();
    }

    private Long createGrogDJ() throws ServiceException {
        Long grogdjId = usersService.newUser(new User("grogdj@gmail.com", "asdasd"));
        usersService.updateBothNames(grogdjId, "Grog", "DJ");
        
        usersService.updateLocation(grogdjId, "77 Fielding Road, London, United Kingdom", -0.2570034000000305, 51.50042680000001);
        //usersService.updateLookingFor(grogdjId, null);
        usersService.updateBio(grogdjId, "This is grog dj bio");
        usersService.updateLongBio(grogdjId, "XXXXXXXXXXXXXXXXXXXXXX This is grog dj longbio");
        usersService.updateWebsite(grogdjId, "marylandsupreme.tumbrl.com");
        usersService.updateLinkedin(grogdjId, "linked in here");
        usersService.updateLive(grogdjId, "true");
        usersService.updateFirstLogin(grogdjId);
        List<String> lookingFor = new ArrayList<String>();
        lookingFor.add("Mentor");
        List<String> iAms = new ArrayList<String>();
        iAms.add("Digital Nomad");
        usersService.updateLookingFor(grogdjId, lookingFor);
        usersService.updateIams(grogdjId, iAms);

        byte[] bytes = null;
        String profilePic = "http://" + serverUrl + "/static/img/public-images/1profile.jpg";
        try {
            InputStream inputStream = new URL(profilePic).openStream();

            bytes = IOUtils.toByteArray(inputStream);
            inputStream.close();
        } catch (IOException ex) {
            Logger.getLogger(PublicInitEndpointServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        usersService.updateAvatar(grogdjId, profilePic, bytes);

        String coverPic = "http://" + serverUrl + "/static/img/public-images/1cover.jpg";
        try {
            InputStream inputStream = new URL(coverPic).openStream();

            bytes = IOUtils.toByteArray(inputStream);
            inputStream.close();
        } catch (IOException ex) {
            Logger.getLogger(PublicInitEndpointServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        usersService.updateCover(grogdjId, coverPic, bytes);

        List<String> interests = new ArrayList<String>();
        interests.add("Design");
        interests.add("Architecture");
        usersService.updateInterests(grogdjId, interests);
        return grogdjId;
    }

    private Long createEze() throws ServiceException {
        Long ezeId = usersService.newUser(new User("eze@asd.asd", "123123"));
        usersService.updateBothNames(ezeId, "Eze", "Sala");
        
        usersService.updateLocation(ezeId, "Capellades, Barcelona, Spain", 1.6951309999999467, 41.521535);
        //usersService.updateLookingFor(grogdjId, null);
        usersService.updateBio(ezeId, "This is esala dj bio");
        usersService.updateLongBio(ezeId, "XXXXXXXXXXXXXXXXXXXXXX This is esala  longbio");
        usersService.updateWebsite(ezeId, "esala.tumbrl.com");
        usersService.updateLinkedin(ezeId, "linked in here");
        usersService.updateLive(ezeId, "true");
        usersService.updateFirstLogin(ezeId);
        List<String> lookingFor = new ArrayList<String>();
        lookingFor.add("Collaborate");
        List<String> iAms = new ArrayList<String>();
        iAms.add("Freelance");
        usersService.updateLookingFor(ezeId, lookingFor);
        usersService.updateIams(ezeId, iAms);
        
        byte[] bytes = null;
        String profilePic = "http://" + serverUrl + "/static/img/public-images/2profile.jpg";
        try {
            InputStream inputStream = new URL(profilePic).openStream();

            bytes = IOUtils.toByteArray(inputStream);
            inputStream.close();
        } catch (IOException ex) {
            Logger.getLogger(PublicInitEndpointServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        usersService.updateAvatar(ezeId, profilePic, bytes);

        String coverPic = "http://" + serverUrl + "/static/img/public-images/2cover.jpg";
        try {
            InputStream inputStream = new URL(coverPic).openStream();

            bytes = IOUtils.toByteArray(inputStream);
            inputStream.close();
        } catch (IOException ex) {
            Logger.getLogger(PublicInitEndpointServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        usersService.updateCover(ezeId, coverPic, bytes);

        List<String> interests = new ArrayList<String>();
        interests.add("Design");
        interests.add("Architecture");
        usersService.updateInterests(ezeId, interests);
        return ezeId;
    }
    
    private Long createGenericUser(String username, String password, String imagesNro, String location, Double lon, Double lat) throws ServiceException {
        Long userId = usersService.newUser(new User(username, password));
        usersService.updateBothNames(userId, username.split("@")[0], "username Lastname");
        
        
        usersService.updateLocation(userId, location, lon, lat);
        usersService.updateBio(userId, "This is "+username+" bio");
        usersService.updateLongBio(userId, "XXXXXXXXXXXXXXXXXXXXXX This "+username+"  longbio");
        usersService.updateWebsite(userId, username+".tumbrl.com");
        usersService.updateLinkedin(userId, username+" linked in here");
        usersService.updateLive(userId, "true");
        usersService.updateFirstLogin(userId);
        List<String> lookingFor = new ArrayList<String>();
        lookingFor.add("Collaborate");
        lookingFor.add("Socialise");
        List<String> iAms = new ArrayList<String>();
        iAms.add("Freelance");
        iAms.add("Entrepreneur");
        usersService.updateLookingFor(userId, lookingFor);
        usersService.updateIams(userId, iAms);
        byte[] bytes = null;
        String profilePic = "http://" + serverUrl + "/static/img/public-images/"+imagesNro+"profile.jpg";
        try {
            InputStream inputStream = new URL(profilePic).openStream();

            bytes = IOUtils.toByteArray(inputStream);
            inputStream.close();
        } catch (IOException ex) {
            Logger.getLogger(PublicInitEndpointServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        usersService.updateAvatar(userId, profilePic, bytes);

        String coverPic = "http://" + serverUrl + "/static/img/public-images/"+imagesNro+"cover.jpg";
        try {
            InputStream inputStream = new URL(coverPic).openStream();

            bytes = IOUtils.toByteArray(inputStream);
            inputStream.close();
        } catch (IOException ex) {
            Logger.getLogger(PublicInitEndpointServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        usersService.updateCover(userId, coverPic, bytes);

        List<String> interests = new ArrayList<String>();
        interests.add("Design");
        interests.add("Architecture");
        usersService.updateInterests(userId, interests);
        return userId;
    }


}
