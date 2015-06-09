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
import org.codename.services.api.ProfilesService;
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

    @Inject
    private ProfilesService profilesService;

    
    private String server_url = "localhost";

    public Response initApplication() throws ServiceException {
        try {
            Long grogdjId = usersService.newUser(new User("grogdj@gmail.com", "asdasd"));
            Long ezeId = usersService.newUser(new User("eze@asd.asd", "123123"));

            interestsService.newInterest("food & drink", "foodAndDrink.jpg");
            interestsService.newInterest("music", "music.jpg");
            interestsService.newInterest("film", "film.jpg");
            interestsService.newInterest("fashion", "fashion.jpg");
            interestsService.newInterest("photography", "photography.jpg");
            interestsService.newInterest("literature", "literature.jpg");
            interestsService.newInterest("animals & nature", "animalsAndNature.jpg");
            interestsService.newInterest("tech & gadgets", "techAndGadgets.jpg");
            interestsService.newInterest("antique", "antique.jpg");
            interestsService.newInterest("extreme", "extreme.jpg");
            interestsService.newInterest("motors", "motors.jpg");
            interestsService.newInterest("dating", "dating.jpg");
            interestsService.newInterest("home & garden", "homeAndGarden.jpg");
            interestsService.newInterest("sports", "sports.jpg");
            interestsService.newInterest("travel", "travel.jpg");
            interestsService.newInterest("kids", "kids.jpg");
            interestsService.newInterest("baby", "baby.jpg");
            interestsService.newInterest("art & design", "artAndDesign.jpg");
            interestsService.newInterest("business", "business.jpg");
            interestsService.newInterest("wedding", "wedding.jpg");
            interestsService.newInterest("hot now", "hotNow.jpg");
            interestsService.newInterest("community", "community.jpg");
            interestsService.newInterest("gaming", "gaming.jpg");
            interestsService.newInterest("health & lifestyle", "healthAndLifestyle.jpg");


            profilesService.create(grogdjId);
            List<Interest> interests = new ArrayList<Interest>();
            interests.add(interestsService.get("sports"));
            interests.add(interestsService.get("antique"));
            profilesService.setInterests(grogdjId, interests);

            profilesService.create(ezeId);
            interests = new ArrayList<Interest>();
            interests.add(interestsService.get("sports"));
            interests.add(interestsService.get("antique"));
            interests.add(interestsService.get("music"));
            profilesService.setInterests(ezeId, interests);

          
            
      

        } catch (Exception ex) {
            Logger.getLogger(InterestsServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.ok().build();
    }
    
   

}
