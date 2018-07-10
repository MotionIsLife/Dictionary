package testPackage.vo;

/**
 * Типы колонок справочника
 */
public enum HandbookFieldsTypes {
  NUMBER("Число"),
  STRING("Текст"),
  DATE("Дата");

  private final String value;

  HandbookFieldsTypes(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return value;
  }
}