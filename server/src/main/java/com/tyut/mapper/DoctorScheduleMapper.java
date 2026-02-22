package com.tyut.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tyut.entity.DoctorSchedule;
import org.apache.ibatis.annotations.Delete;

public interface DoctorScheduleMapper extends BaseMapper<DoctorSchedule> {
    @Delete("delete from doctor_schedule where doctor_id = #{doctorId}")
    int deleteByDoctorId(Long doctorId);
}
