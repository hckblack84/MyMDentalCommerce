package com.MyMDentis.MyMDentistComerce.Service;

import com.MyMDentis.MyMDentistComerce.DTO.DTODepartment;
import com.MyMDentis.MyMDentistComerce.Exception.NotFoundEntityException;
import com.MyMDentis.MyMDentistComerce.Model.Department;
import com.MyMDentis.MyMDentistComerce.Repository.DepartmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DepartmentService departmentService;

    private Department mockedDepartment;
    private DTODepartment dtoDepartment;

    @BeforeEach
    void setUp() {
        mockedDepartment = new Department();
        mockedDepartment.setIdDepartment(1L);
        mockedDepartment.setNameDepartment("Ortodoncia");

        dtoDepartment = new DTODepartment();
        dtoDepartment.parseToDTODepartment(mockedDepartment);
    }

    @Test
    @DisplayName("from repository get departments")
    void emptyListFromGetAllDepartment() {
        when(departmentRepository.findAll()).thenReturn(List.of(mockedDepartment));
        List<DTODepartment> result = departmentService.getAllDepartment();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Ortodoncia", result.getFirst().getNameDepartment());
        verify(departmentRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("no departments in repository")
    void getAllDepartment() {
        when(departmentRepository.findAll()).thenReturn(Collections.emptyList());
        List<DTODepartment> result = departmentService.getAllDepartment();
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(departmentRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("get department by departmentName")
    void foundDepartmentByDepartmentName() {
        when(departmentRepository.findByNameDepartment("Ortodoncia")).thenReturn(Optional.of(mockedDepartment));
        DTODepartment result = departmentService.getDepartmentByName("Ortodoncia");
        assertNotNull(result);
        assertEquals("Ortodoncia", result.getNameDepartment());
    }

    @Test
    @DisplayName("Not found department by departmentName")
    void notFoundDepartmentByDepartmentName() {
        when(departmentRepository.findByNameDepartment(anyString())).thenReturn(Optional.empty());
        assertThrows(NotFoundEntityException.class, () -> {
            departmentService.getDepartmentByName("Nullified Department");
        });
    }
    
    @Test
    @DisplayName("getDepartmentById should return DTO when department is found")
    void getDepartmentById_shouldReturnDto_whenFound() {
        when(departmentRepository.findById(anyLong())).thenReturn(Optional.of(mockedDepartment));
        DTODepartment d = departmentService.getDepartmentById(1L);
        assertEquals("Ortodoncia", d.getNameDepartment());
    }

    @Test
    @DisplayName("getDepartmentById should throw NotFoundEntityException when not found")
    void getDepartmentById_shouldThrowException_whenNotFound() {
        when(departmentRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NotFoundEntityException.class, () -> {
            departmentService.getDepartmentById(1L);
        });
    }

    @Test
    @DisplayName("createDepartment should save and return DTO")
    void createDepartment_shouldSaveAndReturnDto() {
        when(departmentRepository.save(any(Department.class))).thenReturn(mockedDepartment);
        DTODepartment result = departmentService.createDepartment(dtoDepartment);
        assertNotNull(result);
        assertEquals("Ortodoncia", result.getNameDepartment());
        verify(departmentRepository, times(1)).save(any(Department.class));
    }
}
