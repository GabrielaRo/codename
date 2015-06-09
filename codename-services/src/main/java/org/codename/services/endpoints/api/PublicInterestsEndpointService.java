/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.services.endpoints.api;

import java.io.Serializable;
import javax.ejb.Local;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import org.codename.services.exceptions.ServiceException;

/**
 *
 * @author salaboy
 */
@Local
@Path("/public/interests")
public interface PublicInterestsEndpointService extends Serializable {

    @GET
    @Path("/all")
    @Produces({"application/json"})
    Response getAllInterests() throws ServiceException;
}
