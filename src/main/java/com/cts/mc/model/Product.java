package com.cts.mc.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author Bharat Kumar
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

	private String name;
	private String price;
	private String offer;
	private boolean inStock;
	private String soldBy;
	private String emailId;
}
