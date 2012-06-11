package com.kmetop.demsy.comlib.biz;

import com.kmetop.demsy.comlib.entity.IBizComponent;
import com.kmetop.demsy.comlib.entity.ITreeEntity;

public interface IBizCatalog<T> extends IBizComponent, ITreeEntity<T> {

	void setCode(String code);

}
