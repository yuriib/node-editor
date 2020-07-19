package com.graph.controls;

import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class VisualNodeHeader extends HBox {
	@FXML
	private Label title;

	public VisualNodeHeader() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/controls/common/parts/visual-node-header.fxml"));
		loader.setController(this);
		loader.setRoot(this);
		loader.load();
	}

	@FXML
	private void initialize() {
		this.title.setOnMouseDragged(event -> {
			event.consume();
			fireEvent(event);
		});
	}

	public StringProperty titleProperty() {
		return this.title.textProperty();
	}

	public String getTitle() {
		return this.title.getText();
	}

	public void setTitle(String title) {
		this.title.setText(title);
	}
}
