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
 *  Class      :   ModuleRegistrar.java
 *  Author     :   Sean Carrick
 *  Created    :   Oct 27, 2021 @ 8:08:31 AM
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
 * The `ModuleRegistrar` is the auto-loader interface for platform modules to
 * use to announce their availability to the application. Each module needs to
 * implement the `ModuleRegistrar` service provider interface (SPI) with a 
 * service provider.
 * 
 * The `ModuleRegistrar` contains a single method, `register`. The implementation
 * of this method simply needs to provide the module name as the return value.
 * This registration process simply allows for the Platform to know that the 
 * module is loaded and available for use.
 *
 * @author Sean Carrick &lt;sean at gs-unitedlabs dot com&gt;
 *
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class ModuleRegistrar {

    /**
     * Registers a module into the Platform application. Once registered, the
     * module will be loaded and available for use within the Platform
     * application.
     */
    private final void register() {
        
    }
    
    /**
     * Retrieves the module's name for registration.
     * 
     * *Note*: The value of this method will be used for the module's About
     * panel title, so it needs to be short and to the point.
     * 
     * @return the name of the module
     */
    abstract protected String getName();
    
    abstract protected AboutPanelProvider getAboutPanel();
    
}
