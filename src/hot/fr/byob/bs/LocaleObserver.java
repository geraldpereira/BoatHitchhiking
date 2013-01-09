package fr.byob.bs;

import org.hibernate.Query;
import org.hibernate.Session;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.FlushModeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.international.LocaleSelector;
import org.jboss.seam.log.Log;

import fr.byob.bs.debug.MeasureCalls;
import fr.byob.bs.model.utilisateur.Utilisateur;

@Name("localeObserver")
@MeasureCalls
public class LocaleObserver {
	@Logger
	private Log log;

	@In(required = false, scope = ScopeType.SESSION)
	private Utilisateur utilisateurCourant;

	@In
	private Session hibernateSession;
	@In("org.jboss.seam.international.localeSelector")
	LocaleSelector localeSelector;

	/**
	 * Observe la modification de la locale. Cette méthode est appellée a chaque
	 * connexion; heureusement l'utilisateur est null et l'enregistrment en base
	 * n'est donc pas effectué (voir la classe d'authentification)
	 * 
	 * Je sais pas trop pkoi mais l'annotation @Begin permet le commit de
	 * l'update et donc sa prise en compte.
	 * 
	 * @param localeString
	 *            la locale sélectionnée
	 */
	@Observer("org.jboss.seam.localeSelected")
	@Transactional
	@Begin(join = true, flushMode = FlushModeType.COMMIT)
	public void localeModifiee(String localeString) {
		if (utilisateurCourant != null && !localeString.equals(utilisateurCourant.getLanguePreferee())) {
			utilisateurCourant.setLanguePreferee(localeString);
			
			Query query = hibernateSession
					.getNamedQuery("user.updateLangueByPseudo");
			query.setParameter("locale", localeString);
			query.setParameter("pseudo", utilisateurCourant.getPseudonyme());
			query.executeUpdate();
		}
		log.info("Locale modified to {0} for user {1}",
				localeString, utilisateurCourant);

	}
	
}
