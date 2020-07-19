package com.graph.controls.components;

import com.graph.controls.VisualNodeComponent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class LabelComponent extends VisualNodeComponent<String> {
	@FXML
	private Label label;

	public LabelComponent(String name) {
		super(name);
	}

	@Override
	protected void preInitialize() {
		valueProperty().addListener((p, o, n) -> this.label.setText(n));
	}

	@Override
	protected void postInitialize() {
	}
}
