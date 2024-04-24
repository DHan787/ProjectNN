package edu.neu.projnn.projectnn.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class FileUploadController {

    @PostMapping("/upload/style")
    public ResponseEntity<String> uploadStyleImage(@RequestParam("file") MultipartFile file) {
        return uploadFile(file, "src/main/resources/style/", "style.jpg");
    }

    @PostMapping("/upload/original")
    public ResponseEntity<String> uploadOriginalImage(@RequestParam("file") MultipartFile file) {
        return uploadFile(file, "src/main/resources/uploaded/", "original.jpg");
    }

    private ResponseEntity<String> uploadFile(MultipartFile file, String targetDir, String filename) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Cannot upload empty file.");
            }
            Path destinationPath = Paths.get(targetDir + filename);
            Files.createDirectories(destinationPath.getParent());
            file.transferTo(destinationPath);
            return ResponseEntity.ok("File uploaded successfully: " + filename);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to upload file: " + e.getMessage());
        }
    }
}
