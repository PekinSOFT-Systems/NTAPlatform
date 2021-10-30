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
 *  Class      :   Logger.java
 *  Author     :   Sean Carrick
 *  Created    :   Jul 17, 2021 @ 10:09:45 PM
 *  Modified   :   Jul 17, 2021
 * 
 *  Purpose:     See class JavaDoc comment.
 * 
 *  Revision History:
 * 
 *  WHEN          BY                   REASON
 *  ------------  -------------------  -----------------------------------------
 *  May 03, 2007  Sean Carrick         Initial creation.
 *  Oct 21, 2007  Sean Carrick         Modified class to not simply wrap the
 *                                     java.util.logging.Logger. Changed it to
 *                                     use the java.io.FileWriter instead.
 *  Jun 02, 2008  Sean Carrick         Modified class to log critical errors to
 *                                     a separate file and only place a pointer
 *                                     to that error file in the log file.
 *  Jan 23, 2010  Sean Carrick         Updated class to allow for standard and
 *                                     formatted message output to the log file.
 *  Aug 10, 2014  Sean Carrick         Updated class to use its own definitions
 *                                     for logging levels by creating public
 *                                     static final fields.
 *  Mar 21, 2020  Sean Carrick         Added the methodolgy of listing modules 
 *                                     to the critical function so that
 *                                     installed modules may be added to the
 *                                     error log.
 *  Sep 14, 2020  Sean Carrick         Made the Logger class Application aware
 *                                     for use with the Swing Application 
 *                                     Platform that we are developing.
 *  May 12, 2021  Sean Carrick         Modified the critical function to output
 *                                     system, user, and Java information to the
 *                                     error log.
 *  Oct 22, 2021  Sean Carrick         Deleted the MSG_HDR and MSG_FTR constants
 *                                     and created the divider constant in an
 *                                     effort to clean up the log files and make
 *                                     them more readable.
 *  Oct 23, 2021  Sean Carrick         Moved the writing functionality out of 
 *                                     the individual message methods and into
 *                                     its own private method. Now, each message
 *                                     method simply checks to see if the
 *                                     application has the property set for log
 *                                     formatting and calls the write method in
 *                                     the appropriate manner.
 *  Oct 28, 2021  Sean Carrick         Added a second write statement to the
 *                                     writeMessage method that inserts a new
 *                                     line into the file. Also added the ability
 *                                     to set the formatting flag for the logger
 *                                     and to change the logging level for the
 *                                     current Logger instance. Modified the wrap
 *                                     and divider widths from 80 to 65 for 
 *                                     cleaner printouts. Updated version to
 *                                     Version 3.7. 
 *                                     Tested Logger.critical and found all kinds
 *                                     of issues that I fixed. I was appending
 *                                     each datapoint to the msg variable twice.
 *                                     There were issues with items such as
 *                                     Module Path being way to long to process,
 *                                     see change long in StringUtils. Anyway, I
 *                                     spent about an hour fixing the critical
 *                                     method and it is working splendidly.
 *                                     ******************************************
 *                                     * Still need to get app modules printing *
 *                                     ******************************************
 * *****************************************************************************
 */
package com.gs.nta.utils;

import com.gs.nta.NTApp;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;

