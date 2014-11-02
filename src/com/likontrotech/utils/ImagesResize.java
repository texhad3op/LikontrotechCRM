package com.likontrotech.utils;

import java.io.File;

class ImagesResize {

	public static void main(String[] args) {
		new ImagesResize().start();
	}

	public void start() {
		File file = new File("C:/mikrometras");
		System.out.println(file.getName());
		processDirectory(1, file.listFiles());
	}

	private void processDirectory(int level, File[] fileList) {
		level++;

		for (File file : fileList) {
			if (file.isDirectory()) {
				processDirectory(level, file.listFiles());
			} else if (file.isFile())
				
				System.out.println(file.getPath());

		}

	}
}
