package com.kmetop.demsy.comlib.biz.field;

import static com.kmetop.demsy.comlib.LibConst.*;
import com.kmetop.demsy.comlib.biz.ann.BzFld;
import com.kmetop.demsy.comlib.impl.base.web.UploadInfo;

@BzFld(precision = 2000, refrenceFields = "name,path", refrenceSystem = BIZSYS_ADMIN_UPLOAD)
public class MultiUpload extends FakeSubSystem<UploadInfo> {
	public MultiUpload() {
		this("");
	}

	public MultiUpload(String str) {
		super(str, UploadInfo.class);
	}

	public MultiUpload(String str, Class type) {
		super(str, type);
	}
}