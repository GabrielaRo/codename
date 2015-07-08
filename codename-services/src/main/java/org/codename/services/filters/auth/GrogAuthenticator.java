/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.services.filters.auth;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.codename.model.User;
import org.codename.core.api.UsersService;
import org.codename.core.exceptions.ServiceException;
import org.codename.core.util.CodenameUtil;

/**
 *
 * @author grogdj
 */
@Singleton
public final class GrogAuthenticator {

    private static GrogAuthenticator authenticator = null;

    // An authentication token storage which stores <service_key, auth_token>.
    private final Map<String, String> authorizationTokensStorage = new HashMap();

    @Inject
    UsersService userService;

    private final static Logger log = Logger.getLogger(GrogAuthenticator.class.getName());

    private GrogAuthenticator() {

    }

    public String login(String email, String password) throws ServiceException {

        log.log(Level.INFO, "email: {0}", email);
        log.log(Level.INFO, "password: {0}", password);

        if (userService.exist(email)) {

            User user = userService.getByEmail(email);

            String passwordMatch = user.getPassword();
            log.log(Level.INFO, "passwordMatch: {0}", passwordMatch);
            if (passwordMatch.equals(CodenameUtil.hash(password))) {

                /**
                 *
                 * Once all params are matched, the authToken will be
                 *
                 * generated and will be stored in the
                 *
                 * authorizationTokensStorage. The authToken will be needed
                 *
                 * for every REST API invocation and is only valid within
                 *
                 * the login session
                 *
                 */
                String authToken = UUID.randomUUID().toString();

                authorizationTokensStorage.put(authToken, email);

                return authToken;

            }

        }

        throw new ServiceException("Not Authorized, wrong service key for the provided email and password");

    }

    public String loginWithExternalToken(String email, String externalToken) throws ServiceException {

        log.log(Level.INFO, "email: {0}", email);
        log.log(Level.INFO, "externalToken: {0}", externalToken);

        if (userService.exist(email)) {
            //If the user comes from an external provider there is no need for password check
            User user = userService.getByEmail(email);

            /**
             *
             * Once all params are matched, the authToken will be
             *
             * generated and will be stored in the
             *
             * authorizationTokensStorage. The authToken will be needed
             *
             * for every REST API invocation and is only valid within
             *
             * the login session
             *
             */
            authorizationTokensStorage.put(externalToken, email);

            return externalToken;

        }

        throw new ServiceException("Not Authorized, wrong service key for the provided email and password");

    }

    /**
     *
     * The method that pre-validates if the client which invokes the REST API is
     *
     * from a authorized and authenticated source.
     *
     *
     *
     * @param serviceKey The service key
     *
     * @param authToken The authorization token generated after login
     *
     * @return TRUE for acceptance and FALSE for denied.
     *
     */
    public boolean isAuthTokenValid(String serviceKey, String authToken) {

        if (isServiceKeyValid(serviceKey)) {

            String usernameMatch1 = userService.getKey(serviceKey);

            if (authorizationTokensStorage.containsKey(authToken)) {

                String usernameMatch2 = authorizationTokensStorage.get(authToken);

                if (usernameMatch1.equals(usernameMatch2)) {

                    return true;

                }

            }

        }

        return false;

    }

    /**
     *
     * This method checks is the service key is valid
     *
     *
     *
     * @param serviceKey
     *
     * @return TRUE if service key matches the pre-generated ones in service key
     *
     * storage. FALSE for otherwise.
     *
     */
    public boolean isServiceKeyValid(String serviceKey) {

        return userService.existKey(serviceKey);

    }

    public void logout(String serviceKey, String authToken) throws ServiceException {

        if (userService.existKey(serviceKey)) {

            String usernameMatch1 = userService.getKey(serviceKey);

            if (authorizationTokensStorage.containsKey(authToken)) {

                String usernameMatch2 = authorizationTokensStorage.get(authToken);

                if (usernameMatch1.equals(usernameMatch2)) {

                    /**
                     *
                     * When a client logs out, the authentication token will be
                     *
                     * remove and will be made invalid.
                     *
                     */
                    authorizationTokensStorage.remove(authToken);

                    return;

                }

            }

        }

        throw new ServiceException("Invalid service key and authorization token match.", true);

    }

}
