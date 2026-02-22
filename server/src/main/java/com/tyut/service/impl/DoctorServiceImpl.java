package com.tyut.service.impl;

import com.tyut.annotation.DataBackUp;
import com.tyut.constant.AccountConstant;
import com.tyut.constant.ModuleConstant;
import com.tyut.dto.AddDoctorDTO;
import com.tyut.dto.DoctorScheduleUpdateDTO;
import com.tyut.entity.DoctorProfile;
import com.tyut.entity.DoctorSchedule;
import com.tyut.entity.SysUser;
import com.tyut.mapper.DoctorProfileMapper;
import com.tyut.mapper.DoctorScheduleMapper;
import com.tyut.mapper.UserMapper;
import com.tyut.service.DoctorService;
import com.tyut.utils.CryptoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DoctorServiceImpl implements DoctorService {
    @Autowired
    private CryptoUtil cryptoUtil;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DoctorProfileMapper doctorProfileMapper;
    @Autowired
    private DoctorScheduleMapper doctorScheduleMapper;
    /**
     * 添加医生
     * @param addDoctorDTO
     */
    @Transactional
    @Override
    @DataBackUp(module = ModuleConstant.DOCTOR)
    public void registerDoctor(AddDoctorDTO addDoctorDTO) {
        SysUser sysUser=SysUser.builder()
                .username(addDoctorDTO.getUsername())
                .phone(addDoctorDTO.getPhone())
                .idCard(addDoctorDTO.getIdCard())
                .password(cryptoUtil.encodePassword(addDoctorDTO.getPassword()))
                .avatarUrl(addDoctorDTO.getAvatarUrl())
                .status(AccountConstant.STATUS_NORMAL)
                .isDeleted(AccountConstant.NOT_DELETE)
                .roleType(AccountConstant.ROLE_DOCTOR)
                .updateTime(LocalDateTime.now())
                .createTime(LocalDateTime.now())
                .build();
        userMapper.insert(sysUser);
        DoctorProfile doctorProfile=DoctorProfile.builder()
                .userId(sysUser.getId())
                .name(addDoctorDTO.getName())
                .specialty(addDoctorDTO.getSpecialty())
                .title(addDoctorDTO.getTitle())
                .introduction(addDoctorDTO.getIntroduction())
                .departmentId(addDoctorDTO.getDepartmentId())
                .gender(addDoctorDTO.getGender())
                .build();
        doctorProfileMapper.insert(doctorProfile);
        for (DoctorSchedule doctorSchedule : addDoctorDTO.getDoctorSchedules()) {
            doctorSchedule.setDoctorId(sysUser.getId());
            doctorSchedule.setCreateTime(LocalDateTime.now());
            doctorScheduleMapper.insert(doctorSchedule);
        }
    }
    @DataBackUp(module = ModuleConstant.DOCTOR)
    @Transactional
    @Override
    public void doctorSchedule(DoctorScheduleUpdateDTO dto) {
        //删除原本的排班计划
        doctorScheduleMapper.deleteByDoctorId(dto.getDoctorId());
        //插入新的排班计划
        List<DoctorSchedule> doctorSchedules = dto.getDoctorSchedules();
        for (DoctorSchedule doctorSchedule : doctorSchedules) {
            doctorSchedule.setDoctorId(dto.getDoctorId());
            doctorSchedule.setCreateTime(LocalDateTime.now());
            doctorScheduleMapper.insert(doctorSchedule);
        }
    }

}
