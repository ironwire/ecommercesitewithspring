package org.yiqixue.common.book;

import java.util.HashSet;
import java.util.Set;

import org.yiqixue.common.IdBasedEntity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;

@Entity
@Table(name="categories")

public class Category extends IdBasedEntity {
	
	@Column(length = 128, nullable = false, unique = true)
	private String name;
	
	private boolean enabled;
	
	@Column(name = "all_parent_ids", length = 256, nullable = true)
	private String allParentIDs;
	
	@OneToOne
	@JoinColumn(name = "parent_id")
	private Category parent;
	
	@OneToMany(mappedBy = "parent")
	@OrderBy("name asc")
	private Set<Category> children = new HashSet<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getAllParentIDs() {
		return allParentIDs;
	}

	public void setAllParentIDs(String allParentIDs) {
		this.allParentIDs = allParentIDs;
	}
	
	@JsonBackReference
	public Category getParent() {
		return parent;
	}

	public void setParent(Category parent) {
		this.parent = parent;
	}
	
	@JsonManagedReference
	public Set<Category> getChildren() {
		return children;
	}

	public void setChildren(Set<Category> children) {
		this.children = children;
	}


	public Category() {}
	
	public static Category copyIdAndName(Category category) {
		Category cate = new Category();
		cate.setId(category.getId());
		cate.setName(category.getName());
		return  cate;
	}
	
	public static Category copyFull(Category category) {
		Category cate = new Category();
		cate.setId(category.getId());
		cate.setName(category.getName());
		cate.setParent(category.getParent());
		cate.setEnabled(category.isEnabled());
		cate.setChildren(category.getChildren());
		
		return  cate;
		
	}
	
//	public static Category copyFull(Category category, String name) {
//		Category cate = new Category();
//		cate = cate.copyFull(category);
//		cate.setName(name);
//		return  cate;
//		
//	}
	
	public Category(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public Category(String name) {
		super();
		this.name = name;
	}
	
	public Category(String name, Category parent) {
		this(name);
		this.parent = parent;
	
	}
	public Category(Integer id) {
		this.id = id;
	}
	
//	@Transient
//	public String getImagePath() {
//		if (id == null || image == null)
//			return "/images/image-thumbnail.png";
//		return "/category-images/" + this.id + "/" + this.image;
//	}


}
