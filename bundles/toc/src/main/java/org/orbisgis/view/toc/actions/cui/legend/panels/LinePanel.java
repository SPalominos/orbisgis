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
package org.orbisgis.view.toc.actions.cui.legend.panels;

import org.orbisgis.legend.structure.stroke.ConstantColorAndDashesPSLegend;
import org.orbisgis.legend.structure.stroke.constant.ConstantPenStroke;
import org.orbisgis.legend.structure.stroke.constant.ConstantPenStrokeLegend;
import org.orbisgis.legend.structure.stroke.constant.NullPenStrokeLegend;
import org.orbisgis.legend.thematic.SymbolizerLegend;
import org.orbisgis.legend.thematic.constant.IUniqueSymbolLine;
import org.orbisgis.sif.ComponentUtil;
import org.orbisgis.view.toc.actions.cui.components.CanvasSE;
import org.orbisgis.view.toc.actions.cui.legend.components.ColorLabel;
import org.orbisgis.view.toc.actions.cui.legend.components.DashArrayField;
import org.orbisgis.view.toc.actions.cui.legend.components.LineOpacitySpinner;
import org.orbisgis.view.toc.actions.cui.legend.components.LineUOMComboBox;
import org.orbisgis.view.toc.actions.cui.legend.components.LineWidthSpinner;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

import javax.swing.*;

/**
 * "Unique Symbol - Line" settings panel.
 *
 * @author Adam Gouge
 */
public class LinePanel extends AbsOptionalPanel {

    private static final I18n I18N = I18nFactory.getI18n(LinePanel.class);

    private ConstantPenStroke penStrokeMemory;

    private final boolean displayUom;

    private ColorLabel colorLabel;
    private LineUOMComboBox lineUOMComboBox;
    private LineWidthSpinner lineWidthSpinner;
    private LineOpacitySpinner lineOpacitySpinner;
    private DashArrayField dashArrayField;
    private boolean strokeWasSet;

    /**
     * Constructor
     *
     * @param legend       Legend
     * @param preview      Preview
     * @param title        Title
     * @param showCheckBox Draw the Enable checkbox?
     */
    public LinePanel(IUniqueSymbolLine legend,
                     CanvasSE preview,
                     String title,
                     boolean showCheckBox,
                     boolean displayUom) {
        super(legend, preview, title, showCheckBox);
        this.displayUom = displayUom;
        init();
        addComponents();
    }

    @Override
    protected IUniqueSymbolLine getLegend() {
        return (IUniqueSymbolLine) legend;
    }

    @Override
    protected void init() {
        penStrokeMemory = getLegend().getPenStroke();
        //If the stroke was not set, we want to store a default
        //ConstantPenStrokeLegend in penStrokeMemory in order to be able
        //to retrieve it later.
        strokeWasSet = !(penStrokeMemory == null || penStrokeMemory instanceof NullPenStrokeLegend);
        if(!strokeWasSet ){
            if(showCheckBox){
                enableCheckBox.setSelected(false);
            }
            penStrokeMemory = new ConstantPenStrokeLegend();
        }
        colorLabel = new ColorLabel(penStrokeMemory.getFillLegend(), preview);
        if (displayUom) {
            lineUOMComboBox =
                    new LineUOMComboBox((SymbolizerLegend) legend, preview);
        }
        lineWidthSpinner =
                new LineWidthSpinner(penStrokeMemory, preview);
        lineOpacitySpinner =
                new LineOpacitySpinner(penStrokeMemory.getFillLegend(), preview);
        if (penStrokeMemory instanceof ConstantColorAndDashesPSLegend) {
            dashArrayField =
                    new DashArrayField((ConstantColorAndDashesPSLegend) penStrokeMemory, preview);
        } else {
            throw new IllegalStateException("Legend " +
                    getLegend().getLegendTypeName() + " must have a " +
                    "ConstantColorAndDashesPSLegend penstroke in order to " +
                    "initialize the DashArrayField.");
        }
    }

    @Override
    protected void addComponents() {
        // Enable checkbox (if optional).
        if (showCheckBox) {
            add(enableCheckBox, "align l");
        } else {
            // Just add blank space
            add(Box.createGlue());
        }
        // Line color
        add(colorLabel);
        // Unit of measure - line width
        if (displayUom) {
            add(new JLabel(I18N.tr(LINE_WIDTH_UNIT)));
            add(lineUOMComboBox, COMBO_BOX_CONSTRAINTS);
        }
        // Line width
        add(new JLabel(I18N.tr(WIDTH)));
        add(lineWidthSpinner, "growx");
        // Line opacity
        add(new JLabel(I18N.tr(OPACITY)));
        add(lineOpacitySpinner, "growx");
        // Dash array
        add(new JLabel(I18N.tr(DASH_ARRAY)));
        add(dashArrayField, "growx");
        setFieldsState(strokeWasSet);
    }

    @Override
    protected void onClickOptionalCheckBox() {
        if (enableCheckBox.isSelected()) {
            getLegend().setPenStroke(penStrokeMemory);
            setFieldsState(true);
        } else {
            // Remember the old configuration.
            penStrokeMemory = getLegend().getPenStroke();
            getLegend().setPenStroke(new NullPenStrokeLegend());
            setFieldsState(false);
        }
        preview.imageChanged();
    }

    @Override
    protected void setFieldsState(boolean enable) {
        ComponentUtil.setFieldState(enable, colorLabel);
        if (displayUom) {
            if (lineUOMComboBox != null) {
                ComponentUtil.setFieldState(enable, lineUOMComboBox);
            }
        }
        ComponentUtil.setFieldState(enable, lineWidthSpinner);
        ComponentUtil.setFieldState(enable, lineOpacitySpinner);
        ComponentUtil.setFieldState(enable, dashArrayField);
    }
}
