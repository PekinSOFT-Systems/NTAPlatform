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
 *  Class      :   NTApp.java
 *  Author     :   Sean Carrick
 *  Created    :   Aug 14, 2021 @ 11:31:57 PM
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
package com.gs.nta;

import com.gs.nta.api.ActionCommandProvider;
import com.gs.nta.api.MenuProvider;
import com.gs.nta.api.SubMenuProvider;
import com.gs.nta.api.ToolbarButtonProvider;
import com.gs.nta.desktop.AboutDialog;
import com.gs.nta.desktop.MainFrame;
import com.gs.nta.desktop.OptionsDialog;
import com.gs.nta.desktop.SplashScreen;
import com.gs.nta.utils.ArgumentParser;
import com.gs.nta.utils.LogRecord;
import com.gs.nta.utils.Logger;
import com.gs.nta.utils.MessageBox;
import com.gs.nta.utils.Properties;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import static org.jdesktop.application.Application.getInstance;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.Task;

/**
 * The top-level class of the Northwind Traders Basic Edition application.
 * <p>
 * The application accepts command line parameters for setting the logging level
 * and for determining if the application is being developed. The valid command
 * line parameters are:</p>
 * <dl><dt>{@code -i} ~or~ {@code --devel}</dt>
 * <dd>Places the application into developer mode. In this mode, the logging
 * level is set to {@code Logger.DEBUG}, which is the most verbose logging
 * level. Furthermore, the registration process is bypassed for developers.
 * </dd><dt>{@code --level=[LoggingLevel}</dt>
 * <dd>Provides the level for the logging function for the current run of the
 * application. The valid logging levels are listed below.</dd>
 * <dt>{@code -f} ~or~ {@code --fancy}</dt>
 * <dd>Tells the application to break logged message lines at the 80 character
 * mark in the log files. If this parameter is not present, then the logged
 * messages are written to the log file as they are sent to the logger.</dd>
 * </dl>
 * <p>
 * For the {@code --level} and {@code -l} command line parameters, these are the
 * only valid logging levels to supply:</p>
 * <dl><dt>{@code off}</dt><dd>Turns the logging functionality off. Note,
 * however, that critical errors are still logged, even when logging is off.
 * Critical errors are those that cause the application to exit abnormally.</dd>
 * <dt>{@code debug}</dt><dd>This is the most verbose logging level available.
 * At this level, all messages sent to the logger are written to the log file,
 * and printed to the system terminal.</dd>
 * <dt>{@code config}</dt><dd>This allows all messages regarding configuration
 * and higher to be written to the log file and the system terminal.</dd>
 * <dt>{@code info}</dt><dd><em>Default Logging Level.</em> This level only
 * allows those messages logged at the informational level or higher to be
 * written to the log file and printed to the system terminal.</dd>
 * <dt>{@code warn}</dt><dd>This allows only warning messages and higher to be
 * written to the log file and printed to the system terminal.</dd>
 * <dt>{@code error}</dt><dd>At this level, only error messages and critical
 * error messages are written to the log file and printed to the system
 * terminal.</dd>
 * <dt>{@code critical}</dt><dd>At this level, the only messages being written
 * to the log file and system terminal are those that cause the application to
 * exit abnormally.</dd>
 * <dl>
 *
 * @author Sean Carrick &lt;sean at gs-unitedlabs dot com&gt;
 *
 * @version 0.1.0
 * @since 0.1.0
 */
public class NTApp extends SingleFrameApplication {

    private Properties props;
    private Logger logger;
    private final LogRecord record = new LogRecord(NTApp.class.getSimpleName());
    private MainFrame mainFrame;
    private List<MenuProvider> menuList;
    private HashMap<Comparable, String> menuItems;
    private boolean running;

    protected NTApp() {
        setApplicationRunning(false);
    }

    public boolean isApplicationRunning() {
        return running;
    }

    public void setApplicationRunning(boolean running) {
        boolean oldValue = isApplicationRunning();
        this.running = running;
        firePropertyChange("applicationRunning", oldValue, isApplicationRunning());
    }

    @Override
    protected void initialize(String[] args) {
        props = new Properties(getContext());
        logger = Logger.getLogger(this, Logger.TRACE);
        logger.setFormattedOutput(true);
        mainFrame = new MainFrame(this);
        parseArguments(args);
        loadMainMenuItems();
        loadSubMenus();
        loadMenuItems();
        buildMenuSystem();
//        Task startup = new StartupTask(this);
//        startup.execute();
    }

