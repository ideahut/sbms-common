package com.github.ideahut.sbms.common.finder;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public final class ClassFinder {
	
	private static final char PKG_SEPARATOR = '.';

	private static final char DIR_SEPARATOR = '/';

	private static final String CLASS_FILE_SUFFIX = ".class";

	private static final String BAD_PACKAGE_ERROR = "Unable to get resources from path '%s'. Are you sure the package '%s' exists?";
	
	private ClassFinder() {}

	
	public static List<Class<?>> find(String scannedPackage, Class<? extends Annotation>...annotation) {
		String scannedPath = scannedPackage.replace(PKG_SEPARATOR, DIR_SEPARATOR);		
		URL scannedUrl = Thread.currentThread().getContextClassLoader().getResource(scannedPath);
		if (scannedUrl == null) {
			throw new IllegalArgumentException(String.format(BAD_PACKAGE_ERROR, scannedPath, scannedPackage));
		}
		String fullPath = scannedUrl.getFile();
		File directory;
		try {
			directory = new File(scannedUrl.toURI());			
		} catch (URISyntaxException e) {
			throw new RuntimeException(scannedPackage + " (" + scannedUrl + ") does not appear to be a valid URL / URI.  Strange, since we got it from the system...", e);
		} catch (IllegalArgumentException e) {
			directory = null;
		}
		
		Set<Class<? extends Annotation>> annotSet = new HashSet<Class<? extends Annotation>>();
		for (Class<? extends Annotation> c : annotation) {
			annotSet.add(c);
		}
		
		List<Class<?>> classes = new ArrayList<Class<?>>();
		if (directory != null && directory.exists()) {
			for (File file : directory.listFiles()) {
				classes.addAll(findAsPath(file, scannedPackage, annotSet));
			}
		} else {
			String jarPath = fullPath.replaceFirst("[.]jar[!].*", ".jar").replaceFirst("file:", "");
			try {
				JarFile jarFile = new JarFile(jarPath);
				classes.addAll(findAsJar(jarFile, scannedPackage, annotSet));
			} catch (IOException e) {
				throw new RuntimeException("Failed to scan " + jarPath);
			}			
		}
		return classes;
	}

	private static List<Class<?>> findAsPath(File file, String scannedPackage, Set<Class<? extends Annotation>> annotationClasses) {
		List<Class<?>> classes = new ArrayList<Class<?>>();
		String resource = scannedPackage + PKG_SEPARATOR + file.getName();
		if (file.isDirectory()) {
			for (File child : file.listFiles()) {
				classes.addAll(findAsPath(child, resource, annotationClasses));
			}
		} else if (resource.endsWith(CLASS_FILE_SUFFIX)) {
			int endIndex = resource.length() - CLASS_FILE_SUFFIX.length();
			String className = resource.substring(0, endIndex);
			try {
				Class<?> clazz = Class.forName(className);
				if (!annotationClasses.isEmpty()) {
					for (Class<? extends Annotation> annot : annotationClasses) {
						if (clazz.isAnnotationPresent(annot)) {
							classes.add(clazz);
							break;
						}
					}
				} else {
					classes.add(clazz);
				}
			} catch (ClassNotFoundException ignore) { }
		}
		return classes;
	}
	
	private static List<Class<?>> findAsJar(JarFile jarFile, String scannedPackage, Set<Class<? extends Annotation>> annotationClasses) {
		List<Class<?>> classes = new ArrayList<Class<?>>();
		Enumeration<JarEntry> entries = jarFile.entries();
		while (entries.hasMoreElements()) {
			JarEntry entry = entries.nextElement();
			String className = entry.getName();
			className = className.replace('/', '.').replace('\\', '.');
			if (!entry.isDirectory() && className.startsWith(scannedPackage) && className.endsWith(CLASS_FILE_SUFFIX)) {
				className = className.substring(0, className.length() - CLASS_FILE_SUFFIX.length());
				try {
					Class<?> clazz = Class.forName(className);
					if (!annotationClasses.isEmpty()) {
						for (Class<? extends Annotation> annot : annotationClasses) {
							if (clazz.isAnnotationPresent(annot)) {
								classes.add(clazz);
								break;
							}
						}
					} else {
						classes.add(clazz);
					}
				} catch (ClassNotFoundException ignore) { }
			}
		}
		return classes;
	}
	
}
