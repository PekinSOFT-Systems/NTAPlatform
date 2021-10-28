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
 *  Project    :   NTA.Foundation
 *  Class      :   Menu.java
 *  Author     :   Sean Carrick
 *  Created    :   Oct 27, 2021 @ 9:15:09 PM
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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The `Menu` interface provides a means by which modules may provide
 * menus to the Platform application dynamically. This interface is used to
 * decorate a class for any top-level menus which that class needs to provide.
 * For example:
 * 
 * ```java
 * @Menu(name = "fileMenu", text = "File", position = `Integer.MIN_VALUE)`
 * public class MyApplicationClass {
 *     // ... the class implementation ...
 * }
 * ```
 * 
 * Furthermore, the `Menu` interface may be used multiple times on the
 * same class, such as:
 * 
 * ```java
 * @Menu(name = "fileMenu", text = "File", position = Integer.MIN_VALUE)`
 * @Menu(name = "editMenu", text = "Edit", position = Integer.MIN_VALUE + 1)`
 * @Menu(name = "viewMenu", text = "View", position = Integer.MIN_VALUE + 500)`
 * @Menu(name = "toolsMenu", text = "Tools", position = Integer.MAX_VALUE - 500)`
 * @Menu(name = "helpMenu", text = "Help", position = Integer.MAX_VALUE)`
 * public class MyApplicationClass {
 *     // ... the class implementation ...
 * }
 * ```
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
 * @author Sean Carrick &lt;sean at gs-unitedlabs dot com&gt;
 *
 * @version 1.0.0
 * @since 1.0.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(Menus.class)
public @interface Menu {
    /**
     * The name for the menu. This is the value that will be passed to the 
     * `JMenu.setName()` method. By having the name for the menu set in this 
     * manner, the application can be internationalized via the
     * `ApplicationContext.getResourceManager().getResourceMap().getString()`
     * method.
     * 
     * @return the value of the `name` property for this `@Menu` decoration.
     */
    String name();
    
    /**
     * The text that should be displayed on the menu. This value will be passed
     * to the `JMenu.setText()` method.
     * 
     * @return the value of the `text` property for this `@Menu` decoration
     */
    String text();
    int position();
}
