package org.molgenis.data.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.molgenis.data.MolgenisDataException;
import org.molgenis.data.WritableFactory;
import org.molgenis.data.meta.model.AttributeMetaData;
import org.molgenis.data.meta.model.AttributeMetaDataFactory;
import org.molgenis.data.processor.CellProcessor;
import org.molgenis.data.support.AbstractWritable.AttributeWriteMode;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

/**
 * Creates new Excel sheets
 */
public class ExcelWriter implements WritableFactory
{
	private final Workbook workbook;
	private final OutputStream os;
	private final AttributeMetaDataFactory attrMetaFactory;
	private List<CellProcessor> cellProcessors;

	public enum FileFormat
	{
		XLS, XLSX
	}

	public ExcelWriter(OutputStream os, AttributeMetaDataFactory attrMetaFactory)
	{
		this(os, attrMetaFactory, FileFormat.XLS);
	}

	public ExcelWriter(OutputStream os, AttributeMetaDataFactory attrMetaFactory, FileFormat format)
	{
		this.os = requireNonNull(os);
		this.attrMetaFactory = requireNonNull(attrMetaFactory);
		this.workbook = requireNonNull(format) == FileFormat.XLS ? new HSSFWorkbook() : new XSSFWorkbook();
	}

	public ExcelWriter(File file, AttributeMetaDataFactory attrMetaFactory) throws FileNotFoundException
	{
		this(new FileOutputStream(file), attrMetaFactory, FileFormat.XLS);
	}

	public ExcelWriter(File file, AttributeMetaDataFactory attrMetaFactory, FileFormat format)
			throws FileNotFoundException
	{
		this(new FileOutputStream(file), attrMetaFactory, format);
	}

	public void addCellProcessor(CellProcessor cellProcessor)
	{
		if (cellProcessors == null) cellProcessors = new ArrayList<>();
		cellProcessors.add(cellProcessor);
	}

	@Override
	public ExcelSheetWriter createWritable(String entityName, Iterable<AttributeMetaData> attributes,
			AttributeWriteMode attributeWriteMode)
	{
		Sheet poiSheet = workbook.createSheet(entityName);
		return new ExcelSheetWriter(poiSheet, attributes, attributeWriteMode, cellProcessors);
	}

	@Override
	public void close() throws IOException
	{
		try
		{
			workbook.write(os);
		}
		catch (IOException e)
		{
			throw new MolgenisDataException("Exception writing to excel file", e);
		}
		os.close();
	}

	@Override
	public ExcelSheetWriter createWritable(String entityName, List<String> attributeNames)
	{
		List<AttributeMetaData> attributes = attributeNames != null ? attributeNames.stream()
				.map(attrName -> attrMetaFactory.create().setName(attrName)).collect(Collectors.toList()) : null;

		return createWritable(entityName, attributes, AttributeWriteMode.ATTRIBUTE_NAMES);
	}
}