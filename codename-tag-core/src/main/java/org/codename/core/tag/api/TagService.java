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

	Long createTag(Long tagCreatorId, String tagName, Color color, boolean isPublic) throws ServiceException;
	List<Tag> getAllTagsCreatedByUser(Long creatorId);
	void deleteTag(Long tagId) throws ServiceException;
	void editTag(Long tagId, String tagName, Color color, boolean isPublic) throws ServiceException;
	void editTagName(Long tagId, String tagName) throws ServiceException;
	void editTagColor(Long tagId, Color color) throws ServiceException;
	void editTagPublic(Long tagId, boolean isPublic) throws ServiceException;
	Tag getTagById(Long tagId) throws ServiceException;
	List<Tag> getAllPublicTags(boolean isPublic) throws ServiceException;
	boolean isTagNameFree(Long creatorId, String tagName) throws ServiceException;

}
