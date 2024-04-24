package edu.neu.projnn.projectnn.Controller;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;

@RestController
public class ImageController {

    static class ImageRequest {
        private String parameter;

        public String getParameter() {
            return parameter;
        }

        public void setParameter(String parameter) {
            this.parameter = parameter;
        }
    }

    @PostMapping("/generate-image")
    public ResponseEntity<?> generateImage(@RequestBody ImageRequest request) {
        Process process = null;
        BufferedReader reader = null;
        String pythonPath = "D:\\ProjectNN\\venv\\Scripts\\python.exe";
        String pythonScriptPath = "src/main/java/edu/neu/projnn/projectnn/python/main.py";
        String parameter = request.getParameter();  // Use the passed parameter

        try {
            System.out.println("Generating image with parameter: " + parameter);
            ProcessBuilder processBuilder = new ProcessBuilder(pythonPath, pythonScriptPath, parameter);
            processBuilder.redirectErrorStream(true); // Redirect errors to standard output

            process = processBuilder.start();
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.err.println("Script execution failed with errors: " + output.toString());
                return ResponseEntity.internalServerError().body("Error executing script: " + output.toString());
            }

            // Log the output for debugging
            System.out.println("Python script output: " + output.toString());

            System.out.println("Image generated successfully!");
            File file = new File("src/main/resources/static/generated/generated.jpg");
            if (!file.exists()) {
                throw new FileNotFoundException("Generated image not found.");
            }
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            return ResponseEntity.ok().body(resource);

        } catch (IOException | InterruptedException e) {
            System.err.println("Server error: " + e.getMessage());
            return ResponseEntity.internalServerError().body("Server error: " + e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    System.err.println("Failed to close reader: " + e.getMessage());
                }
            }
            if (process != null) {
                process.destroy();
            }
        }
    }
}
