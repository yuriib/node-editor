package com.graph.controls.nodes;

import com.graph.controls.*;
import com.graph.controls.components.LabelComponentManufacturer;
import com.graph.controls.components.NumberDisplayComponentManufacturer;
import com.graph.controls.ports.RoundPinPortManufacturer;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.layout.GridPane;

import java.util.Arrays;
import java.util.List;

public class AddNumbersNodeManufacturer extends VisualNodeManufacturer {
	private final VisualNodeComponentManufacturer numberDisplayComponentManufacturer;
	private final VisualNodeComponentManufacturer labelComponentManufacturer;
	private final VisualNodePortManufacturer labeledPortManufacturer;

	public AddNumbersNodeManufacturer() {
		this.numberDisplayComponentManufacturer = new NumberDisplayComponentManufacturer();
		this.labelComponentManufacturer = new LabelComponentManufacturer();
		this.labeledPortManufacturer = new RoundPinPortManufacturer();
	}

	@Override
	protected VisualNode constructNode(int id) {
		return new AddNumbersNode(id);
	}

	@Override
	protected List<VisualNodeComponent<?>> constructComponents(final int preferredColumnPos) {
		VisualNodeComponent<?> resultValueDisplayComponent = this.numberDisplayComponentManufacturer.construct(AddNumbersNode.COMPONENT_DISPLAY___OUT_NUMBER___VARIABLE);
		VisualNodeComponent<?> inboundPortLabelComponent = this.labelComponentManufacturer.construct(AddNumbersNode.COMPONENT_LABEL___IN_NUMBER___VARIABLE);

		GridPane.setValignment(resultValueDisplayComponent, VPos.CENTER);
		GridPane.setHalignment(resultValueDisplayComponent, HPos.CENTER);
		GridPane.setHalignment(inboundPortLabelComponent, HPos.CENTER);
		GridPane.setHalignment(inboundPortLabelComponent, HPos.CENTER);

		GridPane.setConstraints(resultValueDisplayComponent, preferredColumnPos, 0);
		GridPane.setConstraints(inboundPortLabelComponent, preferredColumnPos, 1);

		return Arrays.asList(
				resultValueDisplayComponent,
				inboundPortLabelComponent
		);
	}

	@Override
	protected List<VisualNodePort<?>> constructInboundPorts(final int preferredColumnPos) {
		VisualNodePort<?> inboundPort = this.labeledPortManufacturer.constructInboundPort(AddNumbersNode.IN_NUMBER___VARIABLE, Double.class);

		GridPane.setValignment(inboundPort, VPos.CENTER);
		GridPane.setHalignment(inboundPort, HPos.CENTER);

		GridPane.setConstraints(inboundPort, preferredColumnPos, 1);

		return Arrays.asList(
				inboundPort
		);
	}

	@Override
	protected List<VisualNodePort<?>> constructOutboundPorts(final int preferredColumnPos) {
		VisualNodePort<?> outboundPort = this.labeledPortManufacturer.constructOutboundPort(AddNumbersNode.OUT_NUMBER___VARIABLE, Double.class);

		GridPane.setValignment(outboundPort, VPos.CENTER);
		GridPane.setHalignment(outboundPort, HPos.CENTER);

		GridPane.setConstraints(outboundPort, preferredColumnPos, 0);

		return Arrays.asList(
				outboundPort
		);
	}
}
