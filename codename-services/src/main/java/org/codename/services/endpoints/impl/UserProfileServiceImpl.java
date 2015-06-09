/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.services.endpoints.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.validation.constraints.NotNull;
import javax.ws.rs.FormParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import org.apache.commons.io.IOUtils;
import org.codename.model.Interest;
import org.codename.model.Profile;
import org.codename.services.api.InterestsService;
import org.codename.services.api.ProfilesService;
import org.codename.services.endpoints.api.UserProfileEndpointService;
import org.codename.services.exceptions.ServiceException;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

/**
 *
 * @author grogdj
 */
@Stateless
public class UserProfileServiceImpl implements UserProfileEndpointService {

    @Inject
    private ProfilesService profileService;

    @Inject
    private InterestsService interestService;

    private final static Logger log = Logger.getLogger(UserProfileServiceImpl.class.getName());

    private final String UPLOADED_FILE_PARAMETER_NAME = "file";

    public UserProfileServiceImpl() {

    }

    @Override
    public Response get(@PathParam("id") Long user_id) throws ServiceException {
        Profile p = profileService.getById(user_id);
        if (p == null) {
            throw new ServiceException("Profile for " + user_id + " doesn't exists");
        }
        JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder();
        jsonObjBuilder.add("bio", (p.getIntroduction() == null) ? "" : p.getIntroduction());
        jsonObjBuilder.add("location", (p.getPostcode() == null) ? "" : p.getPostcode());
        jsonObjBuilder.add("username", (p.getRealname() == null) ? "" : p.getRealname());

        JsonObject jsonObj = jsonObjBuilder.build();
        return Response.ok(jsonObj.toString()).build();
    }

    @Override
    public Response exist(@NotNull @FormParam("user_id") Long user_id) throws ServiceException {
        return Response.ok(profileService.exist(user_id)).build();
    }

    @Override
    public Response create(@NotNull @FormParam("user_id") Long user_id) throws ServiceException {
        if (!profileService.exist(user_id)) {
            profileService.create(user_id);
            return Response.ok().build();
        }
        throw new ServiceException("Profile for " + user_id + " already exists");
    }

    @Override
    public Response update(@NotNull @PathParam("user_id") Long user_id,
            @FormParam("username") String username,
            @FormParam("location") String location,
            @FormParam("bio") String bio) throws ServiceException {
        profileService.update(user_id, username, location, bio);
        return Response.ok().build();

    }

    @Override
    public Response getInterests(@NotNull @PathParam("user_id") Long user_id) throws ServiceException {
        List<Interest> interests = profileService.getInterests(user_id);
        log.info("Interests from the database: (" + user_id + ") " + interests);

        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        for (Interest i : interests) {
            jsonArrayBuilder.add(Json.createObjectBuilder().add("name", i.getName()).add("imagePath", i.getImageURL()));
        }
        JsonArray build = jsonArrayBuilder.build();
        return Response.ok(build.toString()).build();
    }

    public Response setInterests(@NotNull @PathParam("user_id") Long user_id, @FormParam("interests") String interests) throws ServiceException {
        log.info("Storing from the database: (" + user_id + ") " + interests);
        if (interests != null) {
            JsonReader reader = Json.createReader(new ByteArrayInputStream(interests.getBytes()));
            JsonArray array = reader.readArray();
            reader.close();

            List<Interest> interestsList = new ArrayList<Interest>(array.size());

            if (array != null) {

                for (int i = 0; i < array.size(); i++) {
                    log.info("Interest[" + i + "]: " + array.getJsonObject(i).getString("name"));
                    interestsList.add(interestService.get(array.getJsonObject(i).getString("name")));
                }

            }

            profileService.setInterests(user_id, interestsList);
        }

        return Response.ok().build();
    }

    @Override
    public Response uploadAvatar(@NotNull @PathParam("id") Long user_id, MultipartFormDataInput input) throws ServiceException {
        log.info(">>>> sit back - starting file upload for user_id..." + user_id);

        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
        List<InputPart> inputParts = uploadForm.get(UPLOADED_FILE_PARAMETER_NAME);

        for (InputPart inputPart : inputParts) {
            MultivaluedMap<String, String> headers = inputPart.getHeaders();
            String filename = getFileName(headers);

            try {
                InputStream inputStream = inputPart.getBody(InputStream.class, null);

                byte[] bytes = IOUtils.toByteArray(inputStream);

                log.log(Level.INFO, ">>> File '''{'{0}'}''' has been read, size: #'{'{1}'}' bytes", new Object[]{filename, bytes.length});
                profileService.updateAvatar(user_id, filename, bytes);

            } catch (IOException e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
            }
        }
        return Response.ok().build();
    }

    @Override
    public Response removeAvatar(@NotNull @PathParam("id") Long user_id) throws ServiceException {
        profileService.removeAvatar(user_id);
        return Response.ok().build();
    }

    @Override
    public Response getAvatar(@NotNull @PathParam("id") Long user_id) throws ServiceException {
        final byte[] avatar = profileService.getAvatar(user_id);
        return Response.ok().entity(new StreamingOutput() {
            @Override
            public void write(OutputStream output)
                    throws IOException, WebApplicationException {
                output.write(avatar);
                output.flush();
            }
        }).build();
    }

    /**
     * Extract filename from HTTP heaeders.
     *
     * @param headers
     * @return
     */
    private String getFileName(MultivaluedMap<String, String> headers) {
        String[] contentDisposition = headers.getFirst("Content-Disposition").split(";");

        for (String filename : contentDisposition) {
            if ((filename.trim().startsWith("filename"))) {

                String[] name = filename.split("=");

                String finalFileName = sanitizeFilename(name[1]);
                return finalFileName;
            }
        }
        return "unknown";
    }

    private String sanitizeFilename(String s) {
        return s.trim().replaceAll("\"", "");
    }

}
