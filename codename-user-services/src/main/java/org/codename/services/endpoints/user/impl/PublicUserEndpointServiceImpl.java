/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.services.endpoints.user.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import org.codename.model.user.User;
import org.codename.core.user.api.UsersService;
import org.codename.services.endpoints.user.api.PublicUserEndpointService;
import static org.codename.services.endpoints.user.impl.UsersHelper.createPublicJsonUser;
import org.codename.core.exceptions.ServiceException;
import static org.imgscalr.Scalr.*;
import sun.misc.BASE64Decoder;

/**
 *
 * @author grogdj
 */
@Stateless
public class PublicUserEndpointServiceImpl implements PublicUserEndpointService {

    @Inject
    private UsersService usersService;

    private static String serverUrl;

    private String serverContext;

    private final static Logger log = Logger.getLogger(PublicUserEndpointServiceImpl.class.getName());

    public PublicUserEndpointServiceImpl() {

    }

    private String getServerUrl() {
        serverContext = System.getProperty("SERVERCONTEXT");
        serverUrl = System.getProperty("SERVERURL");
        if (serverUrl == null || serverUrl.equals("")) {
            serverUrl = "http://localhost:8080/";
        }
        serverUrl = serverUrl + serverContext;

        return serverUrl;
    }

    @Override
    public Response getAll() throws ServiceException {
        List<User> users = usersService.getAll();
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();

        for (User u : users) {

            JsonObjectBuilder jsonObjBuilder = createPublicJsonUser(u);
            jsonArrayBuilder.add(jsonObjBuilder);

        }
        return Response.ok(jsonArrayBuilder.build().toString()).build();
    }

    @Override
    public Response getByNickName(String nickname) throws ServiceException {
        User u = usersService.getByNickName(nickname);
        if (u == null) {
            throw new ServiceException("User  with nickname: " + nickname + " doesn't exists");
        }
        JsonObjectBuilder jsonObjBuilder = createPublicJsonUser(u);
        return Response.ok(jsonObjBuilder.build().toString()).build();
    }

    @Override
    public Response getAvatar(String nickname, Integer size) throws ServiceException {

        byte[] tmp = usersService.getAvatar(nickname);
        if (tmp != null && tmp.length > 0) {
//
//            avatar = tmp;
            return Response.ok().entity(new StreamingOutput() {
                @Override
                public void write(OutputStream output)
                        throws IOException, WebApplicationException {
                    byte[] avatar;
                    if (size != null) {
                        BASE64Decoder decoder = new BASE64Decoder();
                        avatar = decoder.decodeBuffer(new String(tmp));
                        InputStream in = new ByteArrayInputStream(avatar);
                        BufferedImage bImageFromConvert = ImageIO.read(in);
                        ImageIO.write(resize(bImageFromConvert, size), "JPG", output);
                    } else {
                        output.write(tmp);
                    }
                    output.flush();
                }
            }).build();
        } else {
            try {
//                log.info("avatar not found");
                return Response.temporaryRedirect(new URI(getServerUrl() + "static/img/public-images/default-avatar.jpg")).build();
            } catch (URISyntaxException ex) {
                Logger.getLogger(PublicUserEndpointServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        return Response.serverError().build();

    }

    @Override
    public Response getCover(String nickname, Integer size) throws ServiceException {

        byte[] tmp = usersService.getCover(nickname);
        
        if (tmp != null && tmp.length > 0) {
//            log.info("cover found");
           // avatar = tmp;
            return Response.ok().entity(new StreamingOutput() {
                @Override
                public void write(OutputStream output)
                        throws IOException, WebApplicationException {
                    byte[] avatar;
                    if (size != null) {
                        BASE64Decoder decoder = new BASE64Decoder();
                        avatar = decoder.decodeBuffer(new String(tmp));
                        InputStream in = new ByteArrayInputStream(avatar);
                        BufferedImage bImageFromConvert = ImageIO.read(in);
                        ImageIO.write(resize(bImageFromConvert, size), "JPG", output);
                    } else {
                        output.write(tmp);
                    }
                    output.flush();
                }
            }).build();
        } else {
            try {
//                log.info("cover not found");
                return Response.temporaryRedirect(new URI(getServerUrl() + "/static/img/public-images/default-cover.jpg")).build();
            } catch (URISyntaxException ex) {
                Logger.getLogger(PublicUserEndpointServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        return Response.serverError().build();

    }

}
