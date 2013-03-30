package org.antinori.game.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import javax.imageio.ImageIO;

import org.antinori.game.utils.MaxRectsPacker.Page;
import org.antinori.game.utils.MaxRectsPacker.Rect;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;


/*
 * 
 * Will convert Bioware BAM files to PNG sprite sheets.
 * 
 * This code is taken from the Near Infinity jar, using the decompiler.
 * http://www.sorcerers.net/Games/IEmodding/index.php
 * 
 * Use Near Infinity jar to export the BAM files from the BIF file.
 * 
 * Or use the WinBIFF tool to export the BAM files.
 * 
 */
public class BiowareBamSpriteCreator {

	ArrayList<Bam> bams = new ArrayList<Bam>();
	ArrayList<String> selections = null;

	byte transparent;
	Palette palette;
	
	int TileDim = 32;
	int numSeqs = 0;
	int maxFramesInSeq = 0;
	
	public static final int MIN_FRAMES_PER_ANIM = 5;
	public static final int MAX_SEQUENCES = 95;
	
	public static final String BAMDIR = "D:\\Black Isle\\BAMS";
	public static final String OUTPUTDIR = "C:\\Users\\Paul\\Desktop\\bamSprites\\";
	
	public BiowareBamSpriteCreator() {
	}



	public static void main(String[] args) {

		try {
			
//			String[] names = {"CDFT1","CDFT2","LDCN","MAIRG","MBEHG","MBESG","MDJLG","MEASG","METN","MFIEG","MGCLG","MGCPG",
//					"MGHLG","MGIBG","MGLCG","MGO1","MGO2","MGO3","MGO4","MGWEG","MIMPG","MINOR","MLIC","MLIZ","MMAG",
//					"MMIN","MMIS","MMUM","MMY2","MMYC","MNO1","MNO2","MNO3","MOGH","MOGM","MOGN","MOGR","MOR1","MOR2",
//					"MOR3","MOR4","MOR5","MOTY","MRAK","MSA2","MSAHG","MSAL","MSAT","MSHD","MSHR","MSKB","MSLIG","MSLYG",
//					"MSPI","MTRO","MUMB","MVAF","MVAM","MWER","NIRO","NPIR","NSAI","NSHD","NSOL"};
			//String[] names = {"METN","MGLCG","MLIZ","MNO3","MOTY","MSA2","MSAL","MSKB","MTRO"};
			//String[] names = {"UVOLG", "USAR", "UELM", "NELL"};
			String[] names = {"UVOLG"};

			for (int i=0;i<names.length;i++) {
				String name = names[i];
				
				//clear out any existing files first
				Collection<File> outs = getFiles(OUTPUTDIR, name+"*");
				for (File file : outs) file.delete();
				
				Collection<File> files = getFiles(BAMDIR, name+"*");
				BiowareBamSpriteCreator mr = new BiowareBamSpriteCreator();
				mr.init(name, OUTPUTDIR+name+".png", files);
				
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Collection<File> getFiles(String directoryName, String filter) {
		File directory = new File(directoryName);
		return FileUtils.listFiles(directory, new WildcardFileFilter(filter), null);
	}

	public void init(String name, String out, Collection<File> infiles) {

		try {
						
			for (File inFile : infiles) {
						
				byte[] buffer = FileUtils.readFileToByteArray(inFile);
	
				String signature = new String(buffer, 0, 4);
				if (signature.equals("BAMC")) {
					buffer = decompress(buffer);
				} else if (!signature.equals("BAM ")) {
					throw new Exception("Unsupported BAM-file: " + signature);
				}
	
				int numberframes = convertShort(buffer, 8);
				int numberanims = convertUnsignedByte(buffer, 10);
				this.transparent = buffer[11];
				int frameOffset = convertInt(buffer, 12);
				int paletteOffset = convertInt(buffer, 16);
				int lookupOffset = convertInt(buffer, 20);
	
				this.palette = new Palette(buffer, paletteOffset, lookupOffset - paletteOffset);
	
				Frame[] frames = new Frame[numberframes];
				for (int i = 0; i < numberframes; i++) {
					frames[i] = new Frame(buffer, frameOffset + 12 * i);
					//System.out.println("frame width:"+frames[i].image.getWidth()+" height:"+frames[i].image.getHeight());
				}
	
				int animOffset = frameOffset + 12 * numberframes;
				int lookupCount = 0;
				Anim[] anims = new Anim[numberanims];
				for (int i = 0; i < numberanims; i++) {
					anims[i] = new Anim(buffer, animOffset + 4 * i);
					lookupCount = Math.max(lookupCount, anims[i].getMaxLookup());
				}
				
				int[] lookupTable = new int[lookupCount];
			    for (int i = 0; i < lookupCount; i++)
			        lookupTable[i] = convertShort(buffer, lookupOffset + i * 2);
				
			    Bam bam = new Bam(frames, anims, lookupTable);
				bams.add(bam);
				
				for (int i = 0; i < anims.length; i++) {
					Anim anim = anims[i];
					
					if (numSeqs > MAX_SEQUENCES) 
						continue;
					
					if (anim.frameCount < MIN_FRAMES_PER_ANIM)
						continue;
					
					if (bam.getFrameNr(i,0) == -1 || bam.getFrame(bam.getFrameNr(i,0)).getWidth() < 2 ||
						bam.getFrameNr(i,1) == -1 || bam.getFrame(bam.getFrameNr(i,1)).getWidth() < 2)
						continue; //skip images less than than 1 pixel size
					
					if (anim.frameCount > maxFramesInSeq)
						maxFramesInSeq = anim.frameCount;
					
					for (int j = 0; j < anim.frameCount; j++) {
						int w = bam.getFrame(bam.getFrameNr(i,j)).getWidth();
						int h = bam.getFrame(bam.getFrameNr(i,j)).getHeight();
						if (w > TileDim) TileDim = w;
						if (h > TileDim) TileDim = h;
					}
					
					numSeqs++;
				
				}
				
			}

			if (maxFramesInSeq < 1)
				throw new Exception("No Frames found, exiting.");
			
			//BufferedImage output = ImageTransparency.createTransparentImage(TileDim * numSeqs, TileDim * maxFramesInSeq);
			
	        MaxRectsPacker mrp = new MaxRectsPacker();
			//ArrayList<Rect> inputRects = new ArrayList<Rect>();
			ArrayList<Rect> packedRects = new ArrayList<Rect>();


			int seqnum = 0;
			for (int x = 0; x < bams.size(); x++) {
				Bam bam = bams.get(x);
				Anim[] anims = bam.anims;
				for (int i=0;i<anims.length;i++) {
					Anim anim = anims[i];
					
					if (seqnum > MAX_SEQUENCES)	break;
										
					if (anim.frameCount < MIN_FRAMES_PER_ANIM) 
						continue;
					
					if (bam.getFrameNr(i,0) == -1 || bam.getFrame(bam.getFrameNr(i,0)).getWidth() < 2 ||
						bam.getFrameNr(i,1) == -1 || bam.getFrame(bam.getFrameNr(i,1)).getWidth() < 2) 
						continue;

					for (int j = 0; j < anim.frameCount; j++) {
						
						//System.out.println("totalnumSeqs:"+numSeqs+" currseqnum:"+seqnum+" anim number:"+i+" frameCount:"+anim.frameCount+" frames len:"+bam.frames.length+" index:"+(j+anim.lookupIndex));
						
						BufferedImage fr = bam.getFrame(bam.getFrameNr(i,j));
						int fw = fr.getWidth();
						int fh = fr.getHeight();
						float offw = (TileDim - fw) / 2;
						float offh = (TileDim - fh) / 2;
						
						//output.getGraphics().drawImage(fr, (int) (seqnum * TileDim + offw), (int) (j * TileDim + offh), null);
						
						//for non packed sheet
						//Rect rect = new Rect((int)(seqnum * TileDim),(int)(j * TileDim), TileDim, TileDim);
						//rect.name = "Animation"+seqnum;
						//rect.index = j;
						//inputRects.add(rect);
						
						//for packed sheet
						//BufferedImage tile = ImageTransparency.createTransparentImage(TileDim, TileDim);
						//tile.getGraphics().drawImage(fr,(int)offw, (int)offh, null);
						BufferedImage tile = ImageTransparency.createTransparentImage(fw, fh);
						tile.getGraphics().drawImage(fr,0, 0, null);

						if (selections == null || selections.contains("Animation"+seqnum)) {
							//Rect rect = new Rect(tile,0,0,TileDim,TileDim);
							Rect rect = new Rect(tile,0,0,fw,fh);
							rect.name = "Animation"+seqnum;
							rect.index = j;
							packedRects.add(rect);
						}
						
						//System.out.println("Writing: " + "Animation-"+seqnum+"-"+j);
						//ImageIO.write(fr, "PNG", new File(OUTPUTDIR+"test\\Animation-"+seqnum+"-"+j+".png"));
						
					}
					seqnum ++;
				}
			}
			
			int size = ((TileDim * numSeqs)*(TileDim * maxFramesInSeq))/4;

			System.out.println("Writing: " + out + " with sprite dimensions: " + TileDim + "x" + TileDim + " width:" +(TileDim * numSeqs)+ " height:" +(TileDim * maxFramesInSeq) + " size: " + size);
			int alpha = palette.getColor(this.transparent);
			mrp.alpha = alpha;
//			if (size < 20000000) {
//				ImageTransparency.convert(output, out, alpha);
//			} else {
//				System.err.println("Skipping transparency conversion due to max width limit: "+ out);
//			}
			
       
			
			String parentDir = new File(out).getParent();
			ArrayList<Page> pages = mrp.pack(packedRects);
			mrp.writeImages(new File(parentDir), pages, name);
			mrp.writePackFile(new File(parentDir), pages, name+".txt");
			
			//write the pack file for the non packed png
			//mrp.writePackFileWithRects(new File(parentDir), new File(out).getName() + "-pack.txt", inputRects, new File(out).getName());
			
		    System.out.println("done");


		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	class Bam {
		Frame[] frames;
		Anim[] anims;
		int[] lookupTable;
		
		Bam(Frame[] frames, Anim[] anims, int[] lt) {
			this.frames = frames;
			this.anims = anims;
			this.lookupTable = lt;
		}

		public BufferedImage getFrame(int frameNr) {
			return frames[frameNr].image;
		}

		public int getFrameNr(int animNr, int frameNr) {
			return lookupTable[(frameNr + this.anims[animNr].lookupIndex)];
		}
	}

	class Anim {
		int frameCount;
		int lookupIndex;

		Anim(byte[] buffer, int offset) {
			frameCount = convertShort(buffer, offset);
			lookupIndex = convertShort(buffer, offset + 2);
		}

		int getMaxLookup() {
			return frameCount + lookupIndex;
		}

	}

	class Frame {
		BufferedImage image;

		Frame(byte[] buffer, int offset) {

			int width = convertShort(buffer, offset);
			int height = convertShort(buffer, offset + 2);

			long frameDataOffset = convertUnsignedInt(buffer, offset + 8);
			boolean rle = true;
			if (frameDataOffset > Math.pow(2.0D, 31.0D)) {
				rle = false;
				frameDataOffset -= Math.pow(2.0D, 31.0D);
			}

			if ((height < 1) || (width < 1))
				return;

			byte[] imagedata = new byte[height * width];

			if (!rle) {
				imagedata = getSubArray(buffer, (int) frameDataOffset, imagedata.length);
			} else {
				int w_idx = 0;
				while (w_idx < imagedata.length) {
					byte b = buffer[((int) frameDataOffset++)];
					imagedata[(w_idx++)] = b;
					if (b == BiowareBamSpriteCreator.this.transparent) {
						int toread = buffer[((int) frameDataOffset++)];
						if (toread < 0)
							toread += 256;
						for (int i = 0; i < toread; i++)
							if (w_idx < imagedata.length)
								imagedata[(w_idx++)] = BiowareBamSpriteCreator.this.transparent;
					}
				}
			}
			this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			for (int h_idx = 0; h_idx < height; h_idx++) {
				for (int w_idx = 0; w_idx < width; w_idx++) {
					int rgb = palette.getColor(imagedata[(h_idx * width + w_idx)]);
					this.image.setRGB(w_idx, h_idx, rgb);
				}
			}
		}
		
	}

	public static byte[] getSubArray(byte[] buffer, int offset, int length) {
		byte[] r = new byte[length];
		System.arraycopy(buffer, offset, r, 0, length);
		return r;
	}

	public static byte[] mergeArrays(byte[] a1, byte[] a2) {
		byte[] r = new byte[a1.length + a2.length];
		System.arraycopy(a1, 0, r, 0, a1.length);
		System.arraycopy(a2, 0, r, a1.length, a2.length);
		return r;
	}

	public static byte[] resizeArray(byte[] src, int new_size) {
		byte[] tmp = new byte[new_size];
		System.arraycopy(src, 0, tmp, 0, Math.min(src.length, new_size));
		return tmp;
	}

	public static byte[] convertBack(byte value) {
		byte[] buffer = { value };
		return buffer;
	}

	public static byte[] convertBack(short value) {
		byte[] buffer = new byte[2];
		for (int i = 0; i <= 1; i++)
			buffer[i] = ((byte) (value >> 8 * i & 0xFF));
		return buffer;
	}

	public static byte[] convertBack(int value) {
		byte[] buffer = new byte[4];
		for (int i = 0; i <= 3; i++)
			buffer[i] = ((byte) (value >> 8 * i & 0xFF));
		return buffer;
	}

	public static byte[] convertBack(long value) {
		byte[] buffer = new byte[8];
		for (int i = 0; i <= 7; i++)
			buffer[i] = ((byte) (int) (value >> 8 * i & 0xFF));
		return buffer;
	}

	public static byte convertByte(byte[] buffer, int offset) {
		int value = 0;
		for (int i = 0; i >= 0; i--)
			value = value << 8 | buffer[(offset + i)] & 0xFF;
		return (byte) value;
	}

	public static int convertInt(byte[] buffer, int offset) {
		int value = 0;
		for (int i = 3; i >= 0; i--)
			value = value << 8 | buffer[(offset + i)] & 0xFF;
		return value;
	}

	public static long convertLong(byte[] buffer, int offset) {
		long value = 0L;
		for (int i = 7; i >= 0; i--)
			value = value << 8 | buffer[(offset + i)] & 0xFF;
		return value;
	}

	public static short convertShort(byte[] buffer, int offset) {
		int value = 0;
		for (int i = 1; i >= 0; i--)
			value = value << 8 | buffer[(offset + i)] & 0xFF;
		return (short) value;
	}

	public static String convertString(byte[] buffer, int offset, int length) {
		for (int i = 0; i < length; i++) {
			if (buffer[(offset + i)] == 0)
				return new String(buffer, offset, i);
		}
		return new String(buffer, offset, length);
	}

	public static byte[] convertThreeBack(int value) {
		byte[] buffer = new byte[3];
		for (int i = 0; i <= 2; i++)
			buffer[i] = ((byte) (value >> 8 * i & 0xFF));
		return buffer;
	}

	public static short convertUnsignedByte(byte[] buffer, int offset) {
		short value = (short) convertByte(buffer, offset);
		if (value < 0)
			value = (short) (value + 256);
		return value;
	}

	public static long convertUnsignedInt(byte[] buffer, int offset) {
		long value = convertInt(buffer, offset);
		if (value < 0L)
			value += 4294967296L;
		return value;
	}

	public static int convertUnsignedShort(byte[] buffer, int offset) {
		int value = convertShort(buffer, offset);
		if (value < 0)
			value += 65536;
		return value;
	}

	public static byte[] compress(byte[] data, String signature, String version) {
		byte[] header = mergeArrays(signature.getBytes(), version.getBytes());
		header = mergeArrays(header, convertBack(data.length));
		byte[] result = resizeArray(header, data.length * 2);
		Deflater deflater = new Deflater();
		deflater.setInput(data);
		deflater.finish();
		int clength = deflater.deflate(result, 12, result.length - 12);
		return getSubArray(result, 0, clength + 12);
	}

	public static byte[] decompress(byte[] buffer) throws IOException {

		Inflater inflater = new Inflater();
		byte[] result = new byte[convertInt(buffer, 8)];
		inflater.setInput(buffer, 12, buffer.length - 12);
		try {
			inflater.inflate(result);
		} catch (DataFormatException e) {
			throw new IOException();
		}
		inflater.reset();
		return result;
	}

	class Palette {
		private final int[] colors;

		Palette(byte[] buffer, int offset, int length) {
			this.colors = new int[length / 4];
			for (int i = 0; i < this.colors.length; i++)
				this.colors[i] = convertInt(buffer, offset + i * 4);
		}

		public int getColor(byte[] buffer, int offset, byte index) {
			if (index < 0)
				return convertInt(buffer, offset + (256 + index) * 4);
			return convertInt(buffer, offset + index * 4);
		}

		public int getColor(int index) {
			if (index < 0)
				return this.colors[(index + 256)];
			return this.colors[index];
		}

		public short[] getColorBytes(int index) {
			byte[] bytes = convertBack(getColor(index));
			short[] shorts = new short[bytes.length];
			for (int i = 0; i < bytes.length; i++) {
				shorts[i] = ((short) bytes[i]);
				if (shorts[i] < 0) {
					int tmp43_41 = i;
					short[] tmp43_40 = shorts;
					tmp43_40[tmp43_41] = ((short) (tmp43_40[tmp43_41] + 256));
				}
			}
			return shorts;
		}
	}

	public ArrayList<String> getSelections() {
		return selections;
	}



	public void setSelections(ArrayList<String> selections) {
		this.selections = selections;
	}





}
