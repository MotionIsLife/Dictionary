package testPackage.configure.dialect;

import org.hibernate.dialect.HSQLDialect;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.type.Type;

import java.sql.Types;
import java.util.List;

/**
 * Заглушка переписанных агрегатов, специфичных для Postgres (см. {@link PostgresDialect})<br/>
 * Оба переписанных значения (MODE и UNIQUE) преобразуются в MIN<br/>
 * Класс не в рамках теста, чтобы оставить возможность использования заглушки для тестов в других модуляю
 */
public class TestHsqlDialect extends HSQLDialect {
  public TestHsqlDialect() {
    super();
    this.registerColumnType(Types.JAVA_OBJECT, "longvarchar");
    registerFunction("MODE", new StandardSQLFunction( "MIN" ) {
      @Override
      public String render(Type firstArgumentType, List arguments, SessionFactoryImplementor sessionFactory) {
        if (arguments.size() != 1)
          throw new IllegalArgumentException("MODE function has illegal argument number");
        return String.format("%s(%s)", getName(), arguments.get(0));
      }
    });
    registerFunction("UNIQUE", new StandardSQLFunction( "MIN" ) {
      @Override
      public String render(Type firstArgumentType, List arguments, SessionFactoryImplementor sessionFactory) {
        if (arguments.isEmpty())
          throw new IllegalArgumentException("UNIQUE function has illegal argument number");
        return String.format("%s(%s)", getName(), arguments.get(0));
      }
    });
  }
}
