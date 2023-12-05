package com.illiapinchuk.moodle.service.impl.crud;

import com.illiapinchuk.moodle.persistence.entity.Grade;
import com.illiapinchuk.moodle.persistence.repository.GradeRepository;
import com.illiapinchuk.moodle.service.crud.GradeCRUDService;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/** Implementation of {@link GradeCRUDService} interface. */
@Service
@RequiredArgsConstructor
@Slf4j
public class GradeCRUDServiceImpl implements GradeCRUDService {

    private final GradeRepository gradeRepository;

    @Override
    public Grade saveGrade(@Nonnull final Grade grade) {
        return gradeRepository.save(grade);
    }
}
