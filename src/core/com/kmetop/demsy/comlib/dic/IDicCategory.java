package com.kmetop.demsy.comlib.dic;

import java.util.List;

import soom.entity.IBizComponent;

import com.kmetop.demsy.comlib.entity.ITreeEntity;

public interface IDicCategory extends IBizComponent, ITreeEntity {
	List<? extends IDic> getDics();
}
