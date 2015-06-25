/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.services.impl;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import org.codename.model.Coordinates;
import org.codename.model.ServiceKey;
import org.codename.model.User;
import org.codename.services.api.UsersService;
import org.codename.services.exceptions.ServiceException;
import org.codename.services.util.CodenameUtil;

/**
 *
 * @author grogdj
 */
@ApplicationScoped
public class UsersServiceImpl implements UsersService {

    @Inject
    private EntityManager em;

    private final static Logger log = Logger.getLogger(UsersServiceImpl.class.getName());

    public UsersServiceImpl() {
    }

    @Override
    public Long newUser(User user) throws ServiceException {
        if (getByEmail(user.getEmail()) != null) {
            throw new ServiceException("User already registered with email: " + user.getEmail(), false);
        }
        user.setPassword(CodenameUtil.hash(user.getPassword()));
        em.persist(user);
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
            return em.createNamedQuery("User.getByEmail", User.class).setParameter("email", email).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

    }

    @Override
    public User getById(Long user_id) {
        try {
            return em.find(User.class, user_id);
        } catch (NoResultException e) {
            return null;
        }

    }

    private String generateWebKey(String email) {
        String key = "webkey:" + email;
        log.log(Level.INFO, "Generating Key: {0}", key);
        em.persist(new ServiceKey(key, email));
        return key;
    }

