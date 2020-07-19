package com.graph.controls.nodes;

import com.graph.controls.VisualNode;
import com.graph.controls.VisualNodeComponent;
import com.graph.controls.VisualNodePort;

public final class MulNumbersNode extends VisualNode {
	static final String IN_NUMBER___VARIABLE = "in_number___variable";
	static final String COMPONENT_LABEL___IN_NUMBER___VARIABLE = "component_label___in_number___variable";
	static final String COMPONENT_DISPLAY___OUT_NUMBER___VARIABLE = "component_display___out_number___variable";
	static final String OUT_NUMBER___VARIABLE = "out_number___variable";

	public MulNumbersNode(int guid) {
		super("Mul", guid);
	}

	@Override
	public void evaluateState() {
		double result = 1.0;

		VisualNodeComponent<Double> displaySumResultComponent = getComponent(COMPONENT_DISPLAY___OUT_NUMBER___VARIABLE);
		VisualNodePort<Double> inboundPort = getInboundPort(IN_NUMBER___VARIABLE);
		VisualNodePort<Double> outboundPort = getOutboundPort(OUT_NUMBER___VARIABLE);

		for (VisualNodePort<Double> externalPort : inboundPort.getBoundWith()) {
			result *= externalPort.getData();
		}

		displaySumResultComponent.setValue(result);
		outboundPort.setData(result);
	}

	@Override
	protected void preInitialize() {
		VisualNodeComponent<String> component = getComponent(COMPONENT_LABEL___IN_NUMBER___VARIABLE);

		component.setValue("Number");
	}

	@Override
	protected void postInitialize() {
		VisualNodeComponent<?> component = getComponent(COMPONENT_DISPLAY___OUT_NUMBER___VARIABLE);

		component.setOnMousePressed(event -> {
			event.consume();

			fireEvent(event);
		});
	}
}
