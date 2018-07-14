package testPackage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import testPackage.service.HandbookManager;
import testPackage.vo.Handbook;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/handbook")
public class HandbookController {

  @Autowired
  private HandbookManager handbookManager;

  @PostMapping(value = "/create", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE})
  public Integer create(@RequestBody Map handbook) {
      Map info = (Map) handbook.get("info");
      Handbook res = handbookManager.createHandbook(
          safeToStr(info.get("header")),
          safeToStr(info.get("description")),
          (List) handbook.get("fields"),
          (List) handbook.get("data"));
      return res.getId();
  }

  @PostMapping(value = "/update", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE})
  public Integer update(@RequestBody Handbook handbook) {
    Handbook res = handbookManager.updateHandbook(handbook);
    return res.getId();
  }

  @PostMapping(value = "/create2", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE})
  public Integer create2(@RequestBody Handbook handbook) {
    Handbook res = handbookManager.createHandbook(handbook);
    return res.getId();
  }

  @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE})
  public Handbook details(@PathVariable("id") Integer id) {
    return handbookManager.getHandbook(id);
  }

  @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE})
  public List<Handbook> getAll() {
    return handbookManager.getAll();
  }

  private static String safeToStr(Object d) {
    return d != null ? d.toString() : null;
  }
}
