package com.graph.controls;

public abstract class VisualNodeComponentManufacturer<T extends VisualNodeComponent<?>> {
	public abstract T construct(String name);
}
