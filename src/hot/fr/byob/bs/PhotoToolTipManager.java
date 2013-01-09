package fr.byob.bs;

import static fr.byob.bs.Constantes.PROP_UTILISATEUR_PHOTO_URL;

import java.util.HashMap;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Startup;

import fr.byob.bs.model.Photo;
import fr.byob.bs.model.utilisateur.Utilisateur;

@Startup
@Name("photoToolTipManager")
@Scope(ScopeType.APPLICATION)
public class PhotoToolTipManager {
	
	@In(create = true, required = true)
	protected HashMap<String, String> proprietes;

	/**
	 * Retourn vrais si la photo à un path (et donc un fichier associé)
	 * 
	 * @return vrais ... ou pas.
	 */
	public static boolean hasPhoto(Photo curPhoto) {
		if (curPhoto != null && curPhoto.getPath() != null) {
			return true;
		}
		return false;
	}

	/**
	 * Retourne le path complet de notre photo (dynamique)
	 * 
	 * @return tout pareil
	 */
	public String getFullPath(Utilisateur utilisateur, Photo curPhoto) {
		StringBuilder sb = new StringBuilder();
		sb.append(proprietes.get(PROP_UTILISATEUR_PHOTO_URL)).append(
				utilisateur.getPseudonyme()).append("/").append(
				curPhoto.getPath());
		return sb.toString();
	}
}
