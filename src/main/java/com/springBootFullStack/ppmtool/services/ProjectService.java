package com.springBootFullStack.ppmtool.services;

import com.springBootFullStack.ppmtool.domain.Backlog;
import com.springBootFullStack.ppmtool.domain.Project;
import com.springBootFullStack.ppmtool.exceptions.ProjectIdException;
import com.springBootFullStack.ppmtool.repositories.BacklogRepository;
import com.springBootFullStack.ppmtool.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private BacklogRepository backlogRepository;

    public Project saveOrUpdateProject(Project project) {
        try {
            project.setProjectIdentifier((project.getProjectIdentifier().toUpperCase()));
            if (project.getId() == null) {
                Backlog backlog = new Backlog();
                project.setBacklog(backlog);
                backlog.setProject(project);
                backlog.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            }
            if (project.getId() != null) {
                project.setBacklog(backlogRepository.findByProjectIdentifier(project.getProjectIdentifier().toUpperCase()));

            }
            return projectRepository.save(project);
        } catch (Exception e) {
            throw new ProjectIdException("Project ID " + project.getProjectIdentifier().toUpperCase() + " already exists");
        }
    }

    public Project findProjectByIdentifier(String projectId) {
        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());
        if (project == null) {
            throw new ProjectIdException("Project ID doesn't exist");
        }
        return project;
    }

    public Iterable<Project> findAllProjects() {
        return projectRepository.findAll();
    }

    public void deleteProjectByIdentifier(String projectid) {
        Project project = projectRepository.findByProjectIdentifier(projectid);
        if (project == null) {
            throw new ProjectIdException("Cannot Project with Id " + projectid + ". This project does not exist.");
        }
        projectRepository.delete(project);
    }

}
