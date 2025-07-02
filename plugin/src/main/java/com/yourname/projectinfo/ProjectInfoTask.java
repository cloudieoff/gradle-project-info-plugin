package com.yourname.projectinfo;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

public class ProjectInfoTask extends DefaultTask {
    @TaskAction
    public void analyzeProject() {
        ProjectAnalyzer analyzer = new ProjectAnalyzer(getProject());
        analyzer.analyze();
    }
}