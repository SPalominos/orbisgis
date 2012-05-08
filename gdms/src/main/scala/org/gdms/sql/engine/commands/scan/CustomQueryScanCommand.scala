/**
 * The GDMS library (Generic Datasource Management System)
 * is a middleware dedicated to the management of various kinds of
 * data-sources such as spatial vectorial data or alphanumeric. Based
 * on the JTS library and conform to the OGC simple feature access
 * specifications, it provides a complete and robust API to manipulate
 * in a SQL way remote DBMS (PostgreSQL, H2...) or flat files (.shp,
 * .csv...). It is produced by the "Atelier SIG" team of
 * the IRSTV Institute <http://www.irstv.fr/> CNRS FR 2488.
 *
 *
 * Team leader : Erwan BOCHER, scientific researcher,
 *
 * User support leader : Gwendall Petit, geomatic engineer.
 *
 * Previous computer developer : Pierre-Yves FADET, computer engineer, Thomas LEDUC,
 * scientific researcher, Fernando GONZALEZ CORTES, computer engineer, Maxence LAURENT,
 * computer engineer.
 *
 * Copyright (C) 2007 Erwan BOCHER, Fernando GONZALEZ CORTES, Thomas LEDUC
 *
 * Copyright (C) 2010 Erwan BOCHER, Alexis GUEGANNO, Maxence LAURENT, Antoine GOURLAY
 *
 * Copyright (C) 2012 Erwan BOCHER, Antoine GOURLAY
 *
 * This file is part of Gdms.
 *
 * Gdms is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Gdms is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Gdms. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 *
 * or contact directly:
 * info@orbisgis.org
 */
package org.gdms.sql.engine.commands.scan

import org.gdms.data.DataSource
import org.gdms.data.schema.Metadata
import org.gdms.data.types.TypeFactory
import org.gdms.driver.DataSet
import org.gdms.sql.engine.commands._
import org.gdms.sql.evaluator.Expression
import org.gdms.sql.function.FunctionValidator
import org.gdms.sql.function.table.TableFunction
import org.orbisgis.progress.NullProgressMonitor
import org.gdms.sql.engine.GdmSQLPredef._
import org.orbisgis.progress.ProgressMonitor

/**
 * This class handles the "Scan" of the result of a custom query.
 *
 * The first argument <tt>e</tt> is a list of (constant-based) expressions.
 * The second is a sequence of tables, either:
 *  - the name of a table
 *  - an OutputCommand, that is the result of another custom query
 *    (or in the futur a sub-query...)
 *
 * This Command is also an Output command so as not to need another OutputCommand
 * on top of it to feed it to an operator that needs DataSet objects. Having
 * a QueryOutputCommand on top adds one unnecessary level of caching which consume
 * more disk space and slows down the execution.
 * 
 * @param e expressions to give to the function
 * @param tables tables to give to the function
 * @param f the table function
 * @param alias an alias for the result set of the function
 * @author Antoine Gourlay
 * @since 0.1
 */
class CustomQueryScanCommand(e: Seq[Expression], tables: Seq[Either[String, OutputCommand]], f: TableFunction, alias: Option[String] = None) extends Command with ExpressionCommand with OutputCommand {

  // finds the actual child commands
  children = tables flatMap (_.right toSeq) toList

  protected def exp = e

  private var metadata: Metadata = _

  // holds all tables opened before given to the function
  private var openedTables: List[Either[DataSource, OutputCommand]] = Nil

  // holds the result set of the function
  private var ds: DataSet = _

  override def doPrepare = {
    // initialize expressions
    super.doPrepare

    // functions to open the inputs and get their metadata
    def forDs: String => Metadata = { s =>
      val ds = dsf.getDataSource(s)
      ds.open
      openedTables = Left(ds) :: openedTables
      ds.getMetadata
    }
    def forOut: OutputCommand => Metadata = {  o =>
      openedTables = Right(o) :: openedTables
      o.getMetadata
    }

    // opens everything and gets all metadata
    val dss = tables map (_ fold(forDs, forOut))
    
    // reverse: they were added in reverse order above
    openedTables = openedTables reverse
    
    // validation of table & type signatures
    FunctionValidator.failIfTablesDoNotMatchSignature(dss toArray, f.getFunctionSignatures)
    val types = e map (ev => TypeFactory.createType(ev.evaluator.sqlType))
    FunctionValidator.failIfTypesDoNotMatchSignature(types toArray, f.getFunctionSignatures)
    
    // result metadata of teh function
    metadata = f.getMetadata(dss toArray)
  }

  protected final def doWork(r: Iterator[RowStream])(implicit pm: Option[ProgressMonitor]) = {
    pm.map(_.startTask("Running table function", 0))
    // evaluates the function
    ds = f.evaluate(dsf,
                    openedTables map (_ fold(identity, _.getResult)) toArray,
                    e map { _ evaluate(emptyRow)} toArray, pm.getOrElse(new NullProgressMonitor))

    // gives the result
    val res = (for (i <- (0l until ds.getRowCount).par.view.toIterator) yield {
      Row(i, ds.getRow(i))
    })
  
    pm.map(_.endTask)
    res
  }

  override def doCleanUp = {
    // closes any DataSource object (the other are closed by the OutputCommand)
    f.workFinished
    openedTables flatMap (_.left toSeq) foreach (_.close)
    openedTables = Nil
    
    super.doCleanUp
  }

  override def getMetadata = SQLMetadata("", metadata)

  def getResult = ds
}