    @Override
    protected void startup() {
        mainFrame.getFrame().addWindowListener(new MainFrameAdapter());
        mainFrame.getFrame().setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        mainFrame.getFrame().setIconImage(getContext()
                .getResourceMap(MainFrame.class)
                .getImageIcon("Form.iconImage")
                .getImage());
        mainFrame.getFrame().pack();
        show(mainFrame);
    }
    
    @Override
    protected void ready() {
        this.running = true;
    }

    @Override
    protected void shutdown() {
        record.setInstant(Instant.now());
        record.setSourceMethodName("shutdown");
        record.setParameters(null);
        record.setThreadID(Thread.currentThread().getId());
        record.setMessage("Commencing shutdown procedures.");
        logger.enter(record);
        
        // TODO: Perform all application cleanup here.
        props.storeProperties();
        

        
        record.setInstant(Instant.now());
        record.setSourceMethodName("shutdown");
        record.setParameters(null);
        record.setThreadID(Thread.currentThread().getId());
        record.setMessage("Completed shutdown procedures. "
                + "Calling super.shutdown() and exiting NTA.");
        logger.exit(record);
        // Call the super.shutdown() in order to shutdown any running tasks.
        super.shutdown();
    }

    @Action
    public void showOptionsDialog() {
        OptionsDialog options = new OptionsDialog(mainFrame.getFrame(), true);
        options.pack();
        show(options);
    }
    
    @Action
    public void showAboutDialog() {
        AboutDialog about = new AboutDialog(mainFrame.getFrame(), true);
        about.pack();
        show(about);
    }
    
    /**
     * Retrieves a copy of the application properties. This allows other modules
     * to be able to discover properties of the application, as well as set the
     * properties they need to set.
     * 
     * @return the application properties object.
     */
    public Properties getProperties() {
        return props;
    }

