/******************************************************************************
 *
 * : Col.java,v $
 *
 * Atta Zabihi
 *
 ****************************************************************************
 *
 * : 1.2 $
 *
 * : Col.java,v 1.2 2010/07/22 14:03:34 azabihi Exp $
 *
 ****************************************************************************
 *
 * Copyright (c) 2021 Nokia Inc. All Rights Reserved.
 * Please read the associated COPYRIGHTS file for more details.
 *
 ****************************************************************************
 */
package sshd.shell.springboot.util.table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Col {
    private String header;

    /**
     * Maximum size of this column. The default -1 means the column
     * may grow indefinitely
     */
    int maxSize = -1;

    int size = 0;

    boolean wrap;
    boolean bold;
    boolean cyan;


    /**
     * Alignment
     */
    private HAlign align = HAlign.left;

    public Col(String header) {
        this.header = header;
    }

    public Col align(HAlign align) {
        this.align = align;
        return this;
    }

    public Col alignLeft() {
        this.align = HAlign.left;
        return this;
    }

    public Col alignRight() {
        this.align = HAlign.right;
        return this;
    }

    public Col alignCenter() {
        this.align = HAlign.center;
        return this;
    }

    public Col maxSize(int maxSize) {
        this.maxSize = maxSize;
        return this;
    }

    public Col wrap() {
        return wrap(true);
    }

    public Col wrap(boolean wrap) {
        this.wrap = wrap;
        return this;
    }

    public Col bold() {
        return bold(true);
    }

    public Col bold(boolean bold) {
        this.bold = bold;
        return this;
    }

    public Col cyan() {
        return cyan(true);
    }

    public Col cyan(boolean cyan) {
        this.cyan = cyan;
        return this;
    }

    public int getSize() {
        return size;
    }

    protected void updateSize(int cellSize) {
        if (this.size <= cellSize) {
            this.size = getClippedSize(cellSize);
        }
    }

    private int getClippedSize(int cellSize) {
        return this.maxSize == -1 ? cellSize : Math.min(cellSize, this.maxSize);
    }

    String format(Object cellData) {
        if (cellData == null) {
            cellData = "";
        }
        String fullContent = String.format("%s", cellData);
        if (fullContent.length() == 0) {
            return "";
        }
        String finalContent = cut(fullContent, getClippedSize(fullContent.length()));
        updateSize(finalContent.length());
        return finalContent;
    }

    String getHeader() {
        return header;
    }

    String getContent(String content) {
        List<String> lines = new ArrayList<>();
        lines.addAll(Arrays.asList(content.split("\n")));
        if (wrap) {
            List<String> wrapped = new ArrayList<>();
            for (String line : lines) {
                wrapped.addAll(wrap(line));
            }
            lines = wrapped;
        }
        StringBuilder sb = new StringBuilder();
        for (String line : lines) {
            if (sb.length() > 0) {
                sb.append("\n");
            }
            line = this.align.position(cut(line, size), this.size);
            if (bold) {
                line = SimpleAnsi.INTENSITY_BOLD + line + SimpleAnsi.INTENSITY_NORMAL;
            }
            if (cyan) {
                line = SimpleAnsi.COLOR_CYAN + line + SimpleAnsi.COLOR_DEFAULT;
            }
            sb.append(line);
        }
        return sb.toString();
    }

    protected String cut(String content, int size) {
        if (content.length() <= size) {
            return content;
        } else {
            return content.substring(0, Math.max(0, size - 1));
        }
    }

    protected List<String> wrap(String str) {
        List<String> result = new ArrayList<>();
        Pattern wrap = Pattern.compile("(\\S\\S{" + size + ",}|.{1," + size + "})(\\s+|$)");
        int cur = 0;
        while (cur >= 0) {
            int lst = str.indexOf('\n', cur);
            String s = (lst >= 0) ? str.substring(cur, lst) : str.substring(cur);
            if (s.length() == 0) {
                result.add(s);
            } else {
                Matcher m = wrap.matcher(s);
                while (m.find()) {
                    result.add(m.group());
                }
            }
            if (lst >= 0) {
                cur = lst + 1;
            } else {
                break;
            }
        }
        return result;
    }


}
