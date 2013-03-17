package org.antinori.game.utils;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageConsumer;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.awt.image.WritableRaster;
import java.io.File;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import static java.lang.System.out;

public class ImageTransparency {

	public static int MARKER_RED;
	public static int MARKER_GREEN;
	public static int MARKER_BLUE;

	public static void main(final String[] arguments) throws Exception {
		final String inputFileName = "C:\\Users\\Paul\\Desktop\\sprites\\queen.bmp";
		final String outputFileName = "C:\\Users\\Paul\\Desktop\\sprites\\queen.copy.png";

		// final String inputFileName =
		// "F:\\work\\life-game\\src\\main\\resources\\arno-walking.png";
		// final String outputFileName =
		// "C:\\Users\\Paul\\Desktop\\sprites\\arno-walking.png";

		try {
			convert(inputFileName, outputFileName);
		} catch (Exception e) {
		}

	}

	public static void convert(String inputFileName, String outputFileName) {
		try {

			BufferedImage source = ImageIO.read(new File(inputFileName));
			int rgb = source.getRGB(0, 0);
			convert(inputFileName, outputFileName, rgb);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void convert(String inputFileName, String outputFileName, int rgb) {
		try {

			System.out.println("Copying file " + inputFileName + " to " + outputFileName);

			MARKER_RED = (rgb >> 16) & 0xFF;
			MARKER_GREEN = (rgb >> 8) & 0xFF;
			MARKER_BLUE = rgb & 0xFF;
			
			BufferedImage source = ImageIO.read(new File(inputFileName));
			BufferedImage imageWithTransparency = makeColorTransparent(source, new Color(rgb));
			//BufferedImage transparentImage = imageToBufferedImage(imageWithTransparency);
			ImageIO.write(imageWithTransparency, "PNG", new File(outputFileName));
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public static void convert(BufferedImage source, String outputFileName, int rgb) {
		try {

			System.out.println("converting to " + outputFileName);

			MARKER_RED = (rgb >> 16) & 0xFF;
			MARKER_GREEN = (rgb >> 8) & 0xFF;
			MARKER_BLUE = rgb & 0xFF;
			
			BufferedImage imageWithTransparency = makeColorTransparent(source, new Color(rgb));
			ImageIO.write(imageWithTransparency, "PNG", new File(outputFileName));
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static BufferedImage makeColorTransparent(BufferedImage im, final Color color) {

		final ImageFilter filter = new RGBImageFilter() {
			public int markerRGB = color.getRGB();
			public final int filterRGB(int x, int y, int rgb) {

				int alpha = (rgb >> 24) & 0xff;
				int red = (rgb >> 16) & 0xFF;
				int green = (rgb >> 8) & 0xFF;
				int blue = rgb & 0xFF;

				if (red == MARKER_RED && green == MARKER_GREEN && blue == MARKER_BLUE) {
					// Mark the alpha bits as zero - transparent
					rgb = 0x00FFFFFF & rgb;
				}

				alpha = (rgb >> 24) & 0xff;
				red = (rgb >> 16) & 0xFF;
				green = (rgb >> 8) & 0xFF;
				blue = rgb & 0xFF;

				return rgb;
			}
		};

		ImageProducer ip = new FilteredImageSource(im.getSource(), filter);
		Image i = Toolkit.getDefaultToolkit().createImage(ip);
		
		sun.awt.image.ToolkitImage source = (sun.awt.image.ToolkitImage) i;
		source.preload(null);
		try {
			Thread.sleep(250);
		} catch (Exception e) {
		}
		return source.getBufferedImage();
		
	}
	

	public static BufferedImage imageToBufferedImage(Image image) {
		BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = bufferedImage.createGraphics();
		g2.drawImage(image, 0, 0, null);
		g2.dispose();
		return bufferedImage;

	}
	
	public static BufferedImage createTransparentImage(int width, int height) {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.CLEAR, 0.0f);
		Graphics2D g2d = (Graphics2D) image.getGraphics();
		g2d.setComposite(composite);
		g2d.setColor(new Color(0, 0, 0, 0));
		g2d.fillRect(0, 0, width, height);
		return image;
	}
	
	
	
	
	
	
	
	
	



}
