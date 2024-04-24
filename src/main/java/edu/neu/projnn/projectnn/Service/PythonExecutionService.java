package edu.neu.projnn.projectnn.Service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class PythonExecutionService {
    private final AtomicInteger currentExecutions = new AtomicInteger(0);

    public int getCurrentExecutions() {
        return currentExecutions.get();
    }

    public synchronized String executeScript(String pythonPath, String pythonScriptPath, String parameter) throws InterruptedException, IOException {
        currentExecutions.incrementAndGet();
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(pythonPath, pythonScriptPath, parameter);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            process.waitFor();
            return process.exitValue() == 0 ? "Success" : "Error";
        } finally {
            currentExecutions.decrementAndGet();
        }
    }
}
