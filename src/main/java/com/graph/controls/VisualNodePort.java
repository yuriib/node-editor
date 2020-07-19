package com.graph.controls;

import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.util.LinkedList;
import java.util.List;

public abstract class VisualNodePort<T> extends HBox {
	private static final String SUCCESS_CSS = "success";
	private static final String ERROR_CSS = "error";

	javafx.scene.Node pin;

	VisualNode owner;

	@FXML
	private AnchorPane pinSlot;

	private String name;
	private Type portType;
	private Class<T> dataType;
	private T data;

	private List<VisualNodePort<T>> boundWith;

	public VisualNodePort(String name, Type portType, Class<T> dataType) {
		this.name = name;
		this.portType = portType;
		this.dataType = dataType;
		this.boundWith = new LinkedList<>();
	}

	public boolean bind(VisualNodePort<T> other) {
		if (this.boundWith.contains(other)) {
			System.out.println("Already bound");
			return false;
		}

		this.boundWith.add(other);
		other.boundWith.add(this);
		return true;
	}

	public boolean unbind(VisualNodePort<T> other) {
		if (!this.boundWith.contains(other)) {
			return false;
		}

		this.boundWith.remove(other);
		other.boundWith.remove(this);
		return true;
	}

	@FXML
	protected final void initialize() {
		preInitialize();

		registerPin();
		registerEvents();

		postInitialize();
	}

	private void registerPin() {
		this.pinSlot.getChildren().add(this.pin);
	}

	private void registerEvents() {
		this.pin.setOnMouseDragged(event -> {
			event.consume();
			fireEvent(event);
		});

		setOnMousePressed(event -> {
			event.consume();
			requestFocus();
			getStyleClass().add(SUCCESS_CSS);
			setMouseTransparent(true);
			fireEvent(new Event(Event.MOUSE_PRESSED, event));
		});

		setOnMouseReleased(event -> {
			event.consume();
			setMouseTransparent(false);
			getStyleClass().remove(SUCCESS_CSS);
			fireEvent(new Event(Event.MOUSE_RELEASED, event));
		});

		setOnDragDetected(event -> {
			event.consume();
			startFullDrag();
		});

		setOnMouseDragged(event -> {
			event.consume();
			fireEvent(new Event(Event.MOUSE_DRAGGED, event));
		});

		setOnMouseDragEntered(event -> {
			if (!(event.getGestureSource() instanceof VisualNodePort<?>)) {
				return;
			}

			event.consume();

			VisualNodePort<?> other = (VisualNodePort<?>) event.getGestureSource();
			if (acceptsConnection(other)) {
				getStyleClass().add(SUCCESS_CSS);
			} else {
				getStyleClass().add(ERROR_CSS);
			}
		});

		setOnMouseDragExited(event -> {
			event.consume();
			getStyleClass().remove(SUCCESS_CSS);
			getStyleClass().remove(ERROR_CSS);
		});

		setOnMouseDragReleased(event -> {
			if (!(event.getGestureSource() instanceof VisualNodePort<?>)) {
				return;
			}

			event.consume();

			VisualNodePort<?> other = (VisualNodePort<?>) event.getGestureSource();
			if (acceptsConnection(other)) {
				getStyleClass().remove(SUCCESS_CSS);
				if (!bind((VisualNodePort<T>) other)) {
					unbind((VisualNodePort<T>) other);
				}

				fireEvent(new Event(Event.MOUSE_DRAG_RELEASED, true, event));
			} else {
				getStyleClass().remove(ERROR_CSS);

				fireEvent(new Event(Event.MOUSE_DRAG_RELEASED, false, event));
			}
		});
	}

	protected abstract void preInitialize();

	protected abstract void postInitialize();

	private boolean acceptsConnection(VisualNodePort<?> other) {
		// same node cannot accept connections to itself
		return !shareSameNode(other) && isValidPortType(other) && isValidDataType(other);
	}

	private boolean shareSameNode(VisualNodePort<?> other) {
		return other.getOwner().equals(getOwner());
	}

	private boolean isValidPortType(VisualNodePort<?> other) {
		if (getPortType() == Type.INBOUND) {
			return other.getPortType() == Type.OUTBOUND;
		} else if (getPortType() == Type.OUTBOUND) {
			return other.getPortType() == Type.INBOUND;
		}

		return false;
	}

	private boolean isValidDataType(VisualNodePort<?> other) {
		return getDataType().equals(other.getDataType());
	}

	public Type getPortType() {
		return this.portType;
	}

	Class<T> getDataType() {
		return this.dataType;
	}

	public List<VisualNodePort<T>> getBoundWith() {
		return this.boundWith;
	}

	public String getName() {
		return this.name;
	}

	public T getData() {
		return this.data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public VisualNode getOwner() {
		return owner;
	}

	public AnchorPane getPinSlot() {
		return this.pinSlot;
	}

	public enum Type {
		INBOUND, OUTBOUND
	}

	public static class Event extends javafx.event.Event {
		public static final EventType<Event> MOUSE_PRESSED = new EventType<>(javafx.event.Event.ANY, "VISUAL_NODE_PORT___MOUSE_PRESSED");
		public static final EventType<Event> MOUSE_RELEASED = new EventType<>(javafx.event.Event.ANY, "VISUAL_NODE_PORT___MOUSE_RELEASED");
		public static final EventType<Event> MOUSE_DRAGGED = new EventType<>(javafx.event.Event.ANY, "VISUAL_NODE_PORT___MOUSE_DRAGGED");
		public static final EventType<Event> MOUSE_DRAG_RELEASED = new EventType<>(javafx.event.Event.ANY, "VISUAL_NODE_PORT___MOUSE_DRAG_RELEASED");

		private final boolean connected;
		private final javafx.event.Event original;

		private Event(EventType<? extends javafx.event.Event> eventType, javafx.event.Event original) {
			super(eventType);
			this.original = original;
			this.connected = false;
		}

		private Event(EventType<? extends javafx.event.Event> eventType, boolean connected, javafx.event.Event original) {
			super(eventType);
			this.original = original;
			this.connected = connected;
		}

		public javafx.event.Event getOriginalEvent() {
			return this.original;
		}

		public boolean isConnected() {
			return this.connected;
		}
	}
}
