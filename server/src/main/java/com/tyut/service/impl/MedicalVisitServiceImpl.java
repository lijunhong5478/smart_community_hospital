package com.tyut.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tyut.annotation.DataBackUp;
import com.tyut.constant.AccountConstant;
import com.tyut.constant.ModuleConstant;
import com.tyut.context.BaseContext;
import com.tyut.dto.MedicalVisitQueryDTO;
import com.tyut.entity.DoctorProfile;
import com.tyut.entity.MedicalVisit;
import com.tyut.entity.SysUser;
import com.tyut.exception.BaseException;
import com.tyut.mapper.DoctorProfileMapper;
import com.tyut.mapper.MedicalVisitMapper;

import com.tyut.mapper.ResidentMapper;
import com.tyut.mapper.UserMapper;
import com.tyut.result.PageResult;
import com.tyut.service.MedicalVisitService;
import com.tyut.vo.DoctorDetailVO;
import com.tyut.vo.MedicalVisitVO;
import com.tyut.vo.ResidentDetailVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class MedicalVisitServiceImpl implements MedicalVisitService {
    @Autowired
    private MedicalVisitMapper medicalVisitMapper;
    @Autowired
    private DoctorProfileMapper doctorProfileMapper;
    @Autowired
    private UserMapper userMapper;


    @Override
    public MedicalVisitVO getById(Long id) {
        MedicalVisit medicalVisit = medicalVisitMapper.selectById(id);
        MedicalVisitVO vo = new MedicalVisitVO();
        BeanUtils.copyProperties(medicalVisit, vo);
        DoctorDetailVO doctorDetailVO = userMapper.selectDoctorById(medicalVisit.getDoctorId());
        vo.setDoctorName(doctorDetailVO.getName());
        vo.setDoctorPhone(doctorDetailVO.getPhone());
        vo.setDoctorImage(doctorDetailVO.getAvatarUrl());
        vo.setDoctorTitle(doctorDetailVO.getTitle());
        vo.setDoctorDepartment(doctorDetailVO.getDepartmentName());
        String name = userMapper.selectResidentById(medicalVisit.getResidentId()).getName();
        vo.setResidentName(name);
        return vo;
    }

    @DataBackUp(module = ModuleConstant.CONSULTATION)
    @Override
    public void save(MedicalVisit medicalVisit) {
        medicalVisit.setIsDeleted(AccountConstant.NOT_DELETE);
        medicalVisit.setCreateTime(java.time.LocalDateTime.now());
        medicalVisitMapper.insert(medicalVisit);
    }

    @DataBackUp(module = ModuleConstant.CONSULTATION)
    @Override
    public void update(MedicalVisit medicalVisit) {
        medicalVisitMapper.updateById(medicalVisit);
    }

    @Override
    public PageResult list(MedicalVisitQueryDTO medicalVisitQueryDTO) {
        Page<MedicalVisit> page = new Page<>(medicalVisitQueryDTO.getPageNum(), medicalVisitQueryDTO.getPageSize());
        LambdaQueryWrapper<MedicalVisit> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MedicalVisit::getResidentId, medicalVisitQueryDTO.getResidentId());
        if (medicalVisitQueryDTO.getDoctorName() != null) {
            DoctorProfile doctorProfile = doctorProfileMapper.selectOne(new LambdaQueryWrapper<DoctorProfile>()
                    .eq(DoctorProfile::getName, medicalVisitQueryDTO.getDoctorName()));
            queryWrapper.in(MedicalVisit::getDoctorId, doctorProfile.getUserId());
        }
        if (medicalVisitQueryDTO.getCreateDate() != null) {
            queryWrapper.between(MedicalVisit::getCreateTime, medicalVisitQueryDTO.getCreateDate().atStartOfDay()
                    , medicalVisitQueryDTO.getCreateDate().plusDays(1).atStartOfDay());
        }
        IPage<MedicalVisit> medicalVisitIPage = medicalVisitMapper.selectPage(page, queryWrapper);
        List<MedicalVisit> medicalVisits = medicalVisitIPage.getRecords();
        System.out.println(medicalVisits);
        List<MedicalVisitVO> vos = castToVO(medicalVisits);
        return PageResult.builder()
                .total(medicalVisitIPage.getTotal())
                .dataList(vos)
                .build();
    }

    @Override
    public Boolean contains(Long medicalVisitId, Long doctorId) {
        LambdaQueryWrapper<MedicalVisit> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MedicalVisit::getId, medicalVisitId)
                .eq(MedicalVisit::getDoctorId, doctorId);
        return medicalVisitMapper.selectCount(queryWrapper) > 0;
    }

    private List<MedicalVisitVO> castToVO(List<MedicalVisit> medicalVisits) {
        return medicalVisits.stream().map(medicalVisit -> {
            DoctorDetailVO doctorVO = userMapper.selectDoctorById(medicalVisit.getDoctorId());
            ResidentDetailVO residentVO = userMapper.selectResidentById(medicalVisit.getResidentId());
            MedicalVisitVO vo = MedicalVisitVO.builder()
                    .doctorPhone(doctorVO.getPhone())
                    .doctorName(doctorVO.getName())
                    .doctorImage(doctorVO.getAvatarUrl())
                    .doctorTitle(doctorVO.getTitle())
                    .doctorDepartment(doctorVO.getDepartmentName())
                    .residentName(residentVO.getName())
                    .chiefComplaint(medicalVisit.getChiefComplaint())
                    .treatmentAdvice(medicalVisit.getTreatmentAdvice())
                    .createTime(medicalVisit.getCreateTime())
                    .id(medicalVisit.getId())
                    .build();
            return vo;
        }).collect(java.util.stream.Collectors.toList());
    }

}
