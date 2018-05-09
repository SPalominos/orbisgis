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
package org.orbisgis.mapeditor.map.tools;

import org.locationtech.jts.geom.Envelope;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D;
import javax.swing.ImageIcon;
import org.orbisgis.coremap.layerModel.MapContext;
import org.orbisgis.mapeditor.map.icons.MapEditorIcons;
import org.orbisgis.mapeditor.map.tool.FinishedAutomatonException;
import org.orbisgis.mapeditor.map.tool.ToolManager;
import org.orbisgis.mapeditor.map.tool.TransitionException;

/**
 * Tool to move the map extent
 * 
 */
public class PanTool extends AbstractDragTool {

        @Override
        public ImageIcon getImageIcon() {
            return MapEditorIcons.getIcon("pan");
        }
        
        

	@Override
	public void transitionTo_MouseReleased(MapContext vc, ToolManager tm)
			throws TransitionException, FinishedAutomatonException {
                // get the current point
		double[] v = tm.getValues();

                // get the start point (of the dragging move)
                double[] firstPoint = getFirstPoint();

                // diff
		double dx = firstPoint[0] - v[0];
		double dy = firstPoint[1] - v[1];

                // move the envelope
		Envelope extent = tm.getMapTransform().getExtent();
        if(extent!=null) {
            tm.getMapTransform().setExtent(
                    new Envelope(extent.getMinX() + dx, extent.getMaxX() + dx,
                            extent.getMinY() + dy, extent.getMaxY() + dy));
        }
                // we're done, this will get us back to StandBy
		transition(Code.FINISHED);
	}

	@Override
	public void drawIn_MouseDown(Graphics g, MapContext vc, ToolManager tm) {
                // this is what is displayed when dragging

                // current point position
                double[] firstPoint = getFirstPoint();

                // get the corresponding point in the map
		Point p = tm.getMapTransform().fromMapPoint(
				new Point2D.Double(firstPoint[0], firstPoint[1]));
		int height = tm.getMapTransform().getHeight();
		int width = tm.getMapTransform().getWidth();

                // draw the new map...
		g.clearRect(0, 0, width, height);
		g.drawImage(tm.getMapTransform().getImage(), tm.getLastMouseX() - p.x,
				tm.getLastMouseY() - p.y, null);
	}

        @Override
	public boolean isEnabled(MapContext vc, ToolManager tm) {
		return ToolUtilities.layerCountGreaterThan(vc, 0);
	}

        @Override
	public String getName() {
		return i18n.tr("Pan");
	}

    @Override
    public String getTooltip() {
        return i18n.tr("The Pan Tool");
    }

    @Override
    public ImageIcon getCursor() {
        return MapEditorIcons.getIcon("pan");
    }
        
}
