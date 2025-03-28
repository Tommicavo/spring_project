package com.project.ecom.models.entities;

import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "user")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUser;

    @Column(name = "username", unique = true, nullable = false)
    private String usernameUser;

    @Column(name = "email", unique = true, nullable = false)
    private String emailUser;

    @Column(name = "password", nullable = false)
    private String passwordUser;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_role",
        joinColumns = @JoinColumn(name = "id_user"),
        inverseJoinColumns = @JoinColumn(name = "id_role")
    )
    @JsonIgnore
    private Set<RoleEntity> roles;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private Set<ExpenseEntity> expenses;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private Set<ReportEntity> reports;

    public UserEntity() {}

	public Long getIdUser() {
		return idUser;
	}

	public void setIdUser(Long idUser) {
		this.idUser = idUser;
	}

	public String getUsernameUser() {
		return usernameUser;
	}

	public void setUsernameUser(String usernameUser) {
		this.usernameUser = usernameUser;
	}

	public String getEmailUser() {
		return emailUser;
	}

	public void setEmailUser(String emailUser) {
		this.emailUser = emailUser;
	}

	public String getPasswordUser() {
		return passwordUser;
	}

	public void setPasswordUser(String passwordUser) {
		this.passwordUser = passwordUser;
	}

	public Set<RoleEntity> getRoles() {
		return roles;
	}

	public void setRoles(Set<RoleEntity> roles) {
		this.roles = roles;
	}

	public Set<ExpenseEntity> getExpenses() {
		return expenses;
	}

	public void setExpenses(Set<ExpenseEntity> expenses) {
		this.expenses = expenses;
	}

	public Set<ReportEntity> getReports() {
		return reports;
	}

	public void setReports(Set<ReportEntity> reports) {
		this.reports = reports;
	}

	@Override
	public String toString() {
		return "UserEntity [idUser=" + idUser + ", usernameUser=" + usernameUser + ", emailUser=" + emailUser
				+ ", passwordUser=" + passwordUser + ", roles=" + roles + "]";
	}
}
