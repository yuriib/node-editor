package com;

import com.graph.algorithms.DFSIterator;
import com.sun.javafx.collections.ObservableListWrapper;
import com.graph.controls.*;
import com.graph.controls.nodes.AddNumbersNodeManufacturer;
import com.graph.controls.nodes.MulNumbersNodeManufacturer;
import com.graph.controls.nodes.NumberNodeManufacturer;
import javafx.animation.AnimationTimer;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class Controller {
	@FXML
	private Pane viewport;

	@FXML
	private ToggleGroup controlGroup;

	private ObservableList<VisualNode> nodes;
	private ObservableList<VisualNodePortConnection> highlightedConnections;
	private ObjectProperty<VisualNode> selectedNode;

	private Map<String, VisualNodePortConnection> connectionIdsToConnectionMapping;

	private VisualNodeManufacturer numberNodeManufacturer;
	private VisualNodeManufacturer addNumbersNodeManufacturer;
	private VisualNodeManufacturer mulNumbersNodeManufacturer;

	private VisualNodeManufacturer currentNodeManufacturer;

	public Controller() {
		this.nodes = new ObservableListWrapper<>(new LinkedList<>());
		this.highlightedConnections = new ObservableListWrapper<>(new LinkedList<>());
		this.selectedNode = new SimpleObjectProperty<>();

		this.connectionIdsToConnectionMapping = new LinkedHashMap<>();

		this.numberNodeManufacturer = new NumberNodeManufacturer();
		this.addNumbersNodeManufacturer = new AddNumbersNodeManufacturer();
		this.mulNumbersNodeManufacturer = new MulNumbersNodeManufacturer();

		this.currentNodeManufacturer = this.numberNodeManufacturer;
	}

	@FXML
	private void initialize() {
		List<VisualNode> executionOrderQueue = new LinkedList<>();

		AtomicReference<Double> ox = new AtomicReference<>(0.0);
		AtomicReference<Double> oy = new AtomicReference<>(0.0);

		AtomicReference<PointConnection> guideLine = new AtomicReference<>();

		AtomicReference<VisualNodePort<?>> port1 = new AtomicReference<>();
		AtomicReference<VisualNodePort<?>> port2 = new AtomicReference<>();

		this.viewport.setOnMouseClicked(event -> {
			if (!event.getTarget().equals(this.viewport)) {
				return;
			}

			if (event.getClickCount() >= 2) {
				event.consume();

				// remove focus from any focused element (i.e.: textfield, etc)
				this.viewport.requestFocus();

				addNewNode(event.getX(), event.getY());
				rebuildExecutionOrderQueue(executionOrderQueue);
			}
		});

		this.viewport.setOnMousePressed(event -> {
			event.consume();
			if (event.getTarget().equals(this.viewport)) {
				this.selectedNode.setValue(null);
			}
		});

		this.viewport.addEventHandler(VisualNode.Event.MOUSE_PRESSED, event -> {
			event.consume();

			MouseEvent originalEvent = (MouseEvent) event.getOriginalEvent();
			VisualNode node = (VisualNode) event.getTarget();

			ox.set(originalEvent.getX());
			oy.set(originalEvent.getY());

			this.selectedNode.setValue(node);
		});

		this.viewport.addEventHandler(VisualNodePort.Event.MOUSE_PRESSED, event -> {
			event.consume();

			port1.set((VisualNodePort<?>) event.getTarget());

			double centerX = port1.get().getPinSlot().getWidth() / 2.0;
			double centerY = port1.get().getPinSlot().getHeight() / 2.0;

			Point2D pointInScene = port1.get().getPinSlot().getLocalToSceneTransform().transform(centerX, centerY);

			// create guide line that helps to visualize where connection is started
			guideLine.set(new PointConnection(pointInScene.getX(), pointInScene.getY(), pointInScene.getX(), pointInScene.getY()));
			this.viewport.getChildren().add(guideLine.get());
			guideLine.get().toBack();
		});

		this.viewport.addEventHandler(VisualNodePort.Event.MOUSE_DRAGGED, event -> {
			event.consume();

			MouseEvent originalEvent = (MouseEvent) event.getOriginalEvent();

			guideLine.get().setEndX(originalEvent.getSceneX());
			guideLine.get().setEndY(originalEvent.getSceneY());
		});

		this.viewport.addEventHandler(VisualNodePort.Event.MOUSE_RELEASED, event -> {
			event.consume();

			// if connection was successful
			if (port1.get() != null && port2.get() != null) {
				// exchange start/end positions as we want to preserve connection order
				// (later it can be helpful to avoid connection duplications)
				VisualNodePort<?> start = port1.get().getPortType() == VisualNodePort.Type.OUTBOUND ? port1.get() : port2.get();
				VisualNodePort<?> end = port2.get().getPortType() == VisualNodePort.Type.INBOUND ? port2.get() : port1.get();

				if (connectionExists(start, end)) {
					unbind(start, end);
				} else {
					bind(start, end);
				}
			}

			this.viewport.getChildren().remove(guideLine.get());
			guideLine.set(null);
			port1.set(null);
			port2.set(null);
		});

		this.viewport.addEventHandler(VisualNodePort.Event.MOUSE_DRAG_RELEASED, event -> {
			event.consume();
			if (event.isConnected()) {
				port2.set((VisualNodePort<?>) event.getTarget());
			}
		});

		this.viewport.addEventHandler(VisualNode.Event.MOUSE_DRAGGED, event -> {
			event.consume();

			MouseEvent originalEvent = (MouseEvent) event.getOriginalEvent();
			Node control = (Node) event.getTarget();

			double dx = originalEvent.getX() - ox.get();
			double dy = originalEvent.getY() - oy.get();

			control.setLayoutX(control.getLayoutX() + dx);
			control.setLayoutY(control.getLayoutY() + dy);
		});

		this.selectedNode.addListener((p, o, n) -> {
			this.highlightedConnections.clear();

			if (o != null) {
				o.getStyleClass().remove("focused");
			}

			if (n != null) {
				n.toFront();
				n.getStyleClass().add("focused");

				highlightInboundConnections(n);
			}
		});

		this.controlGroup.selectedToggleProperty().addListener((p, o, n) -> {
			if (n.getUserData() == null) {
				System.err.println("Invalid value provided for toggle group");
				return;
			}

			if (n.getUserData().equals("add")) {
				this.currentNodeManufacturer = this.addNumbersNodeManufacturer;
			} else if (n.getUserData().equals("mul")) {
				this.currentNodeManufacturer = this.mulNumbersNodeManufacturer;
			} else if (n.getUserData().equals("input")) {
				this.currentNodeManufacturer = this.numberNodeManufacturer;
			}
		});

		this.highlightedConnections.addListener((ListChangeListener<? super VisualNodePortConnection>) listener -> {
			while (listener.next()) {
				for (VisualNodePortConnection connection : listener.getAddedSubList()) {
					connection.showDataFlow();
				}

				for (VisualNodePortConnection connection : listener.getRemoved()) {
					connection.hideDataFlow();
				}
			}
		});

		AnimationTimer appLoop = new AnimationTimer() {
			private final int FPS = 60;
			private final float NANOS_IN_SEC = 1_000_000_000.0f;
			private final float FRAME_RATE = 1.0f / FPS - 0.006f; // -0.006 to make user experience smoother

			private long prevTimestamp = System.nanoTime();

			@Override
			public void handle(long now) {
				float deltaTime = (now - this.prevTimestamp) / NANOS_IN_SEC;

				if (deltaTime <= FRAME_RATE) {
					return;
				}

				for (VisualNode node : executionOrderQueue) {
					node.evaluateState();
				}

				for (VisualNodePortConnection connection : highlightedConnections) {
					connection.update(deltaTime);
				}

				this.prevTimestamp = now;
			}
		};

		appLoop.start();
	}

	private boolean connectionExists(VisualNodePort<?> start, VisualNodePort<?> end) {
		return this.connectionIdsToConnectionMapping.containsKey(createConnectionId(start, end));
	}

	private void highlightInboundConnections(VisualNode node) {
		if (node.getInboundPorts() == null) {
			return;
		}

		for (VisualNodePort<?> inboundPort : node.getInboundPorts()) {
			for (VisualNodePort<?> externalPort : inboundPort.getBoundWith()) {
				String connectionId = createConnectionId(externalPort, inboundPort);
				this.highlightedConnections.add(this.connectionIdsToConnectionMapping.get(connectionId));
			}
		}
	}

	private void unbind(VisualNodePort<?> start, VisualNodePort<?> end) {
		String connectionId = createConnectionId(start, end);
		VisualNodePortConnection connection = this.connectionIdsToConnectionMapping.remove(connectionId);
		this.viewport.getChildren().remove(connection);
		this.highlightedConnections.remove(connection);
	}

	private void bind(VisualNodePort<?> start, VisualNodePort<?> end) {
		String connectionId = createConnectionId(start, end);
		VisualNodePortConnection connection = new VisualNodePortConnection(start, end);
		this.connectionIdsToConnectionMapping.put(connectionId, connection);
		this.viewport.getChildren().add(connection);
		connection.toBack();

		// highlight connection if it was created when target node was highlighted
		if (end.getOwner().equals(this.selectedNode.getValue())) {
			this.highlightedConnections.add(connection);
		}
	}

	private void addNewNode(double layoutX, double layoutY) {
		VisualNode node = this.currentNodeManufacturer.construct();

		node.setLayoutX(layoutX);
		node.setLayoutY(layoutY);

		this.nodes.add(node);
		this.viewport.getChildren().add(node);
	}

	private void rebuildExecutionOrderQueue(List<VisualNode> executionOrderQueue) {
		executionOrderQueue.clear();
		for (VisualNode n : new DFSIterator(this.nodes)) {
			executionOrderQueue.add(n);
		}
	}

	private String createConnectionId(VisualNodePort<?> start, VisualNodePort<?> end) {
		return String.format(
				"n%s:%s<--->n%s:%s",
				start.getOwner().getGuid(),
				start.getName(),
				end.getOwner().getGuid(),
				end.getName()
		);
	}
}
