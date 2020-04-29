package org.censo.dev.distributors;

import java.util.List;

import org.censo.dev.common.GanttGraph;
import org.censo.dev.common.Process;

public abstract class AbstractDistributor {
	protected GanttGraph graphGantt;
	protected List<Process> processList;
	
	protected AbstractDistributor(List<Process> processList) {
		this.processList = processList;
		this.graphGantt = new GanttGraph();
	}

	public GanttGraph getGraphGantt() {
		return graphGantt;
	}
}
