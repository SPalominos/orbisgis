/*
 * OrbisGIS is a GIS application dedicated to scientific spatial simulation.
 * This cross-platform GIS is developed at French IRSTV institute and is able to
 * manipulate and create vector and raster spatial information. OrbisGIS is
 * distributed under GPL 3 license. It is produced by the "Atelier SIG" team of
 * the IRSTV Institute <http://www.irstv.cnrs.fr/> CNRS FR 2488.
 *
 *
 *  Team leader Erwan BOCHER, scientific researcher,
 *
 *  User support leader : Gwendall Petit, geomatic engineer.
 *
 *
 * Copyright (C) 2007 Erwan BOCHER, Fernando GONZALEZ CORTES, Thomas LEDUC
 *
 * Copyright (C) 2010 Erwan BOCHER, Pierre-Yves FADET, Alexis GUEGANNO, Maxence LAURENT
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
 *
 * or contact directly:
 * erwan.bocher _at_ ec-nantes.fr
 * gwendall.petit _at_ ec-nantes.fr
 */
package org.orbisgis.core.ui.editorViews.toc.actions.cui.graphic;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import org.orbisgis.core.renderer.se.graphic.ViewBox;
import org.orbisgis.core.renderer.se.parameter.real.RealParameter;
import org.orbisgis.core.ui.editorViews.toc.actions.cui.LegendUIComponent;
import org.orbisgis.core.ui.editorViews.toc.actions.cui.LegendUIController;
import org.orbisgis.core.ui.editorViews.toc.actions.cui.parameter.real.LegendUIMetaRealPanel;
import org.orbisgis.core.ui.preferences.lookandfeel.OrbisGISIcon;

/**
 *
 * @author maxence
 */
public abstract class LegendUIViewBoxPanel extends LegendUIComponent {

	protected ViewBox viewbox;
	private LegendUIMetaRealPanel width;
	private LegendUIMetaRealPanel height;

	public LegendUIViewBoxPanel(LegendUIController controller, LegendUIComponent parent, ViewBox vbox, boolean isNullable) {
		super("View Box", controller, parent, 0, isNullable);
		this.viewbox = vbox;

		if (viewbox == null){
			viewbox = new ViewBox();
			isNullable = true;
		}

		this.setBorder(BorderFactory.createTitledBorder(getName()));

		height = new LegendUIMetaRealPanel("h", controller, this, vbox.getHeight(), true) {

			@Override
			public void realChanged(RealParameter newReal) {
				viewbox.setHeight(newReal);
			}
		};
		height.init();

		width = new LegendUIMetaRealPanel("w", controller, this, vbox.getWidth(), true) {

			@Override
			public void realChanged(RealParameter newReal) {
				viewbox.setWidth(newReal);
			}
		};
		width.init();
	}

	@Override
	public Icon getIcon() {
		return OrbisGISIcon.PALETTE;
	}

	@Override
	protected void mountComponent() {
		editor.add(width, BorderLayout.NORTH);
		editor.add(height, BorderLayout.SOUTH);
	}

	@Override
	public Class getEditedClass() {
		return ViewBox.class;
	}

	@Override
	protected void turnOff() {
		viewBoxChanged(null);
	}

	@Override
	protected void turnOn() {
		viewBoxChanged(viewbox);
	}

	public abstract void viewBoxChanged(ViewBox newViewBox);
}
