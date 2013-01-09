package fr.byob.bs.model;

import javax.persistence.Transient;

public interface BSEntity {

	@Transient
	public String getObjectKey();
	
	@Transient
	public String getObjectLabel();
	
	@Transient
	public String getObjectType();
}
