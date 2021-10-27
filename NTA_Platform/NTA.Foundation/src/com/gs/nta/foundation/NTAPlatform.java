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
 *  Class      :   NTAPlatform.java
 *  Author     :   Sean Carrick
 *  Created    :   Oct 27, 2021 @ 7:53:31 AM
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
package com.gs.nta.foundation;

import java.awt.MenuBar;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JToolBar;
import org.jdesktop.application.SingleFrameApplication;

/**
 *
 * @author Sean Carrick &lt;sean at gs-unitedlabs dot com&gt;
 *
 * @version 1.0.0
 * @since 1.0.0
 */
public class NTAPlatform extends SingleFrameApplication {

    private boolean running;
    private MenuBar menuBar;
    private JToolBar toolBar;
    private JPanel mainPanel;
    private JPanel statusPanel;
    private JLabel statusMessageLabel;
    private JLabel animatedIconLabel;
    private JProgressBar progressBar;
    
    public NTAPlatform () {
        running = false;
    }
    
    @Override
    protected void initialize(String[] args) {
        
    }

    @Override
    protected void startup() {
        show(getMainView());
    }
    
    @Override
    protected void ready() {
        running = true;
    }
    
    public boolean isRunning() {
        return running;
    }

}
