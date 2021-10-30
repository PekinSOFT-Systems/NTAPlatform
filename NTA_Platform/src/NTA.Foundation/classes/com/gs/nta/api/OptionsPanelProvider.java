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
 *  Project    :   NTA.API
 *  Class      :   OptionsPanelProvider.java
 *  Author     :   Sean Carrick
 *  Created    :   Oct 27, 2021 @ 10:27:12 AM
 *  Modified   :   Oct 27, 2021
 * 
 *  Purpose:     See class JavaDoc comment.
 * 
 *  Revision History:
 * 
 *  WHEN          BY                   REASON
 *  ------------  -------------------  -----------------------------------------
 *  Oct 27, 2021  Sean Carrick         Initial creation.
 * *****************************************************************************
 */
package com.gs.nta.api;

/**
 * The `OptionsPanelProvider` interface is used to set a `JPanel` form to be an
 * options panel in the Platform's Options dialog, which is provided by the 
 * `NTA.Foundation` module. Simply create a `JPanel` form in the Matisse GUI
 * Designer and then add `implements OptionsPanelProvider` to the end of the
 * class declaration line.
 * 
 * All that is left to do is to implement the two methods: `getCategory` and
 * `getInstance`. Due to the way that options panels are provided dynamically at
 * runtime, the *singleton* class model must be used.
 * 
 * @see #categ
 *
 * @author Sean Carrick &lt;sean at gs-unitedlabs dot com&gt;
 *
 * @version 1.0.0
 * @since 1.0.0
 */
public interface OptionsPanelProvider {
    /**
     * The category on the Options dialog into which the decorated panel should
     * be placed. The allowable values are:
     * 
     * - ACCOUNTING
     * - GENERAL
     * - INTERFACE
     * - MISCELLANEOUS
     * - REPORTS
     * - SCHEDULING
     * 
     * @return The Options category in which to place the decorated panel.
     */
    public OptionsCategories getCategory();
    
    /**
     * Retrieves an instance of the options provider panel.
     * 
     * @return the panel's instance
     */
    public javax.swing.JPanel getInstance();
    
    /**
     * Retrieves the title for the `OptionsPanelProvider`'s tab in the Options
     * dialog.
     * 
     * @return the tab title for the options panel
     */
    public String getTitle();
    
    /**
     * Saves the settings made on this `OptionsPanelProvider`. When implementing
     * this method, it is the `setSystemProperty` method that should be used. 
     * Otherwise, any changes to the settings on the `OptionsPanelProvider` 
     * implementation will be lost when the application shuts down. The vast majority
     * of settings made on an options dialog should be persisted from run-to-run of
     * an application. The properties object used by the NTA Platform manages both
     * run-time and application settings. Run-time settings are typically set via
     * command-line parameters to the application. Whereas application (or system)
     * settings should rarely need to be modified.
     * 
     * @param properties the application properties object
     */
    public void saveSettings(com.gs.nta.utils.Properties properties);
    
    public void loadSettings(com.gs.nta.utils.Properties properties);
    
}
