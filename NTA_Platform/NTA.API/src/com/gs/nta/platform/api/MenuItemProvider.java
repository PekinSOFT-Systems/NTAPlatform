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
 *  Class      :   MenuItemProvider.java
 *  Author     :   Sean Carrick
 *  Created    :   Oct 27, 2021 @ 10:13:36 AM
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

/**
 * The `MenuItemProvider` interface provides a means by which modules can
 * provide menu items for the Platform application dynamically.
 * 
 * The Platform Foundation only provides the main toolbar, but without buttons.
 * Therefore, it is up to each module to provide the menu items that allow
 * access to the module's features.
 * 
 * When a menu item is provided, its name should be set to the name of the
 * method that it will execute, with the word "Button" appended to it, such as
 * "myMethodButton". By strictly following this standard, there should never be
 * a name conflict between buttons provided by various modules. However, just to
 * be certain of this, the Platform Foundation will verify there are no naming
 * conflicts by adding a number at the end of additional buttons that do end up
 * having the same name. For this reason, this interface requires the `setName`
 * method, so that the Foundation can update the provider's information.
 * 
 * Toolbar buttons in the context of the NTA Platform should execute methods 
 * that are decorated with the `@Action` annotation. Therefore, the 
 * `MenuItemProvider` does not provide methods for getting the icon, 
 * mnemonic, or keyboard shortcut, as these are provided by the 
 * `javax.swing.Action` to which the `@Action` annotations are converted by the
 * application's framework. Due to this, all modules that are providing actions
 * <strong><em>must rely upon</em></strong> the Swing Application Framework
 * library.
 * 
 * <dl><dt><strong><em><a name="JSR296"></a>Acquiring the Swing Application 
 * Framework</em></strong></dt>
 * <dd> The Swing Application Framework was written in 2006 by Hans Muller at
 * Sun Microsystems, in response to JSR-296. This library was abandoned by 2007,
 * but GS United Labs has laid hold of it and is using it extensively within most
 * of their projects. At some point in the future, GS United Labs will start
 * updating the original JSR-296 Framework to JDK11 or greater. For now, however,
 * we are using the original Framework.
 * 
 * If you are unable to find this library anywhere else, you can download it
 * from the NTAPlatform GitHub repository at:
 * [https://github.com/GSUnitedLabs/NTAPlatform/](https://github.com/GSUnitedLabs/NTAPlatform/)
 * 
 * Since you will be developing a module for the NTA Platform, you will have 
 * already pulled our API from the Packages section of our repo, so you know
 * where it is and can easily obtain the original JSR-296 Framework library.
 * 
 * For more information, see the Main Page of our API JavaDocs.</dd></dl>
 * 
 * @see #getName() 
 * @see #setName() 
 * @see #getMethodName() 
 * @see #getOwner() 
 * @see #hasSeparatorAfter() 
 * @see #hasSeparatorBefore() 
 *
 * @author Sean Carrick &lt;sean at gs-unitedlabs dot com&gt;
 *
 * @version 1.0.0
 * @since 1.0.0
 */
public interface MenuItemProvider {
    /**
     * Retrieves the value of the `name` property for this `MenuItemProvider`.
     * The `name` needs to follow the convention of adding the word "MenuItem" to
     * the end of the method name, such as `myMethodMenuItem`. To ensure that this
     * is always the case, one could issue the `return` statement like this:
     * 
     * ```java
     * @Override
     * public String getName() {
     *     return getMethodName() + "MenuItem";
     * }
     * ```
     * 
     * By issuing the `return` statement in such a fashion, even if the method
     * name is changed at some point in the future, the name will always match 
     * the method name and follow the Northwind Traders naming convention.
     * 
     * @return The menu item's name property. This will be set on the 
     * `JMenuItem` for the toolbar:
     * 
     * ```java
     * JMenuItem menuItem = new JMenuItem();
     * menuItem.setName(provider.getName());
     * ```
     */
    public String getName();
    
    /**
     * Sets the `MenuItemProvider`'s `name` property. This method will only
     * be invoked in the (rare) event of a naming collision between menu items 
     * provided by multiple modules. The NTA Foundation should rarely need to 
     * invoke this method, provided that the Northwind Traders naming convention
     * is followed.
     * 
     * @param name The new name for the `MenuItemProvider`.
     */
    public void setName(String name);
    
    /**
     * Retrieves the name of the method which this `MenuItemProvider` will
     * invoke when its resulting menu item is clicked. The method named by
     * `methodName` needs to be decorated with the `@Action` annotation provided
     * by the [<em>JSR-296 Swing Application Framework</em>](#JSR296). The method
     * name is the manner by which the Foundation is able to set the `action`
     * property on the menu item. In turn, the `action` property defines the rest
     * of the menu item's properties, such as icon, text, mnemonic, keyboard
     * shortcut, etc.
     * 
     * @return The name of the method which this menu item will invoke.
     */
    public String getMethodName();
    
    /**
     * The `owner` property allows a menu item to tell the Foundation into which
     * menu it should be placed. Following Northwind Traders naming conventions,
     * this value should be the text of the menu with "Menu" appended to it. For
     * example, if this menu item should be placed in the Tools menu, `getOwner`
     * should return "toolsMenu".
     * 
     * @return The name of the application menu into which this menu item should
     * be placed.
     */
    public String getOwner();
    
    /**
     * If this `MenuItemProvider` would like to have a separator placed before
     * it on the owner menu, then `hasSeparatorBefore` should return `true`.
     * Otherwise it should return `false`.
     * 
     * @return Whether or not the menu item should have a separator placed above
     * it on the owner menu.
     */
    public boolean hasSeparatorBefore();
    
    /**
     * If this `MenuItemProvider` would like to have a separator placed after it
     * on the owner menu, then `hasSeparatorAfter` should return `true`. 
     * Otherwise it should return `false`.
     * 
     * @return Whether or not the menu item should have a separator placed below
     * it on the owner menu.
     */
    public boolean hasSeparatorAfter();
}
