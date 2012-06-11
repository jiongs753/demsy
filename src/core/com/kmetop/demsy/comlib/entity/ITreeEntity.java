package com.kmetop.demsy.comlib.entity;

import java.util.List;

public interface ITreeEntity<T> {
	public T getParent();

	public void setParent(T parent);

	public List<T> getChildren();

	public void setChildren(List<T> children);
}
