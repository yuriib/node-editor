package com.graph.controls;

import com.graph.controls.ports.RoundPinPortManufacturer;
import javafx.fxml.FXMLLoader;

public abstract class VisualNodePortManufacturer {
	private static final FXMLLoader LOADER = new FXMLLoader(RoundPinPortManufacturer.class.getResource("/controls/common/ports/visual-node-port.fxml"));

	public <T> VisualNodePort<T> constructInboundPort(String name, Class<T> acceptType) {
		VisualNodePort<T> port = constructIOPort(name, VisualNodePort.Type.INBOUND, acceptType);

		port.getStyleClass().add("inbound");

		return port;
	}

	public <T> VisualNodePort<T> constructOutboundPort(String name, Class<T> acceptType) {
		VisualNodePort<T> port = constructIOPort(name, VisualNodePort.Type.OUTBOUND, acceptType);

		port.getStyleClass().add("outbound");

		return port;
	}

	public <T> VisualNodePort<T> constructIOPort(String name, VisualNodePort.Type type, Class<T> acceptType) {
		VisualNodePort<T> port = constructPort(name, type, acceptType);

		port.pin = constructPin();
		port.pin.setId("pin");

		try {
			LOADER.setRoot(port);
			LOADER.setController(port);
			return LOADER.load();
		} catch (Exception e) {
			throw new RuntimeException("Could not load FXML", e);
		}
	}

	protected abstract <T> VisualNodePort<T> constructPort(String name, VisualNodePort.Type type, Class<T> acceptType);

	protected abstract <T extends javafx.scene.Node> T constructPin();
}