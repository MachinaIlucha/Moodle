package com.illiapinchuk.moodle.configuration;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class MongoConnectionTest {

  @DisplayName(
      "given object to save" + " when save object using MongoDB template" + " then object is saved")
  @Test
  void shouldSaveObjectSuccessfully(@Autowired MongoTemplate mongoTemplate) {
    DBObject objectToSave = BasicDBObjectBuilder.start().add("key", "value").get();

    mongoTemplate.save(objectToSave, "collection");

    assertThat(mongoTemplate.findAll(DBObject.class, "collection"))
        .extracting("key")
        .containsOnly("value");
  }
}
