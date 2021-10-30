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
 *  Class      :   Launcher.java
 *  Author     :   Sean Carrick
 *  Created    :   Aug 14, 2021 @ 11:33:48 PM
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
package com.gs.nta.launcher;

import com.gs.nta.NTApp;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.jdesktop.application.Application;

/**
 * The {@code Launcher} class is used to launch the Northwind Traders Basic
 * Edition application. 
 * <p>
 * The purpose behind not launching NTA directly is that GS United Labs will 
 * have updated executable {@code JAR} files available for download on our 
 * update server. Part of launching the application is checking for updates
 * <strong><em>prior to launch</em></strong>, as well as during the application
 * run. By having this {@code Launcher} class, we will be able to check for any
 * available updates, download, and apply them prior to launching NTA.</p>
 * <p>
 * Also, if updates are discovered while NTA is running, this class will be able
 * to allow NTA to restart in order to apply the updates. Since there is a 
 * chance that an update may affect a module that is currently in use, an
 * application restart will be required to ensure that all updates are made
 * available as soon as the restart completes.</p>
 * <p>
 * Another viable use for the {@code Launcher} class is to remove some of the
 * initialization, such as that for the first run of the application, from the
 * application itself. This will help to clean up the code in the application
 * class, which will make maintenance of the codebase simpler while moving
 * forward. Some items that could be taken care of in this class are:</p>
 * <ul>
 * <li>Registration &mdash; The registration of NTA could actually be handled in
 *      this class, versus handling it in the initialization method of the 
 *      application class.</li>
 * <li>Application Home &mdash; Setting up the application home folder could be
 *      better handled in this class instead of in the application class.</li>
 * <li>Rotating Logs &mdash; Rotating the log file backups would best be handled
 *      in this class, thereby allowing the application class, as well as all
 *      other classes that make up the application, to only need to worry about
 *      creating the log file for their use.</li>
 * </ul>
 *
 * @author Sean Carrick &lt;sean at gs-unitedlabs dot com&gt;
 *
 * @version 0.1.0
 * @since 0.1.0
 */
public class Launcher {

    /**
     * Starts up the application. The first action performed is to check to see
     * if updates to the application are available. If updates are available,
     * they are downloaded and installed. Once the application has been updated,
     * it is then launched.
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (updateAvailable()) {
            update();
        }
        
        Application.launch(NTApp.class, args);
    }
    
    /**
     * Checks to see if an update to the application is available on the update
     * server. 
     * <p>
     * The method connects to the application server, if the internet is 
     * available, and checks to see if there are any updates available for 
     * download.</p>
     * 
     * @return {@code true} if updates are available; {@code false} otherwise
     */
    private static boolean updateAvailable() {
        // TODO: Code internet check and update server connection
        return false;
    }
    
    /**
     * Connects to the update server, if there is an internet connection, 
     * locates the available updates, downloads the available updates, and 
     * installs the downloaded updates.
     */
    private static void update() {
        // TODO: Code downloading updates and applying them to the application
    }

}
