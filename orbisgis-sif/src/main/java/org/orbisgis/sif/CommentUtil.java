/**
 * OrbisGIS is a java GIS application dedicated to research in GIScience.
 * OrbisGIS is developed by the GIS group of the DECIDE team of the 
 * Lab-STICC CNRS laboratory, see <http://www.lab-sticc.fr/>.
 *
 * The GIS group of the DECIDE team is located at :
 *
 * Laboratoire Lab-STICC – CNRS UMR 6285
 * Equipe DECIDE
 * UNIVERSITÉ DE BRETAGNE-SUD
 * Institut Universitaire de Technologie de Vannes
 * 8, Rue Montaigne - BP 561 56017 Vannes Cedex
 * 
 * OrbisGIS is distributed under GPL 3 license.
 *
 * Copyright (C) 2007-2014 CNRS (IRSTV FR CNRS 2488)
 * Copyright (C) 2015-2017 CNRS (Lab-STICC UMR CNRS 6285)
 *
 * This file is part of OrbisGIS.
 *
 * OrbisGIS is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * OrbisGIS is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * OrbisGIS. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly:
 * info_at_ orbisgis.org
 */
package org.orbisgis.sif;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;

/**
 * Class for commenting SQL code in the SQL console and Java code in the
 * Beanshell console.
 *
 * @author Adam Gouge
 */
public class CommentUtil {

    public static final String SQL_COMMENT_CHARACTER = "--";
    public static final String JAVA_COMMENT_CHARACTER = "//";
    public static final String R_COMMENT_CHARACTER = "#";
    public static final String BLOCK_COMMENT_START = "/*";
    public static final String BLOCK_COMMENT_END = "*/";
    private final static I18n I18N = I18nFactory.getI18n(CommentUtil.class);
    private final static Logger LOGGER = LoggerFactory.getLogger(CommentUtil.class);

    /**
     * Comment the selected text in the given script panel.
     *
     * @param scriptPanel Script panel
     */
    public static void commentOrUncommentSQL(JTextArea scriptPanel) {
        commentOrUncomment(scriptPanel, SQL_COMMENT_CHARACTER);
    }

    /**
     * Comment the selected text in the given script panel.
     *
     * @param scriptPanel Script panel
     */
    public static void commentOrUncommentJava(JTextArea scriptPanel) {
        commentOrUncomment(scriptPanel, JAVA_COMMENT_CHARACTER);
    }
    
    /**
     * Comment the selected R code in the given script panel.
     *
     * @param scriptPanel Script panel
     */
    public static void commentOrUncommentR(JTextArea scriptPanel) {
        commentOrUncomment(scriptPanel, R_COMMENT_CHARACTER);
    }

    /**
     * Comment or uncomment the selected text in the given script panel.
     *
     * @param scriptPanel Script panel
     */
    private static void commentOrUncomment(JTextArea scriptPanel, String commentCharacter) {
        // If the selection contains an unbroken range of commented lines,
        // then we uncomment.
        if (unbrokenRangeOfComments(scriptPanel, commentCharacter)) {
            uncommentSQL(scriptPanel, commentCharacter);
        } // Otherwise, we comment everything.
        else {
            commentSQL(scriptPanel, commentCharacter);
        }
    }

    /**
     * Test whether the selected text consists of an unbroken range of
     * commented lines.
     *
     * @param scriptPanel Script panel
     * @return True iff the selected text consists of an unbroken range of
     * commented lines
     */
    private static boolean unbrokenRangeOfComments(JTextArea scriptPanel, String commentCharacter) {

        final Element root = scriptPanel.getDocument().getDefaultRootElement();

        final int numberOfLastLine = root.getElementIndex(scriptPanel.getSelectionEnd());
        int currentLineNumber = root.getElementIndex(scriptPanel.getSelectionStart());
        while (currentLineNumber <= numberOfLastLine) {
            try {
                int startOffset = root.getElement(currentLineNumber).getStartOffset();
                if (!scriptPanel.getText(startOffset, commentCharacter.length()).equals(commentCharacter)) {
                    return false;
                }
            } catch (BadLocationException e) {
                LOGGER.warn(I18N.tr("Problem when checking for an unbroken range of comments"), e);
            }
            currentLineNumber++;
        }
        return true;
    }

