/*****************************************************************
JADE - Java Agent DEvelopment Framework is a framework to develop
multi-agent systems in compliance with the FIPA specifications.
Copyright (C) 2000 CSELT S.p.A. 

GNU Lesser General Public License

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation, 
version 2.1 of the License. 

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the
Free Software Foundation, Inc., 59 Temple Place - Suite 330,
Boston, MA  02111-1307, USA.
*****************************************************************/

package employment.ontology;

import jade.content.Predicate;

import java.io.*;

public class Person implements Predicate {
	private static final long serialVersionUID = -6413292845716090617L;
	private String _name; // Person's name
	private Long _age; // Person's age
	private Address _address; // Address' age

	// Methods required to use this class to represent the PERSON role
	public void setName(String name) {
		_name = name;
	}

	public String getName() {
		return _name;
	}

	public void setAge(Long age) {
		_age = age;
	}

	public Long getAge() {
		return _age;
	}

	public void setAddress(Address address) {
		_address = address;
	}

	public Address getAddress() {
		return _address;
	}

	// Other application specific methods
	public boolean equals(Person p) {
		if (!_name.equalsIgnoreCase(p.getName()))
			return false;
		if (_age != null && p.getAge() != null) // Age is an optional field
			if (_age.longValue() != p.getAge().longValue())
				return false;
		if (_address != null && p.getAddress() != null) // Address is an
														// optional field
			if (!_address.equals(p.getAddress()))
				return false;
		return true;
	}

	public void getFromStandardInputStream() {
		try {
			BufferedReader buff = new BufferedReader(new InputStreamReader(System.in));
			Address address = new Address();
			System.out.println("ENTER details of person to engage");
			System.out.print("  Person name --> ");
			setName(buff.readLine());
			System.out.print("  Person age ---> ");
			setAge(Long.valueOf(buff.readLine()));
			System.out.println("  Person address");
			System.out.print("    Street -----> ");
			address.setStreet(buff.readLine());
			System.out.print("    Number -----> ");
			address.setNumber(Long.valueOf(buff.readLine()));
			System.out.print("    City   -----> ");
			address.setCity(buff.readLine());
			setAddress(address);
		} catch (IOException ioe) {
			System.err.println("I/O error: " + ioe.getMessage());
		}
	}
}
