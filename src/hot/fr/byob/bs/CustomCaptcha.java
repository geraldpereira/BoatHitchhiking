package fr.byob.bs;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.captcha.Captcha;

@Name("org.jboss.seam.captcha.captcha")
@Scope(ScopeType.SESSION)
public class CustomCaptcha extends Captcha {

	private static final long serialVersionUID = 1L;
	private final int widthImg = 95;
	private final int heightImg = 20;
	private final Color color1 = new Color(3, 165, 221);
	private final Color color2 = new Color(255, 145, 12);
	private final Color color3 = new Color(209, 204, 173);
	
	@Override
	public BufferedImage renderChallenge() {
		BufferedImage challenge = new BufferedImage(widthImg, heightImg, BufferedImage.TYPE_INT_RGB);
		Graphics graphics = challenge.getGraphics();
		graphics.setColor(color3);
		graphics.fillRect(0, 0, getChallengeImageWidth(), heightImg);
		Color col = null;
		for (int i = 0; i < widthImg; i += 2) // gamme des gris
		{
			col = new Color(i + 153, i + 152, i + 151);
			graphics.setColor(col);
			graphics.drawLine(i, 0, i, heightImg);
			graphics.drawLine(i + 1, 0, i + 1, heightImg);
		}

		for (int i = 0; i < 20; i++) {

			int lineArc = (int) (Math.random() * (2 + 1 - 1)) + 1;
			graphics.setColor(color2);
			if (lineArc == 1) {
				int xMin = 0;
				int xMax = getChallengeImageWidth();
				int yMin = 0;
				int yMax = heightImg;
				int x1 = (int) (Math.random() * (xMax + 1 - xMin)) + xMin;
				int x2 = (int) (Math.random() * (xMax + 1 - xMin)) + xMin;
				int y1 = (int) (Math.random() * (yMax + 1 - yMin)) + yMin;
				int y2 = (int) (Math.random() * (yMax + 1 - yMin)) + yMin;
				graphics.drawLine(x1, y1, x2, y2);
			} else {
				int xMin = 0;
				int xMax = getChallengeImageWidth();
				int yMin = 0;
				int yMax = heightImg;
				int x = (int) (Math.random() * (xMax + 1 - xMin)) + xMin;
				int width = (int) (Math.random() * (widthImg + 1 - x)) + x;
				int y = (int) (Math.random() * (yMax + 1 - yMin)) + yMin;
				int height = (int) (Math.random() * (heightImg + 1 - y)) + y;
				int startAngle = (int) (Math.random() * (360 + 1 - 0)) + 0;
				int arcAngle = (int) (Math.random() * (360 + 1 - 0)) + 0;
				graphics.drawArc(x, y, width, height, startAngle, arcAngle);
			}
		}

		graphics.setColor(getChallengeTextColor());
		Font font = new Font("Arial Black", 1, 17);
		graphics.setFont(font);
		graphics.drawString(getChallenge(), 3, 17);
		graphics.setColor(color1);
		graphics.drawString(getChallenge(), 1, 15);
		return challenge;
	}
	
	@Override
	protected int getChallengeImageWidth() {
		return widthImg;
	}
}