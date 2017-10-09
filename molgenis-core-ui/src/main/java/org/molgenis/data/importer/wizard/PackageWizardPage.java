package org.molgenis.data.importer.wizard;

import org.molgenis.data.DatabaseAction;
import org.molgenis.data.FileRepositoryCollectionFactory;
import org.molgenis.data.RepositoryCollection;
import org.molgenis.data.importer.ImportService;
import org.molgenis.data.importer.ImportServiceFactory;
import org.molgenis.data.meta.MetaDataService;
import org.molgenis.ui.wizard.AbstractWizardPage;
import org.molgenis.ui.wizard.Wizard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import static org.molgenis.data.meta.model.Package.PACKAGE_SEPARATOR;

@Component
public class PackageWizardPage extends AbstractWizardPage
{
	private static final Logger LOG = LoggerFactory.getLogger(PackageWizardPage.class);

	/**
	 * Auto generated
	 */
	private static final long serialVersionUID = 1L;

	private FileRepositoryCollectionFactory fileRepositoryCollectionFactory;
	private ImportServiceFactory importServiceFactory;
	private MetaDataService metaDataService;

	public PackageWizardPage(FileRepositoryCollectionFactory fileRepositoryCollectionFactory,
			ImportServiceFactory importServiceFactory, MetaDataService metaDataService)
	{
		this.fileRepositoryCollectionFactory = requireNonNull(fileRepositoryCollectionFactory);
		this.importServiceFactory = requireNonNull(importServiceFactory);
		this.metaDataService = requireNonNull(metaDataService);
	}

	@Override
	public String getTitle()
	{
		return "Packages";
	}

	@Override
	public String handleRequest(HttpServletRequest request, BindingResult result, Wizard wizard)
	{
		ImportWizardUtil.validateImportWizard(wizard);
		ImportWizard importWizard = (ImportWizard) wizard;
		String entityImportOption = importWizard.getEntityImportOption();

		if (entityImportOption != null)
		{
			try
			{
				// convert input to database action
				DatabaseAction entityDbAction = ImportWizardUtil.toDatabaseAction(entityImportOption);
				if (entityDbAction == null) throw new IOException("unknown database action: " + entityImportOption);

				RepositoryCollection repositoryCollection = fileRepositoryCollectionFactory.createFileRepositoryCollection(
						importWizard.getFile());

				ImportService importService = importServiceFactory.getImportService(importWizard.getFile(),
						repositoryCollection);

				// Do integration test only if there are no previous errors found
				if (!importWizard.getEntitiesImportable().containsValue(false))
				{
					// The package name that is selected in the "package selection" page
					String selectedPackage = request.getParameter("selectedPackage");

					// The entities that can be imported
					LinkedHashMap<String, Boolean> entitiesImportable = importService.determineImportableEntities(
							metaDataService, repositoryCollection, selectedPackage);

					// The results of the attribute checks are stored in maps with the entityname as key, those need to be updated with the packagename
					updateFieldReports(importWizard, selectedPackage);
					// Set the entities that can be imported
					importWizard.setEntitiesImportable(entitiesImportable);

					// The entities that can not be imported. If even one entity can not be imported, everything fails
					List<String> entitiesNotImportable = entitiesImportable.entrySet()
																		   .stream()
																		   .filter(entity -> entity.getValue() == false)
																		   .map(Map.Entry::getKey)
																		   .collect(toList());

					if (!entitiesNotImportable.isEmpty()) throw new RuntimeException(
							"You are trying to upload entities that are not compatible with the already existing entities: "
									+ entitiesNotImportable.toString());
				}

			}
			catch (RuntimeException | IOException e)
			{
				ImportWizardUtil.handleException(e, importWizard, result, LOG, entityImportOption);
			}
		}
		return null;
	}

	private void updateFieldReports(ImportWizard importWizard, String pack)
	{
		importWizard.setFieldsAvailable(renameKeys(importWizard.getFieldsAvailable(), pack));
		importWizard.setFieldsDetected(renameKeys(importWizard.getFieldsDetected(), pack));
		importWizard.setFieldsRequired(renameKeys(importWizard.getFieldsRequired(), pack));
		importWizard.setFieldsUnknown(renameKeys(importWizard.getFieldsUnknown(), pack));
	}

	private Map<String, Collection<String>> renameKeys(Map<String, Collection<String>> map, String pack)
	{
		Map<String, Collection<String>> result = new HashMap<>();
		map.keySet().forEach(key -> result.put(pack + PACKAGE_SEPARATOR + key, map.get(key)));
		return result;
	}
}
