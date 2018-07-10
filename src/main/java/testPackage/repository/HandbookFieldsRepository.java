package testPackage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import testPackage.vo.HandbookField;

public interface HandbookFieldsRepository extends JpaRepository<HandbookField, Integer> {
}
