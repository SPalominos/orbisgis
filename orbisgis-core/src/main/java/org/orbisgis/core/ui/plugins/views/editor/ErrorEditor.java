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

package org.orbisgis.core.ui.plugins.views.editor;

import java.awt.Component;

import javax.swing.JLabel;

import org.orbisgis.core.edition.EditableElement;
import org.orbisgis.core.edition.EditableElementException;
import org.orbisgis.core.edition.EditableElementListener;
import org.orbisgis.core.ui.editor.IEditor;
import org.orbisgis.core.ui.pluginSystem.PlugInContext;
import org.orbisgis.core.ui.pluginSystem.ViewPlugIn;
import org.orbisgis.progress.IProgressMonitor;

public class ErrorEditor implements IEditor {

	private String message;
	private String documentName;

	public ErrorEditor(String documentName, String message) {
		this.message = message;
		this.documentName = documentName;
	}

	public boolean acceptElement(String typeId) {
		return false;
	}

	public EditableElement getElement() {
		return new DummyElement();
	}

	public String getTitle() {
		return documentName;
	}

	public void setElement(EditableElement obj) {
	}

	public void delete() {
	}

	public Component getComponent() {
		return new JLabel(message);
	}

	public void loadStatus() {
	}

	public void saveStatus() {
	}

	private class DummyElement implements EditableElement {

		public void close(IProgressMonitor progressMonitor)
				throws UnsupportedOperationException {

		}

		public Object getObject() throws UnsupportedOperationException {
			return null;
		}

		public String getTypeId() {
			return "org.orbisgis.plugins.geocognition.Error";
		}

		public void open(IProgressMonitor progressMonitor)
				throws UnsupportedOperationException, EditableElementException {

		}

		public void save() throws UnsupportedOperationException {

		}

		public boolean isModified() {
			return false;
		}

		public String getId() {
			return null;
		}

		public void addElementListener(EditableElementListener listener) {
		}

		public boolean removeElementListener(EditableElementListener listener) {
			return true;
		}

	}

	public ViewPlugIn getView() {
		return null;
	}

	public void initialize(PlugInContext wbContext)	throws Exception {

	}
}
