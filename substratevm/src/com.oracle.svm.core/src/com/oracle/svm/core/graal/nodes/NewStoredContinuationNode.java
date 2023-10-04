/*
 * Copyright (c) 2022, 2022, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package com.oracle.svm.core.graal.nodes;

import jdk.compiler.graal.core.common.type.StampFactory;
import jdk.compiler.graal.core.common.type.TypeReference;
import jdk.compiler.graal.graph.NodeClass;
import jdk.compiler.graal.nodeinfo.NodeInfo;
import jdk.compiler.graal.nodes.ValueNode;
import jdk.compiler.graal.nodes.java.AbstractNewArrayNode;

import jdk.vm.ci.meta.ResolvedJavaType;

@NodeInfo
public final class NewStoredContinuationNode extends AbstractNewArrayNode {
    public static final NodeClass<NewStoredContinuationNode> TYPE = NodeClass.create(NewStoredContinuationNode.class);

    private final ResolvedJavaType instanceClass;
    private final ResolvedJavaType elementType;

    public NewStoredContinuationNode(ResolvedJavaType instanceType, ResolvedJavaType elementType, ValueNode arrayLength) {
        super(TYPE, StampFactory.objectNonNull(TypeReference.createExactTrusted(instanceType)), arrayLength, true, null);
        this.instanceClass = instanceType;
        this.elementType = elementType;
    }

    /**
     * Gets the instance class being allocated by this node.
     *
     * @return the instance class allocated
     */
    public ResolvedJavaType instanceClass() {
        return instanceClass;
    }

    /**
     * Gets the element type of the inlined array.
     *
     * @return the element type of the inlined array
     */
    public ResolvedJavaType elementType() {
        return elementType;
    }

    @NodeIntrinsic
    public static native Object allocate(@ConstantNodeParameter Class<?> instanceType, @ConstantNodeParameter Class<?> elementType, int arrayLength);
}
