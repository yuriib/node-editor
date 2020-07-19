package com.graph.controls.components;

import com.graph.controls.VisualNodeComponent;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

public class NumberInputComponent extends VisualNodeComponent<Double> {
	@FXML
	private TextField input;

	public NumberInputComponent(String name) {
		super(name);
	}

	@Override
	protected void preInitialize() {
		setValue(0.0);

		Bindings.bindBidirectional(this.input.textProperty(), valueProperty(), new StringConverter<Double>() {
			@Override
			public String toString(Double val) {
				return Double.toString(val);
			}

			@Override
			public Double fromString(String val) {
				return Double.parseDouble(val);
			}
		});
	}

	@Override
	protected void postInitialize() {
	}
}
