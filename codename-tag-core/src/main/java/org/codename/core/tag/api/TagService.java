/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.core.tag.api;

import java.awt.Color;
import java.util.List;

import org.codename.core.exceptions.ServiceException;
import org.codename.model.tag.Tag;


/**
 *
 * @author gabriela
 */

public interface TagService {

	Long createTag(Long tagCreatorId, String tagName, Color color, boolean isPublic);
	List<Tag> getAllTagsCreatedByUser(Long creatorId);
	void deleteTag(Long tagId) throws ServiceException;

}
