package org.censo.dev.common;

import java.util.List;

public class Memory {
    private int framesNum;
    private List<Integer> pageList;

    public int getFramesNum() {
        return framesNum;
    }

    public void setFramesNum(int framesNum) {
        this.framesNum = framesNum;
    }

    public List<Integer> getPageList() {
        return pageList;
    }

    public void setPageList(List<Integer> pageList) {
        this.pageList = pageList;
    }
}
