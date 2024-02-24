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
                                        @RequestParam int gpa,
                                        @RequestParam String colour,
                                        Model model) {
        // Perform a search for the student based on the provided criteria
        List<Student> students = studentRepo.findByNameAndWeightAndHeightAndColourAndGpa(name, weight, height, colour,gpa);

        // Check if any students are found
        if (!students.isEmpty()) {
            // If multiple students are found, you might want to handle that scenario
            // For simplicity, assuming the first student found is the one to edit
            Student studentToEdit = students.get(0);

            // Add the student details to the model for use in the edit page
            model.addAttribute("student", studentToEdit);

            // Redirect to the page for editing student attributes
            return "students/asn2";
        } else {
            // If no students are found, you might want to handle that scenario
            // For simplicity, redirecting back to the search page
            return "redirect:/students/update.html";
        }
    }

    @GetMapping("/students/asn2")
    public String showUpdateForm(@RequestParam int studentId, Model model) {
        // Retrieve the student from the database based on the ID
        Student existingStudent = studentRepo.findById(studentId).orElse(null);

        if (existingStudent != null) {
            // Add the student details to the model for use in the update page
            model.addAttribute("student", existingStudent);

            // Return the name of your HTML template file for the update form
            return "students/asn2";
        } else {
            // If no student is found, you might want to handle that scenario
            // For simplicity, redirecting back to the search page
            return "redirect:/students/update.html";
        }
    }

    @PostMapping("/students/asn2")
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
    /*
    @PostMapping("/students/asn2")
    public String editStudentAttributes(@ModelAttribute Student studentToEdit) {
        // Retrieve the student from the database based on the ID or another unique identifier
        Student existingStudent = studentRepo.findById(studentToEdit.getId()).orElse(null);

        if (existingStudent != null) {
            // Update the attributes of the existing student based on the form parameters
            if (studentToEdit.getName() != null && !studentToEdit.getName().isEmpty()) {
                existingStudent.setName(studentToEdit.getName());
            }
            if (studentToEdit.getWeight() != 0) {
                existingStudent.setWeight(studentToEdit.getWeight());
            }
            if (studentToEdit.getHeight() != 0) {
                existingStudent.setHeight(studentToEdit.getHeight());
            }
            if (studentToEdit.getColour() != null && !studentToEdit.getColour().isEmpty()) {
                existingStudent.setColour(studentToEdit.getColour());
            }
            if (studentToEdit.getGpa() != 0) {
                existingStudent.setGpa(studentToEdit.getGpa());
            }

            // Save the updated student back to the database
            studentRepo.save(existingStudent);

            // Redirect to display the updated student
            return "redirect:/students/view";
        } else {
            // Handle the case where the student is not found
            return "redirect:/home.html"; // Redirect to home page or handle accordingly
        }
    }

/*
    @PostMapping("/students/asn2")
    public String editStudentAttributes(@ModelAttribute Student changedStudent) {
        // Retrieve the student from the database based on the ID or another unique identifier
        Student existingStudent = studentRepo.findById(changedStudent.getId()).orElse(null);

        if (existingStudent != null) {
            // Update the attributes of the existing student based on the form parameters
            //existingStudent.setName(student.getName());
            //existingStudent.setWeight(student.getWeight());
            //existingStudent.setHeight(student.getHeight());
            //existingStudent.setColour(student.getColour());
            //existingStudent.setGpa(student.getGpa());

            System.out.println("update student attributes");
            // Update the attributes of the existing student based on the form parameters
            if (changedStudent.getName() != null && !changedStudent.getName().isEmpty()) {
                existingStudent.setName(changedStudent.getName());
            }
            if (changedStudent.getWeight() != 0) {
                existingStudent.setWeight(changedStudent.getWeight());
            }
            if (changedStudent.getHeight() != 0) {
                existingStudent.setHeight(changedStudent.getHeight());
            }

            if (changedStudent.getColour() != null && !changedStudent.getColour().isEmpty()) {
                existingStudent.setColour(changedStudent.getColour());
            }
            if (changedStudent.getGpa() != 0) {
                existingStudent.setGpa(changedStudent.getGpa());
            }


            // Save the updated student back to the database
           // studentRepo.save(student);

            // Save the updated student back to the database
            studentRepo.save(existingStudent);

            return "redirect:/students/view"; // Redirect to display students or another page
        } else {
            // Handle the case where the student is not found
            return "redirect:/home.html"; // Redirect to home page or handle accordingly
        }
    }
*/


}

