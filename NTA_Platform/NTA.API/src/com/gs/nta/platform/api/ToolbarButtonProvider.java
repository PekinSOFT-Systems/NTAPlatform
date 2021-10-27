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
 *  Class      :   ToolbarButtonProvider.java
 *  Author     :   Sean Carrick
 *  Created    :   Oct 27, 2021 @ 8:36:59 AM
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
 * The `ToolbarButtonProvider` interface provides a means by which modules can
 * provide toolbar buttons for the Platform application dynamically.
 * 
 * The Platform Foundation only provides the main toolbar, but without buttons.
 * Therefore, it is up to each module to provide the toolbar buttons that allow
 * access to the module's features.
 * 
 * When a toolbar button is provided, its name should be set to the name of the
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
 * `ToolbarButtonProvider` does not provide methods for getting the icon, 
 * mnemonic, or keyboard shortcut, as these are provided by the 
 * `javax.swing.Action` to which the `@Action` annotations are converted by the
 * application's framework. Due to this, all modules that are providing actions
 * <strong><em>must rely upon</em></strong> the Swing Application Framework
 * library.
 * 
 * <dl><dt><strong><em><a name="JSR296"></a>Acquiring the Swing Application 
 * Framework</em></stron></dt>
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
 * @see #methodName() 
 * @see #isAction() 
 *
 * @author Sean Carrick &lt;sean at gs-unitedlabs dot com&gt;
 *
 * @version 1.0.0
 * @since 1.0.0
 */
public interface ToolbarButtonProvider {
    
    /**
     * Retrieves the value of the `name` property for this `ToolbarButtonProvider`.
     * The `name` needs to follow the convention of adding the word "Button" to
     * the end of the method name, such as `myMethodButton`. To ensure that this
     * is always the case, one could issue the `return` statement like this:
     * 
     * ```java
     * @Override
     * public String getName() {
     *     return getMethodName() + "Button";
     * }
     * ```
     * 
     * By issuing the `return` statement in such a fashion, even if the method
     * name is changed at some point in the future, the name will always match 
     * the method name and follow the Northwind Traders naming convention.
     * 
     * @return The toolbar button's name property. This will be set on the 
     * `JButton` for the toolbar:
     * 
     * ```java
     * JButton button = new JButton();
     * button.setName(provider.getName());
     * ```
     */
    public String getName();
    
    /**
     * Sets the `ToolbarButtonProvider`'s `name` property. This method will only
     * be invoked in the (rare) event of a naming collision between buttons 
     * provided by multiple modules. The NTA Foundation should rarely need to 
     * invoke this method, provided that the Northwind Traders naming convention
     * is followed.
     * 
     * @param name The new name for the `ToolbarButtonProvider`.
     */
    public void setName(String name);
    
    /**
     * Retrieves the name of the method which this `ToolbarButtonProvider` will
     * invoke when its resulting toolbar button is clicked. The method named by
     * `methodName` needs to be decorated with the `@Action` annotation provided
     * by the [<em>JSR-296 Swing Application Framework</em>](#JSR296). The method
     * name is the manner by which the Foundation is able to set the `action`
     * property on the button. In turn, the `action` property defines the rest
     * of the button's properties, such as icon, text, mnemonic, keyboard
     * shortcut, etc.
     * 
     * @return The name of the method which this toolbar button will invoke.
     */
    public String methodName();
    
    /**
     * The `textOverride` property allows a toolbar button to override the text
     * value that is set by the button's `action` property. Typically, this 
     * property is used to remove the text from the toolbar button by returning
     * an empty string.
     * 
     * @return The text to use for the button instead of the `action` property's
     * text value.
     */
    public String textOverride();
    
    /**
     * If this `ToolbarButtonProvider` would like to have a separator placed before
     * it on the toolbar, then `hasSeparatorBefore` should return `true`.
     * Otherwise it should return `false`.
     * 
     * @return Whether or not the menu item should have a separator placed left
     * of it on the toolbar.
     */
    public boolean hasSeparatorBefore();
    
    /**
     * If this `ToolbarButtonProvider` would like to have a separator placed after it
     * on the toolbar, then `hasSeparatorAfter` should return `true`. 
     * Otherwise it should return `false`.
     * 
     * @return Whether or not the menu item should have a separator placed right
     * of it on the toolbar.
     */
    public boolean hasSeparatorAfter();
}
