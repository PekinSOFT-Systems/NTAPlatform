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
 *  Class      :   Properties.java
 *  Author     :   Sean Carrick
 *  Created    :   Jul 17, 2021 @ 10:13:34 AM
 *  Modified   :   Jul 17, 2021
 * 
 *  Purpose:     See class JavaDoc comment.
 * 
 *  Revision History:
 * 
 *  WHEN          BY                   REASON
 *  ------------  -------------------  -----------------------------------------
 *  Jul 17, 2021  Sean Carrick         Initial creation.
 *  Oct 28, 2021  Sean Carrick         Fixed the catch block in loadProperties
 *                                     to take into account if a "File not found"
 *                                     message is the reason for the exception
 *                                     being thrown. This should only occur on
 *                                     the first run of the application, or if
 *                                     the user deletes the configuration file
 *                                     for some reason.
 *
 *                                     Also, changed the properties maps to take
 *                                     an Object as the value. Created convenience
 *                                     methods for getting the properties as the
 *                                     various types.
 *  Oct 29, 2021  Sean Carrick         Found bug in the getPropertyAsBoolean()
 *                                     methods. When storing the data, it is a
 *                                     String value once written to the file. 
 *                                     Doing an instanceof check continually
 *                                     failed. Changed the method bodies to get
 *                                     the property as a string and then checked
 *                                     if the value was "true" or "false" and
 *                                     did Boolean.parseBoolean(value). Works
 *                                     now, but may run into this same issue with
 *                                     all of the other getPropertyAs(Object)
 *                                     methods.
 * *****************************************************************************
 */
package com.gs.nta.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import org.jdesktop.application.ApplicationContext;

/**
 * The `Properties` class maintains all application properties.
 * <p>
 * Anytime a property is sought, the `Properties` class searches through the
 * list of system properties first. If the property is not found in that list,
 * the runtime properties are searched next. When using the overloaded
 * `getProperty(String propertyName, String defaultValue)` method, if the
 * property that is sought does not exist in either properties list, the
 * supplied `defaultValue` is returned. When using the method
 * `getProperty(String propertyName)`, if the property sought does not exist,
 * `null` is returned.</p>
 * <p>
 * The system properties list is stored to disk whenever requested. However, the
 * runtime properties list is never stored to disk. This is because the runtime
 * properties list can change from run to run, as they are typically set via
 * command-line switches and parameters.</p>
 *
 * @see #getProperty(java.lang.String)
 * @see #getProperty(java.lang.String, java.lang.String)
 * @see #setSystemProperty(java.lang.String, java.lang.String)
 * @see #setRuntimeProperty(java.lang.String, java.lang.String)
 *
 * @author Sean Carrick &lt;sean at gs-unitedlabs dot com&gt;
 *
 * @version 1.1
 * @since 1.0
 */
public class Properties {

    private final Map<String, Object> system;
    private final Map<String, Object> runtime;
    private final ApplicationContext context;
    private final LogRecord record = new LogRecord(Properties.class.getSimpleName());
    private final Logger logger;

    public Properties(ApplicationContext context) {
        this.context = context;
        logger = Logger.getLogger(this.context.getApplication(), Logger.INFO);
        system = new HashMap<>();
        runtime = new HashMap<>();

        loadProperties();
    }

    /**
     * Retrieves the value of the property named `propertyName`. If the
     * `propertyName` does not exist in the system properties list, then the
     * runtime properties list is checked. If the property does not exist in
     * either properties list, then `null` is returned.
     *
     * @param propertyName the name of the property whose value should be
     * retrieved
     * @return the value of the property `propertyName` or `null`
     */
    public Object getProperty(String propertyName) {
        Object value = system.get(propertyName);
        if (value == null) {
            value = runtime.get(propertyName);
        }

        return (value != null) ? value : null;
    }
    
    /**
     * Convenience method to retrieve the property value as a `java.lang.String`.
     * 
     * This method is guaranteed to not return `null`, even if the property is
     * not set. In that case, it will return the supplied `defaultValue`.
     * 
     * @param propertyName the name of the property of interest
     * @param defaultValue a default value for if the property has not been set
     * @return the value stored in the property as a `String`, or an empty string.
     * 
     * @see #getProperty(java.lang.String) 
     */
    public String getPropertyAsString(String propertyName, String defaultValue) {
        return getProperty(propertyName, defaultValue).toString();
    }
    
