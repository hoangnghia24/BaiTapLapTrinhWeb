package vn.iotstart.model;

import java.io.Serializable;

public class Category implements Serializable {

	private static final long serialVersionUID = 1L;

	private int cateId;
	private String cateName;
	private String icons;

	public Category() {
		super();
	}

	public Category(int cateId, String cateName, String icons) {
		super();
		this.cateId = cateId;
		this.cateName = cateName;
		this.icons = icons;
	}

	public int getCateId() {
		return cateId;
	}

	public void setCateId(int cateId) {
		this.cateId = cateId;
	}

	public String getCateName() {
		return cateName;
	}

	public void setCateName(String cateName) {
		this.cateName = cateName;
	}

	public String getIcons() {
		return icons;
	}

	public void setIcons(String icons) {
		this.icons = icons;
	}
}	