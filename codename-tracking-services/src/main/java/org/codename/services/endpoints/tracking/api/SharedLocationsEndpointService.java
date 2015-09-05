/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.services.endpoints.tracking.api;

import java.io.Serializable;
import javax.ejb.Local;
import javax.validation.constraints.NotNull;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codename.core.exceptions.ServiceException;

/**
 *
 * @author grogdj
 */
@Local
@Path("/trace")
public interface SharedLocationsEndpointService extends Serializable {

    @POST
    @Path("")
    @Produces({MediaType.APPLICATION_JSON})
    public Response shareLocation(@NotNull @FormParam("userId") Long userId,
            @NotNull @FormParam("latitude") Double latitude,
            @NotNull @FormParam("longitude") Double longitude, 
            @NotNull @FormParam("description") String description) throws ServiceException;

    @GET
    @Path("")
    @Produces({MediaType.APPLICATION_JSON})
    Response getSharedLocations(@NotNull @QueryParam("userId") Long userId) throws ServiceException;
    
    @DELETE
    @Path("/loc/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response removeSharedLocation(@NotNull @PathParam("id") Long locationId) throws ServiceException;

    @DELETE
    @Path("/{userId}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response clearAllSharedLocations(@NotNull @PathParam("userId") Long userId) throws ServiceException;
    
}
