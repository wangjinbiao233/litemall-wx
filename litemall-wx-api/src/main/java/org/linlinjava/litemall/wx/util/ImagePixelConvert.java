package org.linlinjava.litemall.wx.util;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImagePixelConvert {

	private static final int IMG_WIDTH = 960;
	private static final int IMG_HEIGHT = 1280;

	public static void main(String[] args) {

		try {

			BufferedImage originalImage = ImageIO.read(new File("F:1.jpg"));
			int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB
					: originalImage.getType();

			BufferedImage resizeImageJpg = resizeImage(originalImage, type);
			ImageIO.write(resizeImageJpg, "jpg", new File("F:\\2.jpg"));//图片会变红色

			BufferedImage resizeImagePng = resizeImage(originalImage, type);
			ImageIO.write(resizeImagePng, "png", new File("F:\\3.jpg"));

			BufferedImage resizeImageHintJpg = resizeImageWithHint(
					originalImage, type);
			ImageIO.write(resizeImageHintJpg, "jpg", new File("F:\\4.jpg"));//图片会变红色

			BufferedImage resizeImageHintPng = resizeImageWithHint(
					originalImage, type);
			ImageIO.write(resizeImageHintPng, "png", new File("F:\\5.jpg"));

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

	}

	@SuppressWarnings("unused")
	public static BufferedImage resizeImage(BufferedImage originalImage,
			int type) {
		BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT,
				type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
		g.dispose();

		return resizedImage;
	}

	@SuppressWarnings("unused")
	public static BufferedImage resizeImageWithHint(
			BufferedImage originalImage, int type) {

		BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT,
				type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
		g.dispose();
		g.setComposite(AlphaComposite.Src);

		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setRenderingHint(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		return resizedImage;
	}
}