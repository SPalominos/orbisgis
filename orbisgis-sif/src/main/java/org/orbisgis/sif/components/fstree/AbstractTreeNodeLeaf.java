/**
 * OrbisGIS is a GIS application dedicated to scientific spatial analysis.
 * This cross-platform GIS is developed at the Lab-STICC laboratory by the DECIDE 
 * team located in University of South Brittany, Vannes.
 * 
 * OrbisGIS is distributed under GPL 3 license.
 *
 * Copyright (C) 2007-2014 IRSTV (FR CNRS 2488)
 * Copyright (C) 2015-2016 CNRS (UMR CNRS 6285)
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
package org.orbisgis.sif.components.fstree;

import java.util.Enumeration;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

/**
 * A node without sub-nodes
 * @author Nicolas Fortin
 */
public abstract class AbstractTreeNodeLeaf extends AbstractTreeNode {

        @Override
        public void insert(MutableTreeNode mtn, int i) {
                throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public void remove(int i) {
                throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public void remove(MutableTreeNode mtn) {
                throw new UnsupportedOperationException("Not supported.");
        }
        
        @Override
        public TreeNode getChildAt(int i) {
                throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public int getChildCount() {
                return 0;
        }

        @Override
        public int getIndex(TreeNode tn) {
                throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public boolean getAllowsChildren() {
                return false;
        }

        @Override
        public boolean isLeaf() {
                return true;
        }

        @Override
        public Enumeration<? extends Object> children() {
                throw new UnsupportedOperationException("Not supported.");
        } 
}
