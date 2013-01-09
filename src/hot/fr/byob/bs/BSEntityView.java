package fr.byob.bs;

import static fr.byob.bs.Constantes.CANCELLED;

import org.jboss.seam.annotations.End;

import fr.byob.bs.model.BSEntity;

public abstract class BSEntityView<T extends BSEntity> extends BSEntityManager<T> {

	private static final long serialVersionUID = 1L;

	public boolean estProprietaire() {
		if (utilisateurCourant == null) {
			return false;
		}
		return true;
	}

	@End
	public String cancel() {
		return CANCELLED;
	}

	@Override
	public String persist() {
		throw new UnsupportedOperationException("Use BSEntityEdit instead !");
	}

	@Override
	public String update() {
		throw new UnsupportedOperationException("Use BSEntityEdit instead !");
	}

	@Override
	public String remove() {
		throw new UnsupportedOperationException("Use BSEntityEdit instead !");
	}
}