/*
        @PostMapping("/students/oldchange")
        public String changeUser(@RequestParam Map<String, String> currentstudent, HttpServletResponse response, HttpSession session) {
            //@RequestMapping("/students/change")
            //public String changeUser(@RequestParam Map<String, String> currentstudent, HttpServletResponse response, HttpSession session) {
            System.out.println("change student");
            String currentname = currentstudent.get("name");
            int currentWeight = Integer.parseInt(currentstudent.get("weight"));
            int currentHeight = Integer.parseInt(currentstudent.get("height"));
            String currentColour = currentstudent.get("colour");
            int currentGpa = Integer.parseInt(currentstudent.get("gpa"));
            int currentId = Integer.parseInt(currentstudent.get("id"));

            // Store the values in the session attribute
            //session.setAttribute("currentname", currentname);
            //s/=ession.setAttribute("currentWeight", currentWeight);
            //session.setAttribute("currentHeight", currentHeight);
            //session.setAttribute("currentColour", currentColour);
            //session.setAttribute("currentGpa", currentGpa);
            //check if the student exists
            //List<Student> studentList = studentRepo.findById(currentId);
            List<Student> studentList = studentRepo.findByNameAndWeightAndHeightAndColourAndGpa(currentname, currentWeight, currentHeight, currentColour, currentGpa);
           // if (!studentList.isEmpty()) {
                //Student student = studentList.get(0);
                //session.setAttribute("currentId",currentId);
                //session.setAttribute("currentname", currentname);
                //session.setAttribute("currentWeight", currentWeight);
                //session.setAttribute("currentHeight", currentHeight);
                //session.setAttribute("currentColour", currentColour);
                //session.setAttribute("currentGpa", currentGpa);
                //return "students/changedStudent";
           // }
            if (!studentList.isEmpty()) {
                Student student = studentList.get(0);
                session.setAttribute("foundStudentId", student.getId());
                return "redirect:/students/changedStudent";
            }
            else {
                // if the student is not found we redirect back to here
                //return "redirect:/asn2.html";
                return "redirect:oldchange.html";
            }
        }
        @GetMapping("/students/changedStudent")
        public String displayChangedStudent(Model model, HttpSession session) {
            // Retrieve the stored student ID from the session
            int foundStudentId = (int) session.getAttribute("foundStudentId");


            // Check if the student ID is present in the session


            // Retrieve the student details from the database using the ID
            Student foundStudent = studentRepo.findById(foundStudentId).orElse(null);
            if(foundStudent !=null){
                // Add the foundStudent to the model for use in changedStudent.html
                model.addAttribute("student", foundStudent);


                // Clear the session attribute after retrieving the data
                session.removeAttribute("foundStudentId");


                // Return the changedStudent.html template
                return "students/changedStudent";
            } else {
                // If the student ID is not present, redirect to some error page or handle accordingly
                return "redirect:/oldchange.html";
            }
        }


        @PostMapping("/students/changedStudent")
        //@PostMapping("/changedStudent.html")
        //@PostMapping("/students/change/{uid}")
        public String updateUser(@RequestParam Map<String, String> changedstudent, Model model, HttpServletResponse response, HttpSession session) {
            System.out.println("updating student");
            // Retrieve values from the session attribute
            String currentname = changedstudent.get("name");
            int currentWeight = Integer.parseInt(changedstudent.get("weight"));
            int currentHeight = Integer.parseInt(changedstudent.get("height"));
            String currentColour = changedstudent.get("colour");
            int currentGpa = Integer.parseInt(changedstudent.get("gpa"));


            // Find the student by the given parameters
            List<Student> studentList = studentRepo.findByNameAndWeightAndHeightAndColourAndGpa(
                    currentname, currentWeight, currentHeight, currentColour, currentGpa);


            // Check if the student exists
            if (!studentList.isEmpty()) {
                // Get the first student from the list
                Student student = studentList.get(0);
                System.out.println("update student attributes");
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
                return "redirect:/home.html";
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















