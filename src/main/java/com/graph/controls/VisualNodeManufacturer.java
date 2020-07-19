package com.graph.controls;

import javafx.fxml.FXMLLoader;

import java.util.LinkedHashMap;
import java.util.List;

public abstract class VisualNodeManufacturer {
	private static final FXMLLoader LOADER = new FXMLLoader(VisualNodeManufacturer.class.getResource("/controls/visual-node.fxml"));

	private static int NEXT_ID = 0;

	public final VisualNode construct() {
		VisualNode node = constructNode(NEXT_ID++);

		if (node == null) {
			throw new RuntimeException("Error during node construction: VisualNode has not been provided");
		}

		List<VisualNodeComponent<?>> components = constructComponents(VisualNode.COMPONENTS_POS);
		if (components != null) {
			for (VisualNodeComponent<?> component : components) {
				addComponent(node, component);
			}
		}

		List<VisualNodePort<?>> inboundPorts = constructInboundPorts(VisualNode.INBOUND_PORTS_POS);
		if (inboundPorts != null) {
			for (VisualNodePort<?> inboundPort : inboundPorts) {
				addInboundPort(node, inboundPort);
			}
		}

		List<VisualNodePort<?>> outboundPorts = constructOutboundPorts(VisualNode.OUTBOUND_PORTS_POS);
		if (outboundPorts != null) {
			for (VisualNodePort<?> outboundPort : outboundPorts) {
				addOutboundPort(node, outboundPort);
			}
		}

		try {
			LOADER.setRoot(node);
			LOADER.setController(node);
			return LOADER.load();
		} catch (Exception e) {
			throw new RuntimeException("Could not load FXML", e);
		}
	}

	protected abstract VisualNode constructNode(final int id);

	protected abstract List<VisualNodeComponent<?>> constructComponents(final int preferredColumnPos);

	protected abstract List<VisualNodePort<?>> constructInboundPorts(final int preferredColumnPos);

	protected abstract List<VisualNodePort<?>> constructOutboundPorts(final int preferredColumnPos);

	private void addComponent(VisualNode node, VisualNodeComponent<?> component) {
		String componentName = component.getName();
		if (node.components == null) {
			node.components = new LinkedHashMap<>();
		}

		if (node.components.containsKey(componentName)) {
			throw new RuntimeException(String.format("The component with name '%s' has been already registered", componentName));
		}

		node.components.put(componentName, component);

		component.owner = node;
	}

	private void addOutboundPort(VisualNode node, VisualNodePort<?> outboundPort) {
		String portName = outboundPort.getName();
		if (node.outboundPorts == null) {
			node.outboundPorts = new LinkedHashMap<>();
		}

		if (node.outboundPorts.containsKey(portName)) {
			throw new RuntimeException(String.format("The port with name '%s' has been already registered", portName));
		}

		node.outboundPorts.put(portName, outboundPort);

		outboundPort.owner = node;
	}

	private void addInboundPort(VisualNode node, VisualNodePort<?> inboundPort) {
		String portName = inboundPort.getName();
		if (node.inboundPorts == null) {
			node.inboundPorts = new LinkedHashMap<>();
		}

		if (node.inboundPorts.containsKey(portName)) {
			throw new RuntimeException(String.format("The port with name '%s' has been already registered", portName));
		}

		node.inboundPorts.put(portName, inboundPort);

		inboundPort.owner = node;
	}
}
