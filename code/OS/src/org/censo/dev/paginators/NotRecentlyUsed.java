package org.censo.dev.paginators;

import org.censo.dev.common.Memory;
import org.censo.dev.common.PageMap;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class NotRecentlyUsed {
    private int framesNum;
    private Queue<Integer> pageQueue;
    private PageMap pageMap;
    private List<Integer> inMemoryPageList;

    public NotRecentlyUsed(Memory memory) {
        framesNum = memory.getFramesNum();
        pageQueue = new LinkedList<>(memory.getPageList());
        pageMap = new PageMap(memory.getPageList().size(), memory.getFramesNum());
        inMemoryPageList = new ArrayList<>();
    }

    // Cài đặt:
    // - thiết kế 1 list chứa các tiến trình của 1 cột,
    // - cứ mỗi 1 tiến trình đến sẽ add vào list và vẽ 1 cột bảng trang tương ứng với list đó,
    // - thiết kế 1 queue nguồn lưu toàn bộ tiến trình

    public void run() {
        // con trỏ trỏ tới cột đang xử lí của bảng trang
        int pointer = 0;

        while (!pageQueue.isEmpty()) {
            // lấy lần lượt các tiến trình ra khỏi queue
            int page = pageQueue.poll();

            // kiểm tra xem nó có trùng với cái nào đã có trong list chưa, nếu trùng thì nhảy qua tính cột khác
            boolean isExistPage = inMemoryPageList.contains(page);

            // set trạng thái có sự thay trang khi tiến trình đến (mặc định false)
            boolean isChangePage = false;

            // nếu size của list chưa bằng giới hạn số frame, tức vẫn còn frame trống
            // đẩy tiến trình vào list
            if (inMemoryPageList.size() != framesNum && !isExistPage) {
                inMemoryPageList.add(page);
                isChangePage = true;
            }
            // ngược lại nếu hết frame thì duyệt in-memory list, tạo một list tạm từ phần còn lại của queue tiến trình tổng
            // kiểm tra lần lượt xem các tiến trình đang chiếm frame (trong in-memory list) có tiến trình nào xuất hiện trong phần còn lại list tạm không
            // nếu nó không tồn tại (tức là sau này không cần) thì đặt cờ (cờ trỏ đến vị trí tiến trình có độ ưu tiên thấp nhất) -> break
            // nếu có tồn tại -> so sánh với cờ hiện hành để phân tích tính ưu tiên -> đặt cờ -> duyệt tiếp
            // thay đổi tiến trình mới vào vị trí đặt cờ
            else if (inMemoryPageList.size() == framesNum && !isExistPage) {
                // cờ trong queue để so sánh độ ưu tiên
                // cờ trong list để xác định tiến trình có độ ưu tiên thấp nhất đang chiếm frame
                int flagInQueue = -1;
                int flagInList = -1;
                List<Integer> tmpList = new ArrayList<>(pageQueue);
                for (Integer inMemoryPrc : inMemoryPageList) {
                    // lấy vị trí của tiến trình đang xét trong in-memory list
                    int indexOfInMemoryPrc = inMemoryPageList.indexOf(inMemoryPrc);

                    if (tmpList.contains(inMemoryPrc)) {
                        // lấy vị trí tiến trình đang xét trong phần còn lại của queue
                        int indexInQueue = tmpList.indexOf(inMemoryPrc);
                        // so sánh vị trí trên với cờ, nếu > cờ thì có độ ưu tiên thấp hơn
                        if (indexInQueue > flagInQueue) {
                            flagInList = indexOfInMemoryPrc;
                            flagInQueue = indexInQueue;
                        }
                    }
                    else {
                        // nếu đã không tồn tại trong phần còn lại của queue thì coi như chắc chắn bị thay thế
                        flagInList = indexOfInMemoryPrc;
                        break;
                    }
                }
                inMemoryPageList.set(flagInList, page);
                isChangePage = true;
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
