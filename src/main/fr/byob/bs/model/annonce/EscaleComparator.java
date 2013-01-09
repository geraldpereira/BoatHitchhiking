package fr.byob.bs.model.annonce;

import java.util.Comparator;

public class EscaleComparator implements Comparator<Escale> {
	@Override
	public int compare(Escale escale1, Escale escale2) {
		return escale1.compareTo(escale2);
	}
}
