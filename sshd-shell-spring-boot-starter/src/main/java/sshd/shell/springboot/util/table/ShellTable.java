/******************************************************************************
 *
 * : ShellTable.java,v $
 *
 * Atta Zabihi
 *
 ****************************************************************************
 *
 * : 1.2 $
 *
 * : ShellTable.java,v 1.2 2010/07/22 14:03:34 azabihi Exp $
 *
 ****************************************************************************
 *
 * Copyright (c) 2021 Nokia Inc. All Rights Reserved.
 * Please read the associated COPYRIGHTS file for more details.
 *
 ****************************************************************************
 */
package sshd.shell.springboot.util.table;

import org.jline.terminal.Terminal;

import java.io.IOException;
import java.io.PrintStream;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import sshd.shell.springboot.autoconfiguration.SshSessionContext;

public class ShellTable {
    private static final char SEP_HORIZONTAL = '─';
    private static final char SEP_VERTICAL = '│';
    private static final char SEP_CROSS = '┼';
    static final String TERMINAL = "__terminal";
    private static final char SEP_HORIZONTAL_ASCII = '-';
    private static final char SEP_VERTICAL_ASCII = '|';
    private static final char SEP_CROSS_ASCII = '+';

    private static final String DEFAULT_SEPARATOR = " " + SEP_VERTICAL + " ";
    private static final String DEFAULT_SEPARATOR_ASCII = " " + SEP_VERTICAL_ASCII + " ";
    private static final String DEFAULT_SEPARATOR_NO_FORMAT = "\t";

    private List<Col> cols = new ArrayList<>();
    private List<Row> rows = new ArrayList<>();
    private boolean showHeaders = true;
    private String separator = DEFAULT_SEPARATOR;
    private int size;
    private String emptyTableText;
    private boolean forceAscii;

    public ShellTable() {

    }

    public ShellTable noHeaders() {
        this.showHeaders = false;
        return this;
    }

    public ShellTable separator(String separator) {
        this.separator = separator;
        return this;
    }

    public ShellTable size(int size) {
        this.size = size;
        return this;
    }

    public ShellTable column(Col colunmn) {
        cols.add(colunmn);
        return this;
    }

    public Col column(String header) {
        Col col = new Col(header);
        cols.add(col);
        return col;
    }

    public Row addRow() {
        Row row = new Row();
        rows.add(row);
        return row;
    }

    public ShellTable forceAscii() {
        forceAscii = true;
        return this;
    }

    /**
     * Set text to display if there are no rows in the table.
     *
     * @param text the text to display when the table is empty.
     * @return the shell table.
     */
    public ShellTable emptyTableText(String text) {
        this.emptyTableText = text;
        return this;
    }

    public void print(Writer out) throws IOException {
        print(out, true);
    }

    public void print(Writer out, boolean format) throws IOException {
        print(out, null, format);
    }

    public void print(Writer out, Charset charset, boolean format) throws IOException {
        boolean unicode = supportsUnicode(out, charset);
        String separator = unicode ? this.separator : DEFAULT_SEPARATOR_ASCII;
        // "normal" table rendering, with borders
        Row headerRow = new Row(cols);
        headerRow.formatContent(cols);
        for (Row row : rows) {
            row.formatContent(cols);
        }

        if (size > 0) {
            adjustSize();
        }

        if (format && showHeaders) {
            String headerLine = headerRow.getContent(cols, separator);
            out.write(headerLine+"\n");
            int iCol = 0;
            for (Col col : cols) {
                if (iCol++ == 0) {
                    out.write(underline(col.getSize(), false, unicode));
                } else {
                    out.write(underline(col.getSize() + 3, true, unicode));
                }
                iCol++;
            }
            out.write("\n");
        }

        for (Row row : rows) {
            if (!format) {
                if (separator == null || separator.equals(DEFAULT_SEPARATOR))
                    out.write(row.getContent(cols, DEFAULT_SEPARATOR_NO_FORMAT)+"\n");
                else out.write(row.getContent(cols, separator)+"\n");
            } else {
                out.write(row.getContent(cols, separator)+"\n");
            }
        }

        if (format && rows.size() == 0 && emptyTableText != null) {
            out.write(emptyTableText+"\n");
        }
    }

    private boolean supportsUnicode(Writer out, Charset charset) {
        if (forceAscii) {
            return false;
        }
        if (charset == null) {
            charset = getEncoding(out);
        }
        if (charset == null) {
            return false;
        }
        CharsetEncoder encoder = charset.newEncoder();
        return encoder.canEncode(separator)
               && encoder.canEncode(SEP_HORIZONTAL)
               && encoder.canEncode(SEP_CROSS);
    }

    private Charset getEncoding(Writer ps) {
            try {
                return SshSessionContext.<Terminal>get(TERMINAL).encoding();
            } catch (Throwable t) {
                // ignore
            }

            return null;
    }

    private void adjustSize() {
        int currentSize = 0;
        for (Col col : cols) {
            currentSize += col.size + separator.length();
        }
        currentSize -= separator.length();
        int sizeToGrow = size - currentSize;

        for (int i = cols.size() - 1; i >= 0; i--) {
            Col col = cols.get(i);
            if (col.maxSize == -1) {
                col.size = Math.max(0, col.size + sizeToGrow);
                return;
            }
        }

    }

    private String underline(int length, boolean crossAtBeg, boolean supported) {
        char[] exmarks = new char[length];
        Arrays.fill(exmarks,  supported ? SEP_HORIZONTAL : SEP_HORIZONTAL_ASCII);
        if (crossAtBeg) {
            exmarks[1] = supported ? SEP_CROSS : SEP_CROSS_ASCII;
        }
        return new String(exmarks);
    }

}
