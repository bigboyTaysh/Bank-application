package pl.wolski.bank.repositories;


import pl.wolski.bank.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    Role findRoleByType(Role.Types type);
}
