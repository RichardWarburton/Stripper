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

import static junit.framework.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public class TestMethodPredicates {

	static ClassNode example = new ClassNode();
	static List<MethodNode> methods;

	@BeforeClass
	public static void init() throws IOException {
		final ClassReader cr = new ClassReader("com.insightfullogic.stripper.ExampleClass");
		cr.accept(example, 0);
		methods = new ArrayList<>(example.methods.size());
		for (final Object o : example.methods) {
			methods.add((MethodNode) o);
		}
	}

	@Test
	public void methodNameRegex() {
		final MethodNameRegex name = new MethodNameRegex("toString");
		for (final MethodNode method : methods) {
			assertEquals(method.name.equals("toString"), name.apply(method));
		}
	}

	@Test
	public void returnTypeName() {
		final ReturnTypeName ret = new ReturnTypeName("I");
		for (final MethodNode method : methods) {
			assertEquals(method.name.equals("returnInt"), ret.apply(method));
		}
	}

	@Test
	public void argumentTypeName() {
		final ArgumentTypeName ret = new ArgumentTypeName("I", 0);
		for (final MethodNode method : methods) {
			assertEquals(method.name.equals("takeInt"), ret.apply(method));
		}
	}

}