    /**
     * Convenience method to retrieve the property value as a `java.lang.Boolean`.
     * 
     * This method is guaranteed to not return `null`, even if the property is
     * not set. In that case, it will return the supplied `defaultValue`.
     * 
     * @param propertyName the name of the property of interest
     * @param defaultValue a default value for if the property has not been set
     * @return the value stored in the property as a `Boolean`, or false.
     * 
     * @see #getProperty(java.lang.Boolean) 
     */
    public boolean getPropertyAsBoolean(String propertyName, boolean defaultValue) {
        String str = getPropertyAsString(propertyName);
        
        return (!"true".equals(str) && !"false".equals(str)) 
                ? defaultValue
                : Boolean.parseBoolean(str);
    }
    
    /**
     * Convenience method to retrieve the property value as a `java.lang.Byte`.
     * 
     * This method is guaranteed to not return `null`, even if the property is
     * not set. In that case, it will return the supplied `defaultValue`.
     * 
     * @param propertyName the name of the property of interest
     * @param defaultValue a default value for if the property has not been set
     * @return the value stored in the property as a `Byte`, or `0`.
     * 
     * @see #getProperty(java.lang.Byte) 
     */
    public byte getPropertyAsByte(String propertyName, byte defaultValue) {
        Object value = getProperty(propertyName, String.valueOf(defaultValue));
        
        if (value instanceof Byte) {
            return (Byte) value;
        } else {
            return 0;
        }
    }
    
    /**
     * Convenience method to retrieve the property value as a `java.lang.Short`.
     * 
     * This method is guaranteed to not return `null`, even if the property is
     * not set. In that case, it will return the supplied `defaultValue`.
     * 
     * @param propertyName the name of the property of interest
     * @param defaultValue a default value for if the property has not been set
     * @return the value stored in the property as a `Short`, or `0`.
     * 
     * @see #getProperty(java.lang.Short) 
     */
    public short getPropertyAsShort(String propertyName, short defaultValue) {
        Object value = getProperty(propertyName, String.valueOf(defaultValue));
        
        if (value instanceof Short) {
            return (Short) value;
        } else {
            return 0;
        }
    }
    
    /**
     * Convenience method to retrieve the property value as a `java.lang.Integer`.
     * 
     * This method is guaranteed to not return `null`, even if the property is
     * not set. In that case, it will return the supplied `defaultValue`.
     * 
     * @param propertyName the name of the property of interest
     * @param defaultValue a default value for if the property has not been set
     * @return the value stored in the property as a `Integer`, or `0`.
     * 
     * @see #getProperty(java.lang.Integer) 
     */
    public int getPropertyAsInteger(String propertyName, int defaultValue) {
        Object value = getProperty(propertyName, String.valueOf(defaultValue));
        
        if (value instanceof Integer) {
            return (Integer) value;
        } else {
            return 0;
        }
    }
    
    /**
     * Convenience method to retrieve the property value as a `java.lang.Long`.
     * 
     * This method is guaranteed to not return `null`, even if the property is
     * not set. In that case, it will return the supplied `defaultValue`.
     * 
     * @param propertyName the name of the property of interest
     * @param defaultValue a default value for if the property has not been set
     * @return the value stored in the property as a `Long`, or `0L`.
     * 
     * @see #getProperty(java.lang.Long) 
     */
    public long getPropertyAsLong(String propertyName, long defaultValue) {
        Object value = getProperty(propertyName, String.valueOf(defaultValue));
        
        if (value instanceof Long) {
            return (Long) value;
        } else {
            return 0L;
        }
    }
    
    /**
     * Convenience method to retrieve the property value as a `java.lang.Float`.
     * 
     * This method is guaranteed to not return `null`, even if the property is
     * not set. In that case, it will return the supplied `defaultValue`.
     * 
     * @param propertyName the name of the property of interest
     * @param defaultValue a default value for if the property has not been set
     * @return the value stored in the property as a `Float`, or `0f`.
     * 
     * @see #getProperty(java.lang.Float) 
     */
    public float getPropertyAsFloat(String propertyName, float defaultValue) {
        Object value = getProperty(propertyName, String.valueOf(defaultValue));
        
        if (value instanceof Float) {
            return (Float) value;
        } else {
            return 0.0f;
        }
    }
    
    /**
     * Convenience method to retrieve the property value as a `java.lang.Double`.
     * 
     * This method is guaranteed to not return `null`, even if the property is
     * not set. In that case, it will return the supplied `defaultValue`.
     * 
     * @param propertyName the name of the property of interest
     * @param defaultValue a default value for if the property has not been set
     * @return the value stored in the property as a `Double`, or `0d`.
     * 
     * @see #getProperty(java.lang.Double) 
     */
    public double getPropertyAsDouble(String propertyName, double defaultValue) {
        Object value = getProperty(propertyName, String.valueOf(defaultValue));
        
        if (value instanceof Double) {
            return (Double) value;
        } else {
            return 0.0d;
        }
    }
    
