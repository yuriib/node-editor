package com.graph.controls.components;

import com.graph.controls.VisualNodeComponentManufacturer;
import javafx.fxml.FXMLLoader;

public class LabelComponentManufacturer extends VisualNodeComponentManufacturer<LabelComponent> {
	private static final FXMLLoader LOADER = new FXMLLoader(LabelComponentManufacturer.class.getResource("/controls/common/components/visual-node-label-component.fxml"));

	@Override
	public LabelComponent construct(String name) {
		LabelComponent component = new LabelComponent(name);

		try {
			LOADER.setRoot(component);
			LOADER.setController(component);
			return LOADER.load();
		} catch (Exception e) {
			throw new RuntimeException("Could not load FXML", e);
		}
	}
}
