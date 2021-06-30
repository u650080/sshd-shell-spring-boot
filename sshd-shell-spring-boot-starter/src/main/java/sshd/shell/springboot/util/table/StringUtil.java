/******************************************************************************
 *
 * : StringUtil.java,v $
 *
 * Atta Zabihi
 *
 ****************************************************************************
 *
 * : 1.2 $
 *
 * : StringUtil.java,v 1.2 2010/07/22 14:03:34 azabihi Exp $
 *
 ****************************************************************************
 *
 * Copyright (c) 2021 Nokia Inc. All Rights Reserved.
 * Please read the associated COPYRIGHTS file for more details.
 *
 ****************************************************************************
 */
package sshd.shell.springboot.util.table;

public class StringUtil {
    public static int length(String string) {
        return string == null ? 0 : string.length();
    }
    public static String repeat(String string, int times) {
        if (times <= 0) {
            return "";
        }
        else if (times % 2 == 0) {
            return repeat(string+string, times/2);
        }
        else {
            return string + repeat(string+string, times/2);
        }
    }
}
