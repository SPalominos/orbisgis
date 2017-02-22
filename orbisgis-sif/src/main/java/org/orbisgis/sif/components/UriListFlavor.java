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
package org.orbisgis.sif.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Management of URI for data transfer (drag&drop).
 * @author Nicolas Fortin
 */
public class UriListFlavor {
        private DataFlavor uriListFlavor = null;
        private static final Logger LOGGER = LoggerFactory.getLogger(UriListFlavor.class);
        //RFC 2483 says that lines are terminated with a CRLF pair
        private String lineSep = System.getProperty("line.separator");
        public UriListFlavor() {
                try {
                        uriListFlavor = new DataFlavor("text/uri-list;class=java.lang.String");
                } catch (ClassNotFoundException ex) {
                        LOGGER.error("Uri flavor not supported",ex);
                }
        }

        public DataFlavor getUriListFlavor() {
                return uriListFlavor;
        }
        /**
         * Convert a list of URI into a Transferable data
         * @param uriList
         * @return 
         */
        public String getTransferableData(List<URI> uriList) {
                StringBuilder transferData = new StringBuilder();
                for(URI uri : uriList) {
                        transferData.append(uri);
                        transferData.append(lineSep);
                }
                return transferData.toString();
        }
        /**
         * Convert a transferable into a list of URI
         * @param transferable
         * @return
         * @throws IOException
         * @throws UnsupportedFlavorException 
         */
        public List<URI> getUriList(Transferable transferable) throws IOException, UnsupportedFlavorException {
                List<URI> uriList = new ArrayList<URI>();
                String filesChain = (String) transferable.getTransferData(uriListFlavor);
                for (StringTokenizer stringTokenizer = new StringTokenizer(filesChain, lineSep); stringTokenizer.hasMoreTokens();) {
                        String line = stringTokenizer.nextToken();
                        //If the URI line is not a comment
                        if (!(line.startsWith("#") || line.isEmpty())) {
                                try {
                                        URI dataUri = new URI(line.trim());
                                        uriList.add(dataUri);
                                } catch (URISyntaxException ex) {
                                        LOGGER.debug(ex.getLocalizedMessage(), ex);
                                }
                        }
                }
                return uriList;
        }
}
