package com.graph.controls;

import javafx.geometry.Point2D;
import javafx.scene.transform.Transform;

public class VisualNodePortConnection extends PointConnection {
	private final VisualNodePort<?> start;
	private final VisualNodePort<?> end;

	private final double startCenterX;
	private final double startCenterY;
	private final double endCenterX;
	private final double endCenterY;

	public VisualNodePortConnection(VisualNodePort<?> start, VisualNodePort<?> end) {
		this.start = start;
		this.end = end;

		this.startCenterX = this.start.getWidth() / 2.0;
		this.startCenterY = this.start.getHeight() / 2.0;
		this.endCenterX = this.end.getWidth() / 2.0;
		this.endCenterY = this.end.getHeight() / 2.0;

		this.start.localToSceneTransformProperty().addListener((p, o, n) -> onStartRepositioning(n));
		this.end.localToSceneTransformProperty().addListener((p, o, n) -> onEndRepositioning(n));

		onStartRepositioning(this.start.getLocalToSceneTransform());
		onEndRepositioning(this.end.getLocalToSceneTransform());
	}

	private void onStartRepositioning(Transform transform) {
		Point2D pointOnScreen = transform.transform(this.startCenterX, this.startCenterY);

		setStartX(pointOnScreen.getX());
		setStartY(pointOnScreen.getY());
	}

	private void onEndRepositioning(Transform transform) {
		Point2D pointOnScreen = transform.transform(this.endCenterX, this.endCenterY);

		setEndX(pointOnScreen.getX());
		setEndY(pointOnScreen.getY());
	}

	public VisualNodePort<?> getStart() {
		return this.start;
	}

	public VisualNodePort<?> getEnd() {
		return this.end;
	}
}
