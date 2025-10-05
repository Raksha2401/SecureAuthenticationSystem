package org.authentication.repository;

import org.authentication.entities.UserRole;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<UserRole, Long> {

    UserRole findByName(String name);

}
