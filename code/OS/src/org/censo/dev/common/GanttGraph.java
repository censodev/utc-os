package org.censo.dev.common;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GanttGraph {
	private List<GanttProcess> graph;
	private int totalProcessDuration;
	private double averageWaitTime;
	private double averageSavingTime;

	public GanttGraph() {
		graph = new ArrayList<GanttProcess>();
		totalProcessDuration = 0;
		averageWaitTime = 0;
	}

	public List<GanttProcess> getListProcess() {
		return graph;
	}

	public void setListProcess(List<GanttProcess> listProcess) {
		this.graph = listProcess;
	}
	
	public double getAverageWaitTime() {
		return averageWaitTime;
	}
	
	public void clear() {
		graph.clear();
	}
	
	public void add(GanttProcess ganttProcess, int startTime) {
		// Thời gian đợi của tiến trình sẽ là tổng thời gian chạy các tiến trình trước nó
		ganttProcess.setWait(totalProcessDuration - startTime);
		graph.add(ganttProcess);
		totalProcessDuration += ganttProcess.getDuration();

		// Tính thời gian đợi trung bình mỗi khi có 1 GanttProcess được đẩy vào
		averageWaitTime = graph.stream()
				.map(prc -> (float) prc.getWait())
				.reduce(0F, Float::sum) / graph.size();

		// Tính thời gian lưu trung bình
		averageSavingTime = graph.stream()
				.map(prc -> (float) (prc.getWait() + prc.getDuration()))
				.reduce(0F, Float::sum) / graph.size();
	}
	
	public int processCount() {
		return graph.size();
	}

	public int getTotalProcessDuration() {
		return totalProcessDuration;
	}

	public void writeGraph(String path) {
		File file = new File(path);
		try {
			FileOutputStream fos = new FileOutputStream(file);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

			StringBuilder stringLine = new StringBuilder("|");
			StringBuilder intLine = new StringBuilder();

			for (GanttProcess p : graph) {
				stringLine.append(String.format("P%d%4s|", p.getId(), ""));
				intLine.append(p.getWait() >= 10 ? String.format("%d%5s", p.getWait(), "") : String.format("%d%6s", p.getWait(), ""));
			}

			intLine.append(totalProcessDuration);

			bw.write(stringLine.toString());
			bw.newLine();
			bw.write(intLine.toString());
			bw.newLine();
			bw.write("Average saving time: " + averageSavingTime);
			bw.newLine();
			bw.write("Average wait time: " + averageWaitTime);

			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
