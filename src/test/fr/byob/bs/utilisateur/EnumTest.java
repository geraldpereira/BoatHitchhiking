package fr.byob.bs.utilisateur;

import java.util.Arrays;

import fr.byob.bs.model.utilisateur.Sexe;

public class EnumTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		for (Sexe sexe : Arrays.asList(Sexe.values())){
			System.out.println(sexe + " "+sexe.name());
		}
		System.out.println(Sexe.values());
	}

}
