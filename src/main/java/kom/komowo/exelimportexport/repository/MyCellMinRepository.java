package kom.komowo.exelimportexport.repository;

import kom.komowo.exelimportexport.entity.MyCellMin;
import kom.komowo.exelimportexport.entity.MyTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MyCellMinRepository extends JpaRepository<MyCellMin, Long> {
    public List<MyCellMin> findAllByMyTable(MyTable myTable);
}
