/*
 * Copyright (c) 2018, 2021, Oracle and/or its affiliates. All rights reserved.
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
package com.oracle.svm.graal;

import static org.graalvm.compiler.core.common.StrideUtil.S1;
import static org.graalvm.compiler.core.common.StrideUtil.S2;
import static org.graalvm.compiler.core.common.StrideUtil.S4;

import org.graalvm.compiler.replacements.nodes.ArrayEqualsNode;
import org.graalvm.compiler.replacements.nodes.ArrayRegionEqualsNode;
import org.graalvm.nativeimage.Platform.AMD64;
import org.graalvm.nativeimage.Platforms;
import org.graalvm.word.Pointer;

import com.oracle.svm.core.SubstrateOptions;
import com.oracle.svm.core.annotate.AutomaticFeature;
import com.oracle.svm.core.annotate.Uninterruptible;
import com.oracle.svm.core.graal.InternalFeature;
import com.oracle.svm.core.graal.meta.SubstrateForeignCallsProvider;
import com.oracle.svm.core.snippets.SnippetRuntime.SubstrateForeignCallDescriptor;
import com.oracle.svm.core.snippets.SubstrateForeignCallTarget;

import jdk.vm.ci.meta.JavaKind;

@AutomaticFeature
@Platforms(AMD64.class)
class ArrayEqualsForeignCallsFeature implements InternalFeature {
    @Override
    public boolean isInConfiguration(IsInConfigurationAccess access) {
        return !SubstrateOptions.useLLVMBackend();
    }

    @Override
    public void beforeAnalysis(BeforeAnalysisAccess access) {
        SubstrateGraalUtils.registerStubRoots(access, ArrayEqualsForeignCalls.FOREIGN_CALLS);
    }

    @Override
    public void registerForeignCalls(SubstrateForeignCallsProvider foreignCalls) {
        foreignCalls.register(ArrayEqualsForeignCalls.FOREIGN_CALLS);
    }
}

@Platforms(AMD64.class)
class ArrayEqualsForeignCalls {

    static final SubstrateForeignCallDescriptor[] FOREIGN_CALLS = SubstrateGraalUtils.mapStubs(
                    org.graalvm.compiler.replacements.nodes.ArrayEqualsForeignCalls.STUBS,
                    ArrayEqualsForeignCalls.class);

    // GENERATED CODE BEGIN
    

    @Uninterruptible(reason = "Must not do a safepoint check.")
    @SubstrateForeignCallTarget(stubCallingConvention = false, fullyUninterruptible = true)
    private static boolean booleanArraysEquals(Pointer array1, Pointer array2, int length) {
        return ArrayEqualsNode.equals(array1, array2, length, JavaKind.Boolean);
    }    

    @Uninterruptible(reason = "Must not do a safepoint check.")
    @SubstrateForeignCallTarget(stubCallingConvention = false, fullyUninterruptible = true)
    private static boolean byteArraysEquals(Pointer array1, Pointer array2, int length) {
        return ArrayEqualsNode.equals(array1, array2, length, JavaKind.Byte);
    }    

    @Uninterruptible(reason = "Must not do a safepoint check.")
    @SubstrateForeignCallTarget(stubCallingConvention = false, fullyUninterruptible = true)
    private static boolean charArraysEquals(Pointer array1, Pointer array2, int length) {
        return ArrayEqualsNode.equals(array1, array2, length, JavaKind.Char);
    }    

    @Uninterruptible(reason = "Must not do a safepoint check.")
    @SubstrateForeignCallTarget(stubCallingConvention = false, fullyUninterruptible = true)
    private static boolean shortArraysEquals(Pointer array1, Pointer array2, int length) {
        return ArrayEqualsNode.equals(array1, array2, length, JavaKind.Short);
    }    

    @Uninterruptible(reason = "Must not do a safepoint check.")
    @SubstrateForeignCallTarget(stubCallingConvention = false, fullyUninterruptible = true)
    private static boolean intArraysEquals(Pointer array1, Pointer array2, int length) {
        return ArrayEqualsNode.equals(array1, array2, length, JavaKind.Int);
    }    

    @Uninterruptible(reason = "Must not do a safepoint check.")
    @SubstrateForeignCallTarget(stubCallingConvention = false, fullyUninterruptible = true)
    private static boolean longArraysEquals(Pointer array1, Pointer array2, int length) {
        return ArrayEqualsNode.equals(array1, array2, length, JavaKind.Long);
    }    

    @Uninterruptible(reason = "Must not do a safepoint check.")
    @SubstrateForeignCallTarget(stubCallingConvention = false, fullyUninterruptible = true)
    private static boolean floatArraysEquals(Pointer array1, Pointer array2, int length) {
        return ArrayEqualsNode.equals(array1, array2, length, JavaKind.Float);
    }    

    @Uninterruptible(reason = "Must not do a safepoint check.")
    @SubstrateForeignCallTarget(stubCallingConvention = false, fullyUninterruptible = true)
    private static boolean doubleArraysEquals(Pointer array1, Pointer array2, int length) {
        return ArrayEqualsNode.equals(array1, array2, length, JavaKind.Double);
    }    

    @Uninterruptible(reason = "Must not do a safepoint check.")
    @SubstrateForeignCallTarget(stubCallingConvention = false, fullyUninterruptible = true)
    private static boolean arrayRegionEqualsDynamicStrides(Object arrayA, long offsetA, Object arrayB, long offsetB, int length, int dynamicStrides) {
        return ArrayRegionEqualsNode.regionEquals(arrayA, offsetA, arrayB, offsetB, length, dynamicStrides);
    }    

    @Uninterruptible(reason = "Must not do a safepoint check.")
    @SubstrateForeignCallTarget(stubCallingConvention = false, fullyUninterruptible = true)
    private static boolean arrayRegionEqualsS1S1(Object arrayA, long offsetA, Object arrayB, long offsetB, int length) {
        return ArrayRegionEqualsNode.regionEquals(arrayA, offsetA, arrayB, offsetB, length, S1, S1);
    }    

    @Uninterruptible(reason = "Must not do a safepoint check.")
    @SubstrateForeignCallTarget(stubCallingConvention = false, fullyUninterruptible = true)
    private static boolean arrayRegionEqualsS1S2(Object arrayA, long offsetA, Object arrayB, long offsetB, int length) {
        return ArrayRegionEqualsNode.regionEquals(arrayA, offsetA, arrayB, offsetB, length, S1, S2);
    }    

    @Uninterruptible(reason = "Must not do a safepoint check.")
    @SubstrateForeignCallTarget(stubCallingConvention = false, fullyUninterruptible = true)
    private static boolean arrayRegionEqualsS1S4(Object arrayA, long offsetA, Object arrayB, long offsetB, int length) {
        return ArrayRegionEqualsNode.regionEquals(arrayA, offsetA, arrayB, offsetB, length, S1, S4);
    }    

    @Uninterruptible(reason = "Must not do a safepoint check.")
    @SubstrateForeignCallTarget(stubCallingConvention = false, fullyUninterruptible = true)
    private static boolean arrayRegionEqualsS2S1(Object arrayA, long offsetA, Object arrayB, long offsetB, int length) {
        return ArrayRegionEqualsNode.regionEquals(arrayA, offsetA, arrayB, offsetB, length, S2, S1);
    }    

    @Uninterruptible(reason = "Must not do a safepoint check.")
    @SubstrateForeignCallTarget(stubCallingConvention = false, fullyUninterruptible = true)
    private static boolean arrayRegionEqualsS2S2(Object arrayA, long offsetA, Object arrayB, long offsetB, int length) {
        return ArrayRegionEqualsNode.regionEquals(arrayA, offsetA, arrayB, offsetB, length, S2, S2);
    }    

    @Uninterruptible(reason = "Must not do a safepoint check.")
    @SubstrateForeignCallTarget(stubCallingConvention = false, fullyUninterruptible = true)
    private static boolean arrayRegionEqualsS2S4(Object arrayA, long offsetA, Object arrayB, long offsetB, int length) {
        return ArrayRegionEqualsNode.regionEquals(arrayA, offsetA, arrayB, offsetB, length, S2, S4);
    }    

    @Uninterruptible(reason = "Must not do a safepoint check.")
    @SubstrateForeignCallTarget(stubCallingConvention = false, fullyUninterruptible = true)
    private static boolean arrayRegionEqualsS4S1(Object arrayA, long offsetA, Object arrayB, long offsetB, int length) {
        return ArrayRegionEqualsNode.regionEquals(arrayA, offsetA, arrayB, offsetB, length, S4, S1);
    }    

    @Uninterruptible(reason = "Must not do a safepoint check.")
    @SubstrateForeignCallTarget(stubCallingConvention = false, fullyUninterruptible = true)
    private static boolean arrayRegionEqualsS4S2(Object arrayA, long offsetA, Object arrayB, long offsetB, int length) {
        return ArrayRegionEqualsNode.regionEquals(arrayA, offsetA, arrayB, offsetB, length, S4, S2);
    }    

    @Uninterruptible(reason = "Must not do a safepoint check.")
    @SubstrateForeignCallTarget(stubCallingConvention = false, fullyUninterruptible = true)
    private static boolean arrayRegionEqualsS4S4(Object arrayA, long offsetA, Object arrayB, long offsetB, int length) {
        return ArrayRegionEqualsNode.regionEquals(arrayA, offsetA, arrayB, offsetB, length, S4, S4);
    }

    // GENERATED CODE END
}
