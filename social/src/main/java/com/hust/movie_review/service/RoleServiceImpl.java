package com.hust.movie_review.service;

import com.hust.movie_review.config.exception.ApiException;
import com.hust.movie_review.data.request.role.StoreRequest;
import com.hust.movie_review.data.request.role.UpdateRequest;
import com.hust.movie_review.models.Role;
import com.hust.movie_review.repositories.RoleRepository;
import com.hust.movie_review.service.template.IRoleService;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl extends BaseService<Role> implements IRoleService {
    RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository){
        super(roleRepository);
        this.roleRepository = roleRepository;
    }

    @Override
    public Role insert(StoreRequest request) {
        Role role = new Role();
//        role.setDisplayName(request.getDisplayName());
        role.setName(request.getName());

        return roleRepository.save(role);
    }

    @SneakyThrows
    @Override
    public Role update(UpdateRequest request) {
        Optional<Role> optional = roleRepository.findById(request.getId());

        if(optional.isEmpty()){
            throw new ApiException("Không tìm thấy vai trò có id tương ứng");
        }

        Role role = optional.get();
//        role.setDisplayName(request.getDisplayName());

        return roleRepository.save(role);
    }
}
