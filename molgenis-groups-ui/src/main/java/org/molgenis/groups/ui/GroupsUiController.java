package org.molgenis.groups.ui;

import org.molgenis.data.i18n.LanguageService;
import org.molgenis.data.settings.AppSettings;
import org.molgenis.navigator.NavigatorController;
import org.molgenis.security.core.service.UserAccountService;
import org.molgenis.ui.controller.VuePluginController;
import org.molgenis.ui.menu.MenuReaderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.molgenis.groups.ui.GroupsUiController.URI;

@Controller
@RequestMapping(URI)
public class GroupsUiController extends VuePluginController
{
	public static final String ID = "groups-ui";
	public static final String URI = PLUGIN_URI_PREFIX + ID;

	public GroupsUiController(MenuReaderService menuReaderService, LanguageService languageService,
			AppSettings appSettings, UserAccountService userAccountService)
	{
		super(URI, menuReaderService, languageService, appSettings, userAccountService);
	}

	@GetMapping("/**")
	public String init(Model model)
	{
		super.init(model, ID);
		model.addAttribute("navigatorBaseUrl", getBaseUrl(NavigatorController.ID));
		// Todo add url for navigation link to members manager view
		// model.addAttribute("membersBaseUrl", getBaseUrl(MembersController.ID));
		return "view-groups-ui";
	}

}