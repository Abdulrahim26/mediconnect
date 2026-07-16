package com.mediconnect.mediconnectapi.service;


import com.mediconnect.mediconnectapi.dto.request.CreateHospitalRequest;
import com.mediconnect.mediconnectapi.dto.response.HospitalResponse;
import com.mediconnect.mediconnectapi.entity.Hospital;
import com.mediconnect.mediconnectapi.repository.HospitalRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class HospitalService {


    private final HospitalRepository hospitalRepository;



    public HospitalResponse createHospital(
            CreateHospitalRequest request
    ){

        Hospital hospital = new Hospital();


        hospital.setName(
                request.getName()
        );


        hospital.setLocation(
                request.getLocation()
        );


        hospital.setAddress(
                request.getAddress()
        );


        hospital.setPhone(
                request.getPhone()
        );


        hospital.setEmail(
                request.getEmail()
        );


        Hospital savedHospital =
                hospitalRepository.save(hospital);



        return mapToResponse(savedHospital);
    }



    public List<HospitalResponse> getAllHospitals(){

        return hospitalRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

    }




    public HospitalResponse getHospitalById(
            UUID id
    ){

        Hospital hospital =
                hospitalRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Hospital not found"
                                )
                        );


        return mapToResponse(hospital);
    }




    private HospitalResponse mapToResponse(
            Hospital hospital
    ){

        return new HospitalResponse(

                hospital.getId(),

                hospital.getName(),

                hospital.getLocation(),

                hospital.getAddress(),

                hospital.getPhone(),

                hospital.getEmail()

        );
    }

}