package com.basic.myspringboot.service;

import com.basic.myspringboot.controller.dto.DepartmentDTO;
import com.basic.myspringboot.entity.Department;
import com.basic.myspringboot.exception.BusinessException;
import com.basic.myspringboot.exception.ErrorCode;
import com.basic.myspringboot.repository.DepartmentRepository;
import com.basic.myspringboot.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final StudentRepository studentRepository;

    public List<DepartmentDTO.SimpleResponse> getAllDepartments() {
        return departmentRepository.findAll()
                .stream()
                .map(DepartmentDTO.SimpleResponse::fromEntity)
                .toList();
    }

    public DepartmentDTO.Response getDepartmentById(Long id) {
        return DepartmentDTO.Response.fromEntity(
                departmentRepository.findById(id)
                .orElseThrow(
                        () -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Department", "id", id)
                )
        );
    }

    public DepartmentDTO.Response getDepartmentByCode(String code) {
        return DepartmentDTO.Response.fromEntity(
                departmentRepository.findByCode(code)
                        .orElseThrow(
                                () -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Department", "code", code)
                        )
        );
    }

    @Transactional
    public DepartmentDTO.Response createDepartment(DepartmentDTO.Request request) {
        // 속성 중복 값 확인하기 (name, code)
        if (departmentRepository.existsByCode(request.getCode())) {
            throw new BusinessException(ErrorCode.DEPARTMENT_CODE_DUPLICATE, request.getCode());
        }
        if (departmentRepository.existsByName(request.getName())) {
            throw new BusinessException(ErrorCode.DEPARTMENT_NAME_DUPLICATE, request.getName());
        }

        Department department =
                Department.builder()
                        .code(request.getCode())
                        .name(request.getName())
                        .build();

        Department savedDepartment = departmentRepository.save(department);
        return DepartmentDTO.Response.fromEntity(savedDepartment);
    }

    @Transactional
    public DepartmentDTO.Response updateDepartment(Long id, DepartmentDTO.Request request) {
        // 수정할 객체
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND,
                        "Department", "id", id));

        // 코드가 같은지 비교 & 수정 요청 코드 중복 체크
        if (!department.getCode().equals(request.getCode()) &&
                departmentRepository.existsByCode(request.getCode())) {
            throw new BusinessException(ErrorCode.DEPARTMENT_CODE_DUPLICATE, request.getCode());
        }
        // 이름이 같은지 비교 & 수정 요청 이름 중복 체크
        if (!department.getName().equals(request.getName()) &&
                departmentRepository.existsByName(request.getName())) {
            throw new BusinessException(ErrorCode.DEPARTMENT_NAME_DUPLICATE,
                    request.getName());
        }

        department.setCode(request.getCode());
        department.setName(request.getName());

        Department updatedDepartment = departmentRepository.save(department);
        return DepartmentDTO.Response.fromEntity(updatedDepartment);

    }

    @Transactional
    public void deleteDepartment(Long id) {
        if (!departmentRepository.existsById(id)) {
            throw new BusinessException(ErrorCode.RESOURCE_NOT_FOUND,
                    "Department", "id", id);
        }

        // Check if department has students
        Long studentCount = studentRepository.countByDepartmentId(id);
        if (studentCount > 0) {
            throw new BusinessException(ErrorCode.DEPARTMENT_HAS_STUDENTS,
                    id, studentCount);
        }

        departmentRepository.deleteById(id);
    }
}
