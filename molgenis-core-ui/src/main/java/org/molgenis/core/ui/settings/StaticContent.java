package org.molgenis.core.ui.settings;

import static org.molgenis.core.ui.settings.StaticContentMeta.CONTENT;
import static org.molgenis.core.ui.settings.StaticContentMeta.KEY;

import javax.annotation.CheckForNull;
import org.molgenis.data.Entity;
import org.molgenis.data.meta.model.EntityType;
import org.molgenis.data.support.StaticEntity;

public class StaticContent extends StaticEntity {
  public StaticContent(Entity entity) {
    super(entity);
  }

  public StaticContent(EntityType entityType) {
    super(entityType);
  }

  public StaticContent(String key, EntityType entityType) {
    super(entityType);
    setKey(key);
  }

  public String getKey() {
    return getString(KEY);
  }

  private void setKey(String key) {
    set(KEY, key);
  }

  @CheckForNull
  public String getContent() {
    return getString(CONTENT);
  }

  public void setContent(String content) {
    set(CONTENT, content);
  }
}