    //<editor-fold defaultstate="collapsed" desc="private void parseArguments()">
    private void parseArguments(String[] args) {
        record.setInstant(Instant.now());
        record.setSourceMethodName("parseArguments");
        record.setParameters(args);
        record.setThreadID(Thread.currentThread().getId());
        record.setMessage("Preparing to parse the command-line arguments, if any.");
        logger.enter(record);

        // Initialize a parser object.
        ArgumentParser parser = new ArgumentParser(args);

        record.setInstant(Instant.now());
        record.setParameters(null);
        record.setMessage("Checking for development environment.");
        logger.debug(record);
        if (parser.isSwitchPresent("-i") || parser.isSwitchPresent("--ide")) {
            record.setInstant(Instant.now());
            record.setMessage("Running in the IDE. Setting properties appropriately.");
            logger.config(record);

            props.setRuntimeProperty("development.mode", true);
            props.setRuntimeProperty("logger.level", Logger.TRACE);
            logger.setLevel(Logger.TRACE);
        } else {
            props.setRuntimeProperty("development.mode", false);
            props.setRuntimeProperty("logger.level", Logger.WARN);
        }

        record.setInstant(Instant.now());
        record.setMessage("\"development.mode\" = "
                + props.getPropertyAsBoolean("development.mode")
                + "\n\"logger.level\" = "
                + props.getPropertyAsInteger("logger.level"));
        logger.config(record);

        String arg = null;
        if (!props.getPropertyAsBoolean("development.mode")) {
            if (parser.isSwitchPresent("-l")) {
                arg = "-l";
            } else if (parser.isSwitchPresent("--level")) {
                arg = "--level";
            }

            if (arg != null) {
                String level = parser.getSwitchValue(arg);

                switch (level.toLowerCase()) {
                    case "development":
                    case "developing":
                    case "ide":
                    case "programming":
                        props.setRuntimeProperty("logger.level", Logger.TRACE);
                        logger.setLevel(Logger.TRACE);
                        break;
                    case "debugging":
                    case "debug":
                    case "dbg":
                        props.setRuntimeProperty("logger.level", Logger.DEBUG);
                        logger.setLevel(Logger.DEBUG);
                        break;
                    case "configuration":
                    case "configure":
                    case "config":
                    case "cfg":
                        props.setRuntimeProperty("logger.level", Logger.CONFIG);
                        logger.setLevel(Logger.CONFIG);
                        break;
                    case "informational":
                    case "information":
                    case "inform":
                    case "info":
                    case "inf":
                        props.setRuntimeProperty("logger.level", Logger.INFO);
                        logger.setLevel(Logger.INFO);
                        break;
                    case "warning":
                    case "warn":
                        props.setRuntimeProperty("logger.level", Logger.WARN);
                        logger.setLevel(Logger.WARN);
                        break;
                    case "error":
                    case "err":
                        props.setRuntimeProperty("logger.level", Logger.ERROR);
                        logger.setLevel(Logger.ERROR);
                        break;
                    case "critical":
                    case "severe":
                        props.setRuntimeProperty("logger.level", Logger.CRITICAL);
                        logger.setLevel(Logger.CRITICAL);
                        break;
                    default:
                        props.setRuntimeProperty("logger.level", Logger.OFF);
                        logger.setLevel(Logger.OFF);
                }
            }
        }

        // See if the formatted logs switch is present.
        arg = null;
        if (parser.isSwitchPresent("-f")) {
            arg = "-f";
        } else if (parser.isSwitchPresent("--format")) {
            arg = "--format";
        } else if (parser.isSwitchPresent("--format-logs")) {
            arg = "--format-logs";
        } else if (parser.isSwitchPresent("--formatted")) {
            arg = "--formatted";
        } else if (parser.isSwitchPresent("--formatted-logs")) {
            arg = "--formatted-logs";
        }

        if (arg != null) {
            props.setRuntimeProperty("logs.formatted", true);
        } else {
            props.setRuntimeProperty("logs.formatted", false);
        }
    } // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="private void buildMainMenu()">
    private void loadMainMenuItems() {
        ServiceLoader<MenuProvider> menuLoader = ServiceLoader.load(MenuProvider.class);
        menuList = new ArrayList<>();

        if (menuLoader != null) {
            for (MenuProvider m : menuLoader) {
                menuList.add(m);
            }
        }

        Collections.sort(menuList);
    } // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="private void loadSubMenus()">
    private void loadSubMenus() {
        ServiceLoader<SubMenuProvider> loader = ServiceLoader.load(SubMenuProvider.class);
        menuItems = new HashMap<>();

        if (loader != null) {
            for (SubMenuProvider s : loader) {
                menuItems.put((Comparable) s, s.getOwner());
            }
        }
    } // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="private void loadMenuItems()">
    private void loadMenuItems() {
        ServiceLoader<ActionCommandProvider> loader = ServiceLoader.load(ActionCommandProvider.class);

        if (loader != null) {
            for (ActionCommandProvider m : loader) {
                menuItems.put(m, m.getOwner());
            }
        }

        menuItems = sortMenuItemsByValue();
    } //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="private void buildMenuSystem()">
    private void buildMenuSystem() {
        // First, add the top-level menus
        buildMainMenu();

        // Then, add the submenus and menu items
        recursivelyAddSubMenusAndMenuItems();
    } // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="private void buildMainMenu()">
    private void buildMainMenu() {
        record.setInstant(Instant.now());
        record.setMessage(String.format("Commencing building the main menu "
                + "using the list %s", menuList));
        record.setParameters(null);
        record.setSourceMethodName("buildMainMenu");
        record.setThreadID(Thread.currentThread().getId());
        logger.enter(record);

        menuList.forEach(m -> {
            record.setMessage(String.format("Preparing to build a JMenu for %s", m));
            record.setInstant(Instant.now());
            logger.debug(record);
            JMenu menu = new JMenu();
            menu.setName(m.getName());
            menu.setText(m.getText());
            record.setMessage(String.format("Created the menu %s for "
                    + "MenuProvider %s", menu, m));
            record.setInstant(Instant.now());
            logger.debug(record);
            getContext().getResourceMap().injectComponent(menu);
            mainFrame.getMenuBar().add(menu);
            record.setMessage(String.format("Added JMenu %s to "
                    + "mainFrame.getMenuBar() %s", menu, mainFrame.getMenuBar()));
            record.setInstant(Instant.now());
            logger.debug(record);
        });

        record.setInstant(Instant.now());
        record.setMessage("Completed building the main menu bar.");
        logger.exit(record);
    } // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="private void recursivelyAddSubMenusAndMenuItems()">
    private void recursivelyAddSubMenusAndMenuItems() {
        record.setInstant(Instant.now());
        record.setSourceMethodName("recursivelyAddSubMenusAndMenuItems");
        record.setMessage(String.format("Commencing recursively adding the menu "
                + "items to the main menu using menuItems: %s", menuItems));
        record.setThreadID(Thread.currentThread().getId());
        logger.enter(record);

        // We know that the menuItems map has already been sorted after the menu
        //+ items were added (submenus were added first). Therefore, all of the
        //+ submenus and menu items should be grouped by menu name. So, we need
        //+ to get a list of all submenus and menu items that belong in the same
        //+ menu, then sort that list...
        List<Comparable> items = new ArrayList<>();
        String currentMenu = null;
        String lastMenu = null;

        Set<Comparable> keys = menuItems.keySet();

        for (Comparable key : keys) {
            if (currentMenu == null) {
                // Store the current menu name.
                currentMenu = menuItems.get(key);
                // REMEMBER, the provider instance is the key and the menu name
                //+ is the value.
            }

            record.setInstant(Instant.now());
            record.setMessage(String.format("Verifying that currentMenu (%s) is "
                    + "the same as menuItems.get(key [%s]) (%s)", currentMenu,
                    key, menuItems.get(key)));
            logger.debug(record);

            // Verify that the current key's value is the same as currentMenu.
            if (currentMenu.equals(menuItems.get(key))) {
                record.setInstant(Instant.now());
                record.setMessage(String.format("Adding key (%s) to items (%s)",
                        key, items));
                record.setThreadID(Thread.currentThread().getId());
                logger.debug(record);
                // Add the comparable to the list.
                items.add(key);

                record.setInstant(Instant.now());
                record.setMessage(String.format("Key (%s) added to items (%s)",
                        key, items));
                logger.debug(record);

                // Store the currentMenu for later use.
                lastMenu = currentMenu;
            } else {
                // We have added all of the items to a single menu, so break.
                break;
            }
        }

        Iterator<Comparable> it = keys.iterator();

        record.setInstant(Instant.now());
        record.setMessage(String.format("Iterating over keys iterator (%s)", keys));
        logger.debug(record);
        while (it.hasNext()) {
            Comparable c = it.next();

            record.setInstant(Instant.now());
            record.setMessage(String.format("Checking to see if lastMenu (%s) "
                    + "equals menuItems.get(c) (%s), which is %s", lastMenu,
                    menuItems.get(c), lastMenu.equals(menuItems.get(c))));
            logger.debug(record);
            if (lastMenu.equals(menuItems.get(c))) {
                record.setInstant(Instant.now());
                record.setMessage(String.format("Removing c (%s) from the "
                        + "iterator (%s)", c, it));
                logger.debug(record);
                it.remove();
            }
        }

        record.setInstant(Instant.now());
        record.setThreadID(Thread.currentThread().getId());
        record.setMessage(String.format("Calling addItemsListToMenu(%s, %s)",
                items, currentMenu));
        // Now, we can add these items to the menu they have requested.
        addItemsListToMenu(items, currentMenu);

        record.setInstant(Instant.now());
        record.setSourceMethodName("recursivelyAddSubMenusAndMenuItems");
        record.setMessage(String.format("Checking if menuItems is empty: %s",
                menuItems.isEmpty()));
        logger.debug(record);
        // Now that a single menu is complete, recurse to do the rest of the
        //+ menus. However, only recurse if the original menuItems map is not
        //+ empty.
        if (!menuItems.isEmpty()) {
            record.setInstant(Instant.now());
            record.setMessage("Recursing this method.");
            logger.exit(record);
            recursivelyAddSubMenusAndMenuItems();
        }

        record.setInstant(Instant.now());
        record.setMessage("Done adding sub menus and menu items to the menu system.");
        logger.exit(record);
    } // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="private void addItemsListToMenu(ArrayList items)">
    private void addItemsListToMenu(List<Comparable> list, String menuName) {
        record.setSourceMethodName("addItemsListToMenu");
        record.setParameters(new Object[]{list, menuName});
        record.setMessage(String.format("Calling sortList(%s)", list));
        logger.enter(record);

        // Since we have received an unsorted list of items, we need to sort it.
        list = sortList(list);

        record.setInstant(Instant.now());
        record.setSourceMethodName("addItemsListToMenu");
        record.setParameters(null);
        record.setMessage(String.format("Looping through all of the menus to "
                + "add the list items to the appropriate menu. Outer loop will "
                + "run %s times", mainFrame.getMenuBar().getMenuCount()));
        logger.debug(record);
        // With sorted list in hand, we can loop through the menus to find the
        //+ correct one.
        try {
            boolean separatorJustAdded = false;
            for (int x = 0; x < mainFrame.getMenuBar().getMenuCount(); x++) {
                record.setInstant(Instant.now());
                record.setMessage(String.format("Starting iteration %s of outer "
                        + "loop", x));
                logger.debug(record);

                record.setInstant(Instant.now());
                record.setMessage(String.format("Checking if menuName (%s) equals "
                        + "mainFrame.getMenuBar().getMenu(x).getName() (%s) = %s",
                        menuName, mainFrame.getMenuBar().getMenu(x).getName(),
                        menuName.equals(mainFrame.getMenuBar().getMenu(x).getName())));
                logger.debug(record);
                if (menuName.equals(mainFrame.getMenuBar().getMenu(x).getName())) {
                    // We have a winner! Let's add the items.
                    record.setInstant(Instant.now());
                    record.setMessage(String.format("Entering the inner loop, which "
                            + "will run %s times.", list.size()));
                    logger.debug(record);
                    int loopCounter = 0; // for logging purposes only.
                    for (Comparable c : list) {
                        record.setInstant(Instant.now());
                        record.setMessage(String.format("Starting iteration %s of "
                                + "inner loop.", ++loopCounter));
                        logger.debug(record);

                        record.setInstant(Instant.now());
                        record.setMessage(String.format("Checking if c (%s) "
                                + "instanceof SubMenuProvider: %s\nor instanceof "
                                + "ActionCommandProvider: %s", c,
                                c instanceof SubMenuProvider,
                                c instanceof ActionCommandProvider));
                        logger.debug(record);
                        if (c instanceof SubMenuProvider) {
                            SubMenuProvider p = (SubMenuProvider) c;

                            record.setInstant(Instant.now());
                            record.setMessage(String.format("p.separatorBefore() "
                                    + "= %s", p.separatorBefore()));
                            logger.debug(record);
                            if (p.separatorBefore()) {
                                record.setInstant(Instant.now());
                                record.setMessage("Adding a separator to the menu bar.");
                                logger.debug(record);
                                if (!separatorJustAdded && mainFrame.getMenuBar().getMenu(x).getMenuComponentCount() > 1) {
                                    mainFrame.getMenuBar().getMenu(x).addSeparator();
                                    separatorJustAdded = true;
                                }
                            }

                            record.setInstant(Instant.now());
                            record.setMessage(String.format("Creating a JMenu for p "
                                    + "(%s)", p));
                            logger.debug(record);
                            JMenu menu = new JMenu();
                            menu.setName(p.getName());
                            menu.setText(p.getText());

                            record.setInstant(Instant.now());
                            record.setMessage(String.format("Adding the menu (%s) "
                                    + "to the menu bar.", menu));
                            logger.debug(record);
                            mainFrame.getMenuBar().getMenu(x).add(menu);
                            separatorJustAdded = false;

                            record.setInstant(Instant.now());
                            record.setMessage(String.format("p.separatorAfter() = %s",
                                    p.separatorAfter()));
                            logger.debug(record);
                            if (p.separatorAfter()) {
                                record.setInstant(Instant.now());
                                record.setMessage("Adding a separator to the menu bar.");
                                logger.debug(record);
                                if (!separatorJustAdded) {
                                    mainFrame.getMenuBar().getMenu(x).addSeparator();
                                    separatorJustAdded = true;
                                }
                            }
                        } else if (c instanceof ActionCommandProvider) {
                            ActionCommandProvider p = (ActionCommandProvider) c;

                            record.setInstant(Instant.now());
                            record.setMessage(String.format("p.separatorBeforeMenu() "
                                    + "= %s", p.separatorBeforeMenu()));
                            logger.debug(record);
                            if (p.separatorBeforeMenu() && mainFrame.getMenuBar().getMenuCount() >= 1) {
                                record.setInstant(Instant.now());
                                record.setMessage("Adding a separator to the menu bar.");
                                logger.debug(record);
                                if (!separatorJustAdded) {
                                    mainFrame.getMenuBar().getMenu(x).addSeparator();
                                    separatorJustAdded = true;
                                }
                            }

                            record.setInstant(Instant.now());
                            record.setMessage(String.format("Creating a JMenuItem for p "
                                    + "(%s)", p));
                            logger.debug(record);
                            JMenuItem item = new JMenuItem();
                            item.setName(p.getName() + "MenuItem");
                            item.setAction(getInstance(Application.class).getContext().getActionManager().getActionMap().get(p.getMethodName()));
                            if (p.getTextOverride() != null 
                                    && !"-1".equals(p.getTextOverride())) {
                                item.setText(p.getTextOverride());
                            }

                            record.setInstant(Instant.now());
                            record.setMessage(String.format("Adding the item (%s) "
                                    + "to the menu bar.", item));
                            logger.debug(record);
                            mainFrame.getMenuBar().getMenu(x).add(item);
                            separatorJustAdded = false;

                            record.setInstant(Instant.now());
                            record.setMessage(String.format("p.separatorAfterMenu() = %s",
                                    p.separatorAfterMenu()));
                            logger.debug(record);
                            if (p.separatorAfterMenu()) {
                                record.setInstant(Instant.now());
                                record.setMessage("Adding a separator to the menu bar.");
                                logger.debug(record);
                                if (!separatorJustAdded) {
                                    mainFrame.getMenuBar().getMenu(x).addSeparator();
                                    separatorJustAdded = true;
                                }
                            }
                            
                            if(p.providesToolbarButton()) {
                                JButton button = new JButton();
                                button.setName(p.getName() + "Button");
                                button.setAction(item.getAction());
                                if (p.getTextOverride() != null 
                                        && !"-1".equals(p.getTextOverride())) {
                                    button.setText(p.getTextOverride());
                                }
                                
                                if (p.separatorBeforeButton() && mainFrame.getToolBar().getComponentCount() >= 1) {
                                    mainFrame.getToolBar().addSeparator();
                                }
                                
                                mainFrame.getToolBar().add(button);
                                
                                if (p.separatorAfterButton()) {
                                    mainFrame.getToolBar().addSeparator();
                                }
                            }
                        }

//                        record.setInstant(Instant.now());
//                        record.setMessage(String.format("list.size() = %s"
//                                + "\nRemoving c (%s) from the list.", 
//                                list.size(), c));
//                        logger.debug(record);
//                        list.remove(c);
//                        record.setInstant(Instant.now());
//                        record.setMessage(String.format("Removed c (%s) from the "
//                                + "list.\nlist.size() = %s", c, list.size()));
//                        logger.debug(record);
                    }

                    // We are done, so no use continuing.
                    break;
                }
            }
        } catch (NullPointerException ex) {
            record.setInstant(Instant.now());
            record.setThreadID(Thread.currentThread().getId());
            record.setSourceMethodName("addItemsListToMenu");
            record.setThrown(ex);
            logger.critical(record);
        }
        if (mainFrame.getToolBar().getComponentCount() > 0) {
            if (mainFrame.getToolBar().getComponentAtIndex(mainFrame.getToolBar().getComponentCount() - 1) instanceof JSeparator) {
                mainFrame.getToolBar().remove(mainFrame.getToolBar().getComponentCount() - 1);
            }
            if (mainFrame.getToolBar().getComponentAtIndex(0) instanceof JSeparator) {
                mainFrame.getToolBar().remove(0);
            }
        }
        if (mainFrame.getMenuBar().getMenuCount() > 0) {
            for (int y = 0; y < mainFrame.getMenuBar().getMenuCount(); y++) {
                if (mainFrame.getMenuBar().getMenu(y).getComponentCount() > 0) {
                    if (mainFrame.getMenuBar().getMenu(y).getMenuComponent(0) instanceof JSeparator) {
                        mainFrame.getMenuBar().getMenu(y).remove(y);
                    }
                }
            }
        }

        record.setInstant(Instant.now());
        record.setMessage("Returning to recursivelyAddSubMenusAndMenuItems()");
        record.setThreadID(Thread.currentThread().getId());
        logger.exit(record);
    } // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="private ArrayList sortList(List)">
    private ArrayList<Comparable> sortList(List<Comparable> list) {
        // First, we will get an array of all of the position values.
        int[] positions = new int[list.size()];

        for (int i = 0; i < positions.length; i++) {
            Comparable c = list.get(i);

            if (c instanceof ActionCommandProvider) {
                positions[i] = ((ActionCommandProvider) c).getPosition();
            } else if (c instanceof SubMenuProvider) {
                positions[i] = ((SubMenuProvider) c).getPosition();
            }
        }

        // Now, we can do a bubble sort on these items to get them in their
        //+ positional order.
        for (int outer = 0; outer < positions.length; outer++) {
            for (int inner = 1; inner < positions.length; inner++) {
                if (positions[inner - 1] > positions[inner]) {
                    int temp = positions[inner];
                    positions[inner] = positions[inner - 1];
                    positions[inner - 1] = temp;
                }
            }
        } // End of bubble sort

        // Now that we have the array of positions sorted in escalating value,
        //+ we need to sort the original list based on this. We will need a new
        //+ list...
        ArrayList<Comparable> sorted = new ArrayList<>();

        for (int x = 0; x < positions.length; x++) {
            for (Comparable c : list) {
                if (c instanceof ActionCommandProvider) {
                    if (((ActionCommandProvider) c).getPosition() == positions[x]) {
                        sorted.add(c);
                    }
                } else if (c instanceof SubMenuProvider) {
                    if (((SubMenuProvider) c).getPosition() == positions[x]) {
                        sorted.add(c);
                    }
                }
            }
        }

        // Now that this algorithm has run, we should have our submenus and menu
        //+ items in ascending numerical order, so we just need to return it.
        return sorted;
    } // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="private HashMap<Comparable, String> sortMenuItemsByValue()">
    private HashMap<Comparable, String> sortMenuItemsByValue() {
        List<Map.Entry<Comparable, String>> list = new LinkedList<>(menuItems.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<Comparable, String>>() {
            @Override
            public int compare(Map.Entry<Comparable, String> o1, Map.Entry<Comparable, String> o2) {
                return (o1.getValue().compareTo(o2.getValue()));
            }
        });

        HashMap<Comparable, String> temp = new LinkedHashMap<>();
        for (Map.Entry<Comparable, String> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }

        return temp;
    } // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="private class MainFrameAdapter extends WindowAdapter">
    private class MainFrameAdapter extends WindowAdapter {