/**
 * The `Logger` class is an `Application`-aware logging facility. This `Logger`
 * works almost identically to {@link java.util.logging.Logger} with minor
 * modifications. The manner of logging messages is slightly different, though
 * most of the methods have been replicated, though they may have different
 * names than those found in the `java.util.logging.Logger` class.
 * <p>
 * `Logger` provides its own levels, as described below:</p>
 * | Level | Verbosity | | :---: | :-------- | | `Logger.OFF` | No log messages
 * will be written to the terminal nor the log file. | | `Logger.TRACE` | The
 * most verbose level. All messages will be written to the log file. When this
 * level is set, all messages will also be written to the terminal. This is the
 * best level for use when developing an application project. | | `Logger.DEBUG`
 * | Less verbose than trace level. All messages will be written to the log
 * file, but no debugging messages will be written to the terminal. | |
 * `Logger.CONFIG` | Less verbose level than debugging. All configuration,
 * informational, warning, error, and critical messages will be written to the
 * log file. Configuration messages are not written to the terminal. | |
 * `Logger.INFO` | Less verbose level than configuration. All informational,
 * warning, error, and critical messages will be written to the log file.
 * Informational messages are not written to the terminal. | | `Logger.WARN` |
 * Less verbose level than informational. All messages will be written to both
 * the log file and the terminal. | | `Logger.ERROR` | Less verbose level than
 * warning. All messages will be written to both the log file and the terminal.
 * This level message is for recoverable errors. | | `Logger.CRITICAL` | Least
 * verbose level. All messages will be written to both the log file and the
 * terminal's error stream. This level message is for unrecoverable errors to be
 * logged just before application termination. |
 * <p>
 * The methods for sending logging messages to the log file and/or terminal have
 * names similar to the `Logger` levels to make things easier on the developer.
 * The methods are: `debug`, `config`, `info`, `warn`, `error`, and `critical`.
 * There are also methods that can be called to log entry into and exit from
 * methods: `enter` and `exit`. All of the methods of `Logger` have a single way
 * of receiving the message to be logged and that is via a `LogRecord`
 * object.</p>
 * <p>
 * One other related class is the `LogRecord` class. This class can have its
 * properties set and then send the whole record to any of the logging methods.
 * The properties available for the `LogRecord` class are:</p>
 * | Property | Use | | :------: | :-- | | Instant | Identifies the instant that
 * the logged event occurred | | Level | The logging message level | |
 * LoggerName | The name of the source `Logger` | | Message | The "raw" log
 * message before internationalization or formatting | | Parameters | The
 * parameters of the log message | | ResourceBundle | The localization
 * `ResourceBundle` | | ResourceBundleName | The name of the localization
 * `ResourceBundle` | | SequenceNumber | The sequence number | | SourceClassName
 * | name of the class that (allegedly) issued the logging request | |
 * SourceMethodName | name of the method that (allegedly) issued the logging
 * request | | ThreadID | The identifier for the thread where the message
 * originated | | Thrown | A `Throwable` associated with the log event |
 * <p>
 * The logging levels are only part of the story, however. The most important
 * part is making sure that high-quality messages are written to the `Logger` at
 * the appropriate logging level. Once the message quality improves, the log
 * files will become more useful in tracking down bugs and logic flaws, as well
 * as for getting them fixed.</p>
 *
 * @see LogRecord
 *
 * @author Sean Carrick &lt;sean at gs-unitedlabs dot com&gt;
 *
 * @version 3.7
 * @since 1.0
 */
public class Logger {

    /**
     * No logging takes place.
     */
    public static final int OFF = -1;

    /**
     * Tracing: the most verbose logging level. Usually used to log diagnostic
     * messages. This level is typically used to track down hard-to-find bugs.
     */
    public static final int TRACE = 0;

    /**
     * Debugging: less verbose than the tracing level. Usually used to log debug
     * information traces.
     */
    public static final int DEBUG = 1;

    /**
     * Configuration: less verbose than the debugging level. Usually used to log
     * configuration information, such as initial variable settings.
     */
    public static final int CONFIG = 2;

    /**
     * Informational: less verbose than the configuration level. Usually used to
     * log message of interest to someone reading the log file.
     */
    public static final int INFO = 3;

    /**
     * Warning: less verbose than the informational level. Usually used to log
     * warning messages for events that do not cause an actual error.
     */
    public static final int WARN = 4;

    /**
     * Errors: less verbose than the warning level. Usually used to log messages
     * regarding recoverable errors.
     */
    public static final int ERROR = 5;

    /**
     * Critical Errors: least verbose logging level. Usually used to log
     * messages about system critical errors immediately before the application
     * exits.
     */
    public static final int CRITICAL = 6;

    private static final String DIVIDER = "-".repeat(65);

    private final String tempLogPath;
    private static Logger logger; // The singleton Logger object.
    private NTApp app;   // The Application from which we are logging.
    private FileWriter log; // The file to which messages will be written.
    private String logPath;
    private String errLogPath;
    private FileWriter err; // The file to which error message will be written.
    private int level;      // Level at which to log messages.
    private boolean formattedOutput = false;

