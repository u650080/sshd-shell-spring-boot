/******************************************************************************
 *
 * : SimpleAnsi.java,v $
 *
 * Atta Zabihi
 *
 ****************************************************************************
 *
 * : 1.2 $
 *
 * : SimpleAnsi.java,v 1.2 2010/07/22 14:03:34 azabihi Exp $
 *
 ****************************************************************************
 *
 * Copyright (c) 2021 Nokia Inc. All Rights Reserved.
 * Please read the associated COPYRIGHTS file for more details.
 *
 ****************************************************************************
 */
package sshd.shell.springboot.util.table;

public class SimpleAnsi {
    public static String COLOR_RED = "\u001b[31m";
    public static String COLOR_CYAN = "\u001b[36m";
    public static String COLOR_YELLOW = "\u001b[33m";
    public static String COLOR_DEFAULT = "\u001b[39m";

    public static String INTENSITY_BOLD = "\u001b[1m";
    public static String INTENSITY_NORMAL = "\u001b[22m";

    public static String RESET = "\u001b[m";
}
