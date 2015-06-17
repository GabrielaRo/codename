/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.services.endpoints.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import org.codename.model.Interest;
import org.codename.model.User;
import org.codename.services.api.InterestsService;

import org.codename.services.api.UsersService;
import org.codename.services.endpoints.api.PublicInitEndpointService;
import org.codename.services.exceptions.ServiceException;


//http://localhost:8080/codename-server/rest/public/app/init
@Stateless
public class PublicInitServiceImpl implements PublicInitEndpointService {


    @Inject
    private UsersService usersService;

    @Inject
    private InterestsService interestsService;

    
    private String server_url = "localhost";

    public Response initApplication() throws ServiceException {
        try {
            Long grogdjId = usersService.newUser(new User("grogdj@gmail.com", "asdasd"));
            Long ezeId = usersService.newUser(new User("eze@asd.asd", "123123"));

            interestsService.newInterest("Design", "foodAndDrink.jpg");
            interestsService.newInterest("Software Development", "music.jpg");
            interestsService.newInterest("Architecture", "film.jpg");

            
            List<Interest> interests = new ArrayList<Interest>();
            interests.add(interestsService.get("Design"));
            interests.add(interestsService.get("Architecture"));
            usersService.setInterests(grogdjId, interests);

            
            interests = new ArrayList<Interest>();
            interests.add(interestsService.get("Design"));
            usersService.setInterests(ezeId, interests);


        } catch (Exception ex) {
            Logger.getLogger(InterestsServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.ok().build();
    }
    
   

}