        @Override
        public void windowClosing(WindowEvent e) {
            ActionEvent evt = new ActionEvent(e.getSource(), e.getID(), e.paramString());
            int quitMenuIndex = mainFrame.getMenuBar().getMenu(0).getItemCount() - 1;
            mainFrame.getMenuBar().getMenu(0).getItem(quitMenuIndex).getAction().actionPerformed(evt);
        }
    } // </editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="private class StartupTask">
    private class StartupTask extends Task<Void, Void> {

        private final SplashScreen splash;
        private final Logger log = Logger.getLogger(getApplication(), Logger.TRACE);
        private final LogRecord record = new LogRecord(StartupTask.class.getSimpleName());
        
        public StartupTask(Application application) {
            super(application);
            record.setInstant(Instant.now());
            record.setMessage("Constructing StartupTask");
            record.setParameters(new Object[]{application});
            record.setSequenceNumber(1l);
            record.setSourceMethodName("StartupTask [Constructor]");
            record.setThreadID(Thread.currentThread().getId());
            log.enter(record);
            
            this.splash = new SplashScreen();
//            splash.setUndecorated(true);
            show(splash);
            
            record.setInstant(Instant.now());
            record.setMessage("Constructing StartupTask");
            record.setParameters(new Object[]{application});
            record.setSequenceNumber(2l);
            record.setSourceMethodName("StartupTask [Constructor]");
            record.setThreadID(Thread.currentThread().getId());
            log.exit(record);
        }
        
        

