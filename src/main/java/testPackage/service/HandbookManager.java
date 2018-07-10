package testPackage.service;

import testPackage.vo.Handbook;

import java.util.List;

public interface HandbookManager {
  /**
   * Создать справочник
   * @param name - Наименование справочника
   * @return Handbook
   */
  Handbook createHandbook(String name, String desc, List fields, List data);

  Handbook getHandbook(Integer id);
}
