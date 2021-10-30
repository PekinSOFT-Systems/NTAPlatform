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
 * The `com.gs.nta.api.MenuProvider` interface allows modules to provide 
 * top-level menus to the NTA Platform application. Times that this would be 
 * necessary are such as an accounting module needing a top-level menu called 
 * "Accounting". Then that module can place all of its action commands under the 
 * Accounting menu, so that it is intuitive for the user where to go for 
 * accounting features.
 * 
 * However, providing a top-level menu is not something that every module needs 
 * to do. For the most part, the application will already have all of the 
 * required top-level menus in place, so a module developer would simply need to
 * provide a menu item to be placed into one of the existing menus. The 
 * challenge comes in knowing how to locate an existing menu into which the 
 * modules action command(s) should be placed. 
 * 
 * ***Best Practice***: The NTA Platform has a `ResourceManager` and 
 * `ResourceMap` class that processes resource files (standard Java Properties, 
 * `*.properties`, files) to inject property values into the various GUI 
 * components. Therefore, the best practice is to always use a resource file to 
 * supply property values for the various components that make up a module.
 * 
 * Resource files should be named the same as the class for which the resource 
 * file contains resources. For example, `<ClassName>.properties`, would be the 
 * resource file for the class `<ClassName>.java`. Furthermore, the location of 
 * the resource files is key, they should always be located in a sub-package of 
 * the class for which they provide property values. For example, 
 * `com.my.example.MyClass.java` is in the package `com.my.example`, so its 
 * resource file would need to be in a sub-package, *named **resources***, below
 * the class' package, such as `com.my.example.resources.MyClass.properties`.
 * 
 * So, for a `MenuProvider` named "MyMenu" located in the package 
 * `com.example.MyMenu.java`, the resources file for it would need to be 
 * `com.example.resources.MyMenu.properties`.
 * 
 * As already mentioned, the resources files allow the application to inject the
 * component properties into the components at run-time, thereby allowing for 
 * easy I18N processing. When it is said that "the application injects component
 * properties into the components", this does not just include text/string 
 * properties. If the menu would need an icon, it could be set via the resource 
 * file as well. The contents of a resource file for a menu named "MyMenu", with
 * text and an icon, would look like this:
 * 
 * ```
 * # Properties located in a file called MyMenu.properties in a sub package 
 * # of the class file's package, called resources.
 * text=MenuText
 * icon=/fully/qualified/path/to/icon.png
 * 
 * # OR, for a relative path to a sub-package of the resources sub-package:
 * icon=path/to/icon.png
 * ```
 * 
 * By using the resource injection feature of the NTA Platform, the classes (as 
 * well as the generated `javax.swing.JMenu`) will be all set to be 
 * internationalized from the start.
 * 
 * @see #getName() 
 * @see #getPosition() 
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
     * The position on the menu bar that the generated menu should occupy.
     * 
     * @return the menu's position on the menu bar
     */
    public int getPosition();
    
    /**
     * The text to display on the generated menu. *Best Practice* is to use the
     * resources file for the class to store the text for easy I18N of the UI.
     * 
     * @return the menu's text
     */
    public String getText();
    
}
