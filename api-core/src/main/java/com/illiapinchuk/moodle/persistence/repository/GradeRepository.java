package com.illiapinchuk.moodle.persistence.repository;

import com.illiapinchuk.moodle.persistence.entity.Grade;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GradeRepository extends MongoRepository<Grade, String> {}
