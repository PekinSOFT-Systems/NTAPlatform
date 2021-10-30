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

import java.util.Comparator;

/**
 *
 * @author Sean Carrick &lt;sean at gs-unitedlabs dot com&gt;
 *
 * @version 1.0.0
 * @since 1.0.0
 */
public interface ActionCommandProvider extends Comparable<ActionCommandProvider> {
    public String getName();
    
    public String getTextOverride();
    
    public int getPosition();
    
    public int getButtonPosition();
    
    public String getOwner();
    
    public String getMethodName();
    
    public boolean separatorBeforeMenu();
    
    public boolean separatorAfterMenu();
    
    public boolean providesToolbarButton();
    
    public boolean separatorBeforeButton();
    
    public boolean separatorAfterButton();
}
