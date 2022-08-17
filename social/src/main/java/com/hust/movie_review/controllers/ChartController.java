package com.hust.movie_review.controllers;

import com.hust.movie_review.data.request.chart.ChartStatsRequest;
import com.hust.movie_review.data.request.chart.CreateChartSettingRequest;
import com.hust.movie_review.data.request.chart.UpdateChartSettingRequest;
import com.hust.movie_review.data.response.DfResponse;
import com.hust.movie_review.repositories.ChartCategoryRepository;
import com.hust.movie_review.repositories.ChartTypeRepository;
import com.hust.movie_review.service.template.IChartService;
import com.hust.movie_review.utils.AuthenticationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/chart")
public class ChartController {
    @Autowired
    private ChartCategoryRepository chartCategoryRepository;
    @Autowired
    private ChartTypeRepository chartTypeRepository;

    @Autowired
    private IChartService chartService;

    @GetMapping(value = "/chart-category")
    public ResponseEntity<?> getAllChartCategory(){
        return DfResponse.okEntity(chartCategoryRepository.findAll());
    }

    @GetMapping(value = "/chart-type")
    public ResponseEntity<?> getAllChartType(){
        return DfResponse.okEntity(chartTypeRepository.findAll());
    }

    @PostMapping(value = "/create")
    public ResponseEntity<?> create(@RequestBody @Valid CreateChartSettingRequest request, Authentication authentication){
        request.setUserId(AuthenticationUtils.loggedUserId(authentication));
        return DfResponse.okEntity(chartService.create(request));
    }

    @PostMapping(value = "/update")
    public ResponseEntity<?> update(@RequestBody @Valid UpdateChartSettingRequest request, Authentication authentication){
        request.setUserId(AuthenticationUtils.loggedUserId(authentication));
        return DfResponse.okEntity(chartService.update(request));
    }

    @DeleteMapping(value = "/delete/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteById(@PathVariable int id, Authentication authentication) {
        return DfResponse.okEntity(chartService.deleteById(id, AuthenticationUtils.loggedUserId(authentication)));
    }

    @PostMapping(value = "/stats")
    public ResponseEntity<?> stats(@RequestBody @Valid ChartStatsRequest request){
        return DfResponse.okEntity(chartService.stats(request));
    }
}
