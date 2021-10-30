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
 *  Class      :   AccountingAboutPanelProvider.java
 *  Author     :   Sean Carrick
 *  Created    :   Oct 29, 2021 @ 2:28:35 PM
 *  Modified   :   Oct 29, 2021
 * 
 *  Purpose:     See class JavaDoc comment.
 * 
 *  Revision History:
 * 
 *  WHEN          BY                   REASON
 *  ------------  -------------------  -----------------------------------------
 *  Oct 29, 2021  Sean Carrick         Initial creation.
 * *****************************************************************************
 */
package com.gs.nta.accounting.desktop;

import com.gs.nta.api.AboutPanelProvider;
import com.gs.nta.utils.TerminalErrorPrinter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import org.jdesktop.application.Application;

/**
 *
 * @author Sean Carrick &lt;sean at gs-unitedlabs dot com&gt;
 *
 * @version 1.0.0
 * @since 1.0.0
 */
public class AccountingAboutPanelProvider implements AboutPanelProvider {

    public AccountingAboutPanelProvider () {

    }

    @Override
    public String getTitle() {
        return Application
                .getInstance()
                .getContext()
                // By using the getClass() method to pass the class to the
                //+ getResourceMap() method, copy and paste will be a breeze.
                .getResourceMap(getClass())
                .getString("title");
    }

    @Override
    public ArrayList<String> getContributors() {
        ArrayList<String> list = new ArrayList<>();
        list.add("Sean Carrick: Primary Developer, Illinois, USA");
        list.add("Jiří Kovalský: Primary Tester and Backup Developer,  "
                + "Bohumín, Czech Republic");
        list.add("Kevin Nathan: Contributor, Arizona, USA");
        
        return list;
    }

    @Override
    public String getVendor() {
        return Application
                .getInstance()
                .getContext()
                // By using the getClass() method to pass the class to the
                //+ getResourceMap() method, copy and paste will be a breeze.
                .getResourceMap(getClass())
                .getString("vendor");
    }

    @Override
    public String getCopyright() {
        return Application
                .getInstance()
                .getContext()
                // By using the getClass() method to pass the class to the
                //+ getResourceMap() method, copy and paste will be a breeze.
                .getResourceMap(getClass())
                .getString("copyright");
    }

    @Override
    public ImageIcon getLogo() {
        return Application
                .getInstance()
                .getContext()
                // By using the getClass() method to pass the class to the
                //+ getResourceMap() method, copy and paste will be a breeze.
                .getResourceMap(getClass())
                .getImageIcon("logo");
    }

    @Override
    public URL getLicenseURL() {
        URL url;
        
        try {
            url = new URL("https://www.gnu.org/licenses/");
        } catch (MalformedURLException e) {
            TerminalErrorPrinter.print(e, "Could not create URL for "
                    + "\"https://www.gnu.org/licenses/\"");
            url = null;
        }
        
        return (url != null) ? url : null;
    }

}
