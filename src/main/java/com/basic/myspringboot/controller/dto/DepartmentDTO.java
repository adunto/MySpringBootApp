package com.basic.myspringboot.controller.dto;

import com.basic.myspringboot.entity.Department;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

public class DepartmentDTO {
    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        @NotBlank(message = "필수 입력 : Department name")
        @Size(max = 100, message = "Department name 은 100자 이하여야 합니다.")
        private String name;

        @NotBlank(message = "필수 입력 : Department code")
        @Size(max = 10, message = "Department code 는 10자 이하여야 합니다.")
        private String code;
    }

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long id;
        private String name;
        private String code;
        private Long studentCount;
        private List<StudentDTO.SimpleResponse> students;

        public static Response fromEntity(Department department) {
            return Response.builder()
                    .id(department.getId())
                    .name(department.getName())
                    .code(department.getCode())
                    .studentCount((long) department.getStudents().size())
                    .students(department.getStudents().stream()
                            .map(StudentDTO.SimpleResponse::fromEntity)
                            .collect(Collectors.toList()))
                    .build();
        }
    }

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SimpleResponse {
        private Long id;
        private String name;
        private String code;
        private Long studentCount;

        public static SimpleResponse fromEntity(Department department) {
            return SimpleResponse.builder()
                    .id(department.getId())
                    .name(department.getName())
                    .code(department.getCode())
                    .studentCount((long) department.getStudents().size())
                    .build();
        }
    }
}
