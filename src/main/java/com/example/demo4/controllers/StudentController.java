package com.example.demo4.controllers;
import org.springframework.web.bind.annotation.*;
import com.example.demo4.model.Student;
import com.example.demo4.model.Student;
import com.example.demo4.model.StudentRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.demo4.model.User;

@Controller
public class StudentController {
    @Autowired
    private StudentRepository studentRepo;


    @GetMapping("/students/view")
    public String getAllStudents(Model model) {
        System.out.println("getting all users");
        List<Student> students = studentRepo.findAll();
        model.addAttribute("st", students);
        return "students/showAll"; // Make sure this matches the template location
    }


    @PostMapping("/students/add")
    public String addUser(@RequestParam Map<String, String> newstudent, HttpServletResponse response) {
        System.out.println("Add student");
        String newName = newstudent.get("name");
        int newWeight = Integer.parseInt(newstudent.get("weight"));
        int newHeight = Integer.parseInt(newstudent.get("height"));
        String newColour = newstudent.get("colour");
        float newGpa = Float.parseFloat(newstudent.get("gpa"));

        studentRepo.save(new Student(newName, newWeight, newHeight, newColour, newGpa));

        response.setStatus(201);
        return "students/addedStudent";
    }

    @PostMapping("/students/delete")
    public String deleteUser(@RequestParam Map<String, String> oldstudent, Model model, HttpServletResponse response) {
        System.out.println("delete student");
        String oldName = oldstudent.get("name");
        int oldWeight = Integer.parseInt(oldstudent.get("weight"));
        int oldHeight = Integer.parseInt(oldstudent.get("height"));
        String oldColour = oldstudent.get("colour");
        float oldGpa = Float.parseFloat(oldstudent.get("gpa"));
        // Find the student by the given parameters
        List<Student> studentList = studentRepo.findByNameAndWeightAndHeightAndColourAndGpa(oldName, oldWeight, oldHeight, oldColour, oldGpa);

        // Check if the student exists
        if (!studentList.isEmpty()) {
            Student studentToDelete = studentList.get(0);
            studentRepo.deleteById(studentToDelete.getId()); // Assuming your Student entity has an "id" field
            // response.setStatus(204); // No content
            //return "redirect:/home.html"; // Return the path without the leading slash
            return "students/addedStudent";
        } else {
            response.setStatus(404); // Not Found
            return "redirect:/delete.html";
        }
    }

    @GetMapping("/students/update")
    public String showUpdateForm(Model model) {
        // Add any necessary data to the model if needed
        return "students/update"; // Return the name of your HTML template file
    }

    @PostMapping("/students/update")

    public String editStudentAttributes(@RequestParam String name,
                                        @RequestParam int weight,
                                        @RequestParam int height,
                                        @RequestParam float gpa,
                                        @RequestParam String colour,
                                        Model model) {
        // Perform a search for the student based on the provided criteria
        List<Student> students = studentRepo.findByNameAndWeightAndHeightAndColourAndGpa(name, weight, height, colour, gpa);

        // Check if any students are found
        if (!students.isEmpty()) {
            // If multiple students are found, you might want to handle that scenario
            // For simplicity, assuming the first student found is the one to edit
            Student studentToEdit = students.get(0);

            // Add the student details to the model for use in the edit page
            model.addAttribute("student", studentToEdit);

            // Redirect to the page for editing student attributes
            return "students/edit_attributes";
        } else {
            // If no students are found, you might want to handle that scenario
            // For simplicity, redirecting back to the search page
            return "redirect:/students/update";
        }
    }

    @GetMapping("/students/edit_attributes")
    public String showUpdateForm(@RequestParam int studentId, Model model) {
        // Retrieve the student from the database based on the ID
        Student existingStudent = studentRepo.findById(studentId).orElse(null);

        if (existingStudent != null) {
            // Add the student details to the model for use in the update page
            model.addAttribute("student", existingStudent);

            // Return the name of your HTML template file for the update form
            return "students/edit_attributes";
        } else {
            // If no student is found, you might want to handle that scenario
            // For simplicity, redirecting back to the search page
            return "redirect:/students/update";
        }
    }

    @PostMapping("/students/edit_attributes")
    public String updateUser(@ModelAttribute Student updatedStudent, HttpServletResponse response) {
        System.out.println("Updating student attributes");

        // Retrieve the existing student from the database based on the ID
        Student existingStudent = studentRepo.findById(updatedStudent.getId()).orElse(null);

        if (existingStudent != null) {
            // Update the attributes of the existing student based on the form parameters
            existingStudent.setName(updatedStudent.getName());
            existingStudent.setWeight(updatedStudent.getWeight());
            existingStudent.setHeight(updatedStudent.getHeight());
            existingStudent.setColour(updatedStudent.getColour());
            existingStudent.setGpa(updatedStudent.getGpa());

            // Save the updated student back to the database
            studentRepo.save(existingStudent);

            // Redirect to display the updated student
            return "redirect:/students/view";
        } else {
            // Handle the case where the student is not found
            // Redirect to home page or handle accordingly
            return "redirect:/home.html";
        }
    }
}