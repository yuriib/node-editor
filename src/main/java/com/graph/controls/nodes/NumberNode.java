package com.graph.controls.nodes;

import com.graph.controls.VisualNode;
import com.graph.controls.components.NumberInputComponent;

public final class NumberNode extends VisualNode {
	public static final String COMPONENT_INPUT___OUT_NUMBER___VARIABLE = "component_input___out_number___variable";
	public static final String OUT_NUMBER___VARIABLE = "out_number___variable";

	NumberNode(int guid) {
		super("Number", guid);
	}

	@Override
	public void evaluateState() {
		NumberInputComponent component = getComponent(COMPONENT_INPUT___OUT_NUMBER___VARIABLE);
		getOutboundPort(OUT_NUMBER___VARIABLE).setData(component.getValue());
	}

	@Override
	protected void preInitialize() {
	}

	@Override
	protected void postInitialize() {
	}
}
