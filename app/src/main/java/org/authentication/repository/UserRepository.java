package org.authentication.repository;

import org.authentication.entities.UserInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserInfo, String> {

    public UserInfo findByUsername(String username);

}
