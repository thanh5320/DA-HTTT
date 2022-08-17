package com.hust.movie_review.service;

import com.hust.movie_review.common.Constant;
import com.hust.movie_review.common.OffsetBasedPageable;
import com.hust.movie_review.config.exception.ApiException;
import lombok.SneakyThrows;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class BaseService<T> {
    private final JpaRepository<T, Integer> repository;

    public BaseService(JpaRepository<T, Integer> repository) {
        this.repository = repository;
    }

    public List<T> listing(int page, int pageSize, String sortBy, Boolean desc){
        Sort sort = Sort.by(sortBy);
        if(!desc) sort = sort.ascending(); else sort = sort.descending();
        Pageable pageable = new OffsetBasedPageable(page, pageSize, sort);

        return this.listing(pageable);
    }

    public List<T> listing(Pageable pageable){
        return repository.findAll(pageable).get().collect(Collectors.toList());
    }

    public List<T> listing() {
        int page = Constant.PAGE_DEFAULT;
        int pageSize = Constant.PAGE_SIZE_DEFAULT;
        String sortBy = Constant.SORT_BY_DEFAULT;

        return this.listing(page, pageSize, sortBy, true);
    }

    @SneakyThrows
    public boolean delete(int id) {
        Optional<T> optional = repository.findById(id);
        if(optional.isEmpty()){
            throw new ApiException("Không tìm thấy đối tượng có id tương ứng");
        }

        try {
            repository.delete(optional.get());
        } catch (Exception exception){
            return false;
        }

        return true;
    }

    public T detail(int id) {
        Optional<T> optional = repository.findById(id);
        return optional.orElse(null);
    }
}
