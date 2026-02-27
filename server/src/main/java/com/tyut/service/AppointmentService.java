package com.tyut.service;

import com.tyut.dto.AppointmentQueryDTO;
import com.tyut.dto.ExactTimeAppointmentDTO;
import com.tyut.result.PageResult;

public interface AppointmentService {
    void saveAppointment(ExactTimeAppointmentDTO dto);
    PageResult list(AppointmentQueryDTO dto);
    void cancelAppointment(Long appointmentId, String cancelReason);
    void call(Long appointmentId);
    void startConsult(Long appointmentId);
    void skip(Long appointmentId);
    void finish(Long appointmentId);
    Boolean isAppointed(AppointmentQueryDTO dto);
}
