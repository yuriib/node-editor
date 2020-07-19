package com.graph.controls.components;

import com.graph.controls.VisualNodeComponent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class NumberDisplayComponent extends VisualNodeComponent<Double> {
	@FXML
	private Label display;

	public NumberDisplayComponent(String name) {
		super(name);
	}

	@Override
	protected void preInitialize() {
		valueProperty().addListener((p, o, n) -> this.display.setText(n.toString()));

		setValue(0.0);
	}

	@Override
	protected void postInitialize() {

	}
}
