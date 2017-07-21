package org.molgenis.oneclickimporter.service;

import org.apache.poi.ss.usermodel.Sheet;
import org.molgenis.data.meta.AttributeType;
import org.molgenis.oneclickimporter.model.Column;
import org.molgenis.oneclickimporter.model.DataCollection;

import java.util.List;

public interface OneClickImporterService
{
	/**
	 * Generate {@link DataCollection} from an Excel sheet
	 *
	 * @param dataCollectionName
	 * @param sheet
	 */
	DataCollection buildDataCollection(String dataCollectionName, Sheet sheet);

	/**
	 * Generate {@link DataCollection} from a List of file lines
	 *
	 * @param dataCollectionName
	 * @param lines
	 */
	DataCollection buildDataCollection(String dataCollectionName, List<String> lines);

	/**
	 * Test is values are unique within column.
	 * A column containing null values is considered to be non-unique
	 */
	boolean hasUniqueValues(Column column);

	/**
	 * Cast the given value based in the supplied attribute type.
	 * The method returns the most specific type that can contain the value
	 * @param value
	 * @param type
	 * @return
	 */
	Object castValueAsAttributeType(Object value, AttributeType type);
}
