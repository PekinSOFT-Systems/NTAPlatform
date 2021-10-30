/*
 * Copyright (C) 2021 GS United Labs
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * *****************************************************************************
 *  Project    :   NTA-Basic
 *  Class      :   BugReporter.java
 *  Author     :   Sean Carrick
 *  Created    :   Oct 30, 2021 @ 2:29:15 PM
 *  Modified   :   Oct 30, 2021
 * 
 *  Purpose:     See class JavaDoc comment.
 * 
 *  Revision History:
 * 
 *  WHEN          BY                   REASON
 *  ------------  -------------------  -----------------------------------------
 *  Oct 30, 2021  Sean Carrick         Initial creation.
 * *****************************************************************************
 */
package com.gs.nta.bugs;

import com.gs.nta.NTApp;
import java.io.File;
import java.util.Calendar;
import org.jdesktop.application.Application;

/**
 * The `BugReporter` allows for any application or platform bugs to be sent to
 * GS United Labs
 *
 * @author Sean Carrick &lt;sean at gs-unitedlabs dot com&gt;
 *
 * @version 1.0.0
 * @since 1.0.0
 */
public class BugReporter {
    
    private static BugReporter reporter;
    private final NTApp app = (NTApp) Application.getInstance();
    private final String logsFolder;
    private final String errFolder;
    private Exception ex;
    private String className;
    private String methodName;
    private Calendar timestamp;
    private long milliseconds;
    private Thread thread;

    private BugReporter () {
        String tmp = app.getContext().getLocalStorage().getDirectory().getPath();
        if (!tmp.endsWith(File.separator)) {
            tmp += File.separator;
        }
        logsFolder = tmp + "var" + File.separator + "logs" + File.separator;
        errFolder = tmp + "var" + File.separator + "err" + File.separator;
    }
    
    public static BugReporter getInstance() {
        if (reporter == null) {
            reporter = new BugReporter();
        }
        
        return reporter;
    }

    public Exception getEx() {
        return ex;
    }

    public void setEx(Exception ex) {
        this.ex = ex;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

}
