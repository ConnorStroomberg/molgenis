package org.molgenis.data.importer;

import org.molgenis.data.Entity;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.*;


public class ImportRunTest
{
	@Test
	public void testGetNotifyForUnsetNotifyValue()
	{
		//setup
		Entity entity = mock(Entity.class);
		when(entity.getBoolean(ImportRunMetaData.NOTIFY)).thenReturn(null);
		ImportRun importRun = new ImportRun(entity);

		// execute
		boolean notifyResult = importRun.getNotify();

		// validate
		assertFalse(notifyResult); // we expect false instead of null because null is seen as falsy for this method
	}

	@Test
	public void testGetNotifyNotifyValueSetToTrue()
	{
		//setup
		Entity entity = mock(Entity.class);
		when(entity.getBoolean(ImportRunMetaData.NOTIFY)).thenReturn(true);
		ImportRun importRun = new ImportRun(entity);

		// execute
		boolean notifyResult = importRun.getNotify();

		// validate
		assertTrue(notifyResult);
	}

	@Test
	public void testGetNotifyNotifyValueSetToFalse()
	{
		//setup
		Entity entity = mock(Entity.class);
		when(entity.getBoolean(ImportRunMetaData.NOTIFY)).thenReturn(false);
		ImportRun importRun = new ImportRun(entity);

		// execute
		boolean notifyResult = importRun.getNotify();

		// validate
		assertFalse(notifyResult);
	}

}