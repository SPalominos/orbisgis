package org.gdms.data.edition;

import org.gdms.SourceTest;
import org.gdms.data.InternalDataSource;
import org.gdms.data.DataSourceFactory;
import org.gdms.data.values.NullValue;
import org.gdms.data.values.Value;
import org.gdms.spatial.SpatialDataSource;
import org.gdms.spatial.SpatialDataSourceDecorator;

/**
 * DOCUMENT ME!
 *
 * @author Fernando Gonzalez Cortes
 */
public class UndoRedoTests extends SourceTest {

	public void testAlphanumericModifyUndoRedo() throws Exception {
		InternalDataSource d = dsf.getDataSource(super.getAnyNonSpatialResource(),
				DataSourceFactory.UNDOABLE);

		d.open();
		Value v2 = d.getFieldValue(1, 0);
		Value v1 = d.getFieldValue(0, 0);
		d.setFieldValue(0, 0, v2);
		for (int i = 0; i < 100; i++) {
			d.undo();
			assertTrue(equals(d.getFieldValue(0, 0), v1));
			d.redo();
			assertTrue(equals(d.getFieldValue(0, 0), v2));
		}
		d.undo();
		d.commit();
	}

	public void testAlphanumericDeleteUndoRedo() throws Exception {
		InternalDataSource d = dsf.getDataSource(super.getAnyNonSpatialResource(),
				DataSourceFactory.UNDOABLE);

		d.open();
		Value v1 = d.getFieldValue(1, 0);
		Value v2 = d.getFieldValue(2, 0);
		d.deleteRow(1);
		for (int i = 0; i < 100; i++) {
			d.undo();
			assertTrue(equals(d.getFieldValue(1, 0), v1));
			d.redo();
			assertTrue(equals(d.getFieldValue(1, 0), v2));
		}
		d.undo();
		d.commit();
	}

	public void testAlphanumericInsertUndoRedo() throws Exception {
		InternalDataSource d = dsf.getDataSource(super.getAnyNonSpatialResource(),
				DataSourceFactory.UNDOABLE);

		d.open();
		Value v1 = d.getFieldValue(1, 0);
		d.insertEmptyRowAt(1);
		for (int i = 0; i < 100; i++) {
			d.undo();
			assertTrue(equals(d.getFieldValue(1, 0), v1));
			d.redo();
			assertTrue(d.getFieldValue(1, 0) instanceof NullValue);
		}
		d.undo();
		d.commit();
	}

	private void testSpatialModifyUndoRedo(SpatialDataSource d)
			throws Exception {
		Value v2 = d.getFieldValue(1, 0);
		Value v1 = d.getFieldValue(0, 0);
		d.setFieldValue(0, 0, v2);
		for (int i = 0; i < 100; i++) {
			d.undo();
			assertTrue(equals(d.getFieldValue(0, 0), v1));
			d.redo();
			assertTrue(equals(d.getFieldValue(0, 0), v2));
		}
	}

	public void testSpatialModifyUndoRedo() throws Exception {
		SpatialDataSource d = new SpatialDataSourceDecorator(dsf.getDataSource(
				super.getAnySpatialResource(), DataSourceFactory.UNDOABLE));

		d.open();
		testSpatialModifyUndoRedo(d);
		d.undo();
		d.commit();

		d.open();
		d.buildIndex();
		testSpatialModifyUndoRedo(d);
		d.undo();
		d.commit();

	}

	private void testSpatialDeleteUndoRedo(SpatialDataSource d)
			throws Exception {
		long rc = d.getRowCount();
		Value v1 = d.getFieldValue(1, 0);
		Value v2 = d.getFieldValue(2, 0);
		d.deleteRow(1);
		for (int i = 0; i < 100; i++) {
			d.undo();
			assertTrue(equals(d.getFieldValue(1, 0), v1));
			d.redo();
			assertTrue(equals(d.getFieldValue(1, 0), v2));
			assertTrue(rc - 1 == d.getRowCount());
		}
	}

	public void testSpatialDeleteUndoRedo() throws Exception {
		SpatialDataSource d = new SpatialDataSourceDecorator(dsf.getDataSource(
				super.getAnySpatialResource(), DataSourceFactory.UNDOABLE));

		d.open();
		testSpatialDeleteUndoRedo(d);
		d.undo();
		d.commit();

		d.open();
		d.buildIndex();
		testSpatialDeleteUndoRedo(d);
		d.undo();
		d.commit();
	}

