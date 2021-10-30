package kom.komowo.exelimportexport.repository;

import kom.komowo.exelimportexport.entity.MyTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyTableRepository extends JpaRepository<MyTable, Long> {
    public MyTable findByName(String name);
}
