package org.molgenis.data.security.model;

import org.molgenis.data.meta.AttributeType;
import org.molgenis.data.meta.SystemEntityType;
import org.molgenis.data.meta.model.Attribute;
import org.molgenis.data.meta.model.PackageMetadata;
import org.springframework.stereotype.Component;

import static java.util.Objects.requireNonNull;
import static org.molgenis.data.meta.AttributeType.MREF;
import static org.molgenis.data.meta.AttributeType.ONE_TO_MANY;
import static org.molgenis.data.meta.model.EntityType.AttributeRole.*;
import static org.molgenis.data.meta.model.Package.PACKAGE_SEPARATOR;
import static org.molgenis.data.security.model.SecurityPackage.PACKAGE_SECURITY;

@Component
public class GroupMetadata extends SystemEntityType
{
	private static final String SIMPLE_NAME = "Group";
	public static final String GROUP = PACKAGE_SECURITY + PACKAGE_SEPARATOR + SIMPLE_NAME;

	public static final String LABEL = "label";
	public static final String ID = "id";
	public static final String PARENT = "parent";
	public static final String CHILDREN = "children";
	public static final String ROLES = "roles";
	/**
	 * Acts as a namespace for the top level group, is set to null for child groups
	 */
	public static final String GROUP_PACKAGE = "group_package";

	private final SecurityPackage securityPackage;
	private final RoleMetadata roleMetadata;
	private final PackageMetadata packageMetadata;

	GroupMetadata(SecurityPackage securityPackage, RoleMetadata roleMetadata, PackageMetadata packageMetadata)
	{
		super(SIMPLE_NAME, PACKAGE_SECURITY);
		this.securityPackage = requireNonNull(securityPackage);
		this.roleMetadata = requireNonNull(roleMetadata);
		this.packageMetadata = requireNonNull(packageMetadata);
	}

	@Override
	public void init()
	{
		setLabel("Group");
		setPackage(securityPackage);

		addAttribute(ID, ROLE_ID).setAuto(true).setVisible(false);
		addAttribute(LABEL, ROLE_LABEL, ROLE_LOOKUP).setLabel("Label").setNillable(false);
		Attribute parentAttribute = addAttribute(PARENT).setLabel("Parent")
														.setDataType(AttributeType.XREF)
														.setRefEntity(this)
														.setNillable(true);
		addAttribute(CHILDREN).setLabel("Children")
							  .setDataType(ONE_TO_MANY)
							  .setRefEntity(this)
							  .setMappedBy(parentAttribute);
		addAttribute(ROLES).setLabel("Roles").setDataType(MREF).setRefEntity(roleMetadata);

		addAttribute(GROUP_PACKAGE).setLabel("Group Package")
								   .setDescription("Acts as a namespace for the top level group, is set to null for child groups")
								   .setNillable(true)
								   .setDataType(AttributeType.XREF)
								   .setRefEntity(packageMetadata);
	}
}
