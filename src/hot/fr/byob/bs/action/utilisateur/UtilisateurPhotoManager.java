package fr.byob.bs.action.utilisateur;

import java.io.Serializable;

import org.hibernate.Session;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.log.Log;
import org.richfaces.event.UploadEvent;

import fr.byob.bs.action.PhotoDAO;
import fr.byob.bs.action.PhotoDAOListener;
import fr.byob.bs.debug.MeasureCalls;
import fr.byob.bs.model.utilisateur.PhotoUtilisateur;
import fr.byob.bs.model.utilisateur.Utilisateur;

@Name("utilisateurPhotoManager")
@Scope(ScopeType.CONVERSATION)
@MeasureCalls
public class UtilisateurPhotoManager implements Serializable, PhotoDAOListener<PhotoUtilisateur> {

	private final static int NOMBRE_PHOTOS = 1;

	private static final long serialVersionUID = 1L;

	@Logger
	private transient Log log;

	@In(create = true)
	Session hibernateSession;

	@In(create = true)
	private PhotoDAO<PhotoUtilisateur> photoDAO;

	@In(create = false, required = false)
	protected Utilisateur utilisateurCourant;

	@In(create = true)
	private UtilisateurEdit utilisateurEdit;
	
	public void listener(UploadEvent event) {
		if (event != null) {
			photoDAO.trashAllPhotos();
			photoDAO.removeTMPFiles();
			photoDAO.uploadItem(event.getUploadItem(), new PhotoUtilisateur());
		}
	}

	@Transactional
	public void updatePhoto() {
		log.info("Updating user picture");
		
		utilisateurEdit.getInstance().setPhotoUtilisateur(getPhotoUtilisateur());
		// Supprimer les fichier temporaires et les fichiers des photos
		// supprimées
		photoDAO.copyPhotosToFinalDirectory();
		photoDAO.removeTrashedPhotos();
		photoDAO.removeTMPFiles();
	}

	@Transactional
	public void persistPhoto() {
		log.info("Persisting user picture");
		
		// Set the user name
		photoDAO.setUtilisateurPseudo(utilisateurEdit.getInstance().getPseudonyme());

		utilisateurEdit.getInstance().setPhotoUtilisateur(getPhotoUtilisateur());
		
		photoDAO.copyPhotosToFinalDirectory();
		photoDAO.removeTMPFiles();
	}

	public void supprimerPhotoUtilisateur() {
		photoDAO.trashPhoto(0);
	}

	public void wire() {
		PhotoUtilisateur photo = utilisateurEdit.getInstance().getPhotoUtilisateur();
		String pseudo = null;
		if (utilisateurCourant != null) {
			pseudo = utilisateurCourant.getPseudonyme();
		}
		photoDAO.setUtilisateurPseudo(pseudo);
		photoDAO.loadPhotos(NOMBRE_PHOTOS, photo);
		photoDAO.addPhotoDAOListener(this);
	}
	
	public PhotoUtilisateur getPhotoUtilisateur() {
		if (photoDAO.getPhotos().isEmpty()) {
			return null;
		}
		return photoDAO.getPhotos().get(0);
	}

	@Override
	public void photoRemoved(PhotoUtilisateur photo) {
		hibernateSession.delete(photo);
	}

	@Override
	public void photoPersisted(PhotoUtilisateur photo) {
		hibernateSession.persist(photo);
	}
	
	/*
	 * Supprime le répertoire utilisateur (et donc les photo qui va avec)
	 */
	public void supprimerRepertoireUtilisateur() {
		photoDAO.removeUserDirectory();
	}

	/**
	 * Supprime le fichier associé a notre photo dans le répertorie temporaire
	 * (si présent)
	 */
	public void supprimerFichierTemporaire() {
		photoDAO.removeTMPFiles();
	}
}
