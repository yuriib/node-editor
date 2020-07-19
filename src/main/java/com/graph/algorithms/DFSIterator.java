package com.graph.algorithms;

import com.graph.controls.VisualNode;
import com.graph.controls.VisualNodePort;

import java.util.*;

public final class DFSIterator implements Iterable<VisualNode>, Iterator<VisualNode> {
	private final List<VisualNode> graph;

	private Stack<VisualNode> stack;
	private Set<VisualNode> visited;

	public DFSIterator(List<VisualNode> graph) {
		this.graph = graph;
	}

	/**
	 * In DFS iterator first "drill into" the graph and will return one by one
	 * already visited items
	 * <p>
	 * NOTE: To make DFS for-each friendly
	 *
	 * @return
	 */
	@Override
	public Iterator<VisualNode> iterator() {
		this.stack = new Stack<>();
		this.visited = new LinkedHashSet<>();

		for (VisualNode node : this.graph) {
			visitNodes(node);
		}

		return this;
	}

	@Override
	public boolean hasNext() {
		return !this.stack.isEmpty();
	}

	@Override
	public VisualNode next() {
		return this.stack.pop();
	}

	private void visitNodes(VisualNode node) {
		if (this.visited.contains(node)) {
			return;
		}

		this.visited.add(node);
		for (VisualNodePort<?> outboundPorts : node.getOutboundPorts()) {
			for (VisualNodePort<?> externalPort : outboundPorts.getBoundWith()) {
				visitNodes(externalPort.getOwner());
			}
		}

		this.stack.push(node);
	}
}
