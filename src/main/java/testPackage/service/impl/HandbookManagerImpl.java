package testPackage.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import testPackage.repository.HandbookFieldsRepository;
import testPackage.repository.HandbookRepository;
import testPackage.service.HandbookManager;
import testPackage.vo.Handbook;
import testPackage.vo.HandbookField;
import testPackage.vo.HandbookFieldsTypes;

import java.util.*;

@Component
@SuppressWarnings("unused")
public class HandbookManagerImpl implements HandbookManager {

  @Autowired
  private HandbookRepository handbookRepository;

  @Autowired
  private HandbookFieldsRepository handbookFieldsRepository;

  @Override
  @Transactional
  public Handbook createHandbook(String name, String desc, List fields, List data) {
    return saveHandbook(true, new Handbook(), name, desc, fields, data);
  }

  @Override
  public Handbook getHandbook(Integer id) {
    return handbookRepository.findOne(id);
  }

  @Override
  public List<Handbook> getAll() {
    return handbookRepository.findAll();
  }

  private Handbook saveHandbook(boolean isCreate, Handbook handbook, String name, String desc, List fields, List data) {
    handbook.setName(name);
    handbook.setUpdateDate(new Date());
    handbook.setDesc(desc);
    handbookRepository.save(handbook);
    if (fields != null) {
      Set<HandbookField> handbookFieldsSet = new HashSet<>();
      for (Object f : fields) {
        Map fMap = (Map) f;
        HandbookField field = new HandbookField();
        if (fMap.get("id") != null)
          field = handbookFieldsRepository.findOne(Integer.parseInt(fMap.get("id").toString()));
        field.setKey(fMap.get("key").toString());
        field.setName(fMap.get("name").toString());
        if (fMap.get("type") != null)
          field.setType(HandbookFieldsTypes.valueOf(fMap.get("type").toString()));
        else
          field.setType(HandbookFieldsTypes.STRING);
        field.setHandbook(handbook);
        handbookFieldsSet.add(field);
      }
      handbookFieldsRepository.save(handbookFieldsSet);
      handbook.setFields(handbookFieldsSet);
    }
    return handbook;
  }
}
