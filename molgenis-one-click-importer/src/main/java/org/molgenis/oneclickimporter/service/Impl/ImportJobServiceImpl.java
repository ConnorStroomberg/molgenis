package org.molgenis.oneclickimporter.service.Impl;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.molgenis.data.i18n.LanguageService;
import org.molgenis.data.jobs.Progress;
import org.molgenis.data.meta.model.EntityType;
import org.molgenis.data.settings.AppSettings;
import org.molgenis.file.FileStore;
import org.molgenis.oneclickimporter.exceptions.UnknownFileTypeException;
import org.molgenis.oneclickimporter.model.DataCollection;
import org.molgenis.oneclickimporter.service.EntityService;
import org.molgenis.oneclickimporter.service.ExcelService;
import org.molgenis.oneclickimporter.service.OneClickImporterService;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;


@Service
public class ImportJobServiceImpl
{
	private OneClickImporterService oneClickImporterService;
	private ExcelService excelService;
	private EntityService entityService;
	private FileStore fileStore;;

	public ImportJobServiceImpl(OneClickImporterService oneClickImporterService, ExcelService excelService,
			EntityService entityService, FileStore fileStore)
	{
		this.oneClickImporterService = oneClickImporterService;
		this.excelService = excelService;
		this.entityService = entityService;
		this.fileStore = fileStore;
	}

	public EntityType getEntityType(Progress progress, String filename)
			throws IOException, InvalidFormatException, UnknownFileTypeException
	{
		progress.setProgressMax(3);
		File file = fileStore.getFile(filename);
		String fileExtension = filename.substring(filename.lastIndexOf('.') + 1);
		String dataCollectionName = filename.substring(0, filename.lastIndexOf('.'));

		DataCollection dataCollection;
		if (fileExtension.equals("xls") || fileExtension.equals("xlsx"))
		{
			Sheet sheet = excelService.buildExcelSheetFromFile(file);
			progress.progress(1, "read exel sheet");
			dataCollection = oneClickImporterService.buildDataCollection(dataCollectionName, sheet);
			progress.progress(2, "created dataCollection");
		}
		else
		{
			throw new UnknownFileTypeException(
					String.format("File with extension: %s is not a valid one-click importer file", fileExtension));
		}

		EntityType entityType = entityService.createEntityType(dataCollection);
		progress.progress(3, "import complete");
		return entityType;
	}
}
