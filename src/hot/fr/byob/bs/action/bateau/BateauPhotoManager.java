package fr.byob.bs.action.bateau;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
import fr.byob.bs.model.bateau.PhotoBateau;
import fr.byob.bs.model.utilisateur.Utilisateur;

@Name("bateauPhotoManager")
@Scope(ScopeType.CONVERSATION)
@MeasureCalls
public class BateauPhotoManager implements Serializable, PhotoDAOListener<PhotoBateau> {

	private final static int NOMBRE_PHOTOS = 3;

	private static final long serialVersionUID = 1L;

	@Logger
	private transient Log log;

	@In(create = true)
	Session hibernateSession;

	@In(create = true)
	private PhotoDAO<PhotoBateau> photoDAO;

	@In(create = false, required = false)
	protected Utilisateur utilisateurCourant;
	
	@In(create = true)
	private BateauEdit bateauEdit;
	
	private List<PhotoBateau> photosBCK; // Pour le cancel !

	public void listener(UploadEvent event) {
		if (event != null) {
			photoDAO.uploadItem(event.getUploadItem(), new PhotoBateau(bateauEdit.getInstance()));
		}
	}

	public void reset() {
		photoDAO.removeTMPFiles();
		bateauEdit.getInstance().setPhotosBateau(photosBCK);
		photoDAO.loadPhotos(NOMBRE_PHOTOS, photosBCK);
	}

	@Transactional
	public void removePhotos() {
		log.info("Removing boat pictures");
		photoDAO.trashAllPhotos();
		photoDAO.removeTrashedPhotos();
		photoDAO.removeTMPFiles();
	}

	@Transactional
	public void updatePhotos() {
		log.info("Updating boat pictures");
		// Supprimer les fichier temporaires et les fichiers des photos
		// supprimées
		photoDAO.copyPhotosToFinalDirectory();
		photoDAO.removeTrashedPhotos();
		photoDAO.removeTMPFiles();
	}

	@Transactional
	public void persistPhotos() {
		log.info("Persisting boat pictures");
		photoDAO.copyPhotosToFinalDirectory();
		photoDAO.removeTMPFiles();
	}

	public void supprimerPhotoBateau(final int index) {
		photoDAO.trashPhoto(index);
	}

	public void wire() {
		List<PhotoBateau> photos = bateauEdit.getInstance().getPhotosBateau();
		if (photos == null) {
			photos = new ArrayList<PhotoBateau>();
			bateauEdit.getInstance().setPhotosBateau(photos);
		}
		photosBCK = new ArrayList<PhotoBateau>(photos);
		String pseudo = null;
		if (utilisateurCourant != null) {
			pseudo = utilisateurCourant.getPseudonyme();
		}
		photoDAO.setUtilisateurPseudo(pseudo);
		photoDAO.loadPhotos(NOMBRE_PHOTOS, photos);
		photoDAO.addPhotoDAOListener(this);
	}

	@Override
	public void photoRemoved(PhotoBateau photo) {
		hibernateSession.delete(photo);
	}

	@Override
	public void photoPersisted(PhotoBateau photo) {
		hibernateSession.persist(photo);
	}
}
