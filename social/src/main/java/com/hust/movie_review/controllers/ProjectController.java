package com.hust.movie_review.controllers;

import com.hust.movie_review.data.request.project.CreateProjectRequest;
import com.hust.movie_review.data.request.project.UpdateProjectRequest;
import com.hust.movie_review.data.response.DfResponse;
import com.hust.movie_review.models.Project;
import com.hust.movie_review.repositories.ProjectRepository;
import com.hust.movie_review.service.template.IProjectService;
import com.hust.movie_review.utils.AuthenticationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/api/project")
@CrossOrigin
public class ProjectController {
    @Autowired
    private IProjectService projectService;

    @PostMapping(value = "/create")
    public ResponseEntity<?> create(@RequestBody @Valid CreateProjectRequest request, Authentication authentication){
        request.setUserId(AuthenticationUtils.loggedUserId(authentication));
        return DfResponse.okEntity(projectService.create(request));
    }

    @PutMapping(value = "/update")
    public ResponseEntity<?> update(@RequestBody @Valid UpdateProjectRequest request, Authentication authentication){
        request.setUserId(AuthenticationUtils.loggedUserId(authentication));
        return DfResponse.okEntity(projectService.update(request));
    }

    @GetMapping(value = "/detail/{id}")
    public ResponseEntity<?> findById(@PathVariable int id, Authentication authentication){
        int userId = AuthenticationUtils.loggedUserId(authentication);
        return DfResponse.okEntity(projectService.findById(id,userId));
    }

    @GetMapping(value = "/all")
    @ResponseBody
    public ResponseEntity<?> findByAll(Authentication authentication){
        return DfResponse.okEntity(projectService.getAll(AuthenticationUtils.loggedUserId(authentication)));
    }

    @DeleteMapping(value = "/delete/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteById(@PathVariable int id,Authentication authentication){
        return DfResponse.okEntity(projectService.deleteById(id, AuthenticationUtils.loggedUserId(authentication)));
    }

}
