package bookshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import bookshop.models.Category;


@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

}
