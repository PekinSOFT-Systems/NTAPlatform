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
 *  Class      :   OptionsPanelProvider.java
 *  Author     :   Sean Carrick
 *  Created    :   Oct 27, 2021 @ 10:27:12 AM
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
 * The `@OptionsPanelProvider` annotation is used to decorate a GUI class that
 * provides options that may be set or modified by the user at run-time. This
 * annotation provides the Foundation with the category into which the options
 * panel should be placed. The available categories are detailed by the 
 * `OptionsCategories` enumeration.
 * 
 * @see #categ
 *
 * @author Sean Carrick &lt;sean at gs-unitedlabs dot com&gt;
 *
 * @version 1.0.0
 * @since 1.0.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface OptionsPanelProvider {
    /**
     * The category on the Options dialog into which the decorated panel should
     * be placed. The allowable values are:
     * 
     * - GENERAL
     * - ACCOUNTING
     * - INTERFACE
     * - MISCELLANEOUS
     * 
     * @return The Options category in which to place the decorated panel.
     */
    OptionsCategories category() default OptionsCategories.MISCELLANEOUS;
}
