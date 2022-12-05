package com.appstracta.customer.customer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document(collection = "customers")
public class Customer implements Serializable {
	private static final long serialVersionUID = -638807739368234537L;
	@Id
	private String id;
	private String firstName;
	private String lastName;

	private String telephone;
	private String type;

	public Customer(String firstName, String lastName, String telephone, String type) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.telephone = telephone;
		this.type = type;
	}

}
