/*
 * Copyright (C) 2020 PekinSOFT Systems
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
 *  Project    :   Northwind-Basic
 *  Class      :   StringUtils.java
 *  Author     :   Sean Carrick
 *  Created    :   Mar 8, 2020 @ 12:34:09 PM
 *  Modified   :   Mar 8, 2020
 *
 *  Purpose:
 *
 *  Revision History:
 *
 *  WHEN          BY                  REASON
 *  ------------  ------------------- ------------------------------------------
 *  Mar 08, 2020  Sean Carrick        Initial creation.
 *  Oct 24, 2021  Sean Carrick        All unit tests for insertTabLeader and
 *                                    wrap have passed. The wrap method now
 *                                    takes into account existing newline
 *                                    characters in the source text, as well as
 *                                    hyphenated words. The wrap method also no
 *                                    longer breaks a line within the bounds of
 *                                    a word. Only whole words start a new line.
 *  Oct 28, 2021  Sean Carrick        Added the specialized wrapLogMessage
 *                                    method that replaces all commas in the
 *                                    source text with newline and tab characters.
 *                                    See definition list on the wrap method for
 *                                    details on why.
 *                                    Found error in the logic of the
 *                                    insertTabLeader method. When the rightWord
 *                                    is a path, such as Java Module Path, it can
 *                                    be too long to process by the argument test.
 *                                    Therefore, I added an argument test just
 *                                    before it, checking if rightWord contains
 *                                    "path", and turned the original argument
 *                                    test into an else if block. Seems to be
 *                                    working pretty good now.
 * *****************************************************************************
 */
package com.gs.utils;

/**
 *
 * @author Sean Carrick &lt;sean at pekinsoft dot com&gt;
 *
 * @version 1.05
 * @since 1.0
 */
public class StringUtils {

