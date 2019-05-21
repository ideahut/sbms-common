package com.github.ideahut.sbms.common.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public final class ZipUtil {
	
	private static final int BUFFER_SIZE = 4096;

	
	/*
	 * COMPRESS
	 */
	public static void compress(File out, File...in) throws IOException {
		FileOutputStream fos = null;
		try {
			if (in.length == 0) {
				throw new IOException("File input is required");
			}
			fos = new FileOutputStream(out);
			ZipOutputStream zos = new ZipOutputStream(fos);
			for (File f : in) {
				if (out.getAbsolutePath().equals(f.getAbsolutePath())) {
					continue;
				}
				if (f.isFile()) {
					zipFile(zos, "", f);
				} else if (f.isDirectory()) {
					zipFolder(zos, "", f);
				}
			}
			zos.close();
		} catch (IOException e) {
			throw e;
		} finally {
			try { fos.close(); } catch (Exception e) {}
		}
	}
	
	public static void compress(OutputStream out, File...in) throws Exception {
		if (in.length == 0) {
			throw new IOException("File input is required");
		}
		ZipOutputStream zos = new ZipOutputStream(out);
		for (File f : in) {
			if (f.isFile()) {
				zipFile(zos, "", f);
			} else if (f.isDirectory()) {
				zipFolder(zos, "", f);
			}
		}
	}
	
	private static void zipFolder(ZipOutputStream zos, String parent, File folder) throws IOException {
		String name = parent + folder.getName() + "/";	
		ZipEntry folderZipEntry = new ZipEntry(name);
		zos.putNextEntry(folderZipEntry);	
		File[] contents = folder.listFiles();	
		for (File f : contents) {
			if (f.isFile()) {
				zipFile(zos, name, f);
			} else if (f.isDirectory()) {
				zipFolder(zos, name, f);
			}
		}
		zos.closeEntry();
	}

	private static void zipFile(ZipOutputStream zos, String parent, File file) throws IOException {		
		FileInputStream fis = null;
		try {
			ZipEntry zipEntry = new ZipEntry(parent + file.getName());
			zos.putNextEntry(zipEntry);
			fis = new FileInputStream(file);
			byte[] buf = new byte[BUFFER_SIZE];
			int bytesRead;
			while ((bytesRead = fis.read(buf)) > 0) {
				zos.write(buf, 0, bytesRead);
			}
			zos.closeEntry();
		} catch (IOException e) {
			throw e;
		} finally {
			try { fis.close(); } catch (Exception e) {}
		}		
	}
	
	
	
	/*
	 * EXTRACT
	 */
	public static String extract(File zip, File dir) throws IOException {
		return extract(new FileInputStream(zip), dir);
	}
	
	public static String extract(InputStream zip, File dir) throws IOException {
		ZipInputStream zis = null;
		try {
			zis = new ZipInputStream(zip);
	        ZipEntry entry = zis.getNextEntry();
	        String name = entry.getName();
	        while (entry != null) {
	        	/*if (!entry.isDirectory()) {
	                extract(zis, dir);
	            } else {
	                File d = new File(dir, entry.getName());
	                d.mkdir();
	            }*/
	        	String path = dir + File.separator + entry.getName();
	        	File file = new File(path);	        	
	            if (!entry.isDirectory()) {
	                extract(zis, file);
	            } else {
	                File d = new File(path);
	                d.mkdirs();
	            }
	            zis.closeEntry();
	            entry = zis.getNextEntry();
	        }
	        return name;
		} catch (IOException e) {
        	throw e;
        } finally {
        	try { zis.close(); } catch (Exception e) {}
        }
    }
	
	private static void extract(ZipInputStream zis, File file) throws IOException {
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
        try {
        	fos = new FileOutputStream(file);
        	bos = new BufferedOutputStream(fos);
	        byte[] bytesIn = new byte[BUFFER_SIZE];
	        int read = 0;
	        while ((read = zis.read(bytesIn)) != -1) {
	            bos.write(bytesIn, 0, read);
	        }
	        bos.flush();
        } catch (IOException e) {
        	throw e;
        } finally {
        	try { fos.close(); } catch (Exception e) {}
        	try { bos.close(); } catch (Exception e) {}
        }
    }

}