	private void testSpatialInsertUndoRedo(SpatialDataSource d)
			throws Exception {
		long rc = d.getRowCount();
		d.insertEmptyRow();
		for (int i = 0; i < 100; i++) {
			d.undo();
			assertTrue(rc == d.getRowCount());
			d.redo();
			assertTrue(rc == d.getRowCount() - 1);
		}
	}

	public void testSpatialInsertUndoRedo() throws Exception {
		SpatialDataSource d = new SpatialDataSourceDecorator(dsf.getDataSource(
				super.getAnySpatialResource(), DataSourceFactory.UNDOABLE));

		d.open();
		testSpatialInsertUndoRedo(d);
		d.undo();
		d.commit();

		d.open();
		d.buildIndex();
		d.open();
		testSpatialInsertUndoRedo(d);
		d.undo();
		d.commit();
	}

	public void testAlphanumericEditionUndoRedo(InternalDataSource d) throws Exception {
		Value[][] snapshot1 = super.getDataSourceContents(d);
		d.setFieldValue(0, 0, d.getFieldValue(1, 0));
		Value[][] snapshot2 = super.getDataSourceContents(d);
		d.setFieldValue(0, 0, d.getFieldValue(2, 0));
		Value[][] snapshot3 = super.getDataSourceContents(d);
		d.deleteRow(0);
		Value[][] snapshot4 = super.getDataSourceContents(d);
		d.setFieldValue(0, 1, d.getFieldValue(1, 1));
		Value[][] snapshot5 = super.getDataSourceContents(d);
		d.insertEmptyRowAt(0);
		Value[][] snapshot6 = super.getDataSourceContents(d);
		d.undo();
		assertTrue(equals(snapshot5, super.getDataSourceContents(d)));
		d.redo();
		assertTrue(equals(snapshot6, super.getDataSourceContents(d)));
		d.undo();
		d.undo();
		assertTrue(equals(snapshot4, super.getDataSourceContents(d)));
		d.undo();
		assertTrue(equals(snapshot3, super.getDataSourceContents(d)));
		d.undo();
		assertTrue(equals(snapshot2, super.getDataSourceContents(d)));
		d.redo();
		assertTrue(equals(snapshot3, super.getDataSourceContents(d)));
		d.undo();
		d.undo();
		assertTrue(equals(snapshot1, super.getDataSourceContents(d)));
	}

	public void testAlphanumericEditionUndoRedo() throws Exception {
		InternalDataSource d = dsf.getDataSource(super.getAnyNonSpatialResource(),
				DataSourceFactory.UNDOABLE);

		d.open();
		testAlphanumericEditionUndoRedo(d);
		d.commit();
	}

	public void testSpatialEditionUndoRedo() throws Exception {
		SpatialDataSource d = new SpatialDataSourceDecorator(dsf.getDataSource(super
				.getAnySpatialResource(), DataSourceFactory.UNDOABLE));

		d.open();
		testAlphanumericEditionUndoRedo(d);
		d.commit();

		d.open();
		d.buildIndex();
		testAlphanumericEditionUndoRedo(d);
		d.commit();
	}

	public void testAddTwoRowsAndUndoBoth() throws Exception {
		SpatialDataSource d = new SpatialDataSourceDecorator(dsf.getDataSource(
				super.getAnySpatialResource(), DataSourceFactory.UNDOABLE));

		d.open();
		d.buildIndex();
		Value[] row = d.getRow(0);
		long rc = d.getRowCount();
		d.insertFilledRow(row);
		d.insertFilledRow(row);
		d.undo();
		d.undo();
		assertTrue(d.getRowCount() == rc);
		d.cancel();
	}

	public void testInsertModify() throws Exception {
		SpatialDataSource d = new SpatialDataSourceDecorator(dsf.getDataSource(
				super.getAnySpatialResource(), DataSourceFactory.UNDOABLE));

		d.open();
		d.buildIndex();
		int ri = (int) d.getRowCount();
		d.insertEmptyRow();
		Value v1 = d.getFieldValue(0, 0);
		Value v2 = d.getFieldValue(0, 1);
		d.setFieldValue(ri, 0, v1);
		d.setFieldValue(ri, 1, v2);
		d.undo();
		d.undo();
		d.undo();
		d.redo();
		d.redo();
		d.redo();
		assertTrue(equals(d.getFieldValue(ri, 0), v1));
		assertTrue(equals(d.getFieldValue(ri, 1), v2));
	}
}
