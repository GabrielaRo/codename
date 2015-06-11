/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.services.impl;

import java.util.List;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.codename.model.Interest;
import org.codename.model.Profile;
import org.codename.model.User;
import org.codename.services.api.ProfilesService;
import org.codename.services.exceptions.ServiceException;

/**
 *
 * @author salaboy
 */
@ApplicationScoped
public class ProfilesServiceImpl implements ProfilesService {

    @PersistenceContext(unitName = "primary")
    private EntityManager em;

    private final static Logger log = Logger.getLogger(ProfilesServiceImpl.class.getName());

    @Override
    public boolean exist(Long user_id) throws ServiceException {
        return (em.find(Profile.class, user_id) != null);
    }

    @Override
    public Profile getById(Long user_id) throws ServiceException {
        return em.find(Profile.class, user_id);
    }

    @Override
    public void create(Long user_id) throws ServiceException {
        User user = em.find(User.class, user_id);
        if (user == null) {
            throw new ServiceException("Profile wasn't created because: there is no user with id: " + user_id);
        }
        em.persist(new Profile(user));
    }

    @Override
    public void update(Long user_id, String firstname, String lastname, String location, String bio, String title) throws ServiceException {
        Profile p = em.find(Profile.class, user_id);
        if (p == null) {
            throw new ServiceException("User Profile doesn't exist: " + user_id);
        }
        p.setFirstname(firstname);
        p.setLastname(lastname);
        p.setIntroduction(bio);
        p.setPostcode(location);
        p.setTitle(title);

    }

    @Override
    public void setInterests(Long user_id, List<Interest> interests) throws ServiceException {
        Profile profile = em.find(Profile.class, user_id);
        if (profile == null) {
            throw new ServiceException("User Profile doesn't exist: " + user_id);
        }
        log.info("Storing to the database: " + interests);
        profile.setInterests(interests);
        em.merge(profile);
    }

    @Override
    public List<Interest> getInterests(Long user_id) throws ServiceException {
        Profile profile = em.find(Profile.class, user_id);
        if (profile == null) {
            throw new ServiceException("User Profile doesn't exist: " + user_id);
        }
        log.info("Interest from the database: " + profile.getInterests());
        return profile.getInterests();
    }

    @Override
    public void updateAvatar(Long user_id, String fileName, byte[] content) throws ServiceException {
        Profile find = em.find(Profile.class, user_id);
        if (find == null) {
            throw new ServiceException("User Profile doesn't exist: " + user_id);
        }
        find.setAvatarFileName(fileName);
        find.setAvatarContent(content);
        em.merge(find);
    }

    @Override
    public void updateCover(Long user_id, String fileName, byte[] content) throws ServiceException {
        Profile find = em.find(Profile.class, user_id);
        if (find == null) {
            throw new ServiceException("User Profile doesn't exist: " + user_id);
        }
        find.setCoverFileName(fileName);
        find.setCoverContent(content);
        em.merge(find);
    }

    @Override
    public byte[] getAvatar(Long user_id) throws ServiceException {
        Profile find = em.find(Profile.class, user_id);
        if (find == null) {
            return null;
        }
        return find.getAvatarContent();
    }

    @Override
    public byte[] getCover(Long user_id) throws ServiceException {
        Profile find = em.find(Profile.class, user_id);
        if (find == null) {
            return null;
        }
        return find.getCoverContent();
    }

    @Override
    public void removeAvatar(Long user_id) throws ServiceException {
        Profile find = em.find(Profile.class, user_id);
        if (find == null) {
            throw new ServiceException("User Profile doesn't exist: " + user_id);
        }
        find.setAvatarFileName("");
        find.setAvatarContent(null);
        em.merge(find);
    }

    @Override
    public void removeCover(Long user_id) throws ServiceException {
        Profile find = em.find(Profile.class, user_id);
        if (find == null) {
            throw new ServiceException("User Profile doesn't exist: " + user_id);
        }
        find.setCoverFileName("");
        find.setCoverContent(null);
        em.merge(find);
    }

    @Override
    public List<Profile> getAll() {
        return em.createNamedQuery("Profile.getAll", Profile.class).getResultList();
    }

}
