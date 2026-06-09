package com.example.ims_spring.controller;

import com.example.ims_spring.dto.MentorRequest;
import com.example.ims_spring.dto.MentorResponse;
import com.example.ims_spring.service.MentorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mentors")
@RequiredArgsConstructor
public class MentorController {
    private final MentorService mentorService;

    @GetMapping
    public ResponseEntity<List<MentorResponse>> getMentors(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(required = false) Long departmentId
    ) {
        return ResponseEntity.ok(mentorService.getMentors(search, departmentId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<MentorResponse>> getAllMentors() {
        return ResponseEntity.ok(mentorService.getAllMentors());
    }

    @PostMapping
    public ResponseEntity<MentorResponse> createMentor(@Valid @RequestBody MentorRequest request) {
        MentorResponse response = mentorService.createMentor(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MentorResponse> updateMentor(@PathVariable Long id, @Valid @RequestBody MentorRequest request) {
        return ResponseEntity.ok(mentorService.updateMentor(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMentor(@PathVariable Long id) {
        mentorService.deleteMentor(id);
        return ResponseEntity.noContent().build();
    }

}
