/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this tpmplate file, choose Tools | Tpmplates
 * and open the tpmplate in the editor.
 */
package org.codename.core.impl;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import org.codename.core.api.UsersService;
import org.codename.model.Coordinates;
import org.codename.model.ServiceKey;
import org.codename.model.User;
import org.codename.core.exceptions.ServiceException;
import org.codename.core.util.CodenameUtil;
import org.codename.core.util.PersistenceManager;

/**
 *
 * @author grogdj
 */
@ApplicationScoped
public class UsersServiceImpl implements UsersService {

    @Inject
    private PersistenceManager pm;

    private final static Logger log = Logger.getLogger(UsersServiceImpl.class.getName());

    public UsersServiceImpl() {
    }

    @Override
    public Long newUser(User user) throws ServiceException {
        if (getByEmail(user.getEmail()) != null) {
            throw new ServiceException("User already registered with email: " + user.getEmail(), false);
        }
        user.setPassword(CodenameUtil.hash(user.getPassword()));

        pm.persist(user);

        String key = generateWebKey(user.getEmail());
        log.log(Level.INFO, "User {0} registered. Service Key: {1}", new Object[]{user.getEmail(), key});
        return user.getId();
    }

    @Override
    public boolean exist(String email) {
        return (getByEmail(email) != null);
    }

    @Override
    public boolean exist(Long user_id) {
        return (getById(user_id) != null);
    }

