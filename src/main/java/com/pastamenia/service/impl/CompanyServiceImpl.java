package com.pastamenia.service.impl;

import com.pastamenia.Repository.CompanyRepository;
import com.pastamenia.dto.reportDto.DailySalesMixReportDto;
import com.pastamenia.entity.Company;
import com.pastamenia.report.DailySalesMixReport;
import com.pastamenia.service.CompanyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Pasindu Lakmal
 */
@Service
@Transactional
@Slf4j
public class CompanyServiceImpl implements CompanyService {


    @Autowired
    CompanyRepository companyRepository;


    @Override
    public void createInitialCompanies() {
        List<Company> companies =  new ArrayList<>();
        Company companyOne =  new Company();
        companyOne.setName("Island Wraps");
        companyOne.setToken("3e405b31626349b699a44984f8861c5b");
        Company companyTwo =  new Company();
        companyTwo.setName("PastaMania");
        companyTwo.setToken("d22e68278c144eb8b22c50a2623bccc9");
        companies.add(companyOne);
        companies.add(companyTwo);
        companyRepository.saveAll(companies);
    }

    @Override
    public List<Company> findAll() {
        return companyRepository.findAll();
    }


}
