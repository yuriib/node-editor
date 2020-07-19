package com.graph.controls;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class VisualNodeBody extends GridPane {
	public VisualNodeBody() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/controls/common/parts/visual-node-body.fxml"));
		loader.setController(this);
		loader.setRoot(this);
		loader.load();
	}

	@FXML
	private void initialize() {
	}
}
