package com.graph.controls.ports;

import com.graph.controls.VisualNodePort;

public final class RoundPinPort<T> extends VisualNodePort<T> {
	RoundPinPort(String name, Type type, Class<T> acceptType) {
		super(name, type, acceptType);
	}

	@Override
	protected void preInitialize() {
	}

	@Override
	protected void postInitialize() {
	}
}
