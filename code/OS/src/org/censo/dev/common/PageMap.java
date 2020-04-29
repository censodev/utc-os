package org.censo.dev.common;

import java.io.*;
import java.util.List;

public class PageMap {
    private int[][] map;
    private int pagesNum;
    private int framesNum;
    private int mistakesNum;

    public static final int NULL_BOX = -1;
    public static final int MISTAKE_BOX = -2;

    public PageMap(int pagesNum, int framesNum) {
        this.pagesNum = pagesNum;
        this.framesNum = framesNum;
        mistakesNum = 0;
        map = new int[20][100];
        for (int i = 0; i <= framesNum; i++) {
            for (int j = 0; j < pagesNum; j++) {
                map[i][j] = NULL_BOX;
            }
        }
    }

    public int getPage(int row, int col) {
        return map[row][col];
    }

    public void setPage(int row, int col, int value) {
        map[row][col] = value;
    }

    public void writePageMap(String path) {
        File file = new File(path);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

            for (int i = 0; i <= framesNum; i++) {
                StringBuilder sb = new StringBuilder();
                for (int j = 0; j < pagesNum; j++) {
                    if (j == 0 && i < framesNum)
                        sb.append(String.format("|%4s", "L".concat(String.valueOf(i + 1)).concat(" ")));
                    if (map[i][j] == NULL_BOX)
                        sb.append(String.format("|%4s", " "));
                    else if (map[i][j] == MISTAKE_BOX)
                        sb.append(j == 0
                                ? String.format("|%4s", " ").concat(String.format("|%4s", "x "))
                                : String.format("|%4s", "x "));
                    else
                        sb.append(String.format("|%4s", String.valueOf(map[i][j]).concat(" ")));
                }
                sb.append("|");
                bw.write(sb.toString());
                bw.newLine();
            }
            bw.write("Mistakes: " + mistakesNum);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addMistake(int col) {
        mistakesNum++;
        setPage(framesNum, col, MISTAKE_BOX);
    }
}
