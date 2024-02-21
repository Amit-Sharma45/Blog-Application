package com.bikkadit.blog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bikkadit.blog.entities.Role;

public interface RoleRepo extends JpaRepository<Role, Integer> {

}
