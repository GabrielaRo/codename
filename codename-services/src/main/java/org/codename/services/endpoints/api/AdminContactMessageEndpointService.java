/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.services.endpoints.api;

import java.io.Serializable;
import javax.ejb.Local;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
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
@Path("/admin/contact")
public interface AdminContactMessageEndpointService extends Serializable {

    @GET
    @Path("")
    @Produces({MediaType.APPLICATION_JSON})
    Response getRepliedMessages(@NotNull @QueryParam("replied") String replied) throws ServiceException;

    @GET
    @Path("/byemail")
    @Produces({MediaType.APPLICATION_JSON})
    Response getAll(@NotNull @QueryParam("email") String email) throws ServiceException;


}
