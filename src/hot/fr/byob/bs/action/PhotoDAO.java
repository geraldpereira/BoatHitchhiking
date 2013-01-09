package fr.byob.bs.action;

import static fr.byob.bs.Constantes.PROP_TMP_PHOTO_LOCAL_PATH;
import static fr.byob.bs.Constantes.PROP_TMP_PHOTO_URL;
import static fr.byob.bs.Constantes.PROP_UTILISATEUR_PHOTO_LOCAL_PATH;
import static fr.byob.bs.Constantes.PROP_UTILISATEUR_PHOTO_URL;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;
import org.richfaces.model.UploadItem;

import fr.byob.bs.FileManager;
import fr.byob.bs.model.Photo;

/**
 * Uploader une photo, dans le répertoire temporaire
 * 
 * Copier une photo du répertoire temporaire, vers un répertoire donné
 * 
 * Supprimer une photo, soit dans le répertoire temporaire, soit dans le
 * répertoire final
 * 
 * Gerer un nombre de photos
 * 
 * @author Administrateur
 * 
 */

@Name("photoDAO")
@Scope(ScopeType.CONVERSATION)
public class PhotoDAO<P extends Photo> implements Serializable {

	private static final long serialVersionUID = 1L;

	@Logger
	private transient Log log;

	@In(create = true)
	private HashMap<String, String> proprietes;

	String utilisateurPseudo;

	private List<P> photos;
	private List<P> photosASupprimer;
	private int uploadsAvailable;

	private final List<PhotoDAOListener<P>> listeners = new ArrayList<PhotoDAOListener<P>>();

	public void addPhotoDAOListener(PhotoDAOListener<P> listener) {
		listeners.add(listener);
	}

	public void removePhotoDAOListener(PhotoDAOListener<P> listener) {
		listeners.remove(listener);
	}

	public List<P> getPhotos() {
		return this.photos;
	}

	public List<P> getPhotosASupprimer() {
		return this.photosASupprimer;
	}

	public int getUploadsAvailable() {
		return this.uploadsAvailable;
	}

	public void loadPhotos(final int uploadsAvailable, final P photo) {
		ArrayList<P> photos = new ArrayList<P>(uploadsAvailable);
		if (photo != null) {
			photos.add(photo);
		}
		loadPhotos(uploadsAvailable, photos);
	}

	public void loadPhotos(final int uploadsAvailable) {
		loadPhotos(uploadsAvailable, new ArrayList<P>(uploadsAvailable));
	}

	public void loadPhotos(final int uploadsAvailable, final List<P> photos) {
		this.uploadsAvailable = uploadsAvailable;
		this.photos = photos;
		this.photosASupprimer = new ArrayList<P>(uploadsAvailable);
		for (P photo : photos) {
			loadPhoto(photo, false);
			this.uploadsAvailable--;
		}
	}

	public void loadPhoto(P photo, boolean isTMP) {
		photo.setTMP(isTMP);
		StringBuilder sb = new StringBuilder();
		if (photo.getIsTMP()) {
			sb.append(proprietes.get(PROP_TMP_PHOTO_URL)).append(photo.getPath());
		} else {
			sb.append(proprietes.get(PROP_UTILISATEUR_PHOTO_URL)).append(utilisateurPseudo).append("/").append(photo.getPath());
		}
		photo.setFullPath(sb.toString());

		String repo = "";
		if (photo.getIsTMP()) {
			repo = proprietes.get(PROP_TMP_PHOTO_LOCAL_PATH);
		} else {
			repo = proprietes.get(PROP_UTILISATEUR_PHOTO_LOCAL_PATH) + utilisateurPseudo + File.separator;
		}
		photo.setFilePath(repo + photo.getPath());
	}

	public void trashPhoto(int index) {
		P photo = photos.remove(index);
		if (photo != null) {
			if (photo.getIsTMP()) {
				FileManager.supprimerFichier(photo.getFilePath());
			} else {
				photosASupprimer.add(photo);
			}
		}
		uploadsAvailable++;
	}

	public void trashAllPhotos() {
		if (photos == null) {
			return;
		}
		for (int i = 0; i < photos.size(); i++) {
			trashPhoto(i);
		}
	}

	public void removeTMPFiles() {
		if (photos == null) {
			return;
		}
		for (P photo : photos) {
			if (photo.getIsTMP()) {
				FileManager.supprimerFichier(photo.getFilePath());
			}
		}
	}

