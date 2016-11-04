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
 * Copyright (C) 2015-2016 CNRS (Lab-STICC UMR CNRS 6285)
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

import org.orbisgis.coremap.renderer.se.parameter.ParameterException;
import org.orbisgis.legend.IInterpolationLegend;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.beans.EventHandler;

/**
 * Spinner for the minimum symbol size in proportional legends. We pass a
 * {@link MaxSizeSpinner} to the constructor in order to make sure that the min
 * value is always <= the max value.
 *
 * @author Adam Gouge
 */
public class MinSizeSpinner extends AbsSpinner {

    /**
     * Constructor
     *
     * @param legend         Legend
     * @param maxSizeSpinner The max size spinner whose value controls how
     *                       large the min size spinner's maximum value
     * @throws ParameterException If the legend's first value cannot be
     *                            retrieved.
     */
    public MinSizeSpinner(IInterpolationLegend legend,
                          MaxSizeSpinner maxSizeSpinner) throws ParameterException {
        super(new SpinnerNumberModel(
                legend.getFirstValue(), 0, Double.POSITIVE_INFINITY, LARGE_STEP));
        addChangeListener(EventHandler.create(
                ChangeListener.class, legend, "firstValue", "source.value"));
        maxSizeSpinner.addChangeListener(EventHandler.create(
                ChangeListener.class, this.getModel(), "maximum", "source.value"));
    }

    @Override
    protected double getSpinStep() {
        return LARGE_STEP;
    }
}
