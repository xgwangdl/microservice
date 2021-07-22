package com.accenture.common.okcatch;

import java.util.HashMap;
import java.util.Map;

public class OkCatch<K,V> {

	// max catch size
	private int maxSize;
	
	// is node head
	private Node<K,V> head;
	
	// is node tail
	private Node<K,V> tail;
	
	// catch the data
	private Map<K, Node<K,V>> catchValueMap;
	
	private static class Node<K,V> {
		private V value;
		private K key;
		private Node<K,V> prev, next;
		
		public Node(K key,V value) {
			this.value = value;
			this.key = key;
		}
		
		@Override
		public String toString() {
			return this.value.toString();
		}
	}
	
	private void removeNode(Node<K,V> node) {
		if (node.next == null) {
			this.tail = node.prev;
		} else {
			node.prev.next = node.next;
		}
		
		if (node.prev == null) {
			this.head = node.next;
		} else {
			node.next.prev = node.prev;
		}
	}
	
	private void addNode(Node<K,V> node) {
		if (this.head != null) {
			this.tail.next = node;
			node.prev = this.tail;
			node.next = null;
			this.tail = node;
			
		} else {
			this.head = node;
			this.tail = node;
		}
	}
	
	public OkCatch(int maxsize) {
		this.maxSize = maxsize;
		this.catchValueMap = new HashMap<>();
	}
	
	public void put(K key,V value) {
		if (this.catchValueMap.containsKey(key)) {
			Node<K, V> node = this.catchValueMap.get(key);
			node.value = value;
			this.removeNode(node);
			this.addNode(node);
		} else {
			if (this.catchValueMap.size() >= this.maxSize) {
				this.catchValueMap.remove(this.head.key);
				this.removeNode(this.head);
			}
			Node<K, V> node = new Node<>(key, value);
			this.addNode(node);
			this.catchValueMap.put(key, node);
		}
	}
	
	public V get(K key) {
		if (this.catchValueMap.containsKey(key)) {
			Node<K, V> node = this.catchValueMap.get(key);
			this.removeNode(node);
			this.addNode(node);
			return node.value;
		} else {
			return null;
		}
	}
}
