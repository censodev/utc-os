package org.censo.dev.distributors;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

import org.censo.dev.common.GanttProcess;
import org.censo.dev.common.Process;

public class FirstComeFirstServe extends AbstractDistributor implements Distributor {
	public FirstComeFirstServe(List<Process> processList) {
		super(processList);

		// Sắp xếp lại list process theo chiều tăng dần của burst time
		this.processList = this.processList.stream()
				.sorted((prc1, prc2) -> prc1.getStart() < prc2.getStart() ? -1 : 1)
				.collect(Collectors.toList());
	}

	@Override
	public void run() {
		// Đẩy dữ liệu vào queue để duyệt theo thứ tự
		Queue<Process> processesQueue = new LinkedList<Process>(this.processList);
		
		while (!processesQueue.isEmpty()) {
			// Cho đến khi queue rỗng, lấy từng process ra để phân tích
			Process prc = processesQueue.poll();

			// Map dữ liệu về kiểu GanttProcess để tạo biểu đồ và tính toán
			GanttProcess prcGantt = new GanttProcess();
			prcGantt.setId(prc.getId());
			prcGantt.setDuration(prc.getDuration());

			// Khi đẩy vào Gantt Graph, tự tính toán lại thời gian chờ của mỗi tiến trình được đẩy và thời gian chờ trung bình
			// Chi tiết trong class GanttGraph
			graphGantt.add(prcGantt, prc.getStart());
		}
	}
}
