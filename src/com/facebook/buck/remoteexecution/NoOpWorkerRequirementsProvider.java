/*
 * Copyright 2019-present Facebook, Inc.
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
package com.facebook.buck.remoteexecution;

import com.facebook.buck.core.model.BuildTarget;
import com.facebook.buck.remoteexecution.proto.WorkerRequirements;

/** WorkerRequirementsProvider that always returns default WorkerRequirements */
public class NoOpWorkerRequirementsProvider implements WorkerRequirementsProvider {

  /**
   * Returns default WorkerRequirements always.
   *
   * @param target
   * @return
   */
  @Override
  public WorkerRequirements resolveRequirements(BuildTarget target) {
    return WorkerRequirements.newBuilder()
        .setWorkerSize(WorkerRequirements.WorkerSize.SMALL)
        .setPlatformType(WorkerRequirements.WorkerPlatformType.LINUX)
        .setShouldTryLargerWorkerOnOom(false)
        .build();
  }
}
