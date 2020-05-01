package org.censo.dev.distributors;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

import org.censo.dev.common.GanttProcess;
import org.censo.dev.common.Process;

public class ShortestJobNext extends AbstractDistributor implements Distributor {
	public ShortestJobNext(List<Process> processList) {
		super(processList);

		// Sắp xếp toàn bộ tiến trình theo chiều tăng dần của burst time
		this.processList = this.processList.stream()
				.sorted((prc1, prc2) -> prc1.getDuration() < prc2.getDuration() ? -1 : 1)
				.collect(Collectors.toList());
	}

	@Override
	public void run() {
		while (!processList.isEmpty()) {
		    // Sau khi đã sắp xếp theo burst time,
            // chỉ cần lọc ra các tiến trình có arrival time <= tổng thời gian của tiến trình tổng
            // tiến trình đầu tiên sau khi lọc sẽ là tiến trình được ưu tiên xử lí
			Process process = processList.stream()
					.filter(prc -> prc.getStart() <= graphGantt.getTotalProcessDuration())
					.findFirst()
					.get();

			// xử lí xong thì xóa tiến trình khỏi list và add vào GanttGraph
			processList.remove(process);
			GanttProcess prcGantt = new GanttProcess();
			prcGantt.setId(process.getId());
			prcGantt.setDuration(process.getDuration());
			graphGantt.add(prcGantt, process.getStart());
		}
	}
}
