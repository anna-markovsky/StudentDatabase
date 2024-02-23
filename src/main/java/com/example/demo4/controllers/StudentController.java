package com.example.demo4.controllers;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo4.model.Student;
import com.example.demo4.model.StudentRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.demo4.model.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class StudentController {
    @Autowired
    private StudentRepository studentRepo;

    @GetMapping("/students/view")
    public String getAllStudents(Model model) {
        System.out.println("getting all users");
        //List<User> users= new ArrayList<>();
        List<Student> students = studentRepo.findAll();
        model.addAttribute("st", students);
        return "students/showAll";
    }
/*
    @PostMapping("/students/display")
    public String showUsers(Model model) {
        System.out.println("getting all users");
        List<Student> students = studentRepo.findAll();
        model.addAttribute("students", students); // Make sure to use "students" instead of "st"
        return "students/showAll";
    }
*/


    @PostMapping("/students/add")
    public String addUser(@RequestParam Map<String, String> newstudent, HttpServletResponse response) {
        System.out.println("Add student");
        String newName = newstudent.get("name");
        int newWeight = Integer.parseInt(newstudent.get("weight"));
        int newHeight = Integer.parseInt(newstudent.get("height"));
        String newColour = newstudent.get("colour");
        int newGpa = Integer.parseInt(newstudent.get("gpa"));
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
        int oldGpa = Integer.parseInt(oldstudent.get("gpa"));
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
            model.addAttribute("message", "student not found. Please try again");
            return "redirect:/delete.html";
        }
    }


    @PostMapping("/students/change")
    public String changeUser(@RequestParam Map<String, String> currentstudent, HttpServletResponse response, HttpSession session) {
    //@RequestMapping("/students/change")
    //public String changeUser(@RequestParam Map<String, String> currentstudent, HttpServletResponse response, HttpSession session) {
        System.out.println("change student");
        String currentname = currentstudent.get("name");
        int currentWeight = Integer.parseInt(currentstudent.get("weight"));
        int currentHeight = Integer.parseInt(currentstudent.get("height"));
        String currentColour = currentstudent.get("colour");
        int currentGpa = Integer.parseInt(currentstudent.get("gpa"));

        // Store the values in the session attribute
        session.setAttribute("currentname", currentname);
        session.setAttribute("currentWeight", currentWeight);
        session.setAttribute("currentHeight", currentHeight);
        session.setAttribute("currentColour", currentColour);
        session.setAttribute("currentGpa", currentGpa);

        // Redirect to asn2.html
        // return "redirect:/asn2.html";
        return "students/change";
    }
/*
    @PostMapping("/students/change/{uid}")
    public String updateStudent(@PathVariable("uid") int uid, Model model){
        Students student=studentRepo.findById(uid).orElse(null);
        if(student !=NULL){
            student.setName
        }

    }
    */


    @PostMapping("/students/asn2")
    //@PostMapping("/changeStudent.html")
    //@PostMapping("/students/change/{uid}")
    public String updateUser(@PathVariable("uid") int uid, @RequestParam Map<String, String> changedstudent, HttpServletResponse response, HttpSession session) {
        System.out.println("updating student");

        // Retrieve values from the session attribute
        String currentname = (String) session.getAttribute("currentname");
        int currentWeight = (int) session.getAttribute("currentWeight");
        int currentHeight = (int) session.getAttribute("currentHeight");
        String currentColour = (String) session.getAttribute("currentColour");
        int currentGpa = (int) session.getAttribute("currentGpa");

        // Find the student by the given parameters
        List<Student> studentList = studentRepo.findByNameAndWeightAndHeightAndColourAndGpa(
                currentname, currentWeight, currentHeight, currentColour, currentGpa);

        // Check if the student exists
        if (!studentList.isEmpty()) {
            // Get the first student from the list
            Student student = studentList.get(0);
            System.out.println("update student");
            // Update the attributes of the existing student based on the form parameters
            if (!changedstudent.get("name").isEmpty()) {
                student.setName(changedstudent.get("name"));
            }
            if (!changedstudent.get("weight").isEmpty()) {
                student.setWeight(Integer.parseInt(changedstudent.get("weight")));
            }
            if (!changedstudent.get("height").isEmpty()) {
                student.setHeight(Integer.parseInt(changedstudent.get("height")));
            }
            if (!changedstudent.get("colour").isEmpty()) {
                student.setColour(changedstudent.get("colour"));
            }
            if (!changedstudent.get("gpa").isEmpty()) {
                student.setGpa(Integer.parseInt(changedstudent.get("gpa")));
            }

            // Save the updated student back to the database
            studentRepo.save(student);

            // Redirect to the desired page after updating the student
            //return "redirect:/home.html";
            return "students/addedStudent";
        } else {
            // Redirect to the page indicating the student was not found
            return "redirect:students/changedStudent";
        }
    }
}
/*
    @PostMapping("/students/display")
    public String showUser(@RequestParam Map<String, String> currentstudent, Model model,HttpServletResponse response, HttpSession session) {
        System.out.println("getting all users");
        //List<User> users= new ArrayList<>();
        List<Student> students = studentRepo.findAll();
        //end of database call
        model.addAttribute("st", students);
        return "students/showAll";


    }





    }

*/