        @Override
        protected Void doInBackground() throws Exception {
            record.setInstant(Instant.now());
            record.setMessage("Running task...");
            record.setParameters(null);
            record.setSequenceNumber(3l);
            record.setSourceMethodName("doInBackground");
            record.setThreadID(Thread.currentThread().getId());
            log.enter(record);
            int progress = 0;
            setProgress(0);
            message("startMessage");

            record.setInstant(Instant.now());
            record.setMessage("Setting the toolbar to indeterminate while we "
                    + "prepare to launch...");
            record.setParameters(null);
            record.setSequenceNumber(4l);
            record.setSourceMethodName("doInBackground");
            record.setThreadID(Thread.currentThread().getId());
            log.debug(record);
            
            // First, we need to determine the number of items we are going to
            //+ be processing, so set the progressBar to indeterminate.
            splash.setIndeterminate(true);
            
            record.setInstant(Instant.now());
            record.setMessage("Creating variables to hold the total number of "
                    + "top-level menus, sub menus, menu items and toolbar "
                    + "buttons...");
            record.setParameters(null);
            record.setSequenceNumber(5l);
            record.setSourceMethodName("doInBackground");
            record.setThreadID(Thread.currentThread().getId());
            log.debug(record);
            
            // Get the maximum value for our progressBar.
            final int mainMenuValue = getMainMenuCount();
            final int menuValue = getMenuCount();
            final int toolbarValue = getToolbarCount();
            splash.setMaximum(mainMenuValue + (menuValue * 2) + toolbarValue);
            
            record.setInstant(Instant.now());
            record.setMessage("Setting the progressbar back to determinate, "
                    + "because we now know how many items are being processed...");
            record.setParameters(null);
            record.setSequenceNumber(6l);
            record.setSourceMethodName("doInBackground");
            record.setThreadID(Thread.currentThread().getId());
            log.debug(record);
            
            splash.setIndeterminate(false);
            message("menuMessage");

            record.setInstant(Instant.now());
            record.setMessage("Building the main menu bar...");
            record.setParameters(null);
            record.setSequenceNumber(7l);
            record.setSourceMethodName("doInBackground");
            record.setThreadID(Thread.currentThread().getId());
            log.debug(record);
            
            buildMainMenu();
            setProgress(mainMenuValue);
            
            record.setInstant(Instant.now());
            record.setMessage("Loading the sub menus and menu items...");
            record.setParameters(null);
            record.setSequenceNumber(8l);
            record.setSourceMethodName("doInBackground");
            record.setThreadID(Thread.currentThread().getId());
            log.debug(record);
            
            loadSubMenus();
            loadMenuItems();
            buildMenuSystem();
            setProgress(menuValue);
            
            record.setInstant(Instant.now());
            record.setMessage("Loading the toolbar buttons...");
            record.setParameters(null);
            record.setSequenceNumber(9l);
            record.setSourceMethodName("doInBackground");
            record.setThreadID(Thread.currentThread().getId());
            log.debug(record);
            
            record.setInstant(Instant.now());
            record.setMessage(String.format("Returning %s", (Object) null));
            record.setParameters(null);
            record.setSequenceNumber(10l);
            record.setSourceMethodName("doInBackground");
            record.setThreadID(Thread.currentThread().getId());
            log.exit(record);
            return null;
        }
        
