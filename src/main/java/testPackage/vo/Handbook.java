package testPackage.vo;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "HDBK")
@SequenceGenerator(name = "entity_id_gen", sequenceName = "HDBK_SEQ", allocationSize = 1)
public class Handbook extends AbstractEntity{

  @Column(name = "name", nullable = false, unique = true)
  private String name;

  @Column(name = "update_date", nullable = false)
  private Date updateDate;

  @Column(name = "description", length = 500)
  private String desc;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "handbook", cascade = CascadeType.REMOVE)
  private Set<HandbookField> fields;

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  public Date getUpdateDate() {
    return updateDate;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

  public String getDesc() {
    return desc;
  }

  public void setFields(Set<HandbookField> fields) {
    this.fields = fields;
  }

  public Set<HandbookField> getFields() {
    return fields;
  }
}
