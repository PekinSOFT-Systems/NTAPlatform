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
 *  Class      :   ExitMenuItemProvider.java
 *  Author     :   Sean Carrick
 *  Created    :   Oct 28, 2021 @ 1:37:45 AM
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
package com.gs.nta;

import com.gs.nta.api.MenuItemProvider;

/**
 *
 * @author Sean Carrick &lt;sean at gs-unitedlabs dot com&gt;
 *
 * @version 1.0.0
 * @since 1.0.0
 */
public class ExitMenuItemProvider implements MenuItemProvider {

    public ExitMenuItemProvider () {

    }

    @Override
    public String getName() {
        return "exitMenuItem";
    }

    @Override
    public String getTextOverride() {
        return "-1";
    }

    @Override
    public int getPosition() {
        return Integer.MAX_VALUE;
    }

    @Override
    public String getOwner() {
        return "fileMenu";
    }

    @Override
    public String getMethodName() {
        return "quit";
    }

    @Override
    public boolean separatorBefore() {
        return true;
    }

    @Override
    public boolean separatorAfter() {
        return false;
    }

    @Override
    public int compareTo(MenuItemProvider o) {
        return Integer.compare(getPosition(), o.getPosition());
    }

}
