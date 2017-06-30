package com.viglet.shiohara.persistence.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the ShRegion database table.
 * 
 */
@Entity
@NamedQuery(name="ShRegion.findAll", query="SELECT s FROM ShRegion s")
public class ShRegion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private String id;

	private String name;

	//bi-directional many-to-one association to ShPost
	@ManyToOne
	@JoinColumn(name="post_id")
	private ShPost shPost;

	//bi-directional many-to-one association to ShPostType
	@ManyToOne
	@JoinColumn(name="post_type_id")
	private ShPostType shPostType;

	public ShRegion() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ShPost getShPost() {
		return this.shPost;
	}

	public void setShPost(ShPost shPost) {
		this.shPost = shPost;
	}

	public ShPostType getShPostType() {
		return this.shPostType;
	}

	public void setShpostType(ShPostType shPostType) {
		this.shPostType = shPostType;
	}

}