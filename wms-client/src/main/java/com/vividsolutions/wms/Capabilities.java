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





/*
 * The Unified Mapping Platform (JUMP) is an extensible, interactive GUI 
 * for visualizing and manipulating spatial features with geometry and attributes.
 *
 * Copyright (C) 2003 Vivid Solutions
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * For more information, contact:
 *
 * Vivid Solutions
 * Suite #1A
 * 2328 Government Street
 * Victoria BC  V8T 5G5
 * Canada
 *
 * (250)385-6040
 * www.vividsolutions.com
 */

package com.vividsolutions.wms;

import java.util.*;

//import static com.vividsolutions.jump.workbench.ui.plugin.wms.URLWizardPanel.*;

/**
 * Represents the capabilities WMS XML.
 * @author Chris Hodgson chodgson@refractions.net
 */
public class Capabilities {

  private MapLayer topLayer;
  private String title;
  private ArrayList mapFormats;
  private WMService service;
  private String getMapURL, getFeatureInfoURL;

  /** 
   * Creates a new WMS Capabilities object. Should generally only be used by the Parser.
   * @param service the WMService to which these Capabilites belong
   * @param title the title of this WMService
   * @param topLayer the top MapLayer of the entire layer tree
   * @param mapFormats the Collection of supported formats 
   */  
  public Capabilities(WMService service, String title, MapLayer topLayer, Collection mapFormats) {
    this.service = service;
    this.title = title;
    this.topLayer = topLayer;
    this.mapFormats = new ArrayList( mapFormats );
    this.getMapURL = service.getServerUrl();
    this.getFeatureInfoURL = service.getServerUrl();
  }
  
  public Capabilities(WMService service, String title, MapLayer topLayer, Collection mapFormats, String getMapURL, String getFeatureInfoURL) {
      this(service, title, topLayer, mapFormats);
      this.getMapURL = getMapURL;
      this.getFeatureInfoURL = getFeatureInfoURL;
    }

  /**
   * Gets a reference to the service which these Capabilities describe.
   * @return the WMService which these Capabilities describe
   */
  public WMService getService() {
    return service;
  }
  
  /**
   * Gets the top layer for these Capabilities.
   * @return the top MapLayer for these Capabilities
   */
  public MapLayer getTopLayer() {
    return topLayer;
  }
  
  /**
   * Get a MapLayer by name
   */
   public MapLayer getMapLayerByName(String name) {
       return getMapLayerByName(topLayer, name);
   }
   
   private MapLayer getMapLayerByName(MapLayer mapLayer, String name) {
       String mapName = mapLayer.getName();
       // name is not mandatory for layers which are just containers for sublayers 
       if (mapName != null && mapName.equals(name)) return mapLayer;
       for (MapLayer layer : mapLayer.getSubLayerList()) {
           MapLayer r = getMapLayerByName(layer, name);
           if (r != null) return r;
       }
       return null;
   }
  
  /**
   * Gets the title of the Capabilities.
   * @return the title of the map described by these Capabilities
   */
  public String getTitle() {
    return this.title;
  }
  
  public String getGetMapURL() {
      return getMapURL;
  }
  
  public String getGetFeatureInfoURL() {
      return getFeatureInfoURL;
  }
  
  public void setGetMapURL(String url) {
      getMapURL = url;
  }
  
  /**
   * Gets a copy of the list of formats supported by this getMap requests for this map.
   * @return an array containing the formats supported by getMap requests for this map
   */
  public String[] getMapFormats() {
    String[] formats = new String[mapFormats.size()];
    Iterator it = mapFormats.iterator();
    int i = 0;
    while( it.hasNext() ) {
      formats[i++] = (String)it.next();
    }
    return formats;
  }

}
