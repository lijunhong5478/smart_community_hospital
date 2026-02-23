package com.tyut.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tyut.entity.DoctorSchedule;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Update;

public interface DoctorScheduleMapper extends BaseMapper<DoctorSchedule> {
    @Delete("delete from doctor_schedule where doctor_id = #{doctorId}")
    int deleteByDoctorId(Long doctorId);

    /**
     *每周一把currentNumber清零
     * @return
     */
    @Update("update doctor_schedule set current_number = 0")
    int resetCurrentNumber();
    @Update("update doctor_schedule set status = 1")
    int resetScheduleStatus();
}
