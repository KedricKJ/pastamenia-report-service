package com.pastamenia.Repository;


import com.pastamenia.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Pasindu Lakmal
 */
public interface CompanyRepository extends JpaRepository<Company, Long> {


}
