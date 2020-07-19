package com.graph.controls;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.StrokeType;

public class PointConnection extends Region {
	protected static final Double MAX_CONTROL_X_OFFSET = 100.0;

	private DoubleProperty startX;
	private DoubleProperty startY;
	private DoubleProperty endX;
	private DoubleProperty endY;

	private DoubleProperty distance;

	private DoubleProperty offsetX;

	private CubicCurve lineBackground;
	private CubicCurve lineAnimatedForeground;
	private CubicCurve lineStaticForeground;

	public PointConnection(double startX, double startY, double endX, double endY) {
		this.startX = new SimpleDoubleProperty(startX);
		this.startY = new SimpleDoubleProperty(startY);
		this.endX = new SimpleDoubleProperty(endX);
		this.endY = new SimpleDoubleProperty(endY);

		this.distance = new SimpleDoubleProperty(0.0);

		this.offsetX = new SimpleDoubleProperty(MAX_CONTROL_X_OFFSET);

		this.lineBackground = new CubicCurve();
		this.lineAnimatedForeground = new CubicCurve();
		this.lineStaticForeground = new CubicCurve();

		this.lineBackground.setStrokeWidth(5.0);
		this.lineBackground.setFill(Color.TRANSPARENT);
		this.lineBackground.setStrokeType(StrokeType.CENTERED);
		this.lineBackground.setStroke(Color.BLACK);

		this.lineAnimatedForeground.setStrokeWidth(3.0);
		this.lineAnimatedForeground.setFill(Color.TRANSPARENT);
		this.lineAnimatedForeground.setStrokeType(StrokeType.CENTERED);
		this.lineAnimatedForeground.setStroke(
				Color.ORANGE
//				new LinearGradient(
//						0.0, 0.0, 1.0, 1.0, true, null,
//						new Stop(0.0, Color.web("#4ef2d2")),
//						new Stop(1.0, Color.web("#874ef2"))
//				)
		);
		this.lineAnimatedForeground.getStrokeDashArray().add(8.0);
		this.lineAnimatedForeground.getStrokeDashArray().add(5.0);
		this.lineAnimatedForeground.setVisible(false);

		this.lineStaticForeground.setStrokeWidth(2.0);
		this.lineStaticForeground.setFill(Color.TRANSPARENT);
		this.lineStaticForeground.setStrokeType(StrokeType.CENTERED);
		this.lineStaticForeground.setStroke(
				Color.SILVER
//				new LinearGradient(
//						0.0, 0.0, 1.0, 1.0, true, null,
//						new Stop(0.0, Color.web("#4ef2d2")),
//						new Stop(1.0, Color.web("#874ef2"))
//				)
		);
//		this.lineStaticForeground.setVisible(false);

		// start
		this.lineBackground.startXProperty().bind(this.startX);
		this.lineBackground.startYProperty().bind(this.startY);
		this.lineBackground.controlX1Property().bind(this.startX.add(this.offsetX));
		this.lineBackground.controlY1Property().bind(this.startY);

		this.lineAnimatedForeground.startXProperty().bind(this.startX);
		this.lineAnimatedForeground.startYProperty().bind(this.startY);
		this.lineAnimatedForeground.controlX1Property().bind(this.startX.add(this.offsetX));
		this.lineAnimatedForeground.controlY1Property().bind(this.startY);

		this.lineStaticForeground.startXProperty().bind(this.startX);
		this.lineStaticForeground.startYProperty().bind(this.startY);
		this.lineStaticForeground.controlX1Property().bind(this.startX.add(this.offsetX));
		this.lineStaticForeground.controlY1Property().bind(this.startY);

		// end
		this.lineBackground.endXProperty().bind(this.endX);
		this.lineBackground.endYProperty().bind(this.endY);
		this.lineBackground.controlX2Property().bind(this.endX.subtract(this.offsetX));
		this.lineBackground.controlY2Property().bind(this.endY);

		this.lineAnimatedForeground.endXProperty().bind(this.endX);
		this.lineAnimatedForeground.endYProperty().bind(this.endY);
		this.lineAnimatedForeground.controlX2Property().bind(this.endX.subtract(this.offsetX));
		this.lineAnimatedForeground.controlY2Property().bind(this.endY);

		this.lineStaticForeground.endXProperty().bind(this.endX);
		this.lineStaticForeground.endYProperty().bind(this.endY);
		this.lineStaticForeground.controlX2Property().bind(this.endX.subtract(this.offsetX));
		this.lineStaticForeground.controlY2Property().bind(this.endY);

		this.distance.bind(this.endX.subtract(this.startX).multiply(this.endX.subtract(this.startX))
				.add(this.endY.subtract(this.startY).multiply(this.endY.subtract(this.startY))));

		// when controls are to close to ech other the curve controls
		// must be shrink almost to their origins. When far enough the
		// controls will be displaced no further then MAX_CONTROL_X_OFFSET
		// pixels from their origins
		this.distance.addListener((p, o, n) -> {
			if (n.doubleValue() < (MAX_CONTROL_X_OFFSET * MAX_CONTROL_X_OFFSET)) {
				double newOffsetX = Math.sqrt(n.doubleValue());
				this.offsetX.set(newOffsetX);
			} else {
				this.offsetX.set(MAX_CONTROL_X_OFFSET);
			}
		});

		setMouseTransparent(true);

		getChildren().add(this.lineBackground);
		getChildren().add(this.lineAnimatedForeground);
		getChildren().add(this.lineStaticForeground);
	}

	public PointConnection() {
		this(0.0, 0.0, 0.0, 0.0);
	}

	public void showDataFlow() {
		this.lineAnimatedForeground.setVisible(true);
		this.lineStaticForeground.setVisible(false);
	}

	public void hideDataFlow() {
		this.lineAnimatedForeground.setVisible(false);
		this.lineStaticForeground.setVisible(true);
	}

	public void update(double deltaTime) {
		this.lineAnimatedForeground.setStrokeDashOffset(this.lineAnimatedForeground.getStrokeDashOffset() + -25 * deltaTime);
	}

	public double getStartX() {
		return this.startX.get();
	}

	public void setStartX(double startX) {
		this.startX.set(startX);
	}

	public DoubleProperty startXProperty() {
		return this.startX;
	}

	public double getStartY() {
		return this.startY.get();
	}

	public void setStartY(double startY) {
		this.startY.set(startY);
	}

	public DoubleProperty startYProperty() {
		return this.startY;
	}

	public double getEndX() {
		return this.endX.get();
	}

	public void setEndX(double endX) {
		this.endX.set(endX);
	}

	public DoubleProperty endXProperty() {
		return this.endX;
	}

	public double getEndY() {
		return this.endY.get();
	}

	public void setEndY(double endY) {
		this.endY.set(endY);
	}

	public DoubleProperty endYProperty() {
		return this.endY;
	}
}
