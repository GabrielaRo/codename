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
    //rhc env set JAVA_OPTS_EXT=-DSERVERURL="http://fhellow-restprovider.rhcloud.com/" -a fhellow
    private String serverUrl;

    private static boolean mockUsersCreated = false;
    
    public PublicInitEndpointServiceImpl() {

    }

    private String getServerUrl() {

        serverUrl = System.getProperty("SERVERURL");
        if (serverUrl == null || serverUrl.equals("")) {
            serverUrl = "http://localhost:8080/";
        }
        serverUrl = serverUrl + "codename-server/";

        System.out.println("Server URL : " + serverUrl);
        return serverUrl;
    }

    public Response usersCreated() throws ServiceException {
        return Response.ok(mockUsersCreated).build();
    }
    
    

    public Response initApplication() throws ServiceException {
        try {
            List<String> interests = new ArrayList<String>();
            List<String> lookingFor = new ArrayList<String>();
            List<String> iAm = new ArrayList<String>();
            
            lookingFor.add("Mentor");
            iAm.add("Digital Nomad");
            interests.add("Software");
            
            createUser("grogdj@gmail.com", "asdasd","Grog", "DJ", "Chiswick, United Kingdom",-0.267173200000002, 51.4876272,   "This is grog dj bio", lookingFor, iAm, interests, "0_" );
            
            interests = new ArrayList<String>();
            lookingFor = new ArrayList<String>();
            iAm = new ArrayList<String>();
            
            lookingFor.add("Socialise");
            lookingFor.add("Collabortate");
            iAm.add("Freelance");
            iAm.add("Entrepreneur");
            interests.add("Design");
            interests.add("Photography");
            interests.add("Startups");
            interests.add("Blogging");
            
            createUser("tom48734@gmail.com", "9832kjhs","Tom", "Campion", "Peckham, United Kingdom",-0.06913699999995515, 51.474191,   "I’m a nomadic designer, living and working from wherever the wind takes me. www.mynomadlife.com", 
                    lookingFor, iAm, interests, "tom_" );
            
            
            interests = new ArrayList<String>();
            lookingFor = new ArrayList<String>();
            iAm = new ArrayList<String>();
            
            lookingFor.add("Socialise");
            lookingFor.add("Mentor");
            iAm.add("Digital Nomad");
            iAm.add("Entrepreneur");
            interests.add("Business");
            interests.add("Music");
            interests.add("Education");
            interests.add("Blogging");
            
            createUser("adamchengismyname@gmail.com", "jhsdkaus","Adam", "Cheng", "Shoreditch, United Kingdom",-0.08472800000004099, 51.52849,   "I'm an entrepreneur and recently sold my design agency. I am now spliiting my time bettwen London and Bali.", 
                    lookingFor, iAm, interests, "adam_" );
            
            
            interests = new ArrayList<String>();
            lookingFor = new ArrayList<String>();
            iAm = new ArrayList<String>();
            
            lookingFor.add("Socialise");
            lookingFor.add("Collaborate");
            iAm.add("Freelance");
            
            interests.add("Photography");
            interests.add("Journalism");
            interests.add("Blogging");
            
            createUser("amymaverson@gmail.com", "snlasas","Amy", "Mavorson", "Shoreditch, United Kingdom",-0.08472800000004099, 51.52849,   "I’ve been a freelance journalist for three years now. I’m a fan of Pinterest, cereal and 'inverted commas'.", 
                    lookingFor, iAm, interests, "amy_" );
            
            interests = new ArrayList<String>();
            lookingFor = new ArrayList<String>();
            iAm = new ArrayList<String>();
            
            lookingFor.add("Socialise");
            
            iAm.add("Freelance");
            iAm.add("Digital Nomad");
            iAm.add("Entrepreneur");
            
            interests.add("Photography");
            interests.add("Design");
            
            
            createUser("charlottespencer@gmail.com", "kjas9fa","Charlotte", "Spencer", "Shoreditch, United Kingdom",-0.08472800000004099, 51.52849,   "Fashion illustrator and runner currently working in London for a month.", 
                    lookingFor, iAm, interests, "charlotte_" );
            
            interests = new ArrayList<String>();
            lookingFor = new ArrayList<String>();
            iAm = new ArrayList<String>();
            
            lookingFor.add("Socialise");
            
            iAm.add("Freelance");
            
            interests.add("Photography");
            interests.add("Design");
            interests.add("Blogging");
            interests.add("Startups");
            interests.add("Sports");
            interests.add("Business");
            
            
            createUser("jonasapperly@gmail.com", "aksdhas","Jonas", "Apperly", "Hampstead, United Kingdom",-0.1762025000000449, 51.5556715,   "My girlfriend and I have been living semi-nomadic for a year now - we are currently in London and blogging as we go: thenomadcouple.com", 
                    lookingFor, iAm, interests, "jonas_" );
            
            interests = new ArrayList<String>();
            lookingFor = new ArrayList<String>();
            iAm = new ArrayList<String>();
            
            lookingFor.add("Socialise");
            
            iAm.add("Freelance");
            iAm.add("Digital Nomad");
            
            interests.add("Startups");
           
            
            
            createUser("clarajohnson@gmail.com", "723879","Clara", "Johnson", "Clapham, United Kingdom",-0.13856999999995878, 51.46231,   "I have just begun my digital nomad lifestyle in May from Vancouver, Canada. Stoked to begin a life like this!", 
                    lookingFor, iAm, interests, "clara_" );
            
            interests = new ArrayList<String>();
            lookingFor = new ArrayList<String>();
            iAm = new ArrayList<String>();
            
            lookingFor.add("Socialise");
            lookingFor.add("Collaborate");
            iAm.add("Freelance");
            iAm.add("Digital Nomad");
            iAm.add("Entrepreneur");
            interests.add("Startups");
            interests.add("Design");
            interests.add("Business");
           
            
            
            createUser("alisa.afkhami@gmail.com", "myprofile","Alisa", "Ay", "Wandsworth, United Kingdom",-0.20600100000001476, 51.45755,   "Hi, I'm Alisa a freelance designer from London. My goal is to work from anywhere in the world, preferably somewhere where the sun shines and the are waves to surf on.", 
                    lookingFor, iAm, interests, "alisa_" );
            
            interests = new ArrayList<String>();
            lookingFor = new ArrayList<String>();
            iAm = new ArrayList<String>();
            
            lookingFor.add("Socialise");
            lookingFor.add("Mentor");
            
            iAm.add("Digital Nomad");
            
            interests.add("Startups");
            interests.add("Software");

            
            createUser("gdonald@gmail.com", "asdasd","George", "Donald", "Chiswick, United Kingdom",-0.267173200000002, 51.4876272,   "Hi, I'm George. I like to travel and work from anywhere. I only own what fits in my backpack. ", 
                    lookingFor, iAm, interests, "george_" );
            mockUsersCreated = true;
            

        } catch (Exception ex) {
            Logger.getLogger(PublicInitEndpointServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.ok().build();
    }

    private Long createUser(String email, String password, String firstname, String lastname, 
            String location, Double lon, Double lat, String bio, 
            List<String> lookingFor, List<String> iAms, List<String> interests, String profileId
            ) throws ServiceException{
        User user = new User(email, password);
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setLocation(location);
        user.setLongitude(lon);
        user.setLatitude(lat);
        user.setBio(bio);
        user.setLive(true);
        user.setIsFirstLogin(false);
        user.setLookingFor(lookingFor);
        user.setiAms(iAms);
        user.setInterests(interests);

        Long userId = usersService.newUser(user);
        
        
        byte[] bytes = null;
        String profilePic = getServerUrl() + "static/img/public-images/"+profileId+"pic.jpg";
        try {
            InputStream inputStream = new URL(profilePic).openStream();

            bytes = IOUtils.toByteArray(inputStream);
            inputStream.close();
        } catch (IOException ex) {
            Logger.getLogger(PublicInitEndpointServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        usersService.updateAvatar(userId, profilePic, bytes);

        String coverPic = getServerUrl() + "/static/img/public-images/"+profileId+"cover.jpg";
        try {
            InputStream inputStream = new URL(coverPic).openStream();

            bytes = IOUtils.toByteArray(inputStream);
            inputStream.close();
        } catch (IOException ex) {
            Logger.getLogger(PublicInitEndpointServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        usersService.updateCover(userId, coverPic, bytes);
        
        return userId;
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
        String profilePic = getServerUrl() + "static/img/public-images/2profile.jpg";
        try {
            InputStream inputStream = new URL(profilePic).openStream();

            bytes = IOUtils.toByteArray(inputStream);
            inputStream.close();
        } catch (IOException ex) {
            Logger.getLogger(PublicInitEndpointServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        usersService.updateAvatar(ezeId, profilePic, bytes);

        String coverPic = getServerUrl() + "static/img/public-images/2cover.jpg";
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
        interests.add("Web");
        usersService.updateInterests(ezeId, interests);
        return ezeId;
    }

}
