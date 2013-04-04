package org.antinori.game.utils;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Scanner;
import java.util.SortedSet;
import java.util.TreeSet;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;

public class AnimationStore {
	
	public static final int DURATION = 125;

	String[] tuple = new String[4];
	LinkedHashMap<String, Animation> animations = new LinkedHashMap<String, Animation>(60);
	List<Rect> allRects = new ArrayList<Rect>();

	String sheetName;
	int width;
	int height;
	int maxWidth;
	int maxHeight;
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

					//animation.addFrame(image.getSubImage(left, top, width, height), DURATION);
					Rect rect = new Rect(animationName, image.getSubImage(left, top, width, height), index);
					allRects.add(rect);
					
					this.width = width;
					this.height = height;
					
					if (width > maxWidth) maxWidth = width;
					if (height > maxHeight) maxHeight = height;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			scanner.close();
		}
		
		for (String name : animations.keySet()) {
			List<Rect> frames = getFramesForAnimation(name);
			Animation anim = animations.get(name);
			for (Rect rect : frames)
			anim.addFrame(rect.image, DURATION);
		}
		
		animations = sortAnimations(animations);
		
		allRects.clear();
		allRects = null;
		
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
	
	public List<Rect> getFramesForAnimation(String name) {
		List<Rect> list = new ArrayList<Rect>();
		
		for (Rect rect : allRects) {
			if (rect.name.equals(name)) list.add(rect);
		}
		Collections.sort(list, new Comparator<Rect>() {
			public int compare(Rect o1, Rect o2) {
				return o1.index - o2.index;
			}
		});
		return list;
	}
	
	
	
	class Rect {
		public String name;
		public Image image;
		public int index;
		Rect(String name, Image source, int index) {
			this.index = index;
			this.name = name;
			this.image = source;
		}

	}
	
	private LinkedHashMap<String, Animation> sortAnimations(LinkedHashMap<String, Animation> input) {
		SortedSet<String> keys = new TreeSet<String>(input.keySet());
		LinkedHashMap<String, Animation> sortedMap = new LinkedHashMap<String, Animation>();
		for (String key : keys) {
			sortedMap.put(key, input.get(key));
		}
		return sortedMap;
	}
	
	

}
