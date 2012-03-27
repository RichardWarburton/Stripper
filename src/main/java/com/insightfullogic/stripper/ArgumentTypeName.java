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

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.MethodNode;

import com.google.common.annotations.VisibleForTesting;

public class ArgumentTypeName implements MethodPredicate {

	@VisibleForTesting
	final String typeName;

	@VisibleForTesting
	final int argNumber;

	public ArgumentTypeName(final String typeName, final int argNumber) {
		super();
		this.typeName = typeName;
		this.argNumber = argNumber;
	}

	@Override
	public boolean apply(final MethodNode input) {
		final Type[] args = Type.getArgumentTypes(input.desc);
		if (args.length <= argNumber) {
			return false;
		}

		final Type argumentType = args[argNumber];
		return typeName.equals(argumentType.toString());
	}
}