    /**
     * Block comment or uncomment the selected text in the given script panel.
     *
     * @param scriptPanel Script panel
     */
    public static void blockCommentOrUncomment(JTextArea scriptPanel) {
        if (scriptPanel.getSelectedText() != null) {
            if (alreadyBlockCommented(scriptPanel)) {
                blockUncomment(scriptPanel);
            } else {
                blockComment(scriptPanel);
            }
        }
    }

    /**
     * Return true iff the selection is already block commented.
     * @param scriptPanel Script panel
     * @return True iff the selection is already block commented
     */
    private static boolean alreadyBlockCommented(JTextArea scriptPanel) {
        try {
            return scriptPanel.getText(scriptPanel.getSelectionStart(), BLOCK_COMMENT_START.length()).equals(BLOCK_COMMENT_START)
                    && scriptPanel.getText(scriptPanel.getSelectionEnd() - BLOCK_COMMENT_END.length(), BLOCK_COMMENT_END.length()).equals(BLOCK_COMMENT_END);
        } catch (BadLocationException e) {
            LOGGER.warn(I18N.tr("Assuming the selection is not already block commented."), e);
        }
        return false;
    }


    /**
     * Block comment the selected text in the given script panel.
     *
     * @param scriptPanel Script panel
     */
    private static void blockComment(JTextArea scriptPanel) {
        // Recover the index of the start of the selection.
        final int startOffset = scriptPanel.getSelectionStart();
        // Comment the selection.
        final String commentedSelection = BLOCK_COMMENT_START
                + scriptPanel.getSelectedText() + BLOCK_COMMENT_END;
        scriptPanel.replaceSelection(commentedSelection);
        // Select the commented selection.
        scriptPanel.setSelectionStart(startOffset);
        scriptPanel.setSelectionEnd(startOffset + commentedSelection.length());
    }

    /**
     * Block uncomment the selected text in the given script panel.
     *
     * @param scriptPanel Script panel
     */
    private static void blockUncomment(JTextArea scriptPanel) {
        // Recover the index of the start of the selection.
        final int startOffset = scriptPanel.getSelectionStart();
        final int endOffset = scriptPanel.getSelectionEnd();
        // Delete the comment characters.
        scriptPanel.replaceRange("", endOffset - BLOCK_COMMENT_END.length(), endOffset);
        scriptPanel.replaceRange("", startOffset, startOffset + BLOCK_COMMENT_START.length());
        // Select the uncommented selection.
        scriptPanel.setSelectionStart(startOffset);
        scriptPanel.setSelectionEnd(endOffset - BLOCK_COMMENT_START.length()
                - BLOCK_COMMENT_END.length());
    }

    /**
     * Comment the selected text in the given script panel.
     *
     * @param scriptPanel Script panel
     */
    private static void commentSQL(JTextArea scriptPanel, String commentCharacter) {

        final Element root = scriptPanel.getDocument().getDefaultRootElement();

        final int numberOfLastLine = root.getElementIndex(scriptPanel.getSelectionEnd());
        int currentLineNumber = root.getElementIndex(scriptPanel.getSelectionStart());
        while (currentLineNumber <= numberOfLastLine) {
            scriptPanel.insert(commentCharacter,
                    root.getElement(currentLineNumber).getStartOffset());
            currentLineNumber++;
        }
    }

    /**
     * Uncomment the selected text in the given script panel.
     *
     * @param scriptPanel Script panel
     */
    private static void uncommentSQL(JTextArea scriptPanel, String commentCharacter) {

        final Element root = scriptPanel.getDocument().getDefaultRootElement();

        final int numberOfLastLine = root.getElementIndex(scriptPanel.getSelectionEnd());
        int currentLineNumber = root.getElementIndex(scriptPanel.getSelectionStart());
        while (currentLineNumber <= numberOfLastLine) {
            try {
                int startOffset = root.getElement(currentLineNumber).getStartOffset();
                if (scriptPanel.getText(startOffset, commentCharacter.length()).equals(commentCharacter)) {
                    scriptPanel.replaceRange("", startOffset, startOffset + commentCharacter.length());
                }
            } catch (BadLocationException e) {
                LOGGER.warn(I18N.tr("Invalid length or offset when trying to uncomment code."), e);
            }
            currentLineNumber++;
        }
    }
}
