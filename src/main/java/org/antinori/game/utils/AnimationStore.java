package org.antinori.game.utils;

import java.io.File;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Scanner;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;

public class AnimationStore {
	
	public static final int DURATION = 125;

	String[] tuple = new String[4];
	LinkedHashMap<String, Animation> animations = new LinkedHashMap<String, Animation>(60);
	String sheetName;
	int width;
	int height;
	File packFile;

	public AnimationStore(File packFile) {
	
		try {
			this.packFile = packFile;
			Scanner scanner = new Scanner(packFile);
			init(scanner);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public AnimationStore(InputStream packFile) {
		
		try {
			Scanner scanner = new Scanner(packFile);
			init(scanner);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void init(Scanner scanner) {
		
		try {
			
			Image image = null;

			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();

				if (line == null)
					break;
				if (line.trim().length() < 1)
					continue;
				else if (image == null || line.contains(".png")) {

					sheetName = line;
					try {
						image = new Image(AnimationStore.class.getResourceAsStream(line), line, false);
					} catch(Exception e) {
						if (packFile != null) {
							System.err.println(e.getMessage() + ", now trying to find the file int the same directory as the pack file.");
							//check same directory as the pack file
							String fileName = packFile.getParent() + "\\" + line;
							image = new Image(fileName);
							System.out.println("Successfully loaded the image: "+fileName);
						} else {
							throw e;
						}
					}

					String format = readValue(scanner.nextLine());

					readTuple(scanner.nextLine());
					String min = tuple[0];
					String max = tuple[1];

					String direction = readValue(scanner.nextLine());

				} else {
					
					if (line.contains(".png")) break;
					
					String animationName = line;
					Animation animation = animations.get(animationName);

					if (animation == null) {
						animation = new Animation();
						animations.put(animationName, animation);
					}

					boolean rotate = Boolean.valueOf(readValue(scanner.nextLine()));

					readTuple(scanner.nextLine());
					int left = Integer.parseInt(tuple[0]);
					int top = Integer.parseInt(tuple[1]);

					readTuple(scanner.nextLine());
					int width = Integer.parseInt(tuple[0]);
					int height = Integer.parseInt(tuple[1]);

					readTuple(scanner.nextLine());
					int originalWidth = Integer.parseInt(tuple[0]);
					int originalHeight = Integer.parseInt(tuple[1]);

					readTuple(scanner.nextLine());
					int offsetX = Integer.parseInt(tuple[0]);
					int offsetY = Integer.parseInt(tuple[1]);

					int index = Integer.parseInt(readValue(scanner.nextLine()));

					animation.addFrame(image.getSubImage(left, top, width, height), DURATION);
					
					this.width = width;
					this.height = height;

				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			scanner.close();
		}
	}

	public String readValue(String line) throws Exception {
		int colon = line.indexOf(':');
		if (colon == -1)
			throw new Exception("Invalid line: " + line);
		return line.substring(colon + 1).trim();
	}

	public int readTuple(String line) throws Exception {
		int colon = line.indexOf(':');
		if (colon == -1)
			throw new Exception("Invalid line: " + line);
		int i = 0, lastMatch = colon + 1;
		for (i = 0; i < 3; i++) {
			int comma = line.indexOf(',', lastMatch);
			if (comma == -1) {
				if (i == 0)
					throw new Exception("Invalid line: " + line);
				break;
			}
			tuple[i] = line.substring(lastMatch, comma).trim();
			lastMatch = comma + 1;
		}
		tuple[i] = line.substring(lastMatch).trim();
		return i + 1;
	}
	
	

}
