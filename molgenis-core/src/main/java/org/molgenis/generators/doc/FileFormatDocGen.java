package org.molgenis.generators.doc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.molgenis.MolgenisOptions;
import org.molgenis.generators.Generator;
import org.molgenis.model.MolgenisModel;
import org.molgenis.model.elements.Entity;
import org.molgenis.model.elements.Model;

import freemarker.template.Template;

public class FileFormatDocGen extends Generator
{
	private static final Logger logger = Logger.getLogger(FileFormatDocGen.class);

	@Override
	public String getDescription()
	{
		return "Generates one documentation file describing all entities.";
	}

	@Override
	public void generate(Model model, MolgenisOptions options) throws Exception
	{
		if (options.generate_tests)
		{
		}
		else
		{
			Template template = createTemplate("/" + getClass().getSimpleName() + ".java.ftl");
			Map<String, Object> templateArgs = createTemplateArguments(options);

			File target = new File(this.getDocumentationPath(options) + "/fileformat.html");
			boolean created = target.getParentFile().mkdirs();
			if (!created && !target.getParentFile().exists())
			{
				throw new IOException("could not create " + target.getParentFile());
			}

			List<Entity> entityList = model.getEntities();
			entityList = MolgenisModel.sortEntitiesByDependency(entityList, model); // side
																					// effect?

			templateArgs.put("model", model);
			templateArgs.put("entities", entityList);
			OutputStream targetOut = new FileOutputStream(target);
			template.process(templateArgs, new OutputStreamWriter(targetOut, Charset.forName("UTF-8")));
			targetOut.close();

			logger.info("generated " + target);
		}
	}

}
