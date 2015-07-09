/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.services.endpoints.api;

import java.io.Serializable;
import javax.ejb.Local;
import javax.validation.constraints.NotNull;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.codename.core.exceptions.ServiceException;

/**
 *
 * @author grogdj
 */
@Local
@Path("/query")
public interface UserQueryEndpointService extends Serializable {

    @GET
    @Path("all")
    @Produces({MediaType.APPLICATION_JSON})
    Response getAll(@NotNull @PathParam("lon") Long lon, @NotNull @PathParam("lat") Long lat, 
            @FormParam("interests") String interests, @FormParam("lookingFor") String lookingFor, 
            @FormParam("categories") String categories) throws ServiceException;
    
   

}
