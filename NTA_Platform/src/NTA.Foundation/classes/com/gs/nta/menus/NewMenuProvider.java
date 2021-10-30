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
 *  Class      :   NewMenuProvider.java
 *  Author     :   Sean Carrick
 *  Created    :   Oct 28, 2021 @ 1:27:27 AM
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
package com.gs.nta.menus;

import com.gs.nta.api.SubMenuProvider;

/**
 *
 * @author Sean Carrick &lt;sean at gs-unitedlabs dot com&gt;
 *
 * @version 1.0.0
 * @since 1.0.0
 */
public class NewMenuProvider implements SubMenuProvider {

    public NewMenuProvider () {

    }

    @Override
    public String getName() {
        return getText().toLowerCase() + "Menu";
    }

    @Override
    public String getText() {
        return "New";
    }

    @Override
    public String getOwner() {
        return "fileMenu";
    }

    @Override
    public int getPosition() {
        return Integer.MIN_VALUE;
    }

    @Override
    public boolean separatorBefore() {
        return false;
    }

    @Override
    public boolean separatorAfter() {
        return true;
    }

    @Override
    public int compareTo(SubMenuProvider o) {
        return Integer.compare(getPosition(), o.getPosition());
    }

}
