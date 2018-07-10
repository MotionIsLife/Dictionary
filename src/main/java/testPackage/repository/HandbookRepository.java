package testPackage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import testPackage.vo.Handbook;

public interface HandbookRepository extends JpaRepository<Handbook, Integer> {
}