    private Logger(Application application, int level) {
        app = (NTApp) application;
        this.level = level;
        tempLogPath = System.getProperty("user.home") + File.separator
                + ".log";

        File logFile;

        if (app != null) {
            String logFileName = app.getContext().getResourceMap(app.getClass())
                    .getString("Application.id") + ".log";
            String appHome = app.getContext().getLocalStorage().getDirectory().getAbsolutePath();
            if (!appHome.endsWith(File.separator)) {
                appHome += File.separator;
            }

            logPath = appHome + "var" + File.separator + "log" + File.separator;
            File dir = new File(logPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            errLogPath = appHome + "var" + File.separator + "err" + File.separator;
            dir = new File(errLogPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            // Set up objects that are no longer necessary for garbage collection.
            dir = null;
            appHome = null;

            logFile = new File(logPath + logFileName);
        } else {
            logFile = new File(tempLogPath);
        }

        try {
            log = new FileWriter(logFile);
        } catch (IOException ex) {
            String msg = String.format("Unable to create the log file, %s",
                    logFile);
            TerminalErrorPrinter.print(ex, msg);
        }
    }

    /**
     * Retrieves the singleton `Logger` object. The log file will be named based
     * upon the `Application.id` value in the `Application`'s `ResourceMap`.
     * <p>
     * The path for the log file will be the `LocalStorage.getDirectory()`,
     * where the folders `var/log` will be created. In the event a critical
     * error is logged, the error log file will be located in the
     * `LocalStorage.getDirectory()`, under the folders `var/err`. This folder
     * structure will be created, if it does not exist, the first time a log or
     * critical error log is created.</p>
     *
     * @param application the `Application` which needs a `Logger` object
     * @param level the level at which messages should be logged
     * @return the singleton instance of the `Logger` object
     *
     * @see com.gs.framework.api.Application
     * @see com.gs.platform.api.LocalStorage#getDirectory()
     * @see com.gs.platform.api.ResourceMap#getString(java.lang.String,
     * java.lang.Object...)
     */
    public static Logger getLogger(Application application, int level) {
        if (logger == null) {
            logger = new Logger(application, level);
        }

        return logger;
    }

    public static Logger getLogger(Class cls, int level) {
        if (logger == null) {
            logger = new Logger(null, level);
        }

        return logger;
    }

    /**
     * Provide a `LogRecord` to create a configuration message in the log file.
     * <p>
     * The `LogRecord` needs to contain the source class name, which is set at
     * the time the log is initialized. Also, the `LogRecord` needs to contain
     * the source method name, which is set at the time an entry is made when
     * entering the method. The parameters list of the `LogRecord` can contain
     * parameters to a format string, if a format string is used for the
     * message. However, if not using a format string for the message, then the
     * parameters list needs to be set to `null`. The instant and sequence
     * number for the `LogRecord` is optional, as is the thread ID.</p>
     *
     * @param record the `LogRecord` of the message and message details.
     */
    public void config(LogRecord record) {
        if (record == null) {
            throw new IllegalArgumentException("null LogRecord [record]");
        }

        StringBuilder msg = new StringBuilder();
        msg.append((record.getInstant() == null) ? Instant.now().toString()
                : record.getInstant().toString()).append("[SEQ ");
        msg.append(record.getSequenceNumber()).append("]: CONFIG: ");
        msg.append(record.getSourceClassName()).append(".");
        msg.append(record.getSourceMethodName()).append("(); ");
        msg.append("Thread ID: ").append(record.getThreadID()).append("; ");

        if (record.getParameters() != null) {
            msg.append(String.format(record.getMessage(), record.getParameters()));
        } else {
            msg.append(record.getMessage());
        }

        if (level != OFF && level >= CONFIG || level <= DEBUG) {
            writeMessage(msg.toString());
        }
    }

    /**
     * Provides a method to safely close the log file. This method should be
     * called just prior to the `Application` exiting, possibly during the
     * `shutdown` method. This method only has an effect if logging is not at
     * level `Logger.OFF`.
     */
    public void close() {
        if (level != OFF) {
            try {
                log.flush();
                log.close();
            } catch (IOException e) {
                String msg = String.format("Unable to close the log file %s",
                        log);
                TerminalErrorPrinter.print(e, msg);
            }
        }
    }

    /**
     * Logs a system critical error to the log file, and creates a separate,
     * detailed error log file that contains details about the application, the
     * user, the operating system, and the Java environment.
     * <p>
     * The `LogRecord` for a system critical error needs to have all properties
     * supplied, except the sequence number (which is optional). The more of the
     * properties that are set, the better detail can be provided in the error
     * log file.</p>
     * <p>
     * When an `Exception` occurs that places the application into an unstable
     * or untenable state, the `catch` block should populate as many of the
     * properties of the `LogRecord` as is feasible:</p>
     *
     * ```java public class MyClass {
     *
     * private final Logger logger = Logger.getLogger(application, Logger.INFO);
     * private final LogRecord record = new
     * LogRecord(MyClass.getClass().getSimpleName);
     *
     * public int myMethod(int a, int b, int c) {
     * record.setInstant(Instant.now()); record.setSourceMethodName("myMethod");
     * record.setParameters(new Object[] {a, b, c});
     * record.setThreadID(Thread.currentThread().getId()); logger.enter(record);
     *
     * try { // ... the body of the method that could cause an error ... } catch
     * (Exception e) { // Populate as many LogRecord properties as possible.
     * Since //+ most of the properties were populated for the Logger.enter //+
     * method call, we simply need to update the Instant and add //+ the
     * exception to the LogRecord: record.setInstant(Instant.now());
     * record.setThrown(e);
     *
     *             // The message of the LogRecord can be used to provide more //+
     * information about was taking place when the exception was //+ thrown.
     * Remember, the more information provided, the more //+ useful the error
     * log will be. record.setMessage("Details about what the method was doing "
     * + "when the Exception was thrown.");
     *
     *             // Now, all that is left is to log the critical error message:
     * logger.critical(record); } } } ```
     * <p>
     * When your `LogRecord` is set up as in this example, the details of the
     * critical error log file should aid you in tracking down the source of the
     * error so that it can be properly fixed.</p>
     * <p>
     * <strong><em>Note</em></strong>: The `critical` method will always be
     * executed, regardless of logging level, except for the level
     * `Logger.OFF`.</p>
     *
     * @param record the `LogRecord` of the message and message details.
     */
    public void critical(LogRecord record) {
        ResourceMap map = app.getContext().getResourceMap();
        StringBuilder msg = new StringBuilder();
        msg.append(record.getThrown().getClass().toString()).append(" thrown at ");
        msg.append(record.getInstant().toString()).append("\n");
        msg.append("Location: ").append(record.getSourceClassName()).append(".");
        msg.append(record.getSourceMethodName()).append("(");

        if (record.getParameters() != null && record.getParameters().length > 0) {
            for (Object o : record.getParameters()) {
                msg.append(o.getClass().getSimpleName());
                msg.append(" [").append(o.toString()).append("], ");
            }
        }

        String tmpString = msg.toString();
        if (tmpString.endsWith("], ")) {
            msg.replace(msg.lastIndexOf("], "), msg.length(), "])").append("\n");
        } else {
            msg.append(")\n");
        }

        msg.append("Thread ID: ").append(record.getThreadID()).append("\n");

        msg.append(DIVIDER).append("\nDetail Message: ");
        msg.append(record.getMessage()).append("\n").append(DIVIDER).append("\n");
        msg.append("Application Information:").append("\n\n");
        msg.append(map.getString("Application.name"));
        msg.append(" (").append(map.getString("Application.vendor")).append(")\n");
        msg.append("\tVersion: ").append(map.getString("Application.version"));
        msg.append("\n\nInstalled Modules:\n");

//        String[] installedModules = app.getInstalledModules();
//        for (String module : installedModules) {
//            msg.append("\t").append(module).append("\n");
//        }
        msg.append(DIVIDER).append("\n");

        int tabWidth = 70;
        java.util.Properties p = System.getProperties();
        msg.append("\nSystem Information:\n\n");
        msg.append(StringUtils.insertTabLeader("OS",
                p.getProperty("os.name"), tabWidth, '.'));
        msg.append("\n");
        msg.append(StringUtils.insertTabLeader("OS Version",
                p.getProperty("os.version"), tabWidth, '.'));
        msg.append("\n");
        msg.append(StringUtils.insertTabLeader(
                "Architecture", p.getProperty("os.arch"), tabWidth, '.'));
        msg.append("\n\n").append(DIVIDER);

        msg.append("\nJava Information:").append("\n\n");
        msg.append(StringUtils.insertTabLeader("Java Home",
                p.getProperty("java.home"), tabWidth, '.'));
        msg.append("\n");
        msg.append(StringUtils.insertTabLeader(
                "Virtual Machine", p.getProperty("java.vm.name"), tabWidth,
                '.'));
        msg.append("\n");
        msg.append(StringUtils.insertTabLeader("VM Version", p.getProperty(
                "java.vm.version"), tabWidth, '.'));
        msg.append("\n");
        msg.append(StringUtils.insertTabLeader("Runtime", p.getProperty(
                "java.runtime.name"), tabWidth, '.'));
        msg.append("\n");
        msg.append(StringUtils.insertTabLeader("Runtime Version",
                p.getProperty("java.runtime.version"), tabWidth, '.'));
        msg.append("\n");
        msg.append(StringUtils.insertTabLeader("Specification",
                p.getProperty("java.specification.name"), tabWidth, '.'));
        msg.append("\n");
        msg.append(StringUtils.insertTabLeader("Specification Version",
                p.getProperty("java.specification.version"), tabWidth, '.'));
        msg.append("\n");
        msg.append(StringUtils.insertTabLeader("Vendor",
                p.getProperty("java.vendor"), tabWidth, '.'));
        msg.append("\n");
        msg.append(StringUtils.insertTabLeader("Version",
                p.getProperty("java.version"), tabWidth, '.'));
        msg.append("\n");
        msg.append(StringUtils.insertTabLeader("Version Date",
                p.getProperty("java.version.date"), tabWidth, '.'));
        msg.append("\n");
        msg.append(StringUtils.insertTabLeader("Class Path",
                (p.getProperty("java.class.path").equals("")) ? "UNKNOWN" 
                        : p.getProperty("java.class.path"), tabWidth, '.'));
        msg.append("\n");
        msg.append(StringUtils.insertTabLeader("Class Version",
                p.getProperty("java.class.version"), tabWidth, '.'));
        msg.append("\n");
        msg.append(StringUtils.insertTabLeader("Library Path",
                (p.getProperty("java.library.path").equals("")) ? "UNKNOWN" 
                        : p.getProperty("java.library.path"), tabWidth, '.'));
        msg.append("\n");
        msg.append(StringUtils.insertTabLeader("JDK Debug",
                p.getProperty("jdk.debug"), tabWidth, '.'));
        msg.append("\n");
        msg.append(StringUtils.insertTabLeader("JDK Module Path",
                p.getProperty("jdk.module.path"), tabWidth, '.'));
        msg.append("\n");

        msg.append(DIVIDER).append("\nUser Information:").append("\n\n");
        msg.append(StringUtils.insertTabLeader("User Name",
                p.getProperty("user.name"), tabWidth, '.'));
        msg.append("\n");
        msg.append(StringUtils.insertTabLeader("User Home",
                p.getProperty("user.home"), tabWidth, '.'));
        msg.append("\n");
        msg.append(StringUtils.insertTabLeader("User Directory",
                p.getProperty("user.dir"), tabWidth, '.'));
        msg.append("\n");
        msg.append(StringUtils.insertTabLeader("User Country",
                p.getProperty("user.country"), tabWidth, '.'));
        msg.append("\n");
        msg.append(StringUtils.insertTabLeader("User Language",
                p.getProperty("user.language"), tabWidth, '.'));
        msg.append("\n");

        msg.append(DIVIDER).append("\nException Information:\n\n");
        msg.append("Exception Message: ").append(record.getThrown().getMessage());
        msg.append("\n\nStack Trace:\n");

        for (StackTraceElement element : record.getThrown().getStackTrace()) {
            msg.append("\n\t").append(element.toString());
        }
        msg.append("\n ~ End of Stack Trace ~\n");

        if (level != OFF) {
            String ePath = app.getContext().getLocalStorage().getDirectory().toString();
            if (!ePath.endsWith(File.separator)) {
                ePath += File.separator;
            }
            ePath += "var" + File.separator + "err" + File.separator;

            File errPath = new File(ePath);
            if (!errPath.exists()) {
                errPath.mkdirs();
            }

            try {
                err = new FileWriter(ePath + record.getInstant().toString() + " - "
                        + record.getSourceClassName() + "."
                        + record.getSourceMethodName() + ".err.log");
                err.write(msg.toString());
                err.flush();
                err.close();
                err = null;
            } catch (IOException e) {
                String msg2 = String.format("Unable to write message to log file %s"
                        + "\nMessage: %s", log, msg.toString());
                TerminalErrorPrinter.print(e, msg2);
            }

            msg = new StringBuilder();
            msg.append(record.getInstant().toString()).append(": CRITICAL: ");
            msg.append("See detailed error log at ").append(ePath);
            msg.append(record.getInstant().toString()).append(" - ");
            msg.append(record.getSourceClassName()).append(".");
            msg.append(record.getSourceMethodName()).append(".err.log");
            writeMessage(msg.toString());
        }
    }

    /**
     * Provide a `LogRecord` to create a debugging message in the log file.
     * <p>
     * The `LogRecord` needs to contain the source class name, which is set at
     * the time the log is initialized. Also, the `LogRecord` needs to contain
     * the source method name, which is set at the time an entry is made when
     * entering the method. The parameters list of the `LogRecord` can contain
     * parameters to a format string, if a format string is used for the
     * message. However, if not using a format string for the message, then the
     * parameters list needs to be set to `null`. The instant and sequence
     * number for the `LogRecord` is optional, as is the thread ID.</p>
     *
     * @param record the `LogRecord` of the message and message details.
     */
    public void debug(LogRecord record) {
        if (record == null) {
            throw new IllegalArgumentException("null LogRecord [record]");
        }

        StringBuilder msg = new StringBuilder();
        msg.append((record.getInstant() == null) ? Instant.now().toString()
                : record.getInstant().toString()).append("[SEQ ");
        msg.append(record.getSequenceNumber()).append("]: DEBUG: ");
        msg.append(record.getSourceClassName()).append(".");
        msg.append(record.getSourceMethodName()).append("(); ");
        msg.append("Thread ID: ").append(record.getThreadID()).append("; ");

        if (record.getParameters() != null) {
            msg.append(String.format(record.getMessage(), record.getParameters()));
        } else {
            msg.append(record.getMessage());
        }

        if (level != OFF && level >= DEBUG || level == TRACE) {
            writeMessage(msg.toString());
        }
    }

    /**
     * Provide a `LogRecord` to create a method entry message in the log file.
     * <p>
     * The `LogRecord` needs to contain the source class name, which is set at
     * the time the log is initialized. Also, the `LogRecord` needs to contain
     * the source method name, which is set at the time an entry is made when
     * entering the method. The parameters list of the `LogRecord` should
     * contain the method parameters as an array of `Object`s, if the method
     * takes parameters. However, if the message takes no parameters, then the
     * parameters list needs to be set to `null`. The instant and sequence
     * number for the `LogRecord` is optional, as is the thread ID.</p>
     * <p>
     * <strong><em>Note</em></strong>: The `enter` method will only be executed
     * if the logging level is set to `Logger.TRACE`.</p>
     *
     * @param record the `LogRecord` of the message and message details
     *
     * @see #exit(com.gs.platform.utils.LogRecord)
     */
    public void enter(LogRecord record) {
        if (record == null) {
            throw new IllegalArgumentException("null LogRecord [record]");
        }

        StringBuilder msg = new StringBuilder();
        msg.append((record.getInstant() == null) ? Instant.now().toString()
                : record.getInstant().toString()).append(" [SEQ ");
        msg.append(record.getSequenceNumber()).append("]: ENTERING: ");
        msg.append(record.getSourceClassName()).append(".");
        msg.append(record.getSourceMethodName()).append("(");

        if (record.getParameters() != null && record.getParameters().length > 0) {
            for (Object o : record.getParameters()) {
                msg.append(o.getClass().getSimpleName());
                msg.append(" [").append(o.toString()).append("], ");
            }
        }

        String tmpString = msg.toString();
        if (tmpString.endsWith("], ")) {
            msg.replace(msg.lastIndexOf("], "), msg.length(), "])");
        } else {
            msg.append("); ");
        }

        msg.append("Thread ID: ").append(record.getThreadID()).append("; ");
        msg.append(record.getMessage());

        if (level != OFF && level == TRACE) {
            writeMessage(msg.toString());
        }
    }

    /**
     * Provide a `LogRecord` to create a log message for exiting a method. The
     * `LogRecord` needs to contain the source class name, source method name,
     * message text. If the message returns a value, that value should be added
     * to the parameters list as an `Object` array of a single element,
     * otherwise the parameters list should be set to `null. For example:
     * ```java public class MyClass {
     *
     * private final LogRecord record = new
     * LogRecord(MyClass.class.getSimpleName()); private final Logger logger =
     * Logger.getLogger(application, Logger.DEBUG);
     *
     * public void method(int one, int two) { record.setInstant(Instant.now());
     * record.setSourceMethodName("method"); record.setParameters(new Object[]
     * {one, two}); record.setMessage("Entering the method.")
     * logger.enter(record);
     *
     *         // the body of the method.
     *
     *         // Get the instant of this next message and nullify the //+ parameters
     * list since there is no return value. record.setInstant(Instant.now());
     * record.setParameters(null); record.setMessage("the method has completed
     * successfully."); logger.exit(record); }
     *
     * public int add(int one, int two) { record.setInstant(Instant.now());
     * record.setSourceMethodName("add"); record.setParameters(new Object[]
     * {one, two}); record.setMessage("Adding two numbers.");
     * logger.enter(record);
     *
     * int sum = one + two;
     *
     *         // Get the instant of this next message and set the parameters // list
     * to an Object array of one element containing the sum.
     * record.setInstant(Instant.now()); record.setParameters(new Object[]
     * {sum}); record.setMessage("The sum of " + one " + " + two " is " + sum);
     * logger.exit(record); }
     *
     * }
     * ```
     * <p>
     * The current thread ID and a sequence number are optional. Typically, the
     * `Instant.now()` will be set at the time the method is entered. If the
     * `Instant` is not set, the `Instant.now()` that this method is entered
     * will be recorded.</p>
     * <p>
     * <strong><em>Note</em></strong>: The `exit` method will only be executed
     * if the logging level is set to `Logger.TRACE`.</p>
     *
     * @param record the `LogRecord` of the message and message details
     *
     * @see #enter(com.gs.platform.utils.LogRecord)
     */
    public void exit(LogRecord record) {
        if (record == null) {
            throw new IllegalArgumentException("null LogRecord [record]");
        }

        StringBuilder msg = new StringBuilder();
        msg.append((record.getInstant() == null) ? Instant.now().toString()
                : record.getInstant().toString()).append(" [SEQ ");
        msg.append(record.getSequenceNumber()).append("]: EXITING: ");
        msg.append(record.getSourceClassName()).append(".");
        msg.append(record.getSourceMethodName()).append("() RETURNING");

        if (record.getParameters() != null && record.getParameters().length > 0) {
            for (Object o : record.getParameters()) {
                msg.append(o.getClass().getSimpleName());
                msg.append(" [").append(o.toString()).append("]");
            }
        }

        msg.append("Thread ID: ").append(record.getThreadID()).append("; ");
        msg.append(record.getMessage());

        if (level != OFF && level == TRACE) {
            writeMessage(msg.toString());
        }
    }

    /**
     * Provide a `LogRecord` to create a non-critical error message in the log
     * file.
     * <p>
     * The `LogRecord` needs to contain the source class name, which is set at
     * the time the log is initialized. Also, the `LogRecord` needs to contain
     * the source method name, which is set at the time an entry is made when
     * entering the method. One other requirement for the `error` method is that
     * the `Thrown` entry be made. The parameters list of the `LogRecord` can
     * contain parameters to a format string, if a format string is used for the
     * message. However, if not using a format string for the message, then the
     * parameters list needs to be set to `null`. The instant and sequence
     * number for the `LogRecord` is optional, as is the thread ID.</p>
     *
     * @param record the `LogRecord` of the message and message details.
     */
    public void error(LogRecord record) {
        if (record == null) {
            throw new IllegalArgumentException("null LogRecord [record]");
        }

        StringBuilder msg = new StringBuilder();
        msg.append((record.getInstant() == null) ? Instant.now().toString()
                : record.getInstant().toString()).append("[SEQ ");
        msg.append(record.getSequenceNumber()).append("]: ERROR: ");
        msg.append(record.getSourceClassName()).append(".");
        msg.append(record.getSourceMethodName()).append("(); ");
        msg.append("Thread ID: ").append(record.getThreadID()).append("; ");

        if (record.getParameters() != null) {
            msg.append(String.format(record.getMessage(), record.getParameters()));
        } else {
            msg.append(record.getMessage());
        }

        msg.append(DIVIDER);
        msg.append("\nError Message: ").append(record.getThrown().getMessage());
        msg.append("\nStack Trace:");
        for (StackTraceElement e : record.getThrown().getStackTrace()) {
            msg.append("\n\t").append(e.toString());
        }

        if (level != OFF && level >= ERROR || level <= DEBUG) {
            writeMessage(msg.toString());
        }
    }

    /**
     * Provides a way to retrieve the current logging level for this `Logger`.
     *
     * @return the current logging level
     */
    public int getLevel() {
        return level;
    }
    
    /**
     * Provides a way to change the current logging level.
     * 
     * @param level the new logging level
     */
    public void setLevel(int level) {
        this.level = level;
    }
    
    /**
     * Determines whether the output to the log file should format the log
     * entries.
     * 
     * @return `true` if output is formatted
     */
    public boolean isFormattedOutput() {
        return formattedOutput;
    }
    
    /**
     * Provides a way to set whether or not the log entries should be formatted.
     * 
     * @param formattedOutput `true` to format log entries
     */
    public void setFormattedOutput(boolean formattedOutput) {
        this.formattedOutput = formattedOutput;
    }

    /**
     * Provide a `LogRecord` to create a informational message in the log file.
     * <p>
     * The `LogRecord` needs to contain the source class name, which is set at
     * the time the log is initialized. Also, the `LogRecord` needs to contain
     * the source method name, which is set at the time an entry is made when
     * entering the method. The parameters list of the `LogRecord` can contain
     * parameters to a format string, if a format string is used for the
     * message. However, if not using a format string for the message, then the
     * parameters list needs to be set to `null`. The instant and sequence
     * number for the `LogRecord` is optional, as is the thread ID.</p>
     *
     * @param record the `LogRecord` of the message and message details.
     */
    public void info(LogRecord record) {
        if (record == null) {
            throw new IllegalArgumentException("null LogRecord [record]");
        }

        StringBuilder msg = new StringBuilder();
        msg.append((record.getInstant() == null) ? Instant.now().toString()
                : record.getInstant().toString()).append("[SEQ ");
        msg.append(record.getSequenceNumber()).append("]: INFO: ");
        msg.append(record.getSourceClassName()).append(".");
        msg.append(record.getSourceMethodName()).append("(); ");
        msg.append("Thread ID: ").append(record.getThreadID()).append("; ");

        if (record.getParameters() != null) {
            msg.append(String.format(record.getMessage(), record.getParameters()));
        } else {
            msg.append(record.getMessage());
        }

        if (level != OFF && level >= INFO || level <= DEBUG) {
            writeMessage(msg.toString());
        }
    }

    public void setApplication(Application app) {
        this.app = (NTApp) app;
    }

    /**
     * Provide a `LogRecord` to create a warning message in the log file.
     * <p>
     * The `LogRecord` needs to contain the source class name, which is set at
     * the time the log is initialized. Also, the `LogRecord` needs to contain
     * the source method name, which is set at the time an entry is made when
     * entering the method. The parameters list of the `LogRecord` can contain
     * parameters to a format string, if a format string is used for the
     * message. However, if not using a format string for the message, then the
     * parameters list needs to be set to `null`. The instant and sequence
     * number for the `LogRecord` is optional, as is the thread ID.</p>
     * <p>
     * For warning messages, a `Thrown` object can be present. Not all warnings
     * will involve an `Exception` having been thrown, but if one was, it can be
     * added to the `LogRecord` to provide more details for the log message.</p>
     *
     * @param record the `LogRecord` of the message and message details.
     */
    public void warn(LogRecord record) {
        if (record == null) {
            throw new IllegalArgumentException("null LogRecord [record]");
        }

        StringBuilder msg = new StringBuilder();
        msg.append((record.getInstant() == null) ? Instant.now().toString()
                : record.getInstant().toString()).append("[SEQ ");
        msg.append(record.getSequenceNumber()).append("]: WARNING: ");
        msg.append(record.getSourceClassName()).append(".");
        msg.append(record.getSourceMethodName()).append("(); ");
        msg.append("Thread ID: ").append(record.getThreadID()).append("; ");

        if (record.getParameters() != null) {
            msg.append(String.format(record.getMessage(), record.getParameters()));
        } else {
            msg.append(record.getMessage());
        }

        if (record.getThrown() != null) {
            msg.append("\n").append(DIVIDER).append("\n");
            msg.append(record.getThrown().getMessage()).append("\n");
        }

        if (level != OFF && level >= WARN || level <= DEBUG) {
            writeMessage(msg.toString());
        }
    }

    /**
     * Performs the actual writing of the messages to the log file in a central
     * fashion.
     *
     * @param message the message to write.
     */
    private void writeMessage(String message) {
        try {
            message = (formattedOutput) ? formatLogMessage(message) : message;
            log.write(message);
            log.write("\n");
            log.flush();
        } catch (IOException e) {
            String msg = String.format("Unable to write message to log file %s"
                    + "\nMessage: %s", log, message);
            TerminalErrorPrinter.print(e, msg);
        }
    }

    /**
     * Formats the created message text for output to the log file and/or
     * terminal.
     *
     * @param msg the message to be formatted
     * @return the formatted message
     */
    private String formatLogMessage(String msg) {
        return StringUtils.wrapLogMessage(msg, 65) + "\n" + DIVIDER;
    }

}
