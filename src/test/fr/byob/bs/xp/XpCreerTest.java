package fr.byob.bs.xp;

import org.jboss.seam.mock.SeamTest;
import org.testng.annotations.Test;

import fr.byob.bs.model.bateau.TypeCoque;

public class XpCreerTest extends SeamTest {

	// http://docs.jboss.com/seam/2.1.1.GA/reference/en-US/html/testing.html

	@Test
	public void testFoo() throws Exception {

		new ComponentTest() {

			protected void testComponents() throws Exception

			{
				setValue("#{xpHome.instance.titre}", "testxp");
				setValue("#{xpHome.instance.description}", "C'est trop bien youpi :)");
				setValue("#{xpHome.instance.typeCoque}", 1);
				setValue("#{xpHome.instance.typeDuree}", 2);
				setValue("#{xpHome.instance.typePoste}", 1);
				setValue("#{xpHome.instance.typeXp}", 2);
				setValue("#{xpHome.instance.duree}", 3);
				setValue("#{xpHome.instance.utilisateur}", 2);
				Object str = invokeMethod("#{xpHome.persist}");
				System.out.println(str);
				assert str.equals("success");
			}

		}.run();

	}

}
