/*
 * Copyright (c) 2015, 2021, Oracle and/or its affiliates. All rights reserved.
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
package com.oracle.svm.truffle.api;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import org.graalvm.collections.EconomicMap;
import jdk.compiler.graal.graph.SourceLanguagePositionProvider;
import jdk.compiler.graal.nodes.EncodedGraph;
import jdk.compiler.graal.nodes.StructuredGraph;
import jdk.compiler.graal.nodes.graphbuilderconf.GraphBuilderConfiguration;
import jdk.compiler.graal.nodes.graphbuilderconf.GraphBuilderConfiguration.Plugins;
import jdk.compiler.graal.nodes.graphbuilderconf.InlineInvokePlugin;
import jdk.compiler.graal.nodes.graphbuilderconf.InvocationPlugins;
import jdk.compiler.graal.nodes.graphbuilderconf.NodePlugin;
import jdk.compiler.graal.nodes.graphbuilderconf.ParameterPlugin;
import jdk.compiler.graal.phases.util.Providers;
import jdk.compiler.graal.replacements.PEGraphDecoder;
import jdk.compiler.graal.replacements.PEGraphDecoder.SpecialCallTargetCacheKey;
import jdk.compiler.graal.truffle.compiler.PartialEvaluator;
import jdk.compiler.graal.truffle.compiler.PartialEvaluatorConfiguration;
import jdk.compiler.graal.truffle.compiler.TruffleCompilerConfiguration;
import jdk.compiler.graal.truffle.compiler.TruffleTierContext;
import org.graalvm.nativeimage.Platform;
import org.graalvm.nativeimage.Platforms;

import com.oracle.truffle.compiler.ConstantFieldInfo;
import com.oracle.truffle.compiler.PartialEvaluationMethodInfo;

import jdk.vm.ci.meta.ResolvedJavaField;
import jdk.vm.ci.meta.ResolvedJavaMethod;

public class SubstratePartialEvaluator extends PartialEvaluator {

    private final ConcurrentHashMap<ResolvedJavaMethod, Object> invocationPluginsCache;
    private final ConcurrentHashMap<SpecialCallTargetCacheKey, Object> specialCallTargetCache;

    @Platforms(Platform.HOSTED_ONLY.class)
    public SubstratePartialEvaluator(TruffleCompilerConfiguration config, GraphBuilderConfiguration graphBuilderConfigForRoot) {
        super(config, graphBuilderConfigForRoot);
        this.invocationPluginsCache = new ConcurrentHashMap<>();
        this.specialCallTargetCache = new ConcurrentHashMap<>();
    }

    @Override
    protected PEGraphDecoder createGraphDecoder(TruffleTierContext context, InvocationPlugins invocationPlugins, InlineInvokePlugin[] inlineInvokePlugins, ParameterPlugin parameterPlugin,
                    NodePlugin[] nodePlugins, SourceLanguagePositionProvider sourceLanguagePositionProvider, EconomicMap<ResolvedJavaMethod, EncodedGraph> graphCache,
                    Supplier<AutoCloseable> createCachedGraphScope) {
        return new SubstratePEGraphDecoder(config.architecture(), context.graph, config.lastTier().providers().copyWith(this.constantFieldProvider), loopExplosionPlugin, invocationPlugins,
                        inlineInvokePlugins, parameterPlugin, nodePlugins, types.OptimizedCallTarget_callInlined, sourceLanguagePositionProvider, specialCallTargetCache, invocationPluginsCache);
    }

    @Override
    protected StructuredGraph.Builder customizeStructuredGraphBuilder(StructuredGraph.Builder builder) {
        /*
         * Substrate VM does not need a complete list of methods that were inlined during
         * compilation. Therefore, we do not even store this information in encoded graphs that are
         * part of the image heap.
         */
        return super.customizeStructuredGraphBuilder(builder).recordInlinedMethods(false);
    }

    @Override
    public final PartialEvaluationMethodInfo getMethodInfo(ResolvedJavaMethod method) {
        return ((TruffleMethod) method).getTruffleMethodInfo();
    }

    @Override
    public ConstantFieldInfo getConstantFieldInfo(ResolvedJavaField field) {
        return ((TruffleField) field).getConstantFieldInfo();
    }

    @Override
    protected void registerGraphBuilderInvocationPlugins(InvocationPlugins invocationPlugins, boolean canDelayIntrinsification) {
        super.registerGraphBuilderInvocationPlugins(invocationPlugins, canDelayIntrinsification);
        SubstrateTruffleGraphBuilderPlugins.registerInvocationPlugins(invocationPlugins, canDelayIntrinsification, (SubstrateKnownTruffleTypes) getTypes());
    }

    @Platforms(Platform.HOSTED_ONLY.class)
    @Override
    protected InvocationPlugins createDecodingInvocationPlugins(PartialEvaluatorConfiguration peConfig, Plugins parent, Providers tierProviders) {
        InvocationPlugins decodingInvocationPlugins = new InvocationPlugins();
        registerGraphBuilderInvocationPlugins(decodingInvocationPlugins, false);
        peConfig.registerDecodingInvocationPlugins(decodingInvocationPlugins, false, config.lastTier().providers(), config.architecture());
        decodingInvocationPlugins.closeRegistration();
        return decodingInvocationPlugins;
    }

    @Override
    protected NodePlugin[] createNodePlugins(Plugins plugins) {
        return null;
    }

}
