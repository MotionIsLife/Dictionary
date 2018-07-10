package testPackage.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/**
 * Абстрактный класс-предок всех сущностей
 */
@MappedSuperclass
public abstract class AbstractEntity implements Identifiable {

  @Id
  @GeneratedValue(generator = "entity_id_gen", strategy = GenerationType.SEQUENCE)
  private Integer id;

  @JsonIgnore
  @Version
  private Integer version;

  public AbstractEntity() {
  }

  public AbstractEntity(Integer id) {
    this.id = id;
  }

  @Override
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getVersion() {
    return version;
  }
}