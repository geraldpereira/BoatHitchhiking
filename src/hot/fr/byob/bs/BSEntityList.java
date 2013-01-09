package fr.byob.bs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.event.ActionEvent;

import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.annotations.datamodel.DataModelSelection;
import org.jboss.seam.core.Expressions.ValueExpression;
import org.jboss.seam.framework.HibernateEntityQuery;

import fr.byob.bs.model.BSEntity;
import fr.byob.bs.model.utilisateur.Utilisateur;

public abstract class BSEntityList<T extends BSEntity> extends HibernateEntityQuery<T> {

	public final static int RENDER_TABLE = 0;
	public final static int RENDER_LIST = 1;
	private int renderMode = RENDER_LIST;

	private static final long serialVersionUID = 1L;

	/* Utilisateur Courant */
	@In(create = false, required = false)
	protected Utilisateur utilisateurCourant;

	/* Pagination */
	private Integer currentPage = 0;

	private int pagesDisplayed = 5;
	
	public Integer[] getPagesIntegerTab() {

		if (pagesDisplayed <= 0) {
			return null;
		}

		int pagesLeft = 0;
		int pagesRight = 0;

		if (pagesDisplayed % 2 == 1) {// Impair
			pagesLeft = pagesDisplayed / 2;
			pagesRight = pagesDisplayed / 2;
		} else {// Pair
			pagesLeft = (pagesDisplayed / 2) - 1;
			pagesRight = pagesDisplayed / 2;
		}

		int max = currentPage + pagesRight;
		int min = currentPage - pagesLeft;

		if (min < 0) {
			max += -min;
		} else if (max >= super.getPageCount()) {
			min += (super.getPageCount() - 1) - max;
		}

		if (max >= super.getPageCount()) {
			max = super.getPageCount() - 1;
		}
		if (min < 0) {
			min = 0;
		}

		Integer[] tab = new Integer[max - min + 1];
		for (int i = min, j = 0; i <= max; j++, i++) {
			tab[j] = i;
		}

		return tab;
	}

	public long getPrevFastResult() {
		if (currentPage != null) {
			int page = currentPage;
			page -= pagesDisplayed;
			if (page > 0) {
				return super.getMaxResults() * page;
			}
		}
		return 0;
	}

	public long getNextFastResult() {
		if (currentPage != null) {
			int page = currentPage;
			page += pagesDisplayed;
			if (page < super.getPageCount()) {
				return super.getMaxResults() * page;
			}
		}
		return super.getLastFirstResult();
	}

	@Override
	public void setFirstResult(Integer firstResult) {
		if (getFirstResult() != null && getFirstResult().equals(firstResult))
			return;

		if (firstResult == null || super.getMaxResults() == null) {
			currentPage = 0;
		} else {
			currentPage = firstResult / super.getMaxResults();
		}
		super.setFirstResult(firstResult);
	}

	public boolean isCurrentPage(int pageNumber) {
		return pageNumber == currentPage;
	}

	public int getPagesDisplayed() {
		return pagesDisplayed;
	}

	public void setPagesDisplayed(int pagesDisplayed) {
		this.pagesDisplayed = pagesDisplayed;
	}
	
	/* FIN Pagination */
	
	/* Selection */
	@DataModelSelection
	@Out(required = false)
	private T selection;

	private int currentRow;

	public T getSelection() {
		return this.selection;
	}

	public void setSelection(T selection) {
		this.selection = selection;
	}

	public int getCurrentRow() {
		return this.currentRow;
	}

	public void setCurrentRow(int currentRow) {
		this.currentRow = currentRow;
	}
	/* FIN Selection */
	

	/* Filtres */

	@DataModel
	private List<T> tempResultList;

	private final Map<String, T> tempResultMap = new HashMap<String, T>();
	private Long tempResultCount;

	private final HashMap<String, Object> filters = new HashMap<String, Object>() {
		@Override
		public Object put(String key, Object value) {
			// Hack for the boolean value : we do not want an unchecked checkbox
			// to equal a false value but a null one !
			if (value instanceof Boolean && ((Boolean) value == false)) {
				super.remove(key);
				return null;
			}
			return super.put(key, value);
		}
	};

	public void reset() {
		filters.clear();
		setFirstResult(null);
		reinitFilters();
		doFilterResultList();
	}

	protected abstract void reinitFilters();
	

	@Transactional
	@Override
	public List<T> getResultList() {
		return tempResultList;
	}

	@Transactional
	@Override
	public Long getResultCount() {
		return tempResultCount;
	}

	@Transactional
	@Begin(join = true)
	public void doFilterResultList() {
		tempResultList = super.getResultList();
		tempResultCount = super.getResultCount();
		tempResultMap.clear();
		for (T t : tempResultList) {
			tempResultMap.put(t.getObjectKey(), t);
		}
	}
	
	public T get(String id) {
		return tempResultMap.get(id);
	}

	public void doFilterResultList(ActionEvent e) {
		// Called from the Search button : Reinit the first result
		setFirstResult(null);
		// Do the search
		doFilterResultList();
	}

	@Override
	public void setEjbql(String ejbql) {
		if (getEjbql() != null && getEjbql().equals(ejbql))
			return;

		super.setEjbql(ejbql);
	}

	@Override
	public void setMaxResults(Integer maxResults) {
		if (getMaxResults() != null && getMaxResults().equals(maxResults))
			return;

		super.setMaxResults(maxResults);
	}

	@Override
	public void setOrder(String order) {
		if (getOrder() != null && getOrder().equals(order))
			return;

		super.setOrder(order);
		refresh();
	}

	@Override
	public void setRestrictions(List<ValueExpression> restrictions) {
		if (getRestrictions() != null && getRestrictions().equals(restrictions))
			return;

		super.setRestrictions(restrictions);
	}

	@Override
	public void setOrderDirection(String orderDirection) {
		if (getOrderDirection() != null
				&& getOrderDirection().equals(orderDirection))
			return;

		super.setOrderDirection(orderDirection);
		refresh();
	}

	@Override
	public void setOrderColumn(String orderColumn) {
		if (getOrderColumn() != null && getOrderColumn().equals(orderColumn))
			return;

		super.setOrderColumn(orderColumn);
		refresh();
	}

	public HashMap<String, Object> getFilters() {
		return filters;
	}
	
	/* FIN Filtres */
	

	protected void removeFromList(T ref) {
		if (tempResultList != null) {
			tempResultList.remove(ref);
		}
		if (tempResultCount != null) {
			tempResultCount = tempResultCount - 1;
		}
		tempResultMap.remove(ref.getObjectKey());
		System.out.println(tempResultMap);
	}
	
	protected void addToList(T ref) {
		if (tempResultList != null) {
			tempResultList.add(ref);
		}
		if (tempResultCount != null) {
			tempResultCount = tempResultCount + 1;
		}
		tempResultMap.put(ref.getObjectKey(), ref);
		System.out.println(tempResultMap);
	}

	/* Mode d'affichage */

	public int getRenderMode() {
		return this.renderMode;
	}

	public void switchRenderMode() {
		if (renderMode == RENDER_LIST) {
			this.renderMode = RENDER_TABLE;
		} else {
			this.renderMode = RENDER_LIST;
		}
	}

}
