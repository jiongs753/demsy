package com.kmetop.demsy.comlib.dic;

import com.kmetop.demsy.comlib.entity.IBizComponent;
import com.kmetop.demsy.comlib.entity.ITreeEntity;

public interface IDic extends IBizComponent, ITreeEntity {
	IDicCategory getCategory();
}
