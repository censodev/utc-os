package org.censo.dev.paginators;

import org.censo.dev.common.Memory;
import org.censo.dev.common.PageMap;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class LeastRecentlyUsed {
    private int framesNum;
    private Queue<Integer> pageQueue;
    private PageMap pageMap;
    private List<Integer> inMemoryPageList;
    private Queue<Integer> inMemoryPageQueue;

    public LeastRecentlyUsed(Memory memory) {
        framesNum = memory.getFramesNum();
        pageQueue = new LinkedList<>(memory.getPageList());
        pageMap = new PageMap(memory.getPageList().size(), memory.getFramesNum());
        inMemoryPageList = new ArrayList<>();
        inMemoryPageQueue = new LinkedList<>();
    }

    // Cài đặt:
    // - thiết kế 1 list chứa các tiến trình của 1 cột,
    // - cứ mỗi 1 tiến trình đến sẽ add vào list và vẽ 1 cột bảng trang tương ứng với list đó,
    // - thiết kế 1 queue nguồn lưu toàn bộ tiến trình
    // - thiết kế 1 queue in-memory có dung lượng bằng số frame để kiểm tra thứ tứ tiến trình đến trước tiến trình đến sau đang chiếm frame

    public void run() {
        // con trỏ trỏ tới cột đang xử lí của bảng trang
        int pointer = 0;

        while (!pageQueue.isEmpty()) {
            // lấy lần lượt các tiến trình ra khỏi queue
            int page = pageQueue.poll();

            // kiểm tra xem nó có trùng với cái nào đã có trong list chưa
            boolean isExistPage = inMemoryPageList.contains(page);

            // set trạng thái có sự thay trang khi tiến trình đến (mặc định false)
            boolean isChangePage = false;

            // nếu size của list chưa bằng giới hạn số frame, tức vẫn còn frame trống
            // đẩy tiến trình vào queue in-memory và list
            if (inMemoryPageList.size() != framesNum && !isExistPage) {
                inMemoryPageList.add(page);
                inMemoryPageQueue.add(page);
                isChangePage = true;
            }

            // ngược lại nếu hết frame thì phân tích xem tiến trình nào ở trong frame lâu nhất để thay thế tiến trình mới vào
            // kiểm tra bằng queue in-memory đã thiết kế ở trên
            else if (inMemoryPageList.size() == framesNum && !isExistPage) {
                int removedPage = inMemoryPageQueue.poll();
                inMemoryPageQueue.add(page);
                inMemoryPageList.set(inMemoryPageList.indexOf(removedPage), page);
                isChangePage = true;
            }

            // nếu tiến trình mới đã tồn tại trong queue in-memory thì xóa nó rồi đẩy lại vào queue để bảo toàn nó là tiến trình mới chiếm frame
            else {
                inMemoryPageQueue.remove(inMemoryPageQueue.stream().filter(p -> p == page).findFirst().get());
                inMemoryPageQueue.add(page);
            }

            // cứ mỗi lần xử lí xong 1 tiến trình thì vẽ 1 cột cho bảng trang -> đẩy con trỏ để xử lí tới cột kế
            fillPageMapPerPollTurn(pointer, isChangePage);
            pointer++;
        }
    }

    private void fillPageMapPerPollTurn(int col, boolean isPageChange) {
        int i;
        for (i = 0; i < inMemoryPageList.size(); i++)
            pageMap.setPage(i, col, inMemoryPageList.get(i));
        if (isPageChange)
            pageMap.addMistake(col);
    }

    public PageMap getPageMap() {
        return pageMap;
    }
}
