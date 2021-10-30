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
 *  Class      :   AboutPanelProvider.java
 *  Author     :   Sean Carrick
 *  Created    :   Oct 29, 2021 @ 1:13:19 PM
 *  Modified   :   Oct 29, 2021
 * 
 *  Purpose:     See class JavaDoc comment.
 * 
 *  Revision History:
 * 
 *  WHEN          BY                   REASON
 *  ------------  -------------------  -----------------------------------------
 *  Oct 29, 2021  Sean Carrick         Initial creation.
 * *****************************************************************************
 */
package com.gs.nta.api;

/**
 * The `AboutPanelProvider` provides a means that modules may provide information
 * about the module, the developers/company, etc., to the NTA Platform's About
 * dialog.
 *
 * @author Sean Carrick &lt;sean at gs-unitedlabs dot com&gt;
 *
 * @version 1.0.0
 * @since 1.0.0
 */
public interface AboutPanelProvider {

    /**
     * Retrieves the title value for the About dialog tab. This would typically
     * be the name of the module.
     * 
     * @return the About screen tab title
     */
    public String getTitle();
    
    /**
     * Retrieves the list of contributors that worked on the module.
     * 
     * @return the contributors list
     */
    public java.util.ArrayList<String> getContributors();
    
    /**
     * Retrieves the name of the individual or company that is responsible for
     * creating the module.
     * 
     * @return the vendor person or company name
     */
    public String getVendor();
    
    /**
     * Retrieves the copyright information for the module.
     * 
     * @return the module's copyright information
     */
    public String getCopyright();
    
    /**
     * Retrieves the logo that represents either the module or the vendor of the
     * module.
     * 
     * @return the module or vendor logo
     */
    public javax.swing.ImageIcon getLogo();
    
    /**
     * Retrieves the URL to a website that contains the full text of the license
     * under which the module is released.
     * 
     * @return the website URL for the module's license
     */
    public java.net.URL getLicenseURL();
    
}
