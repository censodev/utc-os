package org.censo.dev.distributors;

import org.censo.dev.common.GanttProcess;
import org.censo.dev.common.Process;

import java.util.*;
import java.util.stream.Collectors;

public class ShortestRemainingNext extends AbstractDistributor implements Distributor {
    public ShortestRemainingNext(List<Process> processList) {
        super(processList);


    }

    @Override
    public void run() {
        while (!processList.isEmpty()) {
            Process chosenProcess = processList.stream()
                    .filter(prc -> prc.getStart() <= graphGantt.getTotalProcessDuration())
                    .sorted((prc1, prc2) -> prc1.getDuration() < prc2.getDuration() ? -1 : 1)
                    .findFirst()
                    .get();

            processList.remove(chosenProcess);

            if (processList.size() > 0) {
                Process nextProcess = processList.stream()
                        .sorted((prc1, prc2) -> prc1.getStart() < prc2.getStart() ? -1 : 1)
                        .findFirst()
                        .get();

                if (nextProcess.getStart() < chosenProcess.getDuration() + graphGantt.getTotalProcessDuration()) {
                    GanttProcess ganttProcess = new GanttProcess();
                    ganttProcess.setId(chosenProcess.getId());
                    ganttProcess.setDuration(nextProcess.getStart() - chosenProcess.getStart());
                    graphGantt.add(ganttProcess);

                    chosenProcess.setDuration(chosenProcess.getDuration() - ganttProcess.getDuration());
//                    chosenProcess.setStart();
                    processList.add(chosenProcess);
                } else {
                    GanttProcess ganttProcess = new GanttProcess();
                    ganttProcess.setId(chosenProcess.getId());
                    ganttProcess.setDuration(chosenProcess.getDuration());
                    graphGantt.add(ganttProcess);
                }
            } else {
                GanttProcess ganttProcess = new GanttProcess();
                ganttProcess.setId(chosenProcess.getId());
                ganttProcess.setDuration(chosenProcess.getDuration());
                graphGantt.add(ganttProcess);
            }

        }
    }
}
