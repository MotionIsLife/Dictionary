package testPackage.configure.dialect;

import org.hibernate.dialect.PostgreSQL94Dialect;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.type.Type;

import java.sql.Types;
import java.util.List;

/**
 * Специализированный диалект для интеграции с БД Postgre
 * Реализован рендеринг для агрегатных функций MODE и UNIQUE<br/>
 * MODE - наиболее часто втречающееся значение, принимает один параметр<br/>
 * UNIQUE - строка уникальных значений, принимает 2 параметра, второй - разделитель, может быть пропущен
 * разделитель по-умолчанию - ', '
 */
public class PostgresDialect extends PostgreSQL94Dialect {
  public PostgresDialect() {
    super();
    this.registerColumnType(Types.JAVA_OBJECT, "jsonb");
    registerFunction("MODE", new StandardSQLFunction("mode") {
      @Override
      public String render(Type firstArgumentType, List arguments, SessionFactoryImplementor sessionFactory) {
        if (arguments.size() != 1)
          throw new IllegalArgumentException("MODE function has illegal argument number");
        return String.format("%s() WITHIN GROUP (ORDER BY %s)", getName(), arguments.get(0));
      }
    });
    registerFunction("UNIQUE", new StandardSQLFunction("string_agg") {
      @Override
      public String render(Type firstArgumentType, List arguments, SessionFactoryImplementor sessionFactory) {
        if (arguments.isEmpty())
          throw new IllegalArgumentException("UNIQUE function has illegal argument number");
        String delimiter = arguments.size() >= 2 ? (String) arguments.get(1) : ", ";
        return String.format("%s(DISTINCT %s, '%s')", getName(), arguments.get(0), delimiter);
      }
    });
  }
}