    @Override
    public String getKey(String serviceKey) {
        try {
            ServiceKey singleResult = em.createNamedQuery("ServiceKey.getByKey", ServiceKey.class).setParameter("key", serviceKey).getSingleResult();
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
        return (em.createNamedQuery("ServiceKey.getByKey", ServiceKey.class).setParameter("key", serviceKey).getResultList().size() > 0);
    }

    @Override
    public void update(Long user_id, String firstname, String lastname, String location, String bio, String title) throws ServiceException {
        User u = em.find(User.class, user_id);
        if (u == null) {
            throw new ServiceException("User doesn't exist: " + user_id);
        }
        u.setFirstname(firstname);
        u.setLastname(lastname);
        u.setBio(bio);
        u.setLocation(location);
        u.setTitle(title);
        em.merge(u);
    }

    @Override
    public void updateFirstName(Long user_id, String firstname) throws ServiceException {
        User u = em.find(User.class, user_id);
        if (u == null) {
            throw new ServiceException("User doesn't exist: " + user_id);
        }
        u.setFirstname(firstname);
        em.merge(u);
    }

    @Override
    public void updateLastName(Long user_id, String lastname) throws ServiceException {
        User u = em.find(User.class, user_id);
        if (u == null) {
            throw new ServiceException("User doesn't exist: " + user_id);
        }
        u.setLastname(lastname);
        em.merge(u);
    }

    @Override
    public void updateBio(Long user_id, String bio) throws ServiceException {
        User u = em.find(User.class, user_id);
        if (u == null) {
            throw new ServiceException("User doesn't exist: " + user_id);
        }
        u.setBio(bio);
        em.merge(u);
    }

    @Override
    public void updateLocation(Long user_id, String location) throws ServiceException {
        User u = em.find(User.class, user_id);
        if (u == null) {
            throw new ServiceException("User doesn't exist: " + user_id);
        }
        u.setLocation(location);
        em.merge(u);
    }

    @Override
    public void updateTitle(Long user_id, String title) throws ServiceException {
        User u = em.find(User.class, user_id);
        if (u == null) {
            throw new ServiceException("User doesn't exist: " + user_id);
        }
        u.setTitle(title);
        em.merge(u);
    }

    @Override
    public void updateLocation(Long user_id, Double lon, Double lat) throws ServiceException {
        User u = em.find(User.class, user_id);
        if (u == null) {
            throw new ServiceException("User doesn't exist: " + user_id);
        }
        u.setLongitude(lon);
        u.setLatitude(lat);
        em.merge(u);
    }

    @Override
    public Coordinates getLocation(Long user_id) throws ServiceException {
        User u = em.find(User.class, user_id);
        if (u == null) {
            throw new ServiceException("User  doesn't exist: " + user_id);
        }
        return new Coordinates(u.getLongitude(), u.getLatitude());
    }

    @Override
    public void updateInterests(Long user_id, List<String> interests) throws ServiceException {
        User u = em.find(User.class, user_id);
        if (u == null) {
            throw new ServiceException("User doesn't exist: " + user_id);
        }
        log.info("Storing to the database: " + interests);
        u.setInterests(interests);
        em.merge(u);
    }

    @Override
    public void updateFirstLogin(Long user_id) throws ServiceException {

        User u = em.find(User.class, user_id);
        if (u == null) {
            throw new ServiceException("User doesn't exist: " + user_id);
        }
        u.setIsFirstLogin(false);
        em.merge(u);

    }

    @Override
    public void updateAvatar(Long user_id, String fileName, byte[] content) throws ServiceException {
        User u = em.find(User.class, user_id);
        if (u == null) {
            throw new ServiceException("User doesn't exist: " + user_id);
        }
        u.setAvatarFileName(fileName);
        u.setAvatarContent(content);
        em.merge(u);
    }

    @Override
    public void updateCover(Long user_id, String fileName, byte[] content) throws ServiceException {
        User find = em.find(User.class, user_id);
        if (find == null) {
            throw new ServiceException("User doesn't exist: " + user_id);
        }
        find.setCoverFileName(fileName);
        find.setCoverContent(content);
        em.merge(find);
    }

    @Override
    public byte[] getAvatar(Long user_id) throws ServiceException {
        User u = em.find(User.class, user_id);
        if (u == null) {
            return null;
        }
        return u.getAvatarContent();
    }

    @Override
    public byte[] getCover(Long user_id) throws ServiceException {
        User u = em.find(User.class, user_id);
        if (u == null) {
            return null;
        }
        return u.getCoverContent();
    }

    @Override
    public void removeAvatar(Long user_id) throws ServiceException {
        User u = em.find(User.class, user_id);
        if (u == null) {
            throw new ServiceException("User doesn't exist: " + user_id);
        }
        u.setAvatarFileName("");
        u.setAvatarContent(null);
        em.merge(u);
    }

    @Override
    public void removeCover(Long user_id) throws ServiceException {
        User u = em.find(User.class, user_id);
        if (u == null) {
            throw new ServiceException("User doesn't exist: " + user_id);
        }
        u.setCoverFileName("");
        u.setCoverContent(null);
        em.merge(u);
    }

    @Override
    public List<User> getAll() {
        return em.createNamedQuery("User.getAll", User.class).getResultList();
    }
    
    @Override
    public List<User> getAllLive() {
        return em.createNamedQuery("User.getAllLive", User.class).getResultList();
    }

    public void updateBothNames(Long user_id, String firstname, String lastname) throws ServiceException {
        User u = em.find(User.class, user_id);
        if (u == null) {
            throw new ServiceException("User doesn't exist: " + user_id);
        }
        u.setFirstname(firstname);
        u.setLastname(lastname);
        em.merge(u);
    }

    public void updateOriginallyFrom(Long user_id, String originallyfrom) throws ServiceException {
        User u = em.find(User.class, user_id);
        if (u == null) {
            throw new ServiceException("User doesn't exist: " + user_id);
        }
        u.setOriginallyFrom(originallyfrom);
        em.merge(u);
    }

    public void updateLookingFor(Long user_id, List<String> lookingForList) throws ServiceException {
        User u = em.find(User.class, user_id);
        if (u == null) {
            throw new ServiceException("User doesn't exist: " + user_id);
        }
        u.setLookingFor(lookingForList);
        em.merge(u);
    }

    public void updateCategories(Long user_id, List<String> categoriesList) throws ServiceException {
        User u = em.find(User.class, user_id);
        if (u == null) {
            throw new ServiceException("User doesn't exist: " + user_id);
        }
        u.setCategories(categoriesList);
        em.merge(u);
    }

    public void updateLongBio(Long user_id, String longbio) throws ServiceException {
        User u = em.find(User.class, user_id);
        if (u == null) {
            throw new ServiceException("User doesn't exist: " + user_id);
        }
        u.setLongBio(longbio);
        em.merge(u);
    }

    public void updateLive(Long user_id, String live) throws ServiceException {
        Boolean liveBoolean = Boolean.valueOf(live);
        User u = em.find(User.class, user_id);
        if (u == null) {
            throw new ServiceException("User doesn't exist: " + user_id);
        }
        System.out.println("LiveBoolean is: "+liveBoolean);
        u.setLive(liveBoolean);
        em.merge(u);
    }

    public void updateIams(Long user_id, List<String> iAmsList) throws ServiceException {
        User u = em.find(User.class, user_id);
        if (u == null) {
            throw new ServiceException("User doesn't exist: " + user_id);
        }
        u.setiAms(iAmsList);
        em.merge(u);
    }

    public void updateTwitter(Long user_id, String twitter) throws ServiceException {
        User u = em.find(User.class, user_id);
        if (u == null) {
            throw new ServiceException("User doesn't exist: " + user_id);
        }
        u.setTwitter(twitter);
        em.merge(u);
    }

    public void updateWebsite(Long user_id, String website) throws ServiceException {
        User u = em.find(User.class, user_id);
        if (u == null) {
            throw new ServiceException("User doesn't exist: " + user_id);
        }
        u.setWebsite(website);
        em.merge(u);
    }

    public void updateLinkedin(Long user_id, String linkedin) throws ServiceException {
        User u = em.find(User.class, user_id);
        if (u == null) {
            throw new ServiceException("User doesn't exist: " + user_id);
        }
        u.setLinkedin(linkedin);
        em.merge(u);
    }
    
    

}
