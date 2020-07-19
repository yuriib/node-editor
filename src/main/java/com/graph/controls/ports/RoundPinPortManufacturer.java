package com.graph.controls.ports;

import com.graph.controls.VisualNodePort;
import com.graph.controls.VisualNodePortManufacturer;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;

public class RoundPinPortManufacturer extends VisualNodePortManufacturer {
	@Override
	protected <T> VisualNodePort<T> constructPort(String name, VisualNodePort.Type type, Class<T> acceptType) {
		return new RoundPinPort<>(name, type, acceptType);
	}

	@Override
	protected <T extends javafx.scene.Node> T constructPin() {
		Circle pinShape = new Circle(5.0);

		AnchorPane.setTopAnchor(pinShape, 0.0);
		AnchorPane.setRightAnchor(pinShape, 0.0);
		AnchorPane.setBottomAnchor(pinShape, 0.0);
		AnchorPane.setLeftAnchor(pinShape, 0.0);

		return (T) pinShape;
	}
}
