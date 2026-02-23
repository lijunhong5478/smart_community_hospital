package com.tyut.service.impl;

import com.tyut.entity.MedicalVisit;
import com.tyut.mapper.MedicalVisitMapper;
import com.tyut.service.MedicalVisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MedicalVisitServiceImpl implements MedicalVisitService {
    @Autowired
    private MedicalVisitMapper medicalVisitMapper;
    @Override
    public MedicalVisit getById(Long id) {
        return medicalVisitMapper.selectById(id);
    }
}
