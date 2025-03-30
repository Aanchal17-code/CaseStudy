/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.casestudy;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors; 
public class CaseStudy {
    public static class Employee {
        int id;
        String firstName;
        String lastName;
        int salary;
        Integer managerId;

        public Employee(int id, String firstName, String lastName, int salary, Integer managerId) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.salary = salary;
            this.managerId = managerId;
        }
    }

    public static void main(String[] args) {
        String filePath = "employees.csv"; // Update with your CSV file path
        List<Employee> employees = readEmployeesFromFile(filePath);

        Map<Integer, List<Employee>> managerToSubordinates = employees.stream()
                .filter(e -> e.managerId != null)
                .collect(Collectors.groupingBy(e -> e.managerId));

        analyzeSalaries(employees, managerToSubordinates);
        analyzeReportingLines(employees);
    }

    public static List<Employee> readEmployeesFromFile(String filePath) {
        List<Employee> employees = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0]);
                String firstName = parts[1];
                String lastName = parts[2];
                int salary = Integer.parseInt(parts[3]);
                Integer managerId = parts[4].isEmpty() ? null : Integer.parseInt(parts[4]);
                employees.add(new Employee(id, firstName, lastName, salary, managerId));
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return employees;
    }

    private static void analyzeSalaries(List<Employee> employees, Map<Integer, List<Employee>> managerToSubordinates) {
        for (Employee manager : employees) {
            if (managerToSubordinates.containsKey(manager.id)) {
                List<Employee> subordinates = managerToSubordinates.get(manager.id);
                double averageSalary = subordinates.stream().mapToInt(e -> e.salary).average().orElse(0);

                double minSalaryThreshold = averageSalary * 1.2; // 20% above average
                double maxSalaryThreshold = averageSalary * 1.5; // 50% above average

                if (manager.salary < minSalaryThreshold) {
                    System.out.printf("Manager %s %s earns less than they should by %.2f%n",
                            manager.firstName, manager.lastName, minSalaryThreshold - manager.salary);
                } else if (manager.salary > maxSalaryThreshold) {
                    System.out.printf("Manager %s %s earns more than they should by %.2f%n",
                            manager.firstName, manager.lastName, manager.salary - maxSalaryThreshold);
                }
            }
        }
    }

    private static void analyzeReportingLines(List<Employee> employees) {
        Map<Integer, Employee> employeeMap = employees.stream()
                .collect(Collectors.toMap(e -> e.id, e -> e));

        for (Employee employee : employees) {
            int reportingDepth = calculateReportingDepth(employee, employeeMap);
            if (reportingDepth > 4) {
                System.out.printf("Employee %s %s has a reporting line that is too long (%d levels)%n",
                        employee.firstName, employee.lastName, reportingDepth);
            }
        }
    }

    public static int calculateReportingDepth(Employee employee, Map<Integer, Employee> employeeMap) {
        int depth = 0;
        while (employee.managerId != null && employeeMap.containsKey(employee.managerId)) {
            depth++;
            employee = employeeMap.get(employee.managerId);
        }
        return depth;
    }
}

