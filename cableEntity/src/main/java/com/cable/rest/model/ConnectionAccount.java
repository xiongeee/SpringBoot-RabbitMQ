package com.cable.rest.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.cable.rest.constants.Status;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="Connection_account")
public class ConnectionAccount extends Audit implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable=false)
	@Getter	@Setter	
	private Long accountId;
	
	@Column(nullable=false)
	@Getter	@Setter	
	private String accountToken;
	
	@Column(nullable=false)
	@Getter	@Setter	
	private String name;
	
	@Column(unique=true,nullable=false)
	@Getter	@Setter	
	private String mobile;
	
	@Column(unique=true,nullable=false)
	@Getter	@Setter	
	private String emailId;
	
	@Column(nullable=false)
	@Getter	@Setter	
	private String address;
	
	@Column(nullable=false)
	@Getter	@Setter	
	private String notes;
	
	@ManyToOne
	@JoinColumn(name="street_Id", nullable=false)
	@Getter	@Setter	
	private Street street;
	
	@ManyToOne
	@JoinColumn(name="project_Id", nullable=false)
	@Getter	@Setter	
	private Project project;
	
	@Column(nullable=false)
	@Getter	@Setter	
	private double rentAmount;
	
	@Column(nullable=false)
	@Getter	@Setter	
	private double advancePaid;
	
	@Column(nullable=false)
	@Enumerated(EnumType.STRING)
	@Getter	@Setter	
	private Status status;
	
	
	
	

}