        @Override
        protected void finished() {
            record.setInstant(Instant.now());
            record.setMessage("Finishing startup...");
            record.setParameters(null);
            record.setSequenceNumber(11l);
            record.setSourceMethodName("finished");
            record.setThreadID(Thread.currentThread().getId());
            log.enter(record);
            
            record.setInstant(Instant.now());
            record.setMessage("Showing the mainFrame and disposing of the "
                    + "SplashScreen...");
            record.setParameters(null);
            record.setSequenceNumber(12l);
            record.setSourceMethodName("finished");
            record.setThreadID(Thread.currentThread().getId());
            log.debug(record);
            
            message("finishedMessage");
            show(mainFrame);
            splash.dispose();
            record.setInstant(Instant.now());
            record.setMessage("Completed the StartupTask processing...");
            record.setParameters(null);
            record.setSequenceNumber(13l);
            record.setSourceMethodName("finished");
            record.setThreadID(Thread.currentThread().getId());
            log.exit(record);
            
        }
        
    } // </editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="private int getMainMenuCount()">
    private int getMainMenuCount() {
        ServiceLoader<MenuProvider> menus = ServiceLoader.load(MenuProvider.class);
        int val = 0;
        Iterator it = menus.iterator();
        it = menus.iterator();
        while(it.hasNext()) {
            val++;
            it.next();
        }
        
        return val;
    } // </editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="private int getMenuCount()">
    private int getMenuCount() {
        ServiceLoader<ActionCommandProvider> menuItems = ServiceLoader.load(ActionCommandProvider.class);
        ServiceLoader<SubMenuProvider> subMenus = ServiceLoader.load(SubMenuProvider.class);
        
        int val = 0;
        
        Iterator it = menuItems.iterator();
        while(it.hasNext()) {
            val++;
            it.next();
        }
        
        it = subMenus.iterator();
        while(it.hasNext()) {
            val++;
            it.next();
        }
        
        return val;
    } // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="private int getToolbarCount()">
    private int getToolbarCount() {
        ServiceLoader<ToolbarButtonProvider> loader = ServiceLoader.load(ToolbarButtonProvider.class);
        
        int val = 0;
        
        Iterator it = loader.iterator();
        while(it.hasNext()) {
            val++;
            it.next();
        }
        
        return val;
    } // </editor-fold>

}
