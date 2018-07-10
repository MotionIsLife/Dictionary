package testPackage.configure.dialect;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.UserType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.io.StringWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MapJsonType implements UserType {

  private static final Logger LOG = LoggerFactory.getLogger(MapJsonType.class);

  @Override
  public int[] sqlTypes() {
    return new int[]{Types.JAVA_OBJECT};
  }

  @Override
  public Class<Map> returnedClass() {
    return Map.class;
  }

  @Override
  public boolean equals(final Object obj1, final Object obj2) {
    if (obj1 == null) {
      return obj2 == null;
    }
    return obj1.equals(obj2);
  }

  @Override
  public int hashCode(final Object obj) {
    return obj.hashCode();
  }

  @Override
  public Object nullSafeGet(final ResultSet rs, final String[] names, final SessionImplementor session,
                            final Object owner) throws SQLException {
    final String cellContent = rs.getString(names[0]);
    if (cellContent == null) {
      return null;
    }
    try {
      final ObjectMapper mapper = new ObjectMapper();
      Map<String, Object> map = new HashMap<>();
      JsonNode node = mapper.readTree(cellContent.getBytes("UTF-8"));

      Iterator<String> iterator = node.fieldNames();
      while (iterator.hasNext()) {
        String name = iterator.next();
        Object value;
        switch (node.get(name).getNodeType())
        {
          case NUMBER:
            value = node.get(name).numberValue();
            break;
          case STRING:
          default:
            value = node.get(name).textValue();
            break;
        }
        map.put(name, value);
      }
      return map;
    } catch (final Exception ex) {
      LOG.error("Failed to convert String to HashMap: " + ex.getMessage(), ex);
      return null;
    }
  }

  @Override
  public void nullSafeSet(final PreparedStatement ps, final Object value, final int idx,
                          final SessionImplementor session) throws SQLException {
    if (value == null) {
      ps.setNull(idx, Types.OTHER);
      return;
    }
    try {
      final ObjectMapper mapper = new ObjectMapper();
      final StringWriter w = new StringWriter();
      mapper.writeValue(w, value);
      w.flush();
      ps.setObject(idx, w.toString(), Types.OTHER);
    } catch (final Exception ex) {
      LOG.error("Failed to convert HashMap to String: " + ex.getMessage(), ex);
    }
  }

  @Override
  public Object deepCopy(final Object value) {
    try {
      Map<String, Object> map = new HashMap<>();
      map.putAll((Map) value);
      return map;
    } catch (Exception ex) {
      throw new HibernateException(ex);
    }
  }

  @Override
  public boolean isMutable() {
    return true;
  }

  @Override
  public Serializable disassemble(final Object value) {
    return (Serializable) this.deepCopy(value);
  }

  @Override
  public Object assemble(final Serializable cached, final Object owner) {
    return this.deepCopy(cached);
  }

  @Override
  public Object replace(final Object original, final Object target, final Object owner) {
    return this.deepCopy(original);
  }
}
