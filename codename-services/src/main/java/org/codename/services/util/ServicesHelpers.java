/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.services.util;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Map;
import javax.ws.rs.core.Response;

/**
 *
 * @author salaboy
 */
public class ServicesHelpers {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    /*
     * Helper methods
     */
    public static Map<String, Object> getResponseEntity(final Response response) throws JsonParseException,
            JsonMappingException, IOException {
        return MAPPER.readValue(response.readEntity(String.class),
                new TypeReference<Map<String, Object>>() {
                });
    }
}
