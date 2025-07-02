package com.yourname.projectinfo;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import org.gradle.api.Project;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ProjectAnalyzer {
    private final Project project;
    private int javaFilesCount;
    private int resourceFilesCount;
    private int totalLinesOfCode;
    private int totalComments;

    public ProjectAnalyzer(Project project) {
        this.project = project;
    }

    public void analyze() {
        countFiles();
        analyzeJavaFiles();
        printResults();
    }

    private void countFiles() {
        javaFilesCount = countFilesByExtension("src/main/java", ".java");
        resourceFilesCount = countFilesByExtension("src/main/resources", "");
    }

    private int countFilesByExtension(String path, String extension) {
        Path dir = project.file(path).toPath();
        if (!Files.exists(dir)) return 0;

        try {
            return (int) Files.walk(dir)
                    .filter(p -> p.toString().endsWith(extension))
                    .count();
        } catch (IOException e) {
            throw new RuntimeException("Failed to count files in " + path, e);
        }
    }

    private void analyzeJavaFiles() {
        Path javaDir = project.file("src/main/java").toPath();
        if (!Files.exists(javaDir)) return;

        try {
            Files.walk(javaDir)
                    .filter(p -> p.toString().endsWith(".java"))
                    .forEach(this::parseJavaFile);
        } catch (IOException e) {
            throw new RuntimeException("Failed to analyze Java files", e);
        }
    }

    private void parseJavaFile(Path javaFile) {
        try {
            // Создаём экземпляр JavaParser
            JavaParser javaParser = new JavaParser();

            // Парсим файл
            ParseResult<CompilationUnit> parseResult = javaParser.parse(javaFile);

            // Проверяем на ошибки
            if (parseResult.isSuccessful() && parseResult.getResult().isPresent()) {
                CompilationUnit cu = parseResult.getResult().get();

                // Подсчёт строк кода
                int lines = cu.toString().split("\n").length;
                totalLinesOfCode += lines;

                // Подсчёт комментариев
                totalComments += cu.getAllContainedComments().size();
            } else {
                System.err.println("Failed to parse: " + javaFile);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + javaFile);
        }
    }

    private void printResults() {
        System.out.println("Project Analysis Results:");
        System.out.println("Java files: " + javaFilesCount);
        System.out.println("Resource files: " + resourceFilesCount);
        System.out.println("Total lines of code: " + totalLinesOfCode);
        System.out.println("Total comments: " + totalComments);
    }
}