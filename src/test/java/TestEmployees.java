/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;
import com.mycompany.casestudy.*;
public class TestEmployees {

    @Test
    public void testCalculateReportingDepth() {
        // Create mock data for testing
        Map<Integer, CaseStudy.Employee> employeeMap = new HashMap<>();
        employeeMap.put(123, new CaseStudy.Employee(123, "Joe", "Doe", 60000, null)); // CEO
        employeeMap.put(124, new CaseStudy.Employee(124, "Martin", "Chekov", 45000, 123));
        
        CaseStudy.Employee emp = new CaseStudy.Employee(125, "Bob", "Romstad", 47000, 124);

        int depth = CaseStudy.calculateReportingDepth(emp, employeeMap);
        
        assertEquals(2, depth); // Bob reports to Martin who reports to Joe
    }
}

