/**
 * Copyright 2009-2011 École de technologie supérieure,
 * Communication Research Centre Canada,
 * Inocybe Technologies Inc. and 6837247 CANADA Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.greenstarnetwork.controller.executor;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.greenstarnetwork.services.utils.ObjectSerializer;


@XmlRootElement
public class ExecutionPlan {
	
	private List<ParallelMigration> parallelActions = null;
	
	public ExecutionPlan() {
	}

	/**
	 * @param parallelActions the parallelActions to set
	 */
	public void setParallelActions(List<ParallelMigration> parallelActions) {
		this.parallelActions = parallelActions;
	}

	/**
	 * @return the parallelActions
	 */
	public List<ParallelMigration> getParallelActions() {
		return parallelActions;
	}

	public void addParallelActions(ParallelMigration action) {
		if (this.parallelActions == null)
			this.parallelActions = new ArrayList<ParallelMigration>();
		this.parallelActions.add(action);
	}

	public String toXML() {
		return ObjectSerializer.toXml(this);
	}
}
