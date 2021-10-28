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
 *  Class      :   NTApp.java
 *  Author     :   Sean Carrick
 *  Created    :   Aug 14, 2021 @ 11:31:57 PM
 *  Modified   :   Aug 14, 2021
 * 
 *  Purpose:     See class JavaDoc comment.
 * 
 *  Revision History:
 * 
 *  WHEN          BY                   REASON
 *  ------------  -------------------  -----------------------------------------
 *  Aug 14, 2021  Sean Carrick         Initial creation.
 * *****************************************************************************
 */
package com.gs.nta;

import com.gs.nta.desktop.MainFrame;
import com.gs.utils.ArgumentParser;
import com.gs.utils.Logger;
import com.gs.utils.Properties;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import org.jdesktop.application.SingleFrameApplication;

/**
 * The top-level class of the Northwind Traders Basic Edition application.
 * <p>
 * The application accepts command line parameters for setting the logging level
 * and for determining if the application is being developed. The valid command
 * line parameters are:</p>
 * <dl><dt>{@code -i} ~or~ {@code --devel}</dt>
 * <dd>Places the application into developer mode. In this mode, the logging
 * level is set to {@code Logger.DEBUG}, which is the most verbose logging
 * level. Furthermore, the registration process is bypassed for developers.
 * </dd><dt>{@code --level=[LoggingLevel}</dt>
 * <dd>Provides the level for the logging function for the current run of the
 * application. The valid logging levels are listed below.</dd>
 * <dt>{@code -f} ~or~ {@code --fancy}</dt>
 * <dd>Tells the application to break logged message lines at the 80 character
 * mark in the log files. If this parameter is not present, then the logged
 * messages are written to the log file as they are sent to the logger.</dd>
 * </dl>
 * <p>
 * For the {@code --level} and {@code -l} command line parameters, these are the
 * only valid logging levels to supply:</p>
 * <dl><dt>{@code off}</dt><dd>Turns the logging functionality off. Note,
 * however, that critical errors are still logged, even when logging is off.
 * Critical errors are those that cause the application to exit abnormally.</dd>
 * <dt>{@code debug}</dt><dd>This is the most verbose logging level available.
 * At this level, all messages sent to the logger are written to the log file,
 * and printed to the system terminal.</dd>
 * <dt>{@code config}</dt><dd>This allows all messages regarding configuration
 * and higher to be written to the log file and the system terminal.</dd>
 * <dt>{@code info}</dt><dd><em>Default Logging Level.</em> This level only
 * allows those messages logged at the informational level or higher to be
 * written to the log file and printed to the system terminal.</dd>
 * <dt>{@code warn}</dt><dd>This allows only warning messages and higher to be
 * written to the log file and printed to the system terminal.</dd>
 * <dt>{@code error}</dt><dd>At this level, only error messages and critical
 * error messages are written to the log file and printed to the system
 * terminal.</dd>
 * <dt>{@code critical}</dt><dd>At this level, the only messages being written
 * to the log file and system terminal are those that cause the application to
 * exit abnormally.</dd>
 * <dl>
 *
 * @author Sean Carrick &lt;sean at gs-unitedlabs dot com&gt;
 *
 * @version 0.1.0
 * @since 0.1.0
 */
public class NTApp extends SingleFrameApplication {

    private Properties props;
    private Logger logger;
    private MainFrame mainFrame;

    @Override
    protected void initialize(String[] args) {
        props = new Properties(getContext());
        ArgumentParser parser = new ArgumentParser(args);

        // To initialize the logging, we need to determine the logging level.
        if (!parser.isSwitchPresent("-i") || !parser.isSwitchPresent("--devel")) {
            int level = Logger.INFO;
            if (parser.isSwitchPresent("-l")) {
                switch (parser.getSwitchValue("-l").toLowerCase()) {
                    case "debug":
                        level = Logger.DEBUG;
                        break;
                    case "info":
                        level = Logger.INFO;
                        break;
                    case "config":
                        level = Logger.CONFIG;
                        break;
                    case "warn":
                        level = Logger.WARN;
                        break;
                    case "error":
                        level = Logger.ERROR;
                        break;
                    case "critical":
                        level = Logger.CRITICAL;
                        break;
                    default:
                        level = Logger.OFF;
                }
            } else if (parser.isSwitchPresent("--level")) {
                switch (parser.getSwitchValue("--level").toLowerCase()) {
                    case "debug":
                        level = Logger.DEBUG;
                        break;
                    case "info":
                        level = Logger.INFO;
                        break;
                    case "config":
                        level = Logger.CONFIG;
                        break;
                    case "warn":
                        level = Logger.WARN;
                        break;
                    case "error":
                        level = Logger.ERROR;
                        break;
                    case "critical":
                        level = Logger.CRITICAL;
                        break;
                    default:
                        level = Logger.OFF;
                }
            }

            props.setRuntimeProperty("loggingLevel", String.valueOf(level));
            props.setRuntimeProperty("isDeveloper", "false");
        } else {
            props.setRuntimeProperty("loggingLevel", String.valueOf(Logger.DEBUG));
            props.setRuntimeProperty("isDeveloper", "true");
        }

        if (parser.isSwitchPresent("-f") || parser.isSwitchPresent("--fancy")) {
            props.setRuntimeProperty("fancyLogs", "true");
        } else {
            props.setRuntimeProperty("fancyLogs", "false");
        }
        
        String lvl = props.getProperty("loggingLevel", String.valueOf(Logger.INFO));

        logger = Logger.getLogger(getInstance(), Integer.valueOf(lvl));
    }

    @Override
    protected void startup() {
        mainFrame = new MainFrame(this);
        getMainFrame().addWindowListener(new MainFrameAdapter());
        show(mainFrame);
    }

    @Override
    protected void shutdown() {
        // TODO: Perform all application cleanup here.
        
        // Call the super.shutdown() in order to shutdown any running tasks.
        super.shutdown();
    }
    
    private class MainFrameAdapter extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            exit(e);
        }
    }

}
