package com.graph.controls.components;

import com.graph.controls.VisualNodeComponentManufacturer;
import javafx.fxml.FXMLLoader;

public class NumberDisplayComponentManufacturer extends VisualNodeComponentManufacturer<NumberDisplayComponent> {
	private static final FXMLLoader LOADER = new FXMLLoader(NumberDisplayComponentManufacturer.class.getResource("/controls/common/components/visual-node-value-display-component.fxml"));

	@Override
	public NumberDisplayComponent construct(String name) {
		NumberDisplayComponent component = new NumberDisplayComponent(name);

		try {
			LOADER.setRoot(component);
			LOADER.setController(component);
			return LOADER.load();
		} catch (Exception e) {
			throw new RuntimeException("Could not load FXML", e);
		}
	}
}
