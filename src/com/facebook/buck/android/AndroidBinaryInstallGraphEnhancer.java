/*
 * Copyright 2017-present Facebook, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.facebook.buck.android;

import com.facebook.buck.io.ProjectFilesystem;
import com.facebook.buck.model.BuildTarget;
import com.facebook.buck.model.Flavor;
import com.facebook.buck.model.InternalFlavor;
import com.facebook.buck.rules.BuildRule;
import com.facebook.buck.rules.BuildRuleResolver;
import com.facebook.buck.rules.NoopBuildRule;
import com.facebook.buck.rules.SourcePathRuleFinder;
import com.google.common.collect.ImmutableSortedSet;
import java.util.Optional;
import java.util.SortedSet;

class AndroidBinaryInstallGraphEnhancer {
  static final Flavor INSTALL_FLAVOR = InternalFlavor.of("install");

  private ProjectFilesystem projectFilesystem;
  private BuildTarget buildTarget;
  private AndroidBinary androidBinary;
  private AndroidInstallConfig androidInstallConfig;

  AndroidBinaryInstallGraphEnhancer(
      AndroidInstallConfig androidInstallConfig,
      ProjectFilesystem projectFilesystem,
      BuildTarget buildTarget,
      AndroidBinary androidBinary) {
    this.projectFilesystem = projectFilesystem;
    this.buildTarget = buildTarget.withFlavors(INSTALL_FLAVOR);
    this.androidBinary = androidBinary;
    this.androidInstallConfig = androidInstallConfig;
  }

  private BuildRule createExoInstaller(
      BuildTarget buildTarget,
      ProjectFilesystem filesystem,
      BuildRuleResolver resolver,
      AndroidBinary binary,
      AndroidInstallConfig androidInstallConfig) {
    BuildRule installer;
    if (androidInstallConfig.getConcurrentInstallEnabled(
        Optional.ofNullable(resolver.getEventBus()))) {
      installer =
          new ExoInstaller(buildTarget, filesystem, new SourcePathRuleFinder(resolver), binary);
    } else {
      installer =
          new NoopBuildRule(buildTarget, filesystem) {
            @Override
            public SortedSet<BuildRule> getBuildDeps() {
              return ImmutableSortedSet.of();
            }
          };
    }
    resolver.addToIndex(installer);
    return installer;
  }

  public void enhance(BuildRuleResolver resolver) {
    resolver.addToIndex(
        createExoInstaller(
            buildTarget, projectFilesystem, resolver, androidBinary, androidInstallConfig));
  }
}
