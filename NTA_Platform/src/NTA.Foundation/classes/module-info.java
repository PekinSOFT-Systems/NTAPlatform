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
 *  Class      :   module-info.java
 *  Author     :   Sean Carrick
 *  Created    :   Aug 14, 2021 @ 10:45:38 PM
 *  Modified   :   Aug 14, 2021
 * 
 *  Purpose:     See class JavaDoc comment.
 * 
 *  Revision History:
 * 
 *  WHEN          BY                   REASON
 *  ------------  -------------------  -----------------------------------------
 *  Aug 14, 2021  Sean Carrick         Initial creation.
 * *****************************************************************************
 */

open module NTA.Foundation {
    // JDK Requirements
    requires java.base;
    requires java.desktop;
    requires java.logging;
    
    // Project Requirements
    requires appframework;
    requires swing.worker;
    
    // Uses Statements
    uses com.gs.nta.api.AboutPanelProvider;
    uses com.gs.nta.api.ActionCommandProvider;
    uses com.gs.nta.api.MenuProvider;
    uses com.gs.nta.api.ModuleRegistrar;
    uses com.gs.nta.api.OptionsPanelProvider;
    uses com.gs.nta.api.SubMenuProvider;
    uses com.gs.nta.api.ToolbarButtonProvider;
    
    // Exports Packages
    exports com.gs.nta;
    exports com.gs.nta.api;
    exports com.gs.nta.utils;
    
    // Provides Packages
    provides com.gs.nta.api.ActionCommandProvider with com.gs.nta.menus.ExitActionCommand,
            com.gs.nta.menus.OptionsActionCommand, com.gs.nta.menus.AboutActionCommand;
    provides com.gs.nta.api.MenuProvider with com.gs.nta.menus.FileMenuProvider,
            com.gs.nta.menus.EditMenuProvider, com.gs.nta.menus.HelpMenuProvider,
            com.gs.nta.menus.ToolsMenuProvider, com.gs.nta.menus.ViewMenuProvider;
    provides com.gs.nta.api.OptionsPanelProvider with com.gs.nta.desktop.panels.ProxyOptionsPanel;
    provides com.gs.nta.api.SubMenuProvider with com.gs.nta.menus.NewMenuProvider;
}
