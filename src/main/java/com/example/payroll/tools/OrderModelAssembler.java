package com.example.payroll.tools;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.example.payroll.controllers.OrderController;
import com.example.payroll.models.Order;
import com.example.payroll.models.Status;

@Component
public class OrderModelAssembler implements RepresentationModelAssembler<Order, EntityModel<Order>> {

	@Override
	public EntityModel<Order> toModel(Order order) {
		// Unconditional links to single-item resource and aggregate root

		EntityModel<Order> orderModel = EntityModel.of(order,
				linkTo(methodOn(OrderController.class).one(order.getId())).withSelfRel(),
				linkTo(methodOn(OrderController.class).all()).withRel("orders"));

		// Conditional links based on state of the order

		if (order.getStatus() == Status.IN_PROGRESS) {
			orderModel.add(linkTo(methodOn(OrderController.class).cancel(order.getId())).withRel("cancel"));
			orderModel.add(linkTo(methodOn(OrderController.class).complete(order.getId())).withRel("complete"));
		}

		return orderModel;
	}

}
