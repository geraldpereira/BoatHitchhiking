package fr.byob.bs.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.hibernate.validator.NotNull;

@MappedSuperclass
public abstract class Photo implements java.io.Serializable, Cloneable {

	private static final long serialVersionUID = 1L;

	@Column(name = "PATH", nullable = false)
	@NotNull
	private String path;
	
	private transient boolean isTMP = false;
	
	private transient String fullPath;
	
	private transient String filePath;

	public Photo() {
	}

	public Photo(final String path) {
		this.path = path;
	}

	protected Photo(String path, boolean isTMP, String fullPath, String filePath) {
		super();
		this.path = path;
		this.isTMP = isTMP;
		this.fullPath = fullPath;
		this.filePath = filePath;
	}

	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public boolean getIsTMP() {
		return this.isTMP;
	}

	public void setTMP(boolean isTMP) {
		this.isTMP = isTMP;
	}

	public String getFullPath() {
		return this.fullPath;
	}

	public void setFullPath(String fullPath) {
		this.fullPath = fullPath;
	}

	public String getFilePath() {
		return this.filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	@Override
	public abstract Photo clone();
	
}
