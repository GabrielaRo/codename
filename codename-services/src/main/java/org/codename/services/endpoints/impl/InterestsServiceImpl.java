/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.services.endpoints.impl;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.validation.constraints.NotNull;
import javax.ws.rs.FormParam;
import javax.ws.rs.core.Response;
import org.codename.model.Interest;
import org.codename.services.api.InterestsService;
import org.codename.services.endpoints.api.InterestsEndpointService;
import org.codename.services.exceptions.ServiceException;
import org.hibernate.validator.constraints.NotEmpty;

@Stateless
public class InterestsServiceImpl implements InterestsEndpointService {

    @Inject
    private InterestsService tagsService;

    
    @Override
    public Response getAllInterests() throws ServiceException {
        List<Interest> allTags = tagsService.getAllInterests();
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        for(Interest t : allTags){
            jsonArrayBuilder.add(jsonObjectBuilder.add("name", (t.getName()==null)?"":t.getName()).add("imagePath", (t.getImageURL()==null)?"":t.getImageURL()));
        }
        JsonArray jsonArray = jsonArrayBuilder.build();
        return Response.ok(jsonArray).build();
        
    }

    @Override
    public Response newInterest(@NotNull @NotEmpty @FormParam("interest") String interest) throws ServiceException {
        tagsService.newInterest(interest);
        return Response.ok().build();
    }
    
}
