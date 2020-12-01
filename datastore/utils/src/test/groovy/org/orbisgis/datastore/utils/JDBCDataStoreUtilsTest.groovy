/*
 * Bundle datastore/utils is part of the OrbisGIS platform
 *
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
 * OSM is distributed under LGPL 3 license.
 *
 * Copyright (C) 2020 CNRS (Lab-STICC UMR CNRS 6285)
 *
 *
 * OSM is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * OSM is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * OSM. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly:
 * info_at_ orbisgis.org
 */
package org.orbisgis.datastore.utils

import org.geotools.data.DataStoreFinder
import org.geotools.jdbc.JDBCDataStore
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
/**
 * Test class dedicated to {@link org.orbisgis.datastore.utils.JDBCDataStoreUtils}.
 *
 * @author Erwan Bocher (CNRS 2020)
 * @author Sylvain PALOMINOS (UBS chaire GEOTERA 2020)
 */
class JDBCDataStoreUtilsTest {

    private static JDBCDataStore ds

    @BeforeAll
    static void beforeAll() {
        def dataStore = DataStoreFinder.getDataStore([dbtype: "h2gis", database: "./target/database_${UUID.randomUUID()}"])
        assert dataStore in JDBCDataStore
        ds = (JDBCDataStore) dataStore
        ds.connection.execute("""
            CREATE TABLE elements (
                id int,
                name varchar(255),
                number int
            );
            INSERT INTO elements (id, name, number) VALUES (1, 'Simple Name', 2846);
            INSERT INTO elements (id, name, number) VALUES (2, 'Maybe a complex Name', 7455);
            INSERT INTO elements (id, name, number) VALUES (3, 'S N', 9272);
        """)
    }

    @Test
    void queryTest() {
        def str = "";
        ds.query("SELECT * FROM elements WHERE id > 1")
                {while(it.next()) {
                    str+=it.name+" "+it.number+" "
                }}
        assert "Maybe a complex Name 7455 S N 9272 " == str

        str = "";
        ds.query("SELECT * FROM elements WHERE id > ?", [1])
                { while(it.next()) {
                    str+=it.name+" "+it.number+" "
                }}
        assert "Maybe a complex Name 7455 S N 9272 " == str

        str = "";
        ds.query("SELECT * FROM elements WHERE id > :id", [id:1])
                { while(it.next()) {
                    str+=it.name+" "+it.number+" "
                }}
        assert "Maybe a complex Name 7455 S N 9272 " == str

        str = "";
        ds.query([id:1], "SELECT * FROM elements WHERE id > :id")
                { while(it.next()) {
                    str+=it.name+" "+it.number+" "
                }}
        assert "Maybe a complex Name 7455 S N 9272 " == str

        str = "";
        def id = 1
        ds.query("SELECT * FROM elements WHERE id > $id")
                { while(it.next()) {
                    str+=it.name+" "+it.number+" "
                }}
        assert "Maybe a complex Name 7455 S N 9272 " == str
    }

