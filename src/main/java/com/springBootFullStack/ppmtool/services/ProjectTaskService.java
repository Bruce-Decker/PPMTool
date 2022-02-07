package com.springBootFullStack.ppmtool.services;

import com.springBootFullStack.ppmtool.domain.Backlog;
import com.springBootFullStack.ppmtool.domain.Project;
import com.springBootFullStack.ppmtool.domain.ProjectTask;
import com.springBootFullStack.ppmtool.exceptions.ProjectNotFoundException;
import com.springBootFullStack.ppmtool.repositories.BacklogRepository;
import com.springBootFullStack.ppmtool.repositories.ProjectRepository;
import com.springBootFullStack.ppmtool.repositories.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectTaskService {

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask) {

        try {
            Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);
            projectTask.setBacklog(backlog);
            Integer BacklogSequence =  backlog.getPTSequence();
            BacklogSequence++;
            backlog.setPTSequence(BacklogSequence);

            projectTask.setProjectSequence(projectIdentifier + "-" + BacklogSequence);
            projectTask.setProjectIdentifier(projectIdentifier);

            if (projectTask.getStatus() == "" || projectTask.getStatus() == null) {
                projectTask.setStatus("TO_DO");
            }

            if (projectTask.getPriority() == null) {
                projectTask.setPriority(3);
            }

            return projectTaskRepository.save(projectTask);
        } catch (Exception e) {
            throw new ProjectNotFoundException("Project not Found");
        }

    }

    public Iterable<ProjectTask> findBacklogById(String id) {
        Project project = projectRepository.findByProjectIdentifier(id);
        if (project == null) {
            throw new ProjectNotFoundException(("Project with ID: " + id + " does not exist"));
        }
        return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
    }

    public ProjectTask findPTByProjectSequence(String backlog_id, String pt_id) {
        return projectTaskRepository.findByProjectSequence(pt_id);
    }

}
