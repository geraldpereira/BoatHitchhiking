package fr.byob.bs;

import fr.byob.bs.model.BSEntity;

public abstract class BSEntityEdit<T extends BSEntity> extends BSEntityManager<T> {

	private static final long serialVersionUID = 1L;

	public abstract void validerAuthorisation();


}