    @Test
    void eachRowTest() {
        def str = "";
        ds.eachRow("SELECT * FROM elements WHERE id > 1")
                { str+=it.name+" "+it.number+" " }
        assert "Maybe a complex Name 7455 S N 9272 " == str

        str = "";
        ds.eachRow("SELECT * FROM elements", 2, 1)
                { str+=it.name+" "+it.number+" " }
        assert "Maybe a complex Name 7455 " == str
        str = "";

        ds.eachRow("SELECT * FROM elements WHERE id > 1")
                { str+=it.getColumnName(2) + " " + it.getColumnName(3) + " "}
                { str+=it.name+" "+it.number+" " }
        assert "NAME NUMBER Maybe a complex Name 7455 S N 9272 " == str
        str = "";

        ds.eachRow("SELECT * FROM elements" ,
                { str+=it.getColumnName(2) + " " + it.getColumnName(3) + " "}, 2, 1)
                { str+=it.name+" "+it.number+" " }
        assert "NAME NUMBER Maybe a complex Name 7455 " == str
        str = "";

        ds.eachRow("SELECT * FROM elements WHERE id > ?" , [1],
                { str+=it.getColumnName(2) + " " + it.getColumnName(3) + " "}, 1, 1)
                { str+=it.name+" "+it.number+" " }
        assert "NAME NUMBER Maybe a complex Name 7455 " == str
        str = "";

        ds.eachRow("SELECT * FROM elements WHERE id > :id" , [id:1],
                { str+=it.getColumnName(2) + " " + it.getColumnName(3) + " "}, 1, 1)
                { str+=it.name+" "+it.number+" " }
        assert "NAME NUMBER Maybe a complex Name 7455 " == str
        str = "";

        ds.eachRow([id:1], "SELECT * FROM elements WHERE id > :id" ,
                { str+=it.getColumnName(2) + " " + it.getColumnName(3) + " "}, 1, 1)
                { str+=it.name+" "+it.number+" " }
        assert "NAME NUMBER Maybe a complex Name 7455 " == str
        str = "";

        ds.eachRow("SELECT * FROM elements WHERE id > ?" , [1])
                { str+=it.getColumnName(2) + " " + it.getColumnName(3) + " "}
                { str+=it.name+" "+it.number+" " }
        assert "NAME NUMBER Maybe a complex Name 7455 S N 9272 " == str
        str = "";

        ds.eachRow("SELECT * FROM elements WHERE id > :id" , [id:1])
                { str+=it.getColumnName(2) + " " + it.getColumnName(3) + " "}
                { str+=it.name+" "+it.number+" " }
        assert "NAME NUMBER Maybe a complex Name 7455 S N 9272 " == str
        str = "";

        ds.eachRow([id:1], "SELECT * FROM elements WHERE id > :id" )
                { str+=it.getColumnName(2) + " " + it.getColumnName(3) + " "}
                { str+=it.name+" "+it.number+" " }
        assert "NAME NUMBER Maybe a complex Name 7455 S N 9272 " == str
        str = "";

        ds.eachRow("SELECT * FROM elements WHERE id > ?" , [1])
                { str+=it.name+" "+it.number+" " }
        assert "Maybe a complex Name 7455 S N 9272 " == str
        str = "";

        ds.eachRow("SELECT * FROM elements WHERE id > :id" , [id:1])
                { str+=it.name+" "+it.number+" " }
        assert "Maybe a complex Name 7455 S N 9272 " == str
        str = "";

        ds.eachRow([id:1], "SELECT * FROM elements WHERE id > :id" )
                { str+=it.name+" "+it.number+" " }
        assert "Maybe a complex Name 7455 S N 9272 " == str
        str = "";

        ds.eachRow("SELECT * FROM elements WHERE id > ?" , [1], 1, 1)
                { str+=it.name+" "+it.number+" " }
        assert "Maybe a complex Name 7455 " == str
        str = "";

        ds.eachRow("SELECT * FROM elements WHERE id > :id" , [id:1], 1, 1)
                { str+=it.name+" "+it.number+" " }
        assert "Maybe a complex Name 7455 " == str
        str = "";

        ds.eachRow([id:1], "SELECT * FROM elements WHERE id > :id" , 1, 1)
                { str+=it.name+" "+it.number+" " }
        assert "Maybe a complex Name 7455 " == str
        str = "";

        ds.eachRow("SELECT * FROM elements WHERE id > ${1}")
                { str+=it.name+" "+it.number+" " }
        assert "Maybe a complex Name 7455 S N 9272 " == str

        str = "";
        ds.eachRow("SELECT * FROM elements WHERE id > ${1}", 1, 1)
                { str+=it.name+" "+it.number+" " }
        assert "Maybe a complex Name 7455 " == str
        str = "";

        ds.eachRow("SELECT * FROM elements WHERE id > ${1}")
                { str+=it.getColumnName(2) + " " + it.getColumnName(3) + " "}
                { str+=it.name+" "+it.number+" " }
        assert "NAME NUMBER Maybe a complex Name 7455 S N 9272 " == str
        str = "";

        ds.eachRow("SELECT * FROM elements WHERE id > ${1}" ,
                { str+=it.getColumnName(2) + " " + it.getColumnName(3) + " "}, 1, 1)
                { str+=it.name+" "+it.number+" " }
        assert "NAME NUMBER Maybe a complex Name 7455 " == str
        str = "";
    }
}
