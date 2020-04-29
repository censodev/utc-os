package org.censo.dev;

import java.util.List;
import java.util.Scanner;

import org.censo.dev.common.FileUtil;
import org.censo.dev.common.Memory;
import org.censo.dev.common.Process;
import org.censo.dev.distributors.FirstComeFirstServe;
import org.censo.dev.distributors.ShortestJobNext;
import org.censo.dev.distributors.ShortestRemainingNext;
import org.censo.dev.paginators.FirstInFirstOut;
import org.censo.dev.paginators.LeastRecentlyUsed;
import org.censo.dev.paginators.NotRecentlyUsed;

public class Startup {
	public static void main(String[] args) {
		List<Process> processList = FileUtil.getListProcessFromJSONFile("resources/cpu.input.json");
		Memory memory = FileUtil.getMemoryInfoFromJSONFIle("resources/paging.input.json");
		
		FirstComeFirstServe fcfs = new FirstComeFirstServe(processList);
		fcfs.run();
		fcfs.getGraphGantt().writeGraph("resources/FCFS.txt");
		
		ShortestJobNext sjn = new ShortestJobNext(processList);
		sjn.run();
		sjn.getGraphGantt().writeGraph("resources/SJN.txt");

//		ShortestRemainingNext srn = new ShortestRemainingNext(processList);
//		srn.run();
//		FileUtil.writeJSONFile("resources/SRN.json", srn.graphGantt);

		FirstInFirstOut fifo = new FirstInFirstOut(memory);
		fifo.run();
		fifo.getPageMap().writePageMap("resources/FIFO.txt");

		LeastRecentlyUsed lru = new LeastRecentlyUsed(memory);
		lru.run();
		lru.getPageMap().writePageMap("resources/LRU.txt");

		NotRecentlyUsed nru = new NotRecentlyUsed(memory);
		nru.run();
		nru.getPageMap().writePageMap("resources/NRU.txt");

		StringBuilder cre = new StringBuilder();
		cre.append("Author: Phuong Bui Viet\n");
		cre.append("Class: UTC K59 IT3\n");
		cre.append("Code: 181210088\n");
		cre.append("------------------------------------------------------------\n");
		cre.append("Complete status: 5/7\n");
		cre.append("Running successfully CPU Scheduling algorithms: FCFS, SJN\n");
		cre.append("Running successfully Pages Switching algorithms: FIFO, LRU, NRU\n");
		cre.append("Please checkout results in folder '/resources'");

		System.out.println(cre.toString());

		Scanner scanner = new Scanner(System.in);
		scanner.nextLine();
	}
}
