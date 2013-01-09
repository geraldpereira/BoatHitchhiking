package fr.byob.bs.utilisateur;

import org.jboss.seam.mock.SeamTest;
import org.testng.annotations.Test;

public class UtilisateurCreerTest extends SeamTest {

	// http://docs.jboss.com/seam/2.1.1.GA/reference/en-US/html/testing.html

	@Test
	public void testFoo() throws Exception {

		new ComponentTest() {

			protected void testComponents() throws Exception

			{
				setValue("#{identifiantHome.instance.pseudonyme}", "testtt");
				setValue("#{identifiantHome.instance.motDePasse}", "test2");
				setValue("#{identifiantHome.instance.mail}", "secret@test.fr");
				setValue("#{identifiantHome.mailConfirm}", "secret@test.fr");
				Object str = invokeMethod("#{utilisateurHome.persist}");
				System.out.println(str);
				assert str.equals("success");
			}

		}.run();

	}

}
