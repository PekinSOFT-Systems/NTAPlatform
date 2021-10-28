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
 *  Class      :   MenuProvider.java
 *  Author     :   Sean Carrick
 *  Created    :   Oct 28, 2021 @ 1:09:26 AM
 *  Modified   :   Oct 28, 2021
 * 
 *  Purpose:     See class JavaDoc comment.
 * 
 *  Revision History:
 * 
 *  WHEN          BY                   REASON
 *  ------------  -------------------  -----------------------------------------
 *  Oct 28, 2021  Sean Carrick         Initial creation.
 * *****************************************************************************
 */
package com.gs.nta.api;

/**
 * The `MenuProvider` interface allows modules to provide a top-level menu to 
 * the Foundation module's menu bar.
 *
 * @author Sean Carrick &lt;sean at gs-unitedlabs dot com&gt;
 *
 * @version 1.0.0
 * @since 1.0.0
 */
public interface MenuProvider extends Comparable<MenuProvider> {
    
    /**
     * Retrieves the name for the generated `JMenu`. Typically, the name of the
     * menu is the text of the menu with "Menu" appended to the end of it. This
     * is the standard that Northwind Traders follows.
     * 
     * @return the name for the generated menu
     */
    public String getName();
    
    /**
     * The text that is displayed on the generated menu.
     * 
     * @return the menu text
     */
    public String getText();
    
    /**
     * The position on the menu bar that the generated menu should occupy.
     * 
     * @return the menu's position on the menu bar
     */
    public int getPosition();
    
}
