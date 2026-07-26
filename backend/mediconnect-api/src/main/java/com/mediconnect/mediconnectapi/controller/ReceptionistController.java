package com.mediconnect.mediconnectapi.controller;


import com.mediconnect.mediconnectapi.dto.request.CreateReceptionistRequest;
import com.mediconnect.mediconnectapi.dto.response.ReceptionistResponse;
import com.mediconnect.mediconnectapi.service.ReceptionistService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/admin/receptionists")
@RequiredArgsConstructor
public class ReceptionistController {


    private final ReceptionistService receptionistService;



    // Hospital Admin creates receptionist
    @PostMapping
    public ResponseEntity<ReceptionistResponse> createReceptionist(
            @Valid @RequestBody CreateReceptionistRequest request
    ){

        return ResponseEntity.ok(
                receptionistService.createReceptionist(request)
        );

    }



    // Hospital Admin views receptionists in their hospital
    @GetMapping
    public ResponseEntity<List<ReceptionistResponse>> getReceptionists(){

        return ResponseEntity.ok(
                receptionistService.getMyHospitalReceptionists()
        );

    }

}