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
 *  Class      :   ManageMenuProvider.java
 *  Author     :   Sean Carrick
 *  Created    :   Oct 28, 2021 @ 2:57:43 PM
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
package com.gs.nta.accounting.menus;

import com.gs.nta.api.MenuProvider;
import org.jdesktop.application.Application;

/**
 *
 * @author Sean Carrick &lt;sean at gs-unitedlabs dot com&gt;
 *
 * @version 1.0.0
 * @since 1.0.0
 */
public class ManageMenuProvider implements MenuProvider {

    public ManageMenuProvider () {

    }

    @Override
    public String getName() {
        return "accountingMenu";
    }

    @Override
    public int getPosition() {
        return 20000;
    }

    @Override
    public int compareTo(MenuProvider o) {
        return Integer.compare(getPosition(), o.getPosition());
    }

    @Override
    public String getText() {
        return Application
                .getInstance()
                .getContext()
                .getResourceMap(getClass())
                .getString("text");
    }

}
