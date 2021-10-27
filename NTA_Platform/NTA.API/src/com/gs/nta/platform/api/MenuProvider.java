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
 *  Class      :   MenuProvider.java
 *  Author     :   Sean Carrick
 *  Created    :   Oct 27, 2021 @ 8:13:51 AM
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
package com.gs.nta.platform.api;

import javax.swing.Icon;

/**
 * The `MenuProvider` interface provides a means by which modules may provide
 * menus to the Platform application dynamically. 
 * 
 * The Platform Foundation only provides the main menu bar, but without menus or
 * menu items. Therefore, it is up to each module to provide the menu(s) or menu
 * item(s) that allow access to the module's features.
 * 
 * When a top-level menu is provided, its name should be set to the text of the
 * menu, with the word "Menu" appended at the end, for example "fileMenu". By
 * following this guideline strictly, whenever another module is trying to add
 * a menu item to the File menu, then that module will be able to properly 
 * specify the name of the owner menu.
 * 
 * Furthermore, by providing menus to the application in this manner, the menus
 * may be positioned, which is not possible for standard `JMenu`s. The `position`
 * property tells the Foundation where on the menu bar the new top-level menu
 * should be placed. This value should be a positive integer value in increments
 * of 100, to allow for "fudging" if two menus are requesting the same position.
 * 
 * The Platform Application, by default, provides only the following menus:
 * 
 * - File: Positioned as `Integer.MIN_VALUE`, so that no menu may be placed to
 * the left of it.
 * - Edit: Positioned as `Integer.MIN_VALUE + 1`, so that no menu may be placed
 * between it and the File menu.
 * - View: Positioned at 500, to allow a menu or two to be placed between it and
 * the Edit menu.
 * - Tools: Positioned at `Integer.MAX_VALUE - 500`, so that a menu or two may
 * be placed between it and the Help menu.
 * - Help: Positioned at `Integer.MAX_VALUE`, so that it is always the right-most
 * menu.
 * 
 * These five menus are placed such as they are to comply with well-established
 * UI standards. The UI standards of today have been established industry-wide
 * for the past few decades, so Northwind Traders does its best to follow them.
 *
 * @author Sean Carrick &lt;sean at gs-unitedlabs dot com&gt;
 *
 * @version 1.0.0
 * @since 1.0.0
 */
public interface MenuProvider {
    
    /**
     * Retrieves the value of the `name` property for this `MenuProvider`. The
     * `name` needs to follow the convention of adding the word "Menu" to the 
     * end of the lower-case version of the menu's text, such as "fileMenu". 
     * To ensure that this is always the case, one could issue the `return` 
     * statement like this:
     * 
     * ```java
     * @Override
     * public String getName() {
     *     return getText().toLowerCase() + "Menu";
     * }
     * ```
     * 
     * By issuing the `return` statement in such a fashion, even if the text of
     * the menu is changed in the future, the name will always match the text
     * and follow the Northwind Traders naming convention.
     * 
     * @return The menu's name property. This property will be set on the `JMenu`:
     * 
     * ```java
     * JMenu menu = new JMenu(provider.getText());
     * menu.setName(provider.getName());
     * ```
     */
    public String getName();
    
    /**
     * Retrieves the `position` property for this `MenuProvider`. The `position`
     * tells the Foundation module where on the menu bar this menu should be
     * placed. The returned value should be in increments of 100 to allow for
     * "fudging" in the event that two `MenuProvider`s request the same location.
     * 
     * @return The positive integer value for where the menu should be placed.
     */
    public int getPosition();
    
    /**
     * Retrieves the `text` property for this `MenuProvider`. The `text` tells
     * the Foundation module what this `MenuProvider`'s menu should display to
     * the user of the application.
     * 
     * @return The text that will be displayed on the menu.
     */
    public String getText();
    
    /**
     * <em>Optional</em>: An icon image to be displayed on the menu. This 
     * property is optional for menus, as it is not common practice for top-level 
     * menus to have icons. However, this same `MenuProvider` interface may be
     * used to add menus within the top-level menus and those menus may have an
     * icon.
     * 
     * @return The icon image to be displayed on the menu.
     */
    public Icon getIcon();
    
}