	public void removeTrashedPhotos() {
		if (photosASupprimer == null) {
			return;
		}
		for (P photo : photosASupprimer) {
			if (FileManager.supprimerFichier(photo.getFilePath())) {
				for (PhotoDAOListener<P> listener : listeners) {
					listener.photoRemoved(photo);
				}
			}
		}
		photosASupprimer.clear();
	}

	public void removeUserDirectory() {
		// Supprimer le répertoire du utilisateur utilisateur (si présent)
		if (utilisateurPseudo != null) {
			String peudo = utilisateurPseudo;
			if (peudo != null) {
				FileManager.supprimerRepertoire(proprietes.get(PROP_UTILISATEUR_PHOTO_LOCAL_PATH) + peudo);
			}
		}
	}

	public boolean copyPhotosToFinalDirectory() {
		if (photos == null) {
			return false;
		}
		for (P photo : photos) {
			if (photo.getIsTMP()) {
				// Copier le fichier
				if (!copyPhotoFile(photo)) {
					return false;
				} else {
					for (PhotoDAOListener<P> listener : listeners) {
						listener.photoPersisted(photo);
					}
				}
			}
		}
		return true;
	}

	private String uploadPhoto(UploadItem uploadItem) {

		if (uploadItem.getFile().isFile()) { // si c'est bien un fichier
			// Dossier d'enregistrement temporaire des photos
			String repo = proprietes.get(PROP_TMP_PHOTO_LOCAL_PATH);
			// Nom du fichier uploadé
			String uploadItemFileName = uploadItem.getFileName().replace(' ', '_');
			// On ne garde que le nom du fichier, le path complet étant géré
			// dynamiquement
			if (uploadItemFileName.contains("/")) {
				int i = uploadItemFileName.lastIndexOf("/");
				uploadItemFileName = uploadItemFileName.substring(i + 1);
			}

			if (uploadItemFileName.contains("\\")) {
				int i = uploadItemFileName.lastIndexOf("\\");
				uploadItemFileName = uploadItemFileName.substring(i + 1);
			}
			
			uploadItemFileName = uploadItemFileName.replace(':', '_');
			
			// Ficher de la photo
			File fileToWrite = getUniqueFile(repo, uploadItemFileName, null);
			File uploadedFile = uploadItem.getFile();
			FileChannel in = null;
			FileChannel out = null;
			try {
				// On recopie le fichier depuis le tmp du serveur d'application
				// vers tmp BS
				in = new FileInputStream(uploadedFile).getChannel();
				out = new FileOutputStream(fileToWrite).getChannel();
				in.transferTo(0, in.size(), out);

				// On retourne le nom du fichier créé
				return fileToWrite.getName();
			} catch (IOException ex1) {
				log.error("Error while copying the file.");
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException ex2) {
						log.error("Can't close input file channel");
					}
					if (out != null) {
						try {
							out.close();
						} catch (IOException ex3) {
							log.error("Can't close ouput file channel");
						}
					}
				}
			}
		}
		return null;
	}

	public void uploadItem(UploadItem item, P photo) {
		photo.setPath(uploadPhoto(item));
		loadPhoto(photo, true);
		photos.add(photo);
		uploadsAvailable--;
	}

	private boolean copyPhotoFile(P photo) {
		if (photo.getIsTMP()) {
			File file = new File(photo.getFilePath());

			String userRepo = proprietes.get(PROP_UTILISATEUR_PHOTO_LOCAL_PATH) + utilisateurPseudo + File.separator;

			File newDir = new File(userRepo);
			newDir.mkdirs();

			File newFile = getUniqueFile(userRepo, photo.getPath(), photo);

			if (file.renameTo(newFile)) {
				loadPhoto(photo, false);
				return true;
			}
		} else {
			log.error("Trying to copy a photo that is not in tmp directory");
		}
		return false;
	}

	private File getUniqueFile(String repo, String fileName, P photo) {
		File fileToWrite = new File(repo + fileName);
		int i = 1;
		// On fait attention qu'aucun fichier de même nom n'existe déjà
		while (fileToWrite.exists()) {
			int index = fileName.lastIndexOf('.');
			String debut = fileName.substring(0, index);
			String fin = fileName.substring(index);
			fileToWrite = new File(repo + debut + "_" + i + fin);
			if (photo != null) {
				photo.setPath(debut + "_" + i + fin);
			}
			i++;
		}
		return fileToWrite;
	}

	public void setUtilisateurPseudo(String utilisateurPseudo) {
		this.utilisateurPseudo = utilisateurPseudo;
	}

}
