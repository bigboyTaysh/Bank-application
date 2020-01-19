package pl.wolski.bank.repositories;


import pl.wolski.bank.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.wolski.bank.models.User;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    Role findRoleByType(Role.Types type);

    List<Role> findByUsers(User user);
}