    @Override
    public User getByEmail(String email) {
        try {
            return pm.createNamedQuery("User.getByEmail", User.class).setParameter("email", email).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

    }

    @Override
    public User getByProviderId(String providerId) {
        try {
            return pm.createNamedQuery("User.getByProviderId", User.class).setParameter("providerId", providerId).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public User getByNickName(String nickname) throws ServiceException {
        try {
            return pm.createNamedQuery("User.getByNickName", User.class).setParameter("nickname", nickname).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public User getById(Long user_id) {
        try {
            return pm.find(User.class, user_id);
        } catch (NoResultException e) {
            return null;
        }

    }

    private String generateWebKey(String email) {
        String key = "webkey:" + email;
        log.log(Level.INFO, "Generating Key: {0}", key);
        pm.persist(new ServiceKey(key, email));
        return key;
    }

    @Override
    public String getKey(String serviceKey) {
        try {
            ServiceKey singleResult = pm.createNamedQuery("ServiceKey.getByKey", ServiceKey.class).setParameter("key", serviceKey).getSingleResult();
            if (singleResult != null) {
                return singleResult.getEmail();
            }
        } catch (NoResultException e) {
            return "";
        }
        return "";
    }

    @Override
    public boolean existKey(String serviceKey) {
        return (pm.createNamedQuery("ServiceKey.getByKey", ServiceKey.class).setParameter("key", serviceKey).getResultList().size() > 0);
    }

    @Override
    public void updateFirstName(Long user_id, String firstname) throws ServiceException {
        User u = pm.find(User.class, user_id);
        if (u == null) {
            throw new ServiceException("User doesn't exist: " + user_id);
        }
        u.setFirstname(firstname);
        pm.merge(u);
    }

    @Override
    public void updateLastName(Long user_id, String lastname) throws ServiceException {
        User u = pm.find(User.class, user_id);
        if (u == null) {
            throw new ServiceException("User doesn't exist: " + user_id);
        }
        u.setLastname(lastname);
        pm.merge(u);
    }

    @Override
    public void updateBio(Long user_id, String bio) throws ServiceException {
        User u = pm.find(User.class, user_id);
        if (u == null) {
            throw new ServiceException("User doesn't exist: " + user_id);
        }
        u.setBio(bio);
        pm.merge(u);
    }

    @Override
    public void updateLocation(Long user_id, String location, Double lon, Double lat) throws ServiceException {
        User u = pm.find(User.class, user_id);
        if (u == null) {
            throw new ServiceException("User doesn't exist: " + user_id);
        }
        u.setLocation(location);
        u.setLongitude(lon);
        u.setLatitude(lat);
        pm.merge(u);
    }

    @Override
    public Coordinates getLocation(Long user_id) throws ServiceException {
        User u = pm.find(User.class, user_id);
        if (u == null) {
            throw new ServiceException("User  doesn't exist: " + user_id);
        }
        return new Coordinates(u.getLongitude(), u.getLatitude());
    }

    @Override
    public void updateInterests(Long user_id, List<String> interests) throws ServiceException {
        User u = pm.find(User.class, user_id);
        if (u == null) {
            throw new ServiceException("User doesn't exist: " + user_id);
        }
        log.info("Storing to the database: " + interests);
        u.setInterests(interests);
        pm.merge(u);
    }

    @Override
    public void updateFirstLogin(Long user_id) throws ServiceException {

        User u = pm.find(User.class, user_id);
        if (u == null) {
            throw new ServiceException("User doesn't exist: " + user_id);
        }
        u.setIsFirstLogin(false);
        pm.merge(u);

    }

    @Override
    public void updateAvatar(String nickname, String fileName, byte[] content) throws ServiceException {
        User u = getByNickName(nickname);
        if (u == null) {
            throw new ServiceException("User doesn't exist: " + nickname);
        }
        u.setAvatarFileName(fileName);
        u.setAvatarContent(content);
        pm.merge(u);
    }

    @Override
    public void updateCover(String nickname, String fileName, byte[] content) throws ServiceException {
        User u = getByNickName(nickname);
        if (u == null) {
            throw new ServiceException("User doesn't exist: " + nickname);
        }
        u.setCoverFileName(fileName);
        u.setCoverContent(content);
        pm.merge(u);
    }

    @Override
    public byte[] getAvatar(String nickname) throws ServiceException {
        User u = getByNickName(nickname);
        if (u == null) {
            return null;
        }
        return u.getAvatarContent();
    }

    @Override
    public byte[] getCover(String nickname) throws ServiceException {
        User u = getByNickName(nickname);
        if (u == null) {
            return null;
        }
        return u.getCoverContent();
    }

    @Override
    public void removeAvatar(String nickname) throws ServiceException {
        User u = getByNickName(nickname);
        if (u == null) {
            throw new ServiceException("User doesn't exist: " + nickname);
        }
        u.setAvatarFileName("");
        u.setAvatarContent(null);
        pm.merge(u);
    }

    @Override
    public void removeCover(String nickname) throws ServiceException {
        User u = getByNickName(nickname);
        if (u == null) {
            throw new ServiceException("User doesn't exist: " + nickname);
        }
        u.setCoverFileName("");
        u.setCoverContent(null);
        pm.merge(u);
    }

    @Override
    public List<User> getAll() {
        return pm.createNamedQuery("User.getAll", User.class).getResultList();
    }

    @Override
    public List<User> getAllLive() {
        return pm.createNamedQuery("User.getAllLive", User.class).getResultList();
    }

    @Override
    public void updateBothNames(Long user_id, String firstname, String lastname) throws ServiceException {
        User u = pm.find(User.class, user_id);
        if (u == null) {
            throw new ServiceException("User doesn't exist: " + user_id);
        }
        u.setFirstname(firstname);
        u.setLastname(lastname);
        pm.merge(u);
    }

    @Override
    public void updateBioLongBioIams(Long user_id, String bio, String longbio, List<String> iAmsList) throws ServiceException {
        User u = pm.find(User.class, user_id);
        if (u == null) {
            throw new ServiceException("User doesn't exist: " + user_id);
        }
        u.setBio(bio);
        u.setLongBio(longbio);
        u.setiAms(iAmsList);
        pm.merge(u);
    }

    @Override
    public void updateOriginallyFrom(Long user_id, String originallyfrom) throws ServiceException {
        User u = pm.find(User.class, user_id);
        if (u == null) {
            throw new ServiceException("User doesn't exist: " + user_id);
        }
        u.setOriginallyFrom(originallyfrom);
        pm.merge(u);
    }

    @Override
    public void updateLookingFor(Long user_id, List<String> lookingForList) throws ServiceException {
        User u = pm.find(User.class, user_id);
        if (u == null) {
            throw new ServiceException("User doesn't exist: " + user_id);
        }
        u.setLookingFor(lookingForList);
        pm.merge(u);
    }

    @Override
    public void updateCategories(Long user_id, List<String> categoriesList) throws ServiceException {
        User u = pm.find(User.class, user_id);
        if (u == null) {
            throw new ServiceException("User doesn't exist: " + user_id);
        }
        u.setCategories(categoriesList);
        pm.merge(u);
    }

    public void updateLongBio(Long user_id, String longbio) throws ServiceException {
        User u = pm.find(User.class, user_id);
        if (u == null) {
            throw new ServiceException("User doesn't exist: " + user_id);
        }
        u.setLongBio(longbio);
        pm.merge(u);
    }

    @Override
    public void updateLive(Long user_id, String live) throws ServiceException {
        Boolean liveBoolean = Boolean.valueOf(live);
        User u = pm.find(User.class, user_id);
        if (u == null) {
            throw new ServiceException("User doesn't exist: " + user_id);
        }
        u.setLive(liveBoolean);
        pm.merge(u);
    }

    @Override
    public void updateIams(Long user_id, List<String> iAmsList) throws ServiceException {
        User u = pm.find(User.class, user_id);
        if (u == null) {
            throw new ServiceException("User doesn't exist: " + user_id);
        }
        u.setiAms(iAmsList);
        pm.merge(u);
    }

    @Override
    public void updateTwitter(Long user_id, String twitter) throws ServiceException {
        User u = pm.find(User.class, user_id);
        if (u == null) {
            throw new ServiceException("User doesn't exist: " + user_id);
        }
        u.setTwitter(twitter);
        pm.merge(u);
    }

    @Override
    public void updateNickName(Long user_id, String nickname) throws ServiceException {
        User u = pm.find(User.class, user_id);
        if (u == null) {
            throw new ServiceException("User doesn't exist: " + user_id);
        }
        u.setNickname(nickname);
        pm.merge(u);
    }

    @Override
    public void updateWebsite(Long user_id, String website) throws ServiceException {
        User u = pm.find(User.class, user_id);
        if (u == null) {
            throw new ServiceException("User doesn't exist: " + user_id);
        }
        u.setWebsite(website);
        pm.merge(u);
    }

    @Override
    public void updateLinkedin(Long user_id, String linkedin) throws ServiceException {
        User u = pm.find(User.class, user_id);
        if (u == null) {
            throw new ServiceException("User doesn't exist: " + user_id);
        }
        u.setLinkedin(linkedin);
        pm.merge(u);
    }

    @Override
    public void updateAdvice(Long user_id, String advice) throws ServiceException {
        User u = pm.find(User.class, user_id);
        if (u == null) {
            throw new ServiceException("User doesn't exist: " + user_id);
        }
        u.setAdviceMessage(advice);
        pm.merge(u);
    }

    @Override
    public void updateHobbies(Long user_id, String hobbies) throws ServiceException {
        User u = pm.find(User.class, user_id);
        if (u == null) {
            throw new ServiceException("User doesn't exist: " + user_id);
        }
        u.setHobbiesMessage(hobbies);
        pm.merge(u);
    }

    @Override
    public void updateResources(Long user_id, String resources) throws ServiceException {
        User u = pm.find(User.class, user_id);
        if (u == null) {
            throw new ServiceException("User doesn't exist: " + user_id);
        }
        u.setResourcesMessage(resources);
        pm.merge(u);
    }

    @Override
    public void updateShare(Long user_id, String share) throws ServiceException {
        User u = pm.find(User.class, user_id);
        if (u == null) {
            throw new ServiceException("User doesn't exist: " + user_id);
        }
        u.setShareMessage(share);
        pm.merge(u);
    }

    @Override
    public void updateMessageMe(Long user_id, String messagpme) throws ServiceException {
        User u = pm.find(User.class, user_id);
        if (u == null) {
            throw new ServiceException("User doesn't exist: " + user_id);
        }
        u.setMessageMeMessage(messagpme);
        pm.merge(u);
    }

    @Override
    public void updateJobTitle(Long user_id, String jobtitle) throws ServiceException {
        User u = pm.find(User.class, user_id);
        if (u == null) {
            throw new ServiceException("User doesn't exist: " + user_id);
        }
        u.setJobTitle(jobtitle);
        pm.merge(u);
    }

    @Override
    public void removeUser(Long user_id) throws ServiceException {
        User u = pm.find(User.class, user_id);
        if (u == null) {
            throw new ServiceException("User doesn't exist: " + user_id);
        }
        pm.remove(u);
    }

}
