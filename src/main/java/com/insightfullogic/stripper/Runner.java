/*
 *   Copyright (c) 2012, Richard Warburton <richard.warburton@gmail.com>
 *   
 *   This file is part of Stripper.
 *     
 *   Stripper is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Stripper is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with Stripper.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.insightfullogic.stripper;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

public class Runner {

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		if (args.length != 2) {
			System.err.println("usage: com.insightfullogic.stripper.Main <rules-file> <directory>");
			System.exit(-1);
		}

		final File directory = new File(args[1]);
		final ClassRules rules = ClassRules.compileFrom(args[0]);

		if (!directory.isDirectory()) {
			throw new IllegalArgumentException(directory + " isn't a directory");
		}

		search(directory, rules);
	}

	static void search(final File directory, final ClassRules rules) {
		for (final String file : directory.list(isJava)) {
			rewrite(file, rules);
		}

		for (final File subdir : directory.listFiles(isDir)) {
			search(subdir, rules);
		}
	}

	static void rewrite(final String classFile, final ClassRules rules) {
		try {
			// Read
			final ClassReader cr = new ClassReader(classFile);
			final ClassNode cls = new ClassNode();
			cr.accept(cls, 0);

			rules.strip(cls);

			// Write
			final ClassWriter cw = new ClassWriter(0);
			cls.accept(cw);
			try (FileOutputStream out = new FileOutputStream(classFile)) {
				out.write(cw.toByteArray());
			}
		} catch (final IOException e) {
			throw new IllegalStateException(e);
		}
	}

	static FilenameFilter isJava = new FilenameFilter() {
		@Override
		public boolean accept(final File dir, final String name) {
			return name.endsWith(".java");
		}
	};

	static FileFilter isDir = new FileFilter() {
		@Override
		public boolean accept(final File pathname) {
			return pathname.isDirectory();
		}
	};

}
