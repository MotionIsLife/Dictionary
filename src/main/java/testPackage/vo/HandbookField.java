package testPackage.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

/**
 * Сущность "Поле справочника" - описание полей справочников
 */

@Entity
@Table(name = "HDBK_FIELDS",
    uniqueConstraints =
        @UniqueConstraint(columnNames = {"name", "handbook_id"})
)
@SequenceGenerator(name = "entity_id_gen", sequenceName = "HDBK_FIELDS_SEQ", allocationSize = 1)
public class HandbookField extends AbstractEntity {
  @Column(name = "name", length = 50, nullable = false)
  private String name;

  @Column(name = "type", nullable = false)
  @Enumerated(EnumType.STRING)
  private HandbookFieldsTypes type;

  @Column(name = "key", length = 32, nullable = false)
  private String key;

  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "handbook_id")
  private Handbook handbook;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public HandbookFieldsTypes getType() {
    return type;
  }

  public void setType(HandbookFieldsTypes type) {
    this.type = type;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public Handbook getHandbook() {
    return handbook;
  }

  public void setHandbook(Handbook handbook) {
    this.handbook = handbook;
  }
}