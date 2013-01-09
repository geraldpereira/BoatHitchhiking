package fr.byob.bs.action.annonce;

import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Startup;

import fr.byob.bs.PhotoToolTipManager;
import fr.byob.bs.action.utilisateur.messagerie.ContactManager;
import fr.byob.bs.debug.MeasureCalls;
import fr.byob.bs.model.Photo;
import fr.byob.bs.model.annonce.Annonce;
import fr.byob.bs.model.annonce.Demande;
import fr.byob.bs.model.annonce.Offre;

@Startup
@Name("annonceUtils")
@Scope(ScopeType.APPLICATION)
@MeasureCalls
public class AnnonceUtils {

	public static boolean hasPhoto(Annonce annonce) {
		boolean hasPhoto = false;

		if (isDemande(annonce)) {
			Demande demande = (Demande) annonce;
			hasPhoto = PhotoToolTipManager.hasPhoto(demande.getUtilisateur().getPhotoUtilisateur());

		} else if (isOffre(annonce)) {
			Offre offre = (Offre) annonce;
			boolean hasPhotoUtilisateur = PhotoToolTipManager.hasPhoto(offre.getUtilisateur().getPhotoUtilisateur());
			boolean hasPhotoBateau = !offre.getBateau().getPhotosBateau().isEmpty();
			hasPhoto = hasPhotoBateau || hasPhotoUtilisateur;
		}

		return hasPhoto;
	}

	public static boolean isOffre(Annonce annonce) {
		return ContactManager.OBJECT_TYPE.OFFRE.name().equals(annonce.getObjectType());
	}

	public static boolean isDemande(Annonce annonce) {
		return ContactManager.OBJECT_TYPE.DEMANDE.name().equals(annonce.getObjectType());
	}

	public static List<Photo> concatPhotos(List<Photo> list, Photo photo) {

		ArrayList<Photo> photos = new ArrayList<Photo>();
		if (list != null && !list.isEmpty()) {
			photos.addAll(list);
		}
		if (photo != null) {
			photos.add(photo);
		}

		return photos;
	}
}
