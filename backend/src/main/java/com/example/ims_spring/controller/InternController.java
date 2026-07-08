package com.example.ims_spring.controller;

import com.example.ims_spring.dto.InternRequest;
import com.example.ims_spring.dto.InternResponse;
import com.example.ims_spring.entity.InternStatus;
import com.example.ims_spring.service.InternService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/interns")
@RequiredArgsConstructor
public class InternController {

    private final InternService internService;

    @GetMapping
    public ResponseEntity<List<InternResponse>> getInterns(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) Long mentorId,
            @RequestParam(required = false) InternStatus status
    ) {
        return ResponseEntity.ok(
                internService.getInterns(search, departmentId, mentorId, status)
        );
    }

    @GetMapping("/statuses")
    public ResponseEntity<List<String>> getStatuses() {
        List<String> statuses = Arrays.stream(InternStatus.values())
                .map(Enum::name)
                .toList();

        return ResponseEntity.ok(statuses);
    }

    @PostMapping
    public ResponseEntity<InternResponse> createIntern(@Valid @RequestBody InternRequest request) {
        InternResponse response = internService.createIntern(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InternResponse> updateIntern(@PathVariable Long id, @Valid @RequestBody InternRequest request) {
        return ResponseEntity.ok(internService.updateIntern(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIntern(@PathVariable Long id) {
        internService.deleteIntern(id);
        return ResponseEntity.noContent().build();
    }
}