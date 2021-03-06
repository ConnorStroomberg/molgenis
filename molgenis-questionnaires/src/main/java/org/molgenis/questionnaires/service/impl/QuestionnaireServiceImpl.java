package org.molgenis.questionnaires.service.impl;

import static java.util.Objects.requireNonNull;
import static org.molgenis.data.EntityManager.CreationMode.POPULATE;
import static org.molgenis.data.meta.model.EntityTypeMetadata.ENTITY_TYPE_META_DATA;
import static org.molgenis.questionnaires.meta.QuestionnaireMetaData.OWNER_USERNAME;
import static org.molgenis.questionnaires.meta.QuestionnaireMetaData.QUESTIONNAIRE;
import static org.molgenis.questionnaires.meta.QuestionnaireStatus.OPEN;
import static org.molgenis.security.core.utils.SecurityUtils.getCurrentUsername;

import java.util.Objects;
import java.util.stream.Stream;
import javax.annotation.CheckForNull;
import javax.annotation.Nullable;
import org.molgenis.core.ui.controller.StaticContentService;
import org.molgenis.data.DataService;
import org.molgenis.data.Entity;
import org.molgenis.data.EntityManager;
import org.molgenis.data.meta.model.EntityType;
import org.molgenis.data.meta.model.EntityTypeMetadata;
import org.molgenis.data.security.EntityIdentityUtils;
import org.molgenis.data.security.EntityTypeIdentity;
import org.molgenis.data.security.EntityTypePermission;
import org.molgenis.questionnaires.exception.QuestionnaireNotRowLevelSecuredException;
import org.molgenis.questionnaires.meta.Questionnaire;
import org.molgenis.questionnaires.meta.QuestionnaireFactory;
import org.molgenis.questionnaires.response.QuestionnaireResponse;
import org.molgenis.questionnaires.service.QuestionnaireService;
import org.molgenis.security.acl.MutableAclClassService;
import org.molgenis.security.core.UserPermissionEvaluator;
import org.springframework.stereotype.Service;

@Service
public class QuestionnaireServiceImpl implements QuestionnaireService {
  private static final String DEFAULT_SUBMISSION_TEXT =
      "<h3>Thank you for submitting the questionnaire.</h3>";

  private final DataService dataService;
  private final EntityManager entityManager;
  private final UserPermissionEvaluator userPermissionEvaluator;
  private final QuestionnaireFactory questionnaireFactory;
  private final StaticContentService staticContentService;
  private final MutableAclClassService mutableAclClassService;

  public QuestionnaireServiceImpl(
      DataService dataService,
      EntityManager entityManager,
      UserPermissionEvaluator userPermissionEvaluator,
      QuestionnaireFactory questionnaireFactory,
      StaticContentService staticContentService,
      MutableAclClassService mutableAclClassService) {
    this.dataService = Objects.requireNonNull(dataService);
    this.entityManager = requireNonNull(entityManager);
    this.userPermissionEvaluator = requireNonNull(userPermissionEvaluator);
    this.questionnaireFactory = requireNonNull(questionnaireFactory);
    this.staticContentService = requireNonNull(staticContentService);
    this.mutableAclClassService = requireNonNull(mutableAclClassService);
  }

  @Override
  public Stream<EntityType> getQuestionnaires() {
    return dataService
        .query(ENTITY_TYPE_META_DATA, EntityType.class)
        .eq(EntityTypeMetadata.EXTENDS, QUESTIONNAIRE)
        .findAll()
        .filter(
            entityType ->
                userPermissionEvaluator.hasPermission(
                    new EntityTypeIdentity(entityType.getId()), EntityTypePermission.ADD_DATA))
        .filter(
            entityType ->
                userPermissionEvaluator.hasPermission(
                    new EntityTypeIdentity(entityType.getId()), EntityTypePermission.UPDATE_DATA));
  }

  @Override
  public QuestionnaireResponse startQuestionnaire(String entityTypeId) {
    Questionnaire questionnaire = findQuestionnaireEntity(entityTypeId);
    if (questionnaire == null) {
      EntityType questionnaireEntityType = dataService.getEntityType(entityTypeId);
      boolean rlsEnabled =
          mutableAclClassService
              .getAclClassTypes()
              .contains(EntityIdentityUtils.toType(questionnaireEntityType));
      if (!rlsEnabled) {
        throw new QuestionnaireNotRowLevelSecuredException(questionnaireEntityType);
      }

      questionnaire =
          questionnaireFactory.create(entityManager.create(questionnaireEntityType, POPULATE));
      questionnaire.setOwner(getCurrentUsername());
      questionnaire.setStatus(OPEN);
      dataService.add(entityTypeId, questionnaire);
      Questionnaire newQuestionnaire =
          findQuestionnaireEntity(questionnaire.getEntityType().getId());
      return QuestionnaireResponse.create(newQuestionnaire);
    } else {
      return QuestionnaireResponse.create(questionnaire);
    }
  }

  @Override
  public String getQuestionnaireSubmissionText(String id) {
    String key = id + "_submissionText";
    String submissionText = staticContentService.getContent(key);

    if (submissionText == null) {
      submissionText = DEFAULT_SUBMISSION_TEXT;
      staticContentService.submitContent(key, submissionText);
    }

    return submissionText;
  }

  @Override
  @Nullable
  @CheckForNull
  public Questionnaire findQuestionnaireEntity(String entityTypeId) {
    Entity questionnaireInstance =
        dataService.query(entityTypeId).eq(OWNER_USERNAME, getCurrentUsername()).findOne();

    if (questionnaireInstance != null) {
      return questionnaireFactory.create(questionnaireInstance);
    } else {
      return null;
    }
  }
}