    //<editor-fold defaultstate="collapsed" desc="Constructor(s)">
    private StringUtils() {
        // Privatized to prevent this class from being instantiated.
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Public Static Methods and Functions">
    /**
     * Abbreviates a String using ellipses.This will turn "Now is the time for
     * all good men" into "Now is the time for...". Specifically: If the number
     * of characters in {@code source} is less than or equal to `maxLength, return
     * `source`. Else abbreviate it to (`substring(source, maxLangth - 3) +
     * "..."`). If {@code width} is less than four, throw and
     * `IllegalArgumentException`. In no case will it return a String of length
     * greater than `width`.
     *
     * <pre>
     * StringUtils.abbreviate(null, *)          = null
     * StringUtils.abbreviate("", 4)            = ""
     * StringUtils.abbreviate(abcdefg, 6)       = "abc..."
     * StringUtils.abbreviate("abcdefg", 7)     = "abcdefg"
     * StringUtils.abbreviate("abcdefg", 8)     = "abcdefg"
     * StringUtils.abbreviate("abcdefg", 4)     = "a..."
     * StringUtils.abbreviate("abcdefg", 3)     = IllegalArgumentException
     * </pre>
     * <p>
     * Throws an {@code IllegalArguementException} if the width is too small.</p>
     *
     * @param source the String to check, may be null
     * @param width maximum length of result String, must be at least 4
     * @return abbreviated String, null if null String input
     */
    public static String abbreviate(String source, int width) {
        if (source == null) {
            return null;
        }
        if (width < 4) {
            throw new IllegalArgumentException("maxLength must be greater than "
                    + "four (4).");
        }
        if (source.length() <= width) {
            return source;
        }

        return source.substring(0, width - 3) + "...";
    }

    /**
     * Deletes all whitespaces from a String as defined by
     * `Character.isWhitespace(char)`.
     *
     * ```java
     * StringUtils.deleteWhitespace(null) = null;
     * StringUtils.deleteWhitespace("") = "";
     * StringUtils.deleteWhitespace("abc") = "abc";
     * StringUtils.deleteWhitespace(" ab c ") = "abc";
     * ```
     *
     * @param source the String to delete whitespace from, may be null
     * @return String without whitespaces, null if null String input
     */
    public static String deleteWhitespace(String source) {
        if (source == null) {
            return null;
        }

        char[] chars = source.toCharArray();
        source = "";

        for (char c : chars) {
            if (!Character.isWhitespace(c)) {
                source += c;
            }
        }

        return source;
    }

    /**
     * Removes a substring only if it is at the beginning of a source string,
     * otherwise returns the source string. A {@code null} source string will return
     * `null`. An empty ("") source string will return the empty string. A
     * {@code null} search string will return the source string.
     *
     * @param source the string from which the substring should be removed.
     * @param remove the substring to remove from the source string.
     * @return java.lang.String the source string with the substring removed,
     * except as provided for above.
     */
    public static String removeStart(String source, String remove) {
        if (source == null) {
            return null;
        } else if (source.isEmpty()) {
            return source;
        }
        if (remove == null) {
            return source;
        }

        String holder = "";

        if (source.startsWith(remove)) {
            holder = source.substring(remove.length());
        }

        return holder;
    }

    public static String removeStartIgnoreCase(String source, String remove) {
        if (source == null) {
            return null;
        } else if (source.isEmpty()) {
            return source;
        }
        if (remove == null) {
            return source;
        }

        String holder = "";

        if (source.toLowerCase().startsWith(remove.toLowerCase())) {
            holder = source.substring(remove.length());
        }

        return holder;
    }

    /**
     * Removes a substring only if it is at the end of a source string,
     * otherwise returns the source string.
     *
     * A {@code null} source string will return `null`. An empty ("") source string
     * will return the empty string. A {@code null} search string will return the
     * source string.
     *
     * @param source the source String to search, may be null
     * @param remove the String to search for and remove, may be null
     * @return the substring with the string removed, if found, {@code null} if `null`
     * String input
     */
    public static String removeEnd(String source, String remove) {
        if (source == null) {
            return null;
        }
        if (remove == null) {
            return source;
        }
        if (source.isEmpty()) {
            return source;
        }

        if (source.endsWith(remove)) {
            return source.substring(0, source.indexOf(remove));
        } else {
            return source;
        }
    }

    /**
     * Pads the provided {@code String} to the left side of the field of the given
     * field width. This method is guaranteed to never return a value longer
     * than the given `fieldWidth`. If the provided value is already longer than
     * the {@code fieldWidth` value, then an abbreviated `String} is returned.
     *
     * @param toPad the {@code String} to left-align in the field.
     * @param fieldWidth the width of the field
     * @return {@code String` containing the `toPad} value padded on the right end
     * with spaces to bring the length of the value to the width of the field.
     * {@code String` of `width` number of spaces if an empty `String} is provided.
     * @throws IllegalArgumentException in the event {@code toPad} is `null`, or
     * {@code fieldWidth} is less than four.
     */
    public static String padLeft(String toPad, int fieldWidth) {
        if (toPad == null || fieldWidth < 4) {
            throw new IllegalArgumentException("Values are no good");
        } else if (toPad.isEmpty()) {
            return repeat(" ", fieldWidth);
        }

        if (toPad.length() > fieldWidth) {
            return abbreviate(toPad, fieldWidth);
        } else {
            return toPad + repeat(" ", fieldWidth - toPad.length());
        }
    }

    /**
     * Pads the provided {@code String} to the right side of the field of the given
     * field width. This method is guaranteed to never return a value longer
     * than the given `fieldWidth`. If the provided value is already longer than
     * the {@code fieldWidth` value, then an abbreviated `String} is returned.
     *
     * @param toPad the {@code String} to right-align in the field.
     * @param fieldWidth the width of the field
     * @return {@code String` containing the `toPad} value padded on the left end with
     * spaces to bring the length of the value to the width of the field.
     * {@code String` of `width` number of spaces if an empty `String} is provided.
     * @throws IllegalArgumentException in the event {@code toPad} is `null`, or
     * {@code fieldWidth} is less than four.
     */
    public static String padRight(String toPad, int fieldWidth) {
        if (toPad == null || fieldWidth < 4) {
            throw new IllegalArgumentException("Values are no good");
        } else if (toPad.isEmpty()) {
            return repeat(" ", fieldWidth);
        }

        if (toPad.length() > fieldWidth) {
            return abbreviate(toPad, fieldWidth);
        } else {
            return repeat(" ", fieldWidth - toPad.length()) + toPad;
        }
    }

    public static String repeat(String toRepeat, int times) {
        StringBuilder sb = new StringBuilder();

        for (int x = 0; x < times; x++) {
            sb.append(toRepeat);
        }

        return sb.toString();
    }
    //</editor-fold>

    /**
     * This method will take the `source` string and wrap it at the specified
     * `width`.
     * 
     * When performing the wrapping of the `source` string, this method first
     * checks to see if there are any newline characters included within it. If
     * there are, the `source` will first be broken into parts at the newline
     * characters. Once that is done, the elements of the created array will be
     * split on the space characters. Once all of this is complete, the string
     * will be rebuilt allowing for wrapping to take place at the specified
     * `width`, without breaking within a single word.
     * 
     * Furthermore, this method takes into account any hyphens contained within
     * the `source` string. If the built up line is getting close to the `width`
     * of the requested string and the next word will go beyond the specified
     * `width`, this method will check for a hyphen within the next word. If a
     * hyphen exists, the word will be temporarily broken on the hyphen to see
     * if that portion of the word will fit on the current line. If it will, it
     * will be added to the current line of text. If not, then the entire
     * hyphenated word will be placed at the beginning of the next line of text.
     * 
     * <dl><dt><strong><em>Note Regarding Additional Splits</em></strong></dt>
     * <dd>When `StringUtils.wrap()` is used for prettifying log output, it is
     * best to call the specialized method `StringUtils.wrapLogMessage()`. This
     * overloaded version of `wrap` replaces comma characters with a newline
     * and tab character. This is due to using `String.format()` to create the
     * log messages to log the state of `Object`s during debugging. The result
     * from `toString` typically involves all properties of an `Object` being
     * strung together separated by only the comma, and without spaces. This
     * causes some information to not be wrapped properly, which allows for very
     * long lines, even in the formatted output.</dd></dl>
     *
     * @param source the source string to be wrapped
     * @param width the width at which the `source` string should be wrapped
     * @return the `source` string, properly wrapped at the specified `width`
     * @throws IllegalArgumentException if `source` is `null`, empty, or blank,
     * or if `width` is less than or equal to zero
     * 
     * @see #wrapLogMessage(java.lang.String, int) 
     */
    public static String wrap(String source, int width) {
        if (source == null) {
            throw new IllegalArgumentException("null source");
        }
        if (source.isBlank()) {
            throw new IllegalArgumentException("blank source");
        }
        if (source.isEmpty()) {
            throw new IllegalArgumentException("empty source");
        }
        if (width <= 0) {
            throw new IllegalArgumentException("width <= 0");
        }

        StringBuilder sb = new StringBuilder();

        // First, split on newline characters. If there are none the lines array
        //+ will only have a single element, so the loop will only run once.
        String[] lines = source.split("\n");
        String holder = "";

        for (String line : lines) {
            // Split the line into its individual words.
            String[] words = line.split(" ");

            if (line.length() <= width) {
                sb.append(line).append("\n");
            } else {

                for (String word : words) {
                    // Check if the holder string is less than the desired width.
                    if (holder.length() < width) {
                        // Check to see if the next word and space will go beyond
                        //+ the desired width.
                        if ((holder.length() + word.length() + 1) <= width) {
                            // If not, add the space and the word to the holder.
                            if (!holder.endsWith(".")) {
                                holder += word + " ";
                            } else {
                                holder += " " + word + " ";
                            }
                        } else {
                            // If so, check to see if the next word has a hyphen.
                            if (word.contains("-")) {
                                // If so, split the word on the hyphen.
                                String[] hyphenated = word.split("-");

                                // Check if hyphenated only has a length of two.
                                if (hyphenated.length == 2) {
                                    // Check if the first element will fit within
                                    //+ the desired width.
                                    if ((holder.length() + hyphenated[0].length()
                                            + 1) <= width) {
                                        // If so, add the first element.
                                        holder += hyphenated[0];

                                        // Append holder to the StringBuilder, 
                                        //+ while adding back the hypen with
                                        //+ a newline character at the end.
                                        sb.append(holder.trim()).append("-");
                                        sb.append("\n");

                                        // Reset holder for the next line.
                                        holder = hyphenated[1] + " ";
                                    }
                                } else if (hyphenated.length > 2) {
                                    // If multiple hyphens, add the first element.
                                    holder += hyphenated[0];

                                    // Append holder to the StringBuilder, 
                                    //+ while adding back the hypen with
                                    //+ a newline character at the end.
                                    sb.append(holder.trim()).append("-").append("\n");

                                    // Add the remaining hyphenated parts to
                                    //+ holder.
                                    for (int x = 1; x < hyphenated.length; x++) {
                                        if (x > 1) {
                                            // Reset holder for the next line.
                                            holder = "";

                                            // Add back the hyphen and the next
                                            //+ part.
                                            holder += "-" + hyphenated[x];
                                        } else {
                                            // Simply add the hyphenated part.
                                            holder += hyphenated[x];
                                        }
                                    }
                                }
                            } else {
                                // The word is not hyphenated, so append holder to
                                //+ the StringBuilder, with a newline character.
                                sb.append(holder.trim()).append("\n");

                                // Reset holder and add the current word to it.
                                holder = word + " ";
                            }
                        }
                    }
                }
            }
        }

        // Once the end of the lines are reached, we need to add whatever text
        //+ is remaining in holder to our StringBuilder.
        sb.append(holder.trim());

        String ret = sb.toString().trim();

        return (ret == null || ret.isBlank() || ret.isEmpty()) ? null : ret;
    }

    /**
     * This method will take the `source` string and wrap it at the specified
     * `width`.
     * 
     * <em>This is a specialized method for use with log messages</em>.
     * 
     * The specialized use of this method is specifically for formatting log
     * message output. The first thing this method does is replace all commas in
     * the `source` with a `\n\t` character sequence. This is so that the output
     * from `Object.toString()` will also be formatted by splitting it into 
     * separate lines. The common output from the `toString` method of any Java
     * `Object` is to concatenate all of the object's `propertyName=value`s and
     * separate each property/value pair by only a comma, without a space. This
     * method, therefore, replaces the commas with the newline/tab sequence to
     * prevent an object's `toString` output from being extremely long and not
     * able to be wrapped properly by the `StringUtils.wrap()` method.
     * 
     * The other thing that is done by this method is to replace all 
     * semicolon/space character combinations with a newline character (`\n`).
     * Typically when writing log messages, developers will split values they
     * are logging by using a semicolon/space combination. Therefore, this method
     * also splits the `source` text on that character combination to better 
     * format the log messages.
     * 
     * The body of this method is simply:
     * 
     * ```java
     * public static String wrapLogMessage(String source, int width) {
     *     return wrap(source.replace(",", "\n\t").replace("; ", "\n"), width);
     * }
     * ```
     * 
     * Therefore, the `wrap` method is still used and this method really only
     * prepares the `source` string for proper wrapping by the `wrap` method.
     * 
     * @param source
     * @param width
     * @return the `source` string, properly wrapped for log message output, to
     * the specified `width`
     * @throws IllegalArgumentException if `source` is `null`, empty, or blank,
     * or if `width` is less than or equal to zero
     * 
     * @see #wrap(java.lang.String, int) 
     */
    public static String wrapLogMessage(String source, int width) {
        if (source == null || source.isBlank() || source.isEmpty()) {
            throw new IllegalArgumentException("null, blank, or empty source");
        }
        if (width <= 0) {
            throw new IllegalArgumentException("width less than or equal to zero");
        }
        
        return wrap(source.replace(",", "\n\t").replace("; ", "\n"), width);
    }
    
    /**
     * Inserts a tab leader with the specified character as the leader.
     * <p>
     * A tab leader is such that a character is inserted between two words or
     * phrases, such as:</p>
     * <pre>
     * OS.....................Microsoft Windows
     * Version...............................11
     * </pre><p>
     * When using a tab leader, the right word or phrase is right-aligned with
     * all words and phrases below it and the leader character appears to the
     * left of the word or phrase. The leader character starts on the left at
     * the right edge of the left word or phrase.<p>
     *
     * @param leftWord the word or phrase that is on the left side of the tab
     * @param rightWord the word or phrase that is on the right side of the tab
     * @param rightMargin the farthest right the text should be placed
     * @param leader the character to repeat in the intervening space between
     * the `rightWord` and `leftWord`. This character needs to be one of
     * underscore (_), period (.), dash (-), or space ( )
     * @return a string formatted with the `leftWord` separated from the
     * `rightWord` by a repeated `leader` character and the `rightWord`s
     * right-aligned
     * @throws IllegalArgumentException if `leftWord`, or `rightWord` are
     * `null`, blank, or empty; if `rightMargin` is less than the length of
     * `leftWord` and `rightWord` plus three (3); if leader is not a symbol
     */
    public static String insertTabLeader(String leftWord, String rightWord,
            int rightMargin, char leader) {
        if (leftWord == null) {
            throw new IllegalArgumentException("null leftWord");
        } else if (leftWord.isBlank()) {
            throw new IllegalArgumentException("blank leftWord");
        } else if (leftWord.isEmpty()) {
            throw new IllegalArgumentException("empty leftWord");
        }
        if (rightWord == null) {
            throw new IllegalArgumentException("null rightWord");
        } else if (rightWord.isBlank()) {
            throw new IllegalArgumentException("blank rightWord");
        } else if (rightWord.isEmpty()) {
            throw new IllegalArgumentException("empty rightWord");
        }
        if (leftWord.toLowerCase().contains("path")) {
            rightWord = splitOnPathSeparator(rightWord);
        } else if (rightMargin < (leftWord.length() + rightWord.length() + 2)) {
            throw new IllegalArgumentException("insufficient space to process");
        }
        if (leader != ' ' && leader != '-' && leader != '.' && leader != '_') {
            throw new IllegalArgumentException("invalid leader character");
        }

        int space = rightMargin - (leftWord.length() + rightWord.length());
        return leftWord + repeatChar(leader, space) + rightWord;
    }
    
    private static String splitOnPathSeparator(String path) {
        return (path.replace(System.getProperty("path.separator"), "\n\t"));
    }

    private static String repeatChar(char toRepeat, int times) {
        String repeated = "";

        for (int x = 1; x <= times; x++) {
            repeated += toRepeat;
        }

        return repeated;
    }

}
