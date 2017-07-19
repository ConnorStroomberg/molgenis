package org.molgenis.oneclickimporter.controller;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.molgenis.data.MolgenisDataException;
import org.molgenis.data.i18n.LanguageService;
import org.molgenis.data.jobs.JobExecutor;
import org.molgenis.data.meta.model.EntityType;
import org.molgenis.data.settings.AppSettings;
import org.molgenis.file.FileStore;
import org.molgenis.oneclickimporter.exceptions.UnknownFileTypeException;
import org.molgenis.oneclickimporter.model.ImportJobExecution;
import org.molgenis.oneclickimporter.model.ImportJobExecutionFactory;
import org.molgenis.oneclickimporter.service.EntityService;
import org.molgenis.oneclickimporter.service.ExcelService;
import org.molgenis.oneclickimporter.service.Impl.ImportJobServiceImpl;
import org.molgenis.oneclickimporter.service.OneClickImporterService;
import org.molgenis.ui.MolgenisPluginController;
import org.molgenis.ui.menu.MenuReaderService;
import org.molgenis.util.ErrorMessageResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

import static java.util.Collections.singletonList;
import static java.util.Objects.requireNonNull;
import static org.molgenis.data.support.Href.concatEntityHref;
import static org.molgenis.oneclickimporter.controller.OneClickImporterController.URI;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping(URI)
public class OneClickImporterController extends MolgenisPluginController
{
	public static final String ONE_CLICK_IMPORTER = "one-click-importer";
	public static final String URI = PLUGIN_URI_PREFIX + ONE_CLICK_IMPORTER;

	private MenuReaderService menuReaderService;
	private LanguageService languageService;
	private AppSettings appSettings;
	private FileStore fileStore;
	private ImportJobExecutionFactory importJobExecutionFactory;
	private JobExecutor jobExecutor;


	public OneClickImporterController(MenuReaderService menuReaderService, LanguageService languageService,
			AppSettings appSettings, FileStore fileStore,ImportJobExecutionFactory importJobExecutionFactory, JobExecutor jobExecutor )
	{
		super(URI);
		this.menuReaderService = requireNonNull(menuReaderService);
		this.languageService = requireNonNull(languageService);
		this.appSettings = requireNonNull(appSettings);
		this.fileStore = requireNonNull(fileStore);
		this.importJobExecutionFactory = requireNonNull(importJobExecutionFactory);
		this.jobExecutor = jobExecutor;
	}

	@RequestMapping(method = GET)
	public String init(Model model)
	{
		model.addAttribute("lng", languageService.getCurrentUserLanguageCode());
		model.addAttribute("fallbackLng", appSettings.getLanguageCode());
		model.addAttribute("baseUrl", getBaseUrl());

		return "view-one-click-importer";
	}

	@ResponseBody
	@RequestMapping(value = "/upload", method = POST, produces = APPLICATION_JSON_VALUE)
	public String importFile(HttpServletResponse response, @RequestParam(value = "file") MultipartFile multipartFile)
			throws UnknownFileTypeException, IOException, InvalidFormatException
	{
		String filename = multipartFile.getOriginalFilename();
		fileStore.store(multipartFile.getInputStream(), filename);

		ImportJobExecution jobExecution = importJobExecutionFactory.create();
		jobExecution.setFileName(filename);
		jobExecution.setUser("user");
		jobExecutor.submit(jobExecution);
		String jobHref = concatEntityHref(jobExecution);

		ServletUriComponentsBuilder builder = ServletUriComponentsBuilder.fromCurrentRequestUri();
		response.setStatus(HttpServletResponse.SC_CREATED);
		response.setHeader("Location", jobHref);

		return jobHref;
	}



	@ResponseBody
	@ResponseStatus(BAD_REQUEST)
	@ExceptionHandler({ UnknownFileTypeException.class, IOException.class, InvalidFormatException.class,
			MolgenisDataException.class })
	public ErrorMessageResponse handleUnknownEntityException(Exception e)
	{
		return new ErrorMessageResponse(singletonList(new ErrorMessageResponse.ErrorMessage(e.getMessage())));
	}

	private String getBaseUrl()
	{
		return menuReaderService.getMenu().findMenuItemPath(OneClickImporterController.ONE_CLICK_IMPORTER);
	}
}
