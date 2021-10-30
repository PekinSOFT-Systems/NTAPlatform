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
 *  Class      :   ActionCommandProvider.java
 *  Author     :   Sean Carrick
 *  Created    :   Oct 28, 2021 @ 1:35:17 AM
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
 * The `ActionCommandProvider` interface allows modules to provide menu items 
 * and toolbar buttons to the Foundation module's menus. Best Practice: The NTA 
 * Platform has a `ResourceManager` and `ResourceMap` class that processes 
 * resource files (standard Java Properties, `*.properties`, files) to inject 
 * property values into the various GUI components. Therefore, the best practice
 * is to always use a resource file to supply property values for the various 
 * components that make up a module.
 * 
 * Resource files should be named the same as the class for which the resource 
 * file contains resources. For example, `<ClassName>.properties`, would be the 
 * resource file for the class `<ClassName>.java`. Furthermore, the location of 
 * the resource files is key, they should always be located in a sub-package of 
 * the class for which they provide property values. For example, 
 * `com.my.example.MyClass.java` is in the package `com.my.example`, so its 
 * resource file would need to be in a sub-package, named resources, below the 
 * class' package, such as `com.my.example.resources.MyClass.properties`.
 * 
 * So, for a `ActionCommandProvider` named "MyMenuItem" located in the package 
 * `com.example.MyMenuItem.java`, the resources file for it would need to be 
 * `com.example.resources.MyMenuItem.properties`.
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
 * # Properties located in a file called MyMenuItem.properties in a sub package 
 * # of the class file's package, called resources.
 * text=MenuItemText
 * icon=/fully/qualified/path/to/icon.png
 * 
 * # OR, for a relative path to a sub-package of the resources sub-package:
 * icon=path/to/icon.png
 * ```
 * 
 * By using the resource injection feature of the NTA Platform, the classes (as 
 * well as the generated `javax.swing.JMenuItem`) will be all set to be 
 * internationalized from the start.
 * 
 * @see #getName()
 * @see #getPosition()
 * @see #getButtonPosition()
 * @see #getTextOverride()
 * @see #getOwner()
 * @see #getMethodName()
 * @see #separatorBefore()
 * @see #separatorAfter()
 * 
 * @author Sean Carrick &lt;sean at gs-unitedlabs dot com&gt;
 *
 * @version 1.0
 * @since 1.0
 */
public interface ActionCommandProvider extends Comparable<ActionCommandProvider> {
    
    /**
     * @deprecated 
     * <em>This method has been deprecated due to changes in the API that allow an
     * `ActionCommandProvider` to provide both a menu item (default) and, 
     * possibly, a toolbar button. The method is marked for removal for the NTA
     * Platform version 1.5, as the Platform will begin using the `getMethodName`
     * value for the `ActionCommandProvider`'s base name.</em>
     * 
     * ***For the time being, we suggest that implementations return `null` or 
     * `getMethodName` from the `getName` method.***
     * 
     * Retrieves the name for the generated `javax.swing.JMenuItem` and 
     * `javax.swing.JButton`. Typically, the name of the menu item is the menu 
     * item text (in lowercase) with the words "MenuItem" appended to the end of 
     * it. However, for `ActionCommandProvider`s, the name is typically the name
     * of the method executed by the action command, with either "MenuItem" or 
     * "Button" appended to it.
     * 
     * Since a method may only return one name, all that needs to be returned by
     * `setName` is the base name of the menu item or button. For example, if 
     * the menu item and button is to exit the application (provided by the NTA 
     * Platform, and implemented as in this example), then all that needs to be 
     * returned from `getName` is "exit":
     * 
     * ```java
     * public class ExitActionCommand implements ActionCommandProvider {
     *     public String getName() {
     *         return "exit";
     *     }
     *     // ... the rest of the class ...
     * }
     * ```
     * 
     * <h4>Example usage
     * ```java
     * public class MyActionCommand implements ActionCommandProvider {
     * 
     *     @Override
     *     public String getName() {
     *         return getMethodName();
     *     }
     * 
     *     // ... the rest of the methods to follow ...
     * 
     * }
     * ```
     * 
     * When the NTA Platform loads these providers, it creates the `JMenuItem`s
     * and `JButton`s dynamically. When creating each of them, it appends the 
     * appropriate "extension" onto the value of `getName`. Here is a part of 
     * the implementation:
     * ```java
     * public class NTApp extends SingleFrameApplication {
     * 
     *     // ... creation and initialization ...
     * 
     *     private void addItemsListToMenu() {
     *         // ... a whole bunch of reflection going on ...
     *         JMenuItem item = new JMenuItem();
     *         item.setName(p.getName() + "MenuItem");
     * 
     *         // ... a lot more stuff happening here ...
     * 
     *         if (p.providesToolbarButton()) {
     *             JButton button = new JButton();
     *             button.setName(p.getName() + "Button");
     *         }
     * 
     *         // ... even more setup and configuration takes place ...
     *     }
     * 
     * }
     * ```
     * 
     * <h4>Example implementation</h4>
     * *To let the `javax.swing.Action` text remain*:
     * ```java
     * public class MyActionCommand implements ActionCommandProvider {
     * 
     *     @Override
     *     public String getTextOverride() {
     *         return null;  
     *         //    ~OR~ 
     *         // return "-1";
     *     }
     * 
     *     // ... the rest of the methods to follow ...
     * 
     * }
     * ```
     * 
     * *To clear the text set by `javax.swing.Action`* (primarily for toolbar buttons):
     * 
     * ```java
     * public class MyActionCommand implements ActionCommandProvider {
     * 
     *     @Override
     *     public String getTextOverride() {
     *         return "";
     *     }
     * 
     *     // ... the rest of the methods to follow ...
     * 
     * }
     * ```
     * 
     * *To provide alternative text, instead of that set by `javax.swing.Action`*:
     * ```java
     * public class MyActionCommand implements ActionCommandProvider {
     * 
     *     @Override
     *     public String getTextOverride() {
     *         return "Alternative Text";
     *     }
     * 
     *     // ... the rest of the methods to follow ...
     * 
     * }
     * ```
     * 
     * As you can see, the NTA Platform appends either "MenuItem" or "Button" 
     * to the end of the return value from `getName()`. Therefore, a proper 
     * implementation only needs `getName` to return the base name of for the 
     * menu item and button.
     * 
     * @return the name for the generated menu item
     */
    @Deprecated
    public String getName();
    
    /**
     * This value allows for the text set by the assigned `javax.swing.Action` 
     * to be overridden. The NTA Platform checks the returned value to see if it
     * is `null` or `-1`. If the value is either of these values, then no action
     * is taken and the `Action`'s text will be used. However, if the text 
     * should be something other than what the `Action`'s properties are, then
     * that value should be returned from this method. For example, to clear the
     * text property from what the `Action` had set, this method should return 
     * an empty string.
     * 
     * @return the text to use to override that set by the assigned 
     * `javax.swing.Action`, `-1` or `null` to allow the `Action` text to remain,
     * or an empty string to clear the text
     */
    public String getTextOverride();
    
    /**
     * The position on the menu that the generated menu item should occupy. It 
     * is important to note that the NTA Platform provides the File: Exit; Edit:
     * Cut, Copy, Paste, Delete; Tools: Options; and Help: Contents, Index, and 
     * About menus and sets their `position` values such that they will always 
     * .be displayed in a specific order on the menu, in compliance with 
     * well-established GUI standards. In other words, no matter the position 
     * that this `ActionCommandProvider` sets for itself, it will never be 
     * placed:
     * 
     * - Exit will always be the bottom-most menu item in the File menu, and the
     * left-most button on the toolbar
     * - Cut, Copy, Paste, and Delete will always be the first four items on the 
     * Edit menu and only allow two buttons (New and Open) between Cut and the 
     * Exit toolbar button
     * - Options will always be the bottom-most menu item in the Tools menu
     * - Contents and Index will always be the first to menu items on the Help 
     * menu, and About will always be the last
     * Those standard menus will always be where they should be according to GUI
     * standards.
     * 
     * NTA Platform standards dictate that menu positions, as well as positions
     * for menu items, sub-menus, and toolbar buttons, should be in increments
     * of `200`. This allows the NTA Platform to "fudge" the position value as 
     * necessary to make sure that all provided items are placed as closely to 
     * their desired position as possible.
     * 
     * <h4>Example implementation</h4>
     * 
     * ```java
     * public class MyActionCommand implements ActionCommandProvider {
     * 
     *     @Override
     *     public int getPosition() {
     *         return 5000;
     *     }
     * 
     *     // ... the rest of the methods to follow ...
     * 
     * }
     * ```
     * 
     * @return the menu item's requested position on the menu
     */
    public int getPosition();
    
    /**
     * The position on the toolbar that the generated button should occupy. It 
     * is important to note that the NTA Platform provides the File: Exit; Edit:
     * Cut, Copy, Paste, Delete; and Help: Contents toolbar buttons and sets 
     * their `position` values such that they will always be displayed in a 
     * specific order on the toolbar, in compliance with well-established GUI 
     * standards. In other words, no matter the position that this 
     * `ActionCommandProvider` sets for itself, it will never be placed:
     * 
     * - Exit will always be the left-most button on the toolbar
     * - Cut, Copy, Paste, and Delete will only allow two buttons (New and Open)
     * between Cut and the Exit toolbar button
     * - Contents will always be the last (right-most) button on the toolbar
     * 
     * Those standard toolbar buttons will always be where they should be 
     * according to GUI standards.
     * 
     * NTA Platform standards dictate that menu positions, as well as positions
     * for menu items, sub-menus, and toolbar buttons, should be in increments 
     * of `200`. This allows the NTA Platform to "fudge" the position value as 
     * necessary to make sure that all provided items are placed as closely to 
     * their desired position as possible.
     * 
     * <h4>Example implementation</h4>
     * ```java
     * public class MyActionCommand implements ActionCommandProvider {
     * 
     *     @Override
     *     public int getButtonPosition() {
     *         return 1600;
     *     }
     * 
     *     // ... the rest of the methods to follow ...
     * 
     * }
     * ```
     * 
     * @return the menu item's requested position on the menu
     */    
    public int getButtonPosition();
    
    /**
     * The name of the menu into which this `ActionCommand` should be placed. 
     * The menu's *name* property value should be used here. If menu the command
     * should be placed into is the "File" menu, then `getOwner` should return 
     * "fileMenu".
     * 
     * ***Note***: The NTA Platform is very standardized in the naming 
     * conventions used for components. The component name is always the 
     * component type, such as `Button`, `Panel`, `Menu`, `MenuItem`, etc.,
     * without the 'J' from the component class name, preceded by the function 
     * of the component. For example, the File menu's name is "fileMenu", the
     * Exit command's name is "exitMenuItem" on the File menu and "exitButton"
     * on the toolbar. The component's function (the lower-case or camel-case 
     * portion) is typically the name of the method that is executed by the 
     * component, for `ActionCommand`s. So, if the `ActionCommand` is to execute
     * a method named `getAllInstances`, then `getOwner` would return the value 
     * "getAllInstances`, which will be prepended to the name of the component 
     * that is created by the NTA Platform. If only a menu item is being created
     * (in other words `providesToolbarButton` returns `false`) then the 
     * generated menu item would have the name `getAllInstancesMenuItem`.
     * 
     * *These standards are strictly followed, and guaranteed to be correct, for
     * all modules created by GS United Labs. We simply ask that third-party 
     * module developers also follow these standards.*
     * 
     * <h4>Example implementation</h4>
     * ```java
     * public class MyActionCommand implements ActionCommandProvider {
     * 
     *     @Override
     *     public String getOwner() {
     *         return "fileMenu";
     *     }
     * 
     *     // ... the rest of the methods to follow ...
     * }
     * ```
     * 
     * @return the name of the method executed by this `ActionCommand`, also 
     * referred to as the component's *base name*
     */
    public String getOwner();
    
    /**
     * Retrieves the name of the method this `ActionCommand` is to execute. The
     * method name returned by this method should be a method decorated by the 
     * `@Action` annotation. The NTA Platform loads all methods decorated by 
     * `@Action` into a `javax.swing.ActionMap` so that individual actions may 
     * be easily located. The name of an `@Action` method is the key in the 
     * `ActionMap` for retrieving the associated `javax.swing.Action`.
     * 
     * Because the NTA Platform uses `Action`s for menu items and toolbar 
     * buttons generated by `ActionCommandProvider`s, the properties are set by
     * the `Action`, instead of needing to be "manually" set by the component 
     * generator.
     * 
     * <h4>Example implementation</h4>
     * ```java
     * public class MyActionCommand implements ActionCommandProvider {
     * 
     *     @Override
     *     public String getMethodName() {
     *         return "myAction";
     *     }
     * 
     *     // ... the rest of the methods to follow ...
     * }
     * ```
     * 
     * @return the name of the `@Action` decorated method this 
     * `ActionCommandProvider` is to execute
     */
    public String getMethodName();
    
    /**
     * Determines whether or not there should be a separator placed on the 
     * `getOwner` menu *prior* to the generated menu item being added. 
     * 
     * The menu item generator checks if the last item added to the menu is a 
     * `javax.swing.JSeparator` ***before*** adding the separator. This prevents
     * the instance where the last added menu item had `separatorAfterMenu` 
     * return `true`, causing multiple separators being added together. In other
     * words, if the last item added to the menu was a `JSeparator`, then the 
     * return from `separatorBeforeMenu` will be ignored.
     * 
     * Also, once the entire menu is generated, and all items have been added to
     * it, the menu item generator checks the first item to see if it is an 
     * instance of `JSeparator`. If it is, it is removed so that no menu has a 
     * separator as the top item. Therefore, even if it is not sure that the 
     * generated menu item will be the first item on the menu, it is still safe 
     * to have `separatorBeforeMenu` return `true`.
     * 
     * @return `true` to place a separator prior to (or above) the generated 
     * menu item
     */
    public boolean separatorBeforeMenu();
    
    /**
     * Determines whether or not there should be a separator placed on the 
     * `getOwner` menu *after* to the generated menu item being added. 
     * 
     * Once the entire menu is generated, and all items have been added to it, 
     * the menu item generator checks to see if the last item on the menu 
     * (`menu.getMenuComponent(menu.getMenuComponentCount() - 1)`) is an 
     * instance of `javax.swing.JSeparator`. If it is, it is removed. Therefore,
     * if it is unsure whether the menu item being provided is the last menu 
     * item in a given menu, `separatorAfterMenu` can still return `true`, with 
     * no worry that the last item on the menu will be a separator.
     * 
     * @return `true` to place a separator after to (or below) the generated 
     * menu item
     */
    public boolean separatorAfterMenu();
    
    /**
     * Determines if a toolbar button will also be generated, along with a menu
     * item. It is very common for a menu item to also have an associated 
     * toolbar button, especially when the action executed by the menu item is 
     * commonly used by the user. The toolbar button provides an easier, quicker
     * access to the menu command.
     * 
     * However, it is rare that a toolbar button does not have a menu item 
     * associated with it. In those rare instances, the 
     * [`ToolbarButtonProvider`]() interface may be used to provide one.
     * 
     * @return `true` if a toolbar button should also be generated for this 
     * `ActionCommandProvider`
     */
    public boolean providesToolbarButton();
    
    public boolean separatorBeforeButton();
    
    public boolean separatorAfterButton();
}
