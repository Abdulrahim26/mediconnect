package com.mediconnect.mediconnectapi.repository;


import com.mediconnect.mediconnectapi.entity.DoctorSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.UUID;


public interface DoctorScheduleRepository extends JpaRepository<DoctorSchedule, UUID> {


    List<DoctorSchedule> findByDoctorId(UUID doctorId);


    List<DoctorSchedule> findByDoctorIdAndDayOfWeek(
            UUID doctorId,
            DayOfWeek dayOfWeek
    );

}