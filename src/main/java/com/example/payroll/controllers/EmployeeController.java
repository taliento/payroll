package com.example.payroll.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.payroll.exceptions.EmployeeNotFoundException;
import com.example.payroll.models.Employee;
import com.example.payroll.repository.EmployeeRepository;
import com.example.payroll.tools.EmployeeModelAssembler;

@RestController
public class EmployeeController {

	@Autowired
	private EmployeeRepository repository;
	
	@Autowired
	private EmployeeModelAssembler assembler;

	// Aggregate root
	// tag::get-aggregate-root[]
	@GetMapping("/employees")
	public CollectionModel<EntityModel<Employee>> all() {

		List<EntityModel<Employee>> employees = repository.findAll().stream() //
			      .map(assembler::toModel) //
			      .collect(Collectors.toList());

		return CollectionModel.of(employees, linkTo(methodOn(EmployeeController.class).all()).withSelfRel());
	}
	// end::get-aggregate-root[]

	@PostMapping("/employees")
	public Employee newEmployee(@RequestBody Employee newEmployee) {
		return repository.save(newEmployee);
	}

	// Single item

	@GetMapping("/employees/{id}")
	public EntityModel<Employee> one(@PathVariable Long id) {

		Employee employee = repository.findById(id) //
				.orElseThrow(() -> new EmployeeNotFoundException(id));

		return assembler.toModel(employee);
	}

	@PutMapping("/employees/{id}")
	public Employee replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {

		return repository.findById(id)
				.map(employee -> {
					employee.setName(newEmployee.getName());
					employee.setRole(newEmployee.getRole());
					return repository.save(employee);
				})
				.orElseGet(() -> {
					newEmployee.setId(id);
					return repository.save(newEmployee);
				});
	}

	@DeleteMapping("/employees/{id}")
	public void deleteEmployee(@PathVariable Long id) {
		repository.deleteById(id);
	}
}
