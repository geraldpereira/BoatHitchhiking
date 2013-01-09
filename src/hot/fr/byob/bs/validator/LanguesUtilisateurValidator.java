package fr.byob.bs.validator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.validator.Validator;

import fr.byob.bs.model.Langue;
import fr.byob.bs.model.utilisateur.LangueUtilisateur;

public class LanguesUtilisateurValidator implements
		Validator<LanguesUtilisateur> {

	@Override
	public boolean isValid(Object o) {
		if (o == null) {
			return false;
		}
		if (o instanceof List) {
			return isValid((List<LangueUtilisateur>) o);
		}
		return false;
	}

	protected boolean isValid(List<LangueUtilisateur> languesUtilisateur) {
		Set<Langue> setLangues = new HashSet<Langue>(languesUtilisateur.size());
		// Parcours des langues utilisateur
		for (LangueUtilisateur curLangueUtilisateur : languesUtilisateur) {
			if (!setLangues.add(curLangueUtilisateur.getLangue())) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void initialize(LanguesUtilisateur arg0) {

	}
}