package com.example.demo4.model;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudentRepository extends JpaRepository<Student,Integer> {
    List<Student> findByWeight(int weight);

    List<Student> findByHeight(int height);

    List<Student> findByGpa(float gpa);

    List<Student> findByNameAndWeightAndHeightAndColourAndGpa(String name,int weight,int height, String colour,float gpa);


/*
    @Transactional
    @Modifying
    @Query("UPDATE Student s SET s.name =:newname, s.weight = :newWeight, s.height = :newHeight, s.colour = :newColour, s.gpa = :newGpa WHERE s.name = :currentname AND s.weight = :currentWeight AND s.height = :currentHeight AND s.colour = :currentColour AND s.gpa = :currentGpa")
    void updateByNameAndWeightAndHeightAndColourAndGpa(
            @Param("name") String currentname,
            @Param("oldWeight") int currentWeight,
            @Param("oldHeight") int currentHeight,
            @Param("oldColour") String currentColour,
            @Param("oldGpa") int currentGpa,
            @Param("newname") int newname,
            @Param("newWeight") int newWeight,
            @Param("newHeight") int newHeight,
            @Param("newColour") String newColour,
            @Param("newGpa") int newGpa);
};
*/
}