    /**
     * Convenience method to retrieve the property value as a `java.lang.String`.
     * 
     * This method is guaranteed to not return `null`, even if the property is
     * not set. In that case, it will return an empty string.
     * 
     * @param propertyName the name of the property of interest
     * @return the value stored in the property as a `String`, or an empty string.
     * 
     * @see #getProperty(java.lang.String) 
     */
    public String getPropertyAsString(String propertyName) {
        return getProperty(propertyName).toString();
    }
    
    /**
     * Convenience method to retrieve the property value as a `java.lang.Boolean`.
     * 
     * This method is guaranteed to not return `null`, even if the property is
     * not set. In that case, it will return false.
     * 
     * @param propertyName the name of the property of interest
     * @return the value stored in the property as a `Boolean`, or false.
     * 
     * @see #getProperty(java.lang.Boolean) 
     */
    public boolean getPropertyAsBoolean(String propertyName) {
        return Boolean.parseBoolean(getPropertyAsString(propertyName));
    }
    
    /**
     * Convenience method to retrieve the property value as a `java.lang.Byte`.
     * 
     * This method is guaranteed to not return `null`, even if the property is
     * not set. In that case, it will return `0`.
     * 
     * @param propertyName the name of the property of interest
     * @return the value stored in the property as a `Byte`, or `0`.
     * 
     * @see #getProperty(java.lang.Byte) 
     */
    public byte getPropertyAsByte(String propertyName) {
        Object value = getProperty(propertyName);
        
        if (value instanceof Byte) {
            return (Byte) value;
        } else {
            return 0;
        }
    }
    
    /**
     * Convenience method to retrieve the property value as a `java.lang.Short`.
     * 
     * This method is guaranteed to not return `null`, even if the property is
     * not set. In that case, it will return `0`.
     * 
     * @param propertyName the name of the property of interest
     * @return the value stored in the property as a `Short`, or `0`.
     * 
     * @see #getProperty(java.lang.Short) 
     */
    public short getPropertyAsShort(String propertyName) {
        Object value = getProperty(propertyName);
        
        if (value instanceof Short) {
            return (Short) value;
        } else {
            return 0;
        }
    }
    
    /**
     * Convenience method to retrieve the property value as a `java.lang.Integer`.
     * 
     * This method is guaranteed to not return `null`, even if the property is
     * not set. In that case, it will return `0`.
     * 
     * @param propertyName the name of the property of interest
     * @return the value stored in the property as a `Integer`, or `0`.
     * 
     * @see #getProperty(java.lang.Integer) 
     */
    public int getPropertyAsInteger(String propertyName) {
        Object value = getProperty(propertyName);
        
        if (value instanceof Integer) {
            return (Integer) value;
        } else {
            return 0;
        }
    }
    
    /**
     * Convenience method to retrieve the property value as a `java.lang.Long`.
     * 
     * This method is guaranteed to not return `null`, even if the property is
     * not set. In that case, it will return `0L`.
     * 
     * @param propertyName the name of the property of interest
     * @return the value stored in the property as a `Long`, or `0L`.
     * 
     * @see #getProperty(java.lang.Long) 
     */
    public long getPropertyAsLong(String propertyName) {
        Object value = getProperty(propertyName);
        
        if (value instanceof Long) {
            return (Long) value;
        } else {
            return 0L;
        }
    }
    
    /**
     * Convenience method to retrieve the property value as a `java.lang.Float`.
     * 
     * This method is guaranteed to not return `null`, even if the property is
     * not set. In that case, it will return `0f`.
     * 
     * @param propertyName the name of the property of interest
     * @return the value stored in the property as a `Float`, or `0f`.
     * 
     * @see #getProperty(java.lang.Float) 
     */
    public float getPropertyAsFloat(String propertyName) {
        Object value = getProperty(propertyName);
        
        if (value instanceof Float) {
            return (Float) value;
        } else {
            return 0.0f;
        }
    }
    
    /**
     * Convenience method to retrieve the property value as a `java.lang.Double`.
     * 
     * This method is guaranteed to not return `null`, even if the property is
     * not set. In that case, it will return `0d`.
     * 
     * @param propertyName the name of the property of interest
     * @return the value stored in the property as a `Double`, or `0d`.
     * 
     * @see #getProperty(java.lang.Double) 
     */
    public double getPropertyAsDouble(String propertyName) {
        Object value = getProperty(propertyName);
        
        if (value instanceof Double) {
            return (Double) value;
        } else {
            return 0.0d;
        }
    }

