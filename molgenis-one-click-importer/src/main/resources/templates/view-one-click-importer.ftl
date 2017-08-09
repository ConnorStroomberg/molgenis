<#include "resource-macros.ftl">
<#include "theme-macros.ftl">
<!doctype html>
<html lang="en">
<head>
    <title>One Click Importer</title>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta http-equiv="X-UA-Compatible" content="chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="shortcut icon" href="/img/favicon.ico">
    <link rel="stylesheet" href="<@theme_href "/css/theme/bootstrap-4/${app_settings.bootstrapTheme?html}"/>" type="text/css"
          id="bootstrap-theme">
    <link href="<@resource_href "/css/molgenis-one-click-importer/app.css" />" rel="stylesheet">
</head>
<body>
<#include "molgenis-header.ftl">
<#include "molgenis-footer.ftl">

<#assign js = []>
<#assign css = ["molgenis-one-click-importer/app.css"]>
<#assign version = 2>

<@header css js version/>
<div id="app"></div>

<script type="text/javascript">
    window.__INITIAL_STATE__ = {
        baseUrl: '${baseUrl}',
        lng: '${lng}',
        fallbackLng: '${fallbackLng}'<#if navigatorBaseUrl??>,
        navigatorBaseUrl: '${navigatorBaseUrl}'</#if><#if dataExplorerBaseUrl??>,
        dataExplorerBaseUrl: '${dataExplorerBaseUrl}'</#if>
    }
</script>

<script type=text/javascript src="<@resource_href "/js/molgenis-one-click-importer/manifest.js"/>"></script>
<script type=text/javascript src="<@resource_href "/js/molgenis-one-click-importer/vendor.js"/>"></script>
<script type=text/javascript src="<@resource_href "/js/molgenis-one-click-importer/app.js"/>"></script>
<@footer/>