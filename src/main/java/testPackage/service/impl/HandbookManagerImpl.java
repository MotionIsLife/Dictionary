package testPackage.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import testPackage.repository.HandbookFieldsRepository;
import testPackage.repository.HandbookRepository;
import testPackage.service.HandbookManager;
import testPackage.vo.Handbook;
import testPackage.vo.HandbookField;
import testPackage.vo.HandbookFieldsTypes;

import java.util.*;
import java.util.stream.Collectors;

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

  public Handbook createHandbook(Handbook handbook) {
    handbook.setUpdateDate(new Date());
    handbookRepository.save(handbook);
    if(!CollectionUtils.isEmpty(handbook.getFields())) {
      handbook.getFields().forEach(handbookField -> handbookField.setHandbook(handbook));
      List<HandbookField> handbookFields = handbookFieldsRepository.save(handbook.getFields());
      handbook.setFields(new HashSet<>(handbookFields));
    }
    return handbook;
  }

  @Override
  public Handbook updateHandbook(Handbook handbook) {
    Handbook result = handbookRepository.getOne(handbook.getId());
    if(result != null) {
      result.setName(handbook.getName());
    }
    handbookRepository.save(result);

    if(!CollectionUtils.isEmpty(handbook.getFields())) {
      for (HandbookField handbookField : handbook.getFields()) {
        HandbookField foundHandbookField = null;
        if(handbookField.getId() != null) {
          foundHandbookField = handbookFieldsRepository.getOne(handbookField.getId());
        }
        if(foundHandbookField == null) {
          handbookField.setHandbook(result);
          handbookFieldsRepository.save(handbookField);
          result.getFields().add(handbookField);
        } else {
          foundHandbookField.setType(handbookField.getType());
          foundHandbookField.setKeyHandbook(handbookField.getKeyHandbook());
          foundHandbookField.setName(handbookField.getName());
          handbookFieldsRepository.save(foundHandbookField);
          result.getFields().add(foundHandbookField);
        }
      }
    }
    return result;
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
        field.setKeyHandbook(fMap.get("keyHandbook").toString());
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