    /**
     * Retrieves the value of the property named `propertyName`. If the
     * `propertyName` does not exist in the system properties list, then the
     * runtime properties list is checked. if the property does not exist in
     * either properties list, the supplied `defaultValue` is returned.
     *
     * @param propertyName the name of the property whose value should be
     * retrieved
     * @param defaultValue the default value if the property does not exist or
     * is not setSystemProperty
     * @return the value of the property `propertyName` or the `defaultValue`
     */
    public Object getProperty(String propertyName, String defaultValue) {
        Object value = system.get(propertyName);
        if (value == null) {
            value = runtime.get(propertyName);
        }

        return (value == null) ? defaultValue : value;
    }

    /**
     * Sets the runtime property named `propertyName` to the `value` provided.
     * <p>
     * <strong><em>Note</em></strong>: Runtime properties are for only those
     * properties that are set by command-line parameters. If the property needs
     * to be persisted between `Application` runs, it should be stored to the
     * system properties.</p>
     *
     * @param propertyName the name of the property to set
     * @param value the value to which the property is to be set
     *
     * @see #setSystemProperty(java.lang.String, java.lang.String)
     */
    public void setRuntimeProperty(String propertyName, Object value) {
        runtime.put(propertyName, value);
    }

    /**
     * Sets the system property named `propertyName` to the `value` provided.
     * <p>
     * <strong><em>Note</em></strong>: System properties are for only those
     * properties that need to be persisted between `Application` runs. If the
     * property is set by a command-line parameter, then it should be stored to
     * the runtime properties.</p>
     *
     * @param propertyName the name of the property to set
     * @param value the value to which the property is to be set
     *
     * @see #setRuntimeProperty(java.lang.String, java.lang.String)
     * @see #storeProperties()
     */
    public void setSystemProperty(String propertyName, Object value) {
        system.put(propertyName, value);
    }

    /**
     * Stores the system properties to the `Application`'s configuration file.
     * <p>
     * <strong><em>Note</em></strong>: The system properties list is written to
     * disk when `storeProperties` is called. However, the runtime properties
     * list is not persisted to disk, as these are throw-away properties that
     * only affect the current run of the `Application`. Always make sure to set
     * properties to the appropriate properties list.</p>
     *
     * @see #setRuntimeProperty(java.lang.String, java.lang.String)
     * @see #setSystemProperty(java.lang.String, java.lang.String)
     */
    public void storeProperties() {
        String cfgPath = context.getLocalStorage().getDirectory().toString();
        if (!cfgPath.endsWith(File.separator)) {
            cfgPath += File.separator;
        }
        cfgPath += "etc" + File.separator;
        cfgPath += context.getResourceMap().getString("Application.id") + ".cfg";

        File cfg = new File(cfgPath);
        File cfgDir = new File(cfg.getParent());
        if (!cfgDir.exists()) {
            cfgDir.mkdirs();
        }
        cfgDir = null;

        try (BufferedWriter out = new BufferedWriter(new FileWriter(cfg))) {
            for (String key : system.keySet()) {
                out.write(key + "=" + system.get(key) + "\n");
                out.flush();
            }
            
            out.flush();
            out.close();
        } catch (IOException e) {
            String msg = "Attempting to read the properties file";
            record.setInstant(Instant.now());
            record.setMessage(msg);
            record.setParameters(null);
            record.setSourceMethodName("loadProperties");
            Long tID = Thread.currentThread().getId();
            record.setThreadID(tID.intValue());
            record.setThrown(e);
            logger.error(record);
        }
    }

    private void loadProperties() {
        String cfgPath = context.getLocalStorage().getDirectory().toString();
        if (!cfgPath.endsWith(File.separator)) {
            cfgPath += File.separator;
        }
        cfgPath += "etc" + File.separator;
        cfgPath += context.getResourceMap().getString("Application.id") + ".cfg";

        File cfg = new File(cfgPath);
        File cfgDir = new File(cfg.getParent());
        if (!cfgDir.exists()) {
            cfgDir.mkdirs();
        }
        cfgDir = null;

        try (BufferedReader in = new BufferedReader(new FileReader(cfg))) {
            String line = in.readLine();

            while (line != null) {
                String[] property = line.split("=");
                system.put(property[0], property[1]);

                line = in.readLine();
            }
        } catch (IOException e) {
            if (e.getMessage().toLowerCase().contains("file not found")) {
                try {
                    cfg.createNewFile();
                } catch (IOException ex) {
                    TerminalErrorPrinter.print(ex, "Configuration file path "
                            + "not found: %s" + cfgPath);
                }
            } else {
                String msg = "Attempting to read the properties file";
                record.setInstant(Instant.now());
                record.setMessage(msg);
                record.setParameters(null);
                record.setSourceMethodName("loadProperties");
                Long tID = Thread.currentThread().getId();
                record.setThreadID(tID.intValue());
                record.setThrown(e);
                logger.error(record);
            }
        }
    }

}
