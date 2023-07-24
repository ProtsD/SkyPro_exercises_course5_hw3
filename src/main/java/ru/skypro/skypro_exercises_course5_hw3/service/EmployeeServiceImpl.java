package ru.skypro.skypro_exercises_course5_hw3.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.skypro.skypro_exercises_course5_hw3.dto.EmployeeDTO;
import ru.skypro.skypro_exercises_course5_hw3.dto.EmployeeFullInfo;
import ru.skypro.skypro_exercises_course5_hw3.dto.EmployeeMapper;
import ru.skypro.skypro_exercises_course5_hw3.entity.Position;
import ru.skypro.skypro_exercises_course5_hw3.repository.EmployeeRepository;
import ru.skypro.skypro_exercises_course5_hw3.repository.PositionRepository;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final PositionRepository positionRepository;
    Logger logger = LoggerFactory.getLogger(ReportServiceImpl.class);

    @Override
    public void addEmployees(EmployeeDTO[] employeeDTO) {
        logger.info("Invoke addEmployees() with argument: employeeDTO={}", Arrays.stream(employeeDTO).toList());
        employeeRepository.saveAll(Arrays.stream(employeeDTO)
                .map(e -> {
                    Position position = positionRepository.getPositionByName(e.getPosition());
                    return EmployeeMapper.toEmployee(e, position);
                })
                .toList());
        logger.debug("addEmployees() is processed ok");
    }

    @Override
    public void addEmployee(EmployeeDTO employeeDTO) {
        logger.info("Invoke addEmployee() with argument: employeeDTO={}", employeeDTO);
        Position position = positionRepository.getPositionByName(employeeDTO.getPosition());
        System.out.println("hjijkpjoiiokokokokokokok" + position);
        employeeRepository.save(EmployeeMapper.toEmployee(employeeDTO, position));
        logger.debug("addEmployee() is processed ok");
    }

    @Override
    public void putEmployee(Integer id, EmployeeDTO employeeDTO) {
        logger.info("Invoke putEmployee() with argument: id={}, employeeDTO={}", id, employeeDTO);
        if (employeeRepository.existsById(id)) {
            employeeDTO.setId(id);
            Position position = positionRepository.getPositionByName(employeeDTO.getPosition());
            employeeRepository.save(EmployeeMapper.toEmployee(employeeDTO, position));
            logger.debug("putEmployee() is processed ok");
        }
    }

    @Override
    public EmployeeDTO getEmployee(Integer id) {
        logger.info("Invoke getEmployee() with argument: id={}", id);
        if (employeeRepository.findById(id).isPresent()) {
            EmployeeDTO employeeDTO = EmployeeMapper.fromEmployee(employeeRepository.findById(id).get());
            logger.debug("putEmployee() is processed ok");
            return employeeDTO;
        }
        logger.error("Invoke getEmployee() with argument: id={}. There is no employee with the id.", id);
        throw new IndexOutOfBoundsException();
    }

    @Override
    public void delEmployee(Integer id) {
        logger.info("Invoke delEmployee() with argument: id={}", id);
        employeeRepository.deleteById(id);
        logger.debug("delEmployee() is processed ok");
    }

    @Override
    public List<EmployeeDTO> getEmployeeWithSalaryHigherThan(int salary) {
        logger.info("Invoke getEmployeeWithSalaryHigherThan() with argument: salary={}", salary);
        List<EmployeeDTO> listEmployeeDTO = employeeRepository.getSalaryHigherThan(salary).stream().map(EmployeeMapper::fromEmployee).toList();
        logger.debug("getEmployeeWithSalaryHigherThan() is processed ok");
        return listEmployeeDTO;
    }

    @Override
    public List<EmployeeDTO> getEmployeesWithHighestSalary() {
        logger.info("Invoke getEmployeesWithHighestSalary() w/o arguments");
        List<EmployeeDTO> listEmployeeDTO = employeeRepository.getEmployeesWithHighestSalary().stream().map(EmployeeMapper::fromEmployee).toList();
        logger.debug("getEmployeesWithHighestSalary() is processed ok");
        return listEmployeeDTO;
    }

    @Override
    public List<EmployeeDTO> getEmployeesOnPosition(String positionName) {
        logger.info("Invoke getEmployeesOnPosition() with argument: position={}", positionName);
        List<EmployeeDTO> EmployeeDTOList;
        Position position = positionRepository.getPositionByName(positionName);
        System.out.println(position);
        if (positionName == null || positionName.isBlank() || position == null) {
            EmployeeDTOList = employeeRepository.findAll().stream()
                    .map(EmployeeMapper::fromEmployee)
                    .toList();
        } else {
            EmployeeDTOList = employeeRepository.getEmployeesOnPosition(position.getId()).stream()
                    .map(EmployeeMapper::fromEmployee)
                    .toList();
        }
        logger.debug("getEmployeesOnPosition() is processed ok");
        return EmployeeDTOList;
    }


    @Override
    public EmployeeFullInfo getEmployeeFullInfo(Integer id) {
        logger.info("Invoke getEmployeeFullInfo() with argument: id={}", id);
        EmployeeFullInfo employeeInfo = employeeRepository.getEmployeeFullInfo(id);
        logger.debug("getEmployeeFullInfo() is processed ok");
        return employeeInfo;
    }

    @Override
    public List<EmployeeDTO> getEmployeePage(PageRequest pageRequest) {
        logger.info("Invoke getEmployeePage() with argument: pageRequest={}", pageRequest);
        List<EmployeeDTO> employeesList = employeeRepository
                .findAll(pageRequest)
                .stream()
                .map(EmployeeMapper::fromEmployee)
                .toList();
        logger.debug("getEmployeePage() is processed ok");
        return employeesList;
    }
}
