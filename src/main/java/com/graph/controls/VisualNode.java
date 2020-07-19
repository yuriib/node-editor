package com.graph.controls;

import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

import java.util.Collection;
import java.util.Map;

public abstract class VisualNode extends VBox {
	static final int INBOUND_PORTS_POS = 0;
	static final int COMPONENTS_POS = 1;
	static final int OUTBOUND_PORTS_POS = 2;

	Map<String, VisualNodeComponent<?>> components;
	Map<String, VisualNodePort<?>> inboundPorts;
	Map<String, VisualNodePort<?>> outboundPorts;

	private int guid;
	private String name;

	@FXML
	private VisualNodeHeader header;

	@FXML
	private VisualNodeBody body;

	protected VisualNode(String name, int guid) {
		this.name = name;
		this.guid = guid;
	}

	@FXML
	private void initialize() {
		preInitialize();

		this.header.setTitle(this.name);

		registerComponents();
		registerInboundPorts();
		registerOutboundPorts();
		registerEvents();

		postInitialize();
	}

	private void registerComponents() {
		if (this.components != null) {
			this.body.getChildren().addAll(getComponents());
		}
	}

	private void registerInboundPorts() {
		if (this.inboundPorts != null) {
			this.body.getChildren().addAll(getInboundPorts());
		}
	}

	private void registerOutboundPorts() {
		if (this.outboundPorts != null) {
			this.body.getChildren().addAll(getOutboundPorts());
		}
	}

	private void registerEvents() {
		//region Global Node Events
		addEventHandler(VisualNodeComponent.Event.MOUSE_PRESSED, event -> {
			event.consume();
			fireEvent(new Event(Event.MOUSE_PRESSED, event.getOriginalEvent()));
		});
		//endregion

		//region Header Events
		this.header.setOnMousePressed(event -> {
			event.consume();
			requestFocus();
			fireEvent(new Event(Event.MOUSE_PRESSED, event));
		});

		// you can drag node by its header only
		this.header.setOnMouseDragged(event -> {
			event.consume();
			fireEvent(new Event(Event.MOUSE_DRAGGED, event));
		});
		//endregion

		//region Body Events
		this.body.setOnMousePressed(event -> {
			event.consume();
			fireEvent(event);
		});

		this.body.setOnMouseDragged(event -> {
			if (!(event.getTarget() instanceof VisualNodePort)) {
				event.consume();
			}
		});
		//endregion
	}

	protected abstract void preInitialize();

	protected abstract void postInitialize();

	public abstract void evaluateState();

	//region Get/Set Properties
	public <T extends VisualNodeComponent<?>> T getComponent(String componentName) {
		if (this.components == null) {
			return null;
		}

		return (T) this.components.get(componentName);
	}

	public <T> VisualNodePort<T> getInboundPort(String portName) {
		if (this.inboundPorts == null) {
			return null;
		}

		return (VisualNodePort<T>) this.inboundPorts.get(portName);
	}

	public <T> VisualNodePort<T> getOutboundPort(String portName) {
		if (this.outboundPorts == null) {
			return null;
		}

		return (VisualNodePort<T>) this.outboundPorts.get(portName);
	}

	public Collection<VisualNodeComponent<?>> getComponents() {
		if (this.components == null) {
			return null;
		}

		return this.components.values();
	}

	public Collection<VisualNodePort<?>> getInboundPorts() {
		if (this.inboundPorts == null) {
			return null;
		}

		return this.inboundPorts.values();
	}

	public Collection<VisualNodePort<?>> getOutboundPorts() {
		if (this.outboundPorts == null) {
			return null;
		}

		return this.outboundPorts.values();
	}

	public String getName() {
		return this.name;
	}

	public int getGuid() {
		return this.guid;
	}
	//endregion

	public static class Event extends javafx.event.Event {
		public static final EventType<Event> MOUSE_PRESSED = new EventType<>(javafx.event.Event.ANY, "VISUAL_NODE___MOUSE_PRESSED");
		public static final EventType<Event> MOUSE_DRAGGED = new EventType<>(javafx.event.Event.ANY, "VISUAL_NODE___MOUSE_DRAGGED");

		private final javafx.event.Event original;

		private Event(EventType<? extends javafx.event.Event> eventType, javafx.event.Event original) {
			super(eventType);
			this.original = original;
		}

		public javafx.event.Event getOriginalEvent() {
			return this.original;
		}
	}
}
