/*
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
package org.orbisgis.geocatalogtree.impl;

import org.orbisgis.editorjdbc.EditableSource;
import org.orbisgis.geocatalogtree.api.GeoCatalogTreeNode;
import org.orbisgis.geocatalogtree.api.TreeNodeFactory;
import org.orbisgis.sif.components.fstree.TransferableList;
import org.orbisgis.sif.components.resourceTree.TreeSelectionIterable;
import org.orbisgis.sif.edition.EditableElement;
import org.orbisgis.sif.edition.EditorTransferHandler;

import javax.swing.JComponent;
import javax.swing.JTree;
import java.awt.datatransfer.Transferable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Manager Drag&Drop events on DbTree
 * @author Nicolas Fortin
 */
public class DBTreeTranferHandler extends EditorTransferHandler {
    private JTree tree;

    public DBTreeTranferHandler(JTree tree) {
        this.tree = tree;
    }

    @Override
    public int getSourceActions(JComponent jc) {
        return COPY_OR_MOVE;
    }

    @Override
    protected boolean canImportEditableElement(EditableElement editableElement) {
        return false;
    }

    @Override
    protected Transferable createTransferable(JComponent c) {
        Set<TreeNodeFactory> treeNodeFactorySet = new HashSet<>();
        for(GeoCatalogTreeNode treeNode : new TreeSelectionIterable<>(tree.getSelectionPaths(), GeoCatalogTreeNode.class)) {
            if(treeNode.getFactory() != null) {
                treeNodeFactorySet.add(treeNode.getFactory());
            }
        }
        List<Transferable> transferableList = new ArrayList<>(treeNodeFactorySet.size());
        for(TreeNodeFactory treeNodeFactory : treeNodeFactorySet) {
            Transferable transferable = treeNodeFactory.createTransferable(tree);
            if(transferable != null) {
                transferableList.add(transferable);
            }
        }
        if(transferableList.isEmpty()) {
            return super.createTransferable(c);
        } else if(transferableList.size() == 1) {
            return transferableList.get(0);
        } else {
            TransferableList transferable = new TransferableList();
            for(Transferable transferableItem : transferableList) {
                transferable.addTransferable(transferableItem);
            }
            return transferable;
        }
    }
}
