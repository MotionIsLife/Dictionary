package testPackage.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

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

  @Column(name = "keyHandbook", length = 32, nullable = false)
  private String keyHandbook;

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

  public String getKeyHandbook() {
    return keyHandbook;
  }

  public void setKeyHandbook(String keyHandbook) {
    this.keyHandbook = keyHandbook;
  }

  public Handbook getHandbook() {
    return handbook;
  }

  public void setHandbook(Handbook handbook) {
    this.handbook = handbook;
  }
}