package org.burningokr.model.okr.histories;

import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.burningokr.model.activity.Trackable;
import org.burningokr.model.okr.okrTopicDraft.OkrTopicDraft;

// @EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class OkrTopicDraftHistory implements Trackable<Long> {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  public String getName() {
    return "History " + id;
  }

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(mappedBy = "history", cascade = CascadeType.REMOVE, targetEntity = OkrTopicDraft.class)
  private Collection<OkrTopicDraft> units = new ArrayList<>();

  public void addUnit(OkrTopicDraft unit) {
    units.add(unit);
  }

  public Collection<OkrTopicDraft> getUnits() {
    return units;
  }

  public void clearUnits() {
    units.clear();
  }

  public void removeUnit(OkrTopicDraft unit) {
    units.remove(unit);
  }
}
