package com.example.mbaningapijpapractice.features.file;


import com.example.mbaningapijpapractice.domain.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<File, Integer> {

    Optional<File> findByname(String filename);


}
