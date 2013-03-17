package org.antinori.game.utils;

//http://www.reinerstilesets.de/2d-grafiken/2d-humans/

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import org.apache.commons.lang.StringUtils;

public class ExpandSprites {

	public static void main(String[] argv) throws Exception {
		combine();
	}

	private static void expand() throws Exception {

		String dir = "C:\\Users\\Paul\\Desktop\\sprites\\mummy 45 bitmaps\\";
		String outfile = "C:\\Users\\Paul\\Desktop\\sprites\\mummy-walking.png";
		String prefix = "mummy 45 läuft ";

		int actions = 9;
		int size = 96;
		int directions = 8;

		ArrayList<String> files = new ArrayList<String>();
		for (int i = 0; i < actions; i++)
			files.add(prefix + "e" + StringUtils.leftPad("" + i, 4, '0') + ".bmp");
		for (int i = 0; i < actions; i++)
			files.add(prefix + "n" + StringUtils.leftPad("" + i, 4, '0') + ".bmp");
		for (int i = 0; i < actions; i++)
			files.add(prefix + "ne" + StringUtils.leftPad("" + i, 4, '0') + ".bmp");
		for (int i = 0; i < actions; i++)
			files.add(prefix + "nw" + StringUtils.leftPad("" + i, 4, '0') + ".bmp");
		for (int i = 0; i < actions; i++)
			files.add(prefix + "s" + StringUtils.leftPad("" + i, 4, '0') + ".bmp");
		for (int i = 0; i < actions; i++)
			files.add(prefix + "se" + StringUtils.leftPad("" + i, 4, '0') + ".bmp");
		for (int i = 0; i < actions; i++)
			files.add(prefix + "sw" + StringUtils.leftPad("" + i, 4, '0') + ".bmp");
		for (int i = 0; i < actions; i++)
			files.add(prefix + "w" + StringUtils.leftPad("" + i, 4, '0') + ".bmp");

		BufferedImage output = new BufferedImage(size * directions, size * actions, BufferedImage.TYPE_INT_ARGB);

		System.out.println("files.size=" + files.size());

		int index = 0;
		for (int i = 0; i < directions; i++) {
			for (int j = 0; j < actions; j++) {
				if (index == files.size())
					continue;
				System.out.println("i=" + i + " j=" + j + " index=" + index + " file=" + files.get(index));
				BufferedImage input = ImageIO.read(new File(files.get(index)));
				output.getGraphics().drawImage(input, i * size, j * size, null);
				index++;
			}
		}

		System.out.println("Writing: " + outfile);
		ImageIO.write(output, "PNG", new File(outfile));

		ImageTransparency.convert(outfile, outfile);

	}

	private static void combine() throws Exception {

		String dir = "C:\\Users\\Paul\\Desktop\\sprites\\PlanetCute PNG\\";
		String outfile = "sheet.png";

		
		String file;
		File folder = new File(dir);
		File[] files = folder.listFiles();

		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile()) {
				file = files[i].getName();
				if (file.endsWith(".png")) {
					System.out.println(file);
				}
			}
		}
		
		int w = 101;
		int h = 171;
		int directions = 6;
		int actions = 10;

		BufferedImage output = new BufferedImage(w * directions, h * actions, BufferedImage.TYPE_INT_ARGB);

		System.out.println("files.size=" + files.length);

		int index = 0;
		for (int i = 0; i < directions; i++) {
			for (int j = 0; j < actions; j++) {
				if (index == files.length)
					continue;
				System.out.println("i=" + i + " j=" + j + " index=" + index + " file=" + files[index]);
				BufferedImage input = ImageIO.read(files[index]);
				output.getGraphics().drawImage(input, i * w, j * h, null);
				index++;
			}
		}

		System.out.println("Writing: " + dir + outfile);
		ImageIO.write(output, "PNG", new File(dir + outfile));

		//ImageTransparency.convert(outfile, outfile);

	}

}
