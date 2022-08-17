package com.hust.movie_review.controllers;

import com.hust.movie_review.data.request.dashboard.CreateDashboardRequest;
import com.hust.movie_review.data.request.dashboard.UpdateDashboardRequest;
import com.hust.movie_review.data.response.DfResponse;
import com.hust.movie_review.service.template.IDashboardService;
import com.hust.movie_review.utils.AuthenticationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/dashboard")
public class DashboardController {

    @Autowired
    private IDashboardService dashboardService;

    @PostMapping(value = "/create")
    public ResponseEntity<?> create(@RequestBody @Valid CreateDashboardRequest request, Authentication authentication) {
        request.setUserId(AuthenticationUtils.loggedUserId(authentication));
        return DfResponse.okEntity(dashboardService.create(request));
    }

    @PutMapping(value = "/update")
    public ResponseEntity<?> update(@RequestBody @Valid UpdateDashboardRequest request, Authentication authentication) {
        request.setUserId(AuthenticationUtils.loggedUserId(authentication));
        return DfResponse.okEntity(dashboardService.update(request));
    }

    @GetMapping(value = "/detail/{id}")
    public ResponseEntity<?> findById(@PathVariable int id, Authentication authentication) {
        int userId = AuthenticationUtils.loggedUserId(authentication);
        return DfResponse.okEntity(dashboardService.findById(id, userId));
    }

    @GetMapping(value = "/all")
    @ResponseBody
    public ResponseEntity<?> findByAll(Authentication authentication) {
        return DfResponse.okEntity(dashboardService.getAll(AuthenticationUtils.loggedUserId(authentication)));
    }

    @DeleteMapping(value = "/delete/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteById(@PathVariable int id, Authentication authentication) {
        return DfResponse.okEntity(dashboardService.deleteById(id, AuthenticationUtils.loggedUserId(authentication)));
    }

}
