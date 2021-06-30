/******************************************************************************
 *
 * : AnsiColumn.java,v $
 *
 * Atta Zabihi
 *
 ****************************************************************************
 *
 * : 1.2 $
 *
 * : AnsiColumn.java,v 1.2 2010/07/22 14:03:34 azabihi Exp $
 *
 ****************************************************************************
 *
 * Copyright (c) 2021 Nokia Inc. All Rights Reserved.
 * Please read the associated COPYRIGHTS file for more details.
 *
 ****************************************************************************
 */
package sshd.shell.springboot.util.table;

import org.jline.utils.AttributedStringBuilder;

public class AnsiColumn extends Col{
    private int color;
    private boolean bold;

    public AnsiColumn(String header, int color, boolean bold) {
        super(header);
        this.color = color;
        this.bold = bold;
    }

    @Override
    public String getContent(String content) {
        String in = super.getContent(content);

        AttributedStringBuilder sb = new AttributedStringBuilder();
        sb.style(sb.style().foreground(color));

        if (bold)
            sb.style(sb.style().bold());

        sb.append(in);

        if (bold)
            sb.style(sb.style().boldOff());

        sb.style(sb.style().foregroundOff());

        return sb.toAnsi();
    }

}
