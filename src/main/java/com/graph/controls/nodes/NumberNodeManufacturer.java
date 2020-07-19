package com.graph.controls.nodes;

import com.graph.controls.VisualNodeComponent;
import com.graph.controls.VisualNode;
import com.graph.controls.VisualNodeManufacturer;
import com.graph.controls.VisualNodePort;
import com.graph.controls.components.NumberInputComponentManufacturer;
import com.graph.controls.ports.RoundPinPortManufacturer;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.layout.GridPane;

import java.util.Arrays;
import java.util.List;

public class NumberNodeManufacturer extends VisualNodeManufacturer {
	private final NumberInputComponentManufacturer numberInputComponentManufacturer;
	private final RoundPinPortManufacturer labeledPortManufacturer;

	public NumberNodeManufacturer() {
		this.numberInputComponentManufacturer = new NumberInputComponentManufacturer();
		this.labeledPortManufacturer = new RoundPinPortManufacturer();
	}

	@Override
	protected VisualNode constructNode(int id) {
		return new NumberNode(id);
	}

	@Override
	protected List<VisualNodeComponent<?>> constructComponents(final int preferredColumnPos) {
		VisualNodeComponent<?> component = this.numberInputComponentManufacturer.construct(NumberNode.COMPONENT_INPUT___OUT_NUMBER___VARIABLE);

		GridPane.setValignment(component, VPos.CENTER);
		GridPane.setHalignment(component, HPos.CENTER);

		GridPane.setConstraints(component, preferredColumnPos, 0);

		return Arrays.asList(
				component
		);
	}

	@Override
	protected List<VisualNodePort<?>> constructInboundPorts(final int preferredColumnPos) {
		return null;
	}

	@Override
	protected List<VisualNodePort<?>> constructOutboundPorts(final int preferredColumnPos) {
		VisualNodePort<?> outboundPort = this.labeledPortManufacturer.constructOutboundPort(NumberNode.OUT_NUMBER___VARIABLE, Double.class);

		GridPane.setValignment(outboundPort, VPos.CENTER);
		GridPane.setHalignment(outboundPort, HPos.CENTER);

		GridPane.setConstraints(outboundPort, preferredColumnPos, 0);

		return Arrays.asList(
				outboundPort
		);
	}
}
