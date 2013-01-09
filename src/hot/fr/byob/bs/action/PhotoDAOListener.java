package fr.byob.bs.action;

import fr.byob.bs.model.Photo;

public interface PhotoDAOListener<P extends Photo> {

	public void photoPersisted(P photo);

	public void photoRemoved(P photo);
	
}
