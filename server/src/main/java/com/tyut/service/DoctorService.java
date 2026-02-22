package com.tyut.service;

import com.tyut.dto.AddDoctorDTO;
import com.tyut.dto.DoctorScheduleUpdateDTO;

public interface DoctorService {
    void registerDoctor(AddDoctorDTO addDoctorDTO);
    void doctorSchedule(DoctorScheduleUpdateDTO dto);
}
