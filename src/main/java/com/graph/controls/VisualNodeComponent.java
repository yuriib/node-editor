package com.graph.controls;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public abstract class VisualNodeComponent<T> extends GridPane {
	private final ObjectProperty<T> value = new SimpleObjectProperty<>();

	VisualNode owner;

	private String name;

	public VisualNodeComponent(String name) {
		this.name = name;
	}

	@FXML
	protected final void initialize() {
		preInitialize();

		addEventFilter(MouseEvent.MOUSE_PRESSED, event -> fireEvent(new Event(Event.MOUSE_PRESSED, event)));

		postInitialize();
	}

	protected abstract void preInitialize();

	protected abstract void postInitialize();

	public String getName() {
		return this.name;
	}

	public T getValue() {
		return value.get();
	}

	public void setValue(T value) {
		this.value.set(value);
	}

	public VisualNode getOwner() {
		return this.owner;
	}

	public ObjectProperty<T> valueProperty() {
		return this.value;
	}

	public static class Event extends javafx.event.Event {
		public static final EventType<Event> MOUSE_PRESSED = new EventType<>(javafx.event.Event.ANY, "VISUAL_NODE_COMPONENT___MOUSE_PRESSED");

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
