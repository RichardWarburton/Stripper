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

import static com.google.common.collect.Collections2.filter;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

public class ClassRules {

	@VisibleForTesting
	final List<MethodPredicate> predicates;

	public static ClassRules compileFrom(final String rulesFile) {
		// load up rules
		final PythonInterpreter python = new PythonInterpreter();
		python.execfile(rulesFile);
		final PyObject rules = python.get("rules");

		final List<MethodPredicate> predicates = new ArrayList<>();
		for (int i = 0; i < rules.__len__(); i++) {
			final PyObject rule = rules.__getitem__(i);
			predicates.add((MethodPredicate) rule.__tojava__(MethodPredicate.class));
		}
		return new ClassRules(predicates);
	}

	private ClassRules(final List<MethodPredicate> predicates) {
		this.predicates = predicates;
	}

	@SuppressWarnings("unchecked")
	public void strip(final ClassNode cls) {
		final Predicate<MethodNode> onePredicate = Predicates.not(Predicates.or(predicates));
		cls.methods = new ArrayList<>(filter(cls.methods, onePredicate));
	}
}
