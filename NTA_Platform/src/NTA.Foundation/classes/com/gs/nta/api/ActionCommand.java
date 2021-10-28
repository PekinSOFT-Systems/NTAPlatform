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
 *  Class      :   ActionCommand.java
 *  Author     :   Sean Carrick
 *  Created    :   Oct 27, 2021 @ 9:41:15 PM
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
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author Sean Carrick &lt;sean at gs-unitedlabs dot com&gt;
 *
 * @version 1.0.0
 * @since 1.0.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ActionCommand {
    /**
     * The `methodName` property for this `@ActionCommand` decoration is the 
     * name of the method being decorated. 
     * 
     * The value of this property is used to get the `javax.swing.Action` from
     * the application's `ActionMap`. The value of this property is also used to
     * set the name of the toolbar button, menu item, or both.
     * 
     * @return the name of the `@Action` decorated method that this 
     * `@ActionCommand` also decorates
     */
    String methodName();
    
    /**
     * Determines whether this `@ActionCommand` provides a toolbar button.
     * 
     * @return whether or not a toolbar button should be created
     */
    boolean toolButton() default false;
    
    /**
     * Determines whether this `@ActionCommand` provides a menu item.
     * 
     * @return whether or not a menu item should be created
     */
    boolean menuItem() default true;
    
    /**
     * The path to the small icon for this `@ActionCommand`. The small icon is
     * used on the menu item, if one is created.
     * 
     * @return the path to the small icon for the menu item
     */
    String smallIconPath() default "";
    
    /**
     * The path to the large icon for this `@ActionCommand`. The large icon is
     * used on the toolbar button, if one is created.
     * @return 
     */
    String largeIconPath() default "";
    
    /**
     * The text to use on the toolbar button instead of the text provided by
     * the `javax.swing.Action` that is assigned to it. If it is desired to use
     * the text from the `javax.swing.Action`, then this property does not need
     * to be set. If it is desired that the toolbar button has no text, then set
     * this property to an empty string. Otherwise, set this property to the
     * text that should be displayed.
     * 
     * @return the text to override the `javax.swing.Action` text
     */
    String textOverride() default "-1";
    
    /**
     * The single character to use as the mnemonic letter for the menu item 
     * and/or toolbar button. This is the character in the text that shows up as
     * underlined.
     * 
     * @return the mnemonic character
     */
    String mnemonic() default "";
    
    /**
     * The index number of the character to use as the mnemonic letter for the
     * menu item and/or toolbar button. This is the absolute position of the 
     * desired mnemonic letter. For example, if the text is "Yellowy" if only 
     * the mnemonic character is set to 'Y', the the first 'Y' will be the 
     * mnemonic character. However, if it is desired that the second 'y' be the
     * mnemonic, then this property would need to be set to 6, which will cause
     * the lowercase 'y' to be the mnemonic.
     * 
     * @return the absolute position of the mnemonic letter in the text
     */
    int mnemonicIndex() default -1;
    
    /**
     * The key sequence that should be used as a shortcut for this 
     * `@ActionCommand`. This key sequence should be entered as
     * `ctrl pressed Y`, for example. If the keyboard shortcut involves more 
     * than one non-letter/numeric key, such as the CTRL+SHIFT+Y, then the
     * shortcut should be entered as `shift ctrl pressed Y`.
     * 
     * @return a string representation of the keyboard shortcut
     */
    String shortcut() default "";
}
