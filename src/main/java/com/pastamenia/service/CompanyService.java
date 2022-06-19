package com.pastamenia.service;

import com.pastamenia.entity.Company;

import java.util.List;

/**
 * @author Pasindu Lakmal
 */
public interface CompanyService {

     void createInitialCompanies();

     List<Company> findAll();


}
