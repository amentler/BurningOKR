package org.burningokr.model.okr.okrTopicDraft;

import javax.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.burningokr.model.okr.OkrTopicDescription;
import org.burningokr.model.okr.histories.OkrTopicDraftHistory;
import org.burningokr.model.okrUnits.OkrUnit;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class OkrTopicDraft extends OkrTopicDescription {

  @ManyToOne private OkrUnit parentUnit;

  @ManyToOne private OkrTopicDraftHistory history;

  @Enumerated(EnumType.STRING)
  private OkrTopicDraftStatusEnum currentStatus;
}
