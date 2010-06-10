/**
 * OrbisGIS is a GIS application dedicated to scientific spatial simulation.
 * This cross-platform GIS is developed at French IRSTV institute and is able to
 * manipulate and create vector and raster spatial information. OrbisGIS is
 * distributed under GPL 3 license. It is produced by the geo-informatic team of
 * the IRSTV Institute <http://www.irstv.cnrs.fr/> CNRS FR 2488.
 * 
 *  
 *  Lead Erwan BOCHER, scientific researcher, 
 *
 *  Developer lead : Pierre-Yves FADET, computer engineer. 
 *  
 *  User support lead : Gwendall Petit, geomatic engineer. 
 * 
 * Previous computer developer : Thomas LEDUC, scientific researcher, Fernando GONZALEZ
 * CORTES, computer engineer.
 * 
 * Copyright (C) 2007 Erwan BOCHER, Fernando GONZALEZ CORTES, Thomas LEDUC
 * 
 * Copyright (C) 2010 Erwan BOCHER, Fernando GONZALEZ CORTES, Thomas LEDUC
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
 * For more information, please consult: <http://orbisgis.cerma.archi.fr/>
 * <http://sourcesup.cru.fr/projects/orbisgis/>
 * 
 * or contact directly: 
 * erwan.bocher _at_ ec-nantes.fr 
 * Pierre-Yves.Fadet _at_ ec-nantes.fr
 * gwendall.petit _at_ ec-nantes.fr
 **/

package org.orbisgis.core.ui.plugins.views.sqlConsole.syntax;

import java.awt.Color;

import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import org.orbisgis.core.ui.components.text.UndoableDocument;

public abstract class AbstractSyntaxColoringDocument extends UndoableDocument {
	protected JTextPane textPane;
	protected boolean styling = false;

	protected static SimpleAttributeSet getStyle(Color color) {
		SimpleAttributeSet id = new SimpleAttributeSet();
		StyleConstants.setForeground(id, color);
		StyleConstants.setFontFamily(id, "Monospaced");
		return id;
	}

	public AbstractSyntaxColoringDocument(JTextPane textPane) {
		super();
		this.textPane = textPane;
		this.setDocumentFilter(new ColorizerDocumentFilter());
		try {
			colorize(0, textPane.getText().length());
		} catch (BadLocationException e) {
		}
	}

	private void colorize(int init, int end) throws BadLocationException {
		if (end > 0) {
			int caretPosition = textPane.getCaretPosition();
			int selStart = textPane.getSelectionStart();
			int selEnd = textPane.getSelectionEnd();
			colorIn(init, end);
			textPane.setCaretPosition(caretPosition);
			textPane.setSelectionStart(selStart);
			textPane.setSelectionEnd(selEnd);
		}
	}

	protected abstract void colorIn(int init, int end)
			throws BadLocationException;

	private final class ColorizerDocumentFilter extends DocumentFilter {
		public void replace(FilterBypass fb, int offset, int length,
				String text, AttributeSet attrs) throws BadLocationException {
			// replace tabs by spaces. Grammar needs that
			text = text.replaceAll("\t", "    ");

			// Indent as many spaces as after the last \n
			if (text.equals("\n")) {
				String currentText = textPane.getText().substring(0, offset);
				int lastIndex = currentText.lastIndexOf("\n");
				for (int i = lastIndex + 1; i < currentText.length()
						&& currentText.charAt(i) == ' '; i++) {
					text += ' ';
				}
			}

			// Style by default
			if (!styling) {
				attrs = getCommentStyle();
			}

			boolean alreadyGrouping = isGrouping;
			if (!alreadyGrouping) {
				groupUndoEdits(true);
			}

			// Save token bounds
			int[] originalTokenBounds = new int[] { 0, 0 };
			if (!styling) {
				originalTokenBounds = getTokenBounds(offset, text.length());
			}

			// Do replacement
			super.replace(fb, offset, length, text, attrs);

			// Get current token bounds
			// If not already applying style, apply it
			int[] currentTokenBounds;
			if (!styling) {
				currentTokenBounds = getTokenBounds(offset, text.length());
				colorize(Math
						.min(originalTokenBounds[0], currentTokenBounds[0]),
						Math.max(originalTokenBounds[1] + length + 1,
								currentTokenBounds[1]));
			}

			if (!alreadyGrouping) {
				groupUndoEdits(false);
			}
		}

		public void remove(FilterBypass fb, int offset, int length)
				throws BadLocationException {
			boolean alreadyGrouping = isGrouping;
			if (!alreadyGrouping) {
				groupUndoEdits(true);
			}

			// Save token bounds
			int[] originalTokenBounds = new int[] { 0, 0 };
			if (!styling) {
				originalTokenBounds = getTokenBounds(offset, length);
			}

			// do removal
			super.remove(fb, offset, length);

			// Get current token bounds
			// If not already applying style, apply it
			if (!styling) {
				int[] currentTokenBounds = getTokenBounds(offset, length);
				colorize(Math
						.min(originalTokenBounds[0], currentTokenBounds[0]),
						Math.max(originalTokenBounds[1] - length - 1,
								currentTokenBounds[1]));
			}

			if (!alreadyGrouping) {
				groupUndoEdits(false);
			}
		}

		public void insertString(FilterBypass fb, int offset, String text,
				AttributeSet attr) throws BadLocationException {
			replace(fb, offset, 0, text, attr);
		}
	}

	protected void styleComment(String sqlText, int startText,
			int lastStyledPos, int beginPos) throws BadLocationException {
		int spaceLength = beginPos - lastStyledPos;
		String comment = sqlText.substring(lastStyledPos, lastStyledPos
				+ spaceLength);
		if (comment.trim().length() > 0) {
			styling = true;
			super.remove(startText + lastStyledPos, spaceLength);
			super.insertString(startText + lastStyledPos, comment,
					getCommentStyle());
			styling = false;
		}
	}

	protected void styleError(String sqlText, int startText, int lastStyledPos,
			int beginPos) throws BadLocationException {

		int spaceLength = beginPos - lastStyledPos;
		String comment = sqlText.substring(lastStyledPos, lastStyledPos
				+ spaceLength);
		if (comment.trim().length() > 0) {
			styling = true;
			super.remove(startText + lastStyledPos, spaceLength);
			super.insertString(startText + lastStyledPos, comment,
					getStyle(Color.red));
			styling = false;
		}
	}

	protected abstract int[] getTokenBounds(int offset, int length);

	protected abstract AttributeSet getCommentStyle();
}
