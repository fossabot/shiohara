package com.viglet.shiohara.persistence.model.site;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.viglet.shiohara.object.ShObjectType;
import com.viglet.shiohara.persistence.model.folder.ShFolder;
import com.viglet.shiohara.persistence.model.object.ShObject;

/**
 * The persistent class for the ShSite database table.
 * 
 */
@Entity
@NamedQuery(name = "ShSite.findAll", query = "SELECT s FROM ShSite s")
@JsonIgnoreProperties({ "shFolders", "shPosts" })
@PrimaryKeyJoinColumn(name = "object_id")
public class ShSite extends ShObject {
	private static final long serialVersionUID = 1L;

	private String name;

	private String description;

	private String url;

	@Column(name = "post_type_layout", length = 5 * 1024 * 1024) // 5Mb
	private String postTypeLayout;

	@OneToMany(mappedBy = "shSite")
	@Cascade({ CascadeType.ALL })
	@Fetch(org.hibernate.annotations.FetchMode.SUBSELECT)
	private Set<ShFolder> shFolders = new HashSet<ShFolder>();

	public ShSite() {
		this.setObjectType(ShObjectType.SITE);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<ShFolder> getShFolders() {
		return this.shFolders;
	}

	public void setShFolders(Set<ShFolder> shFolders) {
		this.shFolders.clear();
		if (shFolders != null) {
			this.shFolders.addAll(shFolders);
		}
	}

	public String getPostTypeLayout() {
		return postTypeLayout;
	}

	public void setPostTypeLayout(String postTypeLayout) {
		this.postTypeLayout = postTypeLayout;
	}
	
	@Override
	public String getObjectType() {
		return ShObjectType.SITE;
	}

	@Override
	public void setObjectType(String objectType) {		
		super.setObjectType(ShObjectType.SITE);
	}
}
