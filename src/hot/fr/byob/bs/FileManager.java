package fr.byob.bs;

import java.io.File;

public class FileManager {

	public static void supprimerRepertoire(String path) {
		File repertoire = new File(path);
		supprimerRepertoire(repertoire);
	}

	private static boolean supprimerRepertoire(File repertoire) {
		if (repertoire.isDirectory()) {
			File[] fileList = repertoire.listFiles();
			for (int i = 0; i < fileList.length; i++) {
				if (fileList[i].isDirectory()) {
					supprimerRepertoire(fileList[i]);
				} else {
					fileList[i].delete();
				}
			}
		}
		return repertoire.delete();
	}

	public static boolean supprimerFichier(String path) {
		if (path == null || "".equals(path)) {
			return false;
		}
		File fichier = new File(path);
		return fichier.delete();
	}

}
