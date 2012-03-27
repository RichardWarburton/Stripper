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

import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public class TestClassRules {

	@Test
	public void factoryLoadsAndRuns() throws IOException {
		final ClassNode example = new ClassNode();
		final ClassReader cr = new ClassReader("com.insightfullogic.stripper.ExampleClass");
		cr.accept(example, 0);

		final ClassRules rules = ClassRules.compileFrom("src/test/resources/example-rules.py");
		assertEquals(3, rules.predicates.size());

		final MethodNameRegex name = (MethodNameRegex) rules.predicates.get(0);
		assertEquals("toString", name.regex.pattern());

		final ReturnTypeName ret = (ReturnTypeName) rules.predicates.get(1);
		assertEquals("I", ret.typeName);

		final ArgumentTypeName arg = (ArgumentTypeName) rules.predicates.get(2);
		assertEquals("I", arg.typeName);
		assertEquals(0, arg.argNumber);

		rules.strip(example);

		assertEquals(2, example.methods.size());
		final MethodNode meth = (MethodNode) example.methods.get(1);
		assertEquals("takeIntFalsePositive", meth.name);
	}

}
