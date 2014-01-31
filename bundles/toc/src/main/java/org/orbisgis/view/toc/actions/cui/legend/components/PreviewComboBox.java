/**
 * OrbisGIS is a GIS application dedicated to scientific spatial simulation.
 * This cross-platform GIS is developed at French IRSTV institute and is able to
 * manipulate and create vector and raster spatial information.
 *
 * OrbisGIS is distributed under GPL 3 license. It is produced by the "Atelier SIG"
 * team of the IRSTV Institute <http://www.irstv.fr/> CNRS FR 2488.
 *
 * Copyright (C) 2007-2012 IRSTV (FR CNRS 2488)
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
package org.orbisgis.view.toc.actions.cui.legend.components;

import org.orbisgis.legend.Legend;
import org.orbisgis.view.toc.actions.cui.components.CanvasSE;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Root class for combo boxes that update a preview.
 *
 * @author Adam Gouge
 */
public abstract class PreviewComboBox<E> extends AbsComboBox<E> {

    protected CanvasSE preview;

    /**
     * Constructor
     *
     * @param items   Items to be contained in the combo box
     * @param legend  Legend
     * @param preview Preview
     */
    public PreviewComboBox(E[] items,
                           Legend legend,
                           CanvasSE preview) {
        super(items);
        this.legend = legend;
        this.preview = preview;
        if (preview != null) {
            addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    updatePreview();
                }
            });
        }
    }

    /**
     * Update the preview according to the item selected in the combo box.
     */
    protected abstract void updatePreview();
}
