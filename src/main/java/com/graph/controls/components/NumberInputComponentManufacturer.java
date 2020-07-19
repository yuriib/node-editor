package com.graph.controls.components;

import com.graph.controls.VisualNodeComponentManufacturer;
import javafx.fxml.FXMLLoader;

public class NumberInputComponentManufacturer extends VisualNodeComponentManufacturer<NumberInputComponent> {
	private static final FXMLLoader LOADER = new FXMLLoader(NumberInputComponentManufacturer.class.getResource("/controls/common/components/visual-node-number-input-component.fxml"));

	@Override
	public NumberInputComponent construct(String name) {
		NumberInputComponent component = new NumberInputComponent(name);

		try {
			LOADER.setRoot(component);
			LOADER.setController(component);
			return LOADER.load();
		} catch (Exception e) {
			throw new RuntimeException("Could not load FXML", e);
		}
	}
}
