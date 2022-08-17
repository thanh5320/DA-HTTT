package com.hust.movie_review.controllers;

import com.hust.movie_review.config.exception.ApiException;
import com.hust.movie_review.data.request.role.StoreRequest;
import com.hust.movie_review.data.request.role.UpdateRequest;
import com.hust.movie_review.data.response.DfResponse;
import com.hust.movie_review.models.Role;
import com.hust.movie_review.service.template.IRoleService;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/role")
public class RoleController {
    IRoleService roleService;

    public RoleController(IRoleService roleService){
        this.roleService = roleService;
    }

    @GetMapping("listing")
    public ResponseEntity<DfResponse<List<Role>>> listing(){
        return DfResponse.okEntity(roleService.listing());
    }

    @GetMapping("detail/{id}")
    public ResponseEntity<DfResponse<Role>> detail(@PathVariable int id){
        return DfResponse.okEntity(roleService.detail(id));
    }

    @PostMapping("store")
    public ResponseEntity<DfResponse<Role>> store(@RequestBody @Valid StoreRequest request){
        Role newRole = roleService.insert(request);
        return DfResponse.okEntity(newRole);
    }

    @PostMapping("update")
    public ResponseEntity<DfResponse<Role>> update(@RequestBody @Valid UpdateRequest request){
        Role updateRole = roleService.update(request);
        return DfResponse.okEntity(updateRole);
    }

    @SneakyThrows
    @PostMapping("delete/{id}")
    public ResponseEntity<DfResponse<String>> delete(@PathVariable int id){
        boolean result = roleService.delete(id);
        if(!result){
            throw new ApiException("Có lỗi xảy ra");
        }

        return DfResponse.okEntity("Xóa thành công");
    }
}
