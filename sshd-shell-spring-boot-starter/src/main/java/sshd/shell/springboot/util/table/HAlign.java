/******************************************************************************
 *
 * : HAlign.java,v $
 *
 * Atta Zabihi
 *
 ****************************************************************************
 *
 * : 1.2 $
 *
 * : HAlign.java,v 1.2 2010/07/22 14:03:34 azabihi Exp $
 *
 ****************************************************************************
 *
 * Copyright (c) 2021 Nokia Inc. All Rights Reserved.
 * Please read the associated COPYRIGHTS file for more details.
 *
 ****************************************************************************
 */

package sshd.shell.springboot.util.table;

import static sshd.shell.springboot.util.table.StringUtil.length;
import static sshd.shell.springboot.util.table.StringUtil.repeat;

public enum HAlign {
    center {
        @Override
        public String position(String text, int colWidth) {
            int width = colWidth - length(text);
            text = repeat(" ", width / 2) + text + repeat(" ", width / 2);
            if (length(text) < colWidth) {
                // if colWidth is odd we add space at the end.
                text += " ";
            }
            return text;
        }
    },

    /**
     * Left align.
     */
    left {
        @Override
        public String position(String text, int colWidth) {
            return text + repeat(" ", colWidth - length(text));
        }
    },

    /**
     * Right align.
     */
    right {
        @Override
        public String position(String text, int colWidth) {
            return repeat(" ", colWidth - length(text)) + text;
        }
    };

    /**
     * Calculate text position.
     *
     * @param text  the text to align.
     * @param colWidth the column width.
     * @return the string at the given position.
     */
    public abstract String position(String text, int colWidth);

}
