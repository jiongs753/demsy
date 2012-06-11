package com.kmetop.demsy.mvc.nutz;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nutz.filepool.NutFilePool;
import org.nutz.mvc.adaptor.AbstractAdaptor;
import org.nutz.mvc.adaptor.ParamInjector;
import org.nutz.mvc.adaptor.injector.MapPairInjector;
import org.nutz.mvc.adaptor.injector.ObjectPairInjector;
import org.nutz.mvc.adaptor.injector.PathArgInjector;
import org.nutz.mvc.annotation.Param;
import org.nutz.mvc.upload.FastUploading;
import org.nutz.mvc.upload.FieldMeta;
import org.nutz.mvc.upload.TempFile;
import org.nutz.mvc.upload.UploadException;
import org.nutz.mvc.upload.Uploading;
import org.nutz.mvc.upload.UploadingContext;
import org.nutz.mvc.upload.Uploads;
import org.nutz.mvc.upload.injector.FileInjector;
import org.nutz.mvc.upload.injector.FileMetaInjector;
import org.nutz.mvc.upload.injector.InputStreamInjector;
import org.nutz.mvc.upload.injector.MapArrayInjector;
import org.nutz.mvc.upload.injector.MapItemInjector;
import org.nutz.mvc.upload.injector.MapListInjector;
import org.nutz.mvc.upload.injector.MapSelfInjector;
import org.nutz.mvc.upload.injector.ReaderInjector;
import org.nutz.mvc.upload.injector.TempFileInjector;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.lang.Ex;

/**
 * 与{@link org.nutz.mvc.upload.UploadAdaptor}的区别：
 * <UL>
 * <LI>路径参数放在方法的最后，以便于支持可变数量的路径参数。
 * <LI>如：public Map upload(@Param("upload") TempFile tmpfile, String moduleId,
 * String fieldId)
 * </UL>
 * 
 */
public class DemsyUploadAdaptor extends AbstractAdaptor {

	private UploadingContext context;

	public DemsyUploadAdaptor() throws IOException {
		context = new UploadingContext(Demsy.appconfig.getTempDir());
	}

	public DemsyUploadAdaptor(UploadingContext context) {
		this.context = context;
	}

	public DemsyUploadAdaptor(String path) {
		context = new UploadingContext(path);
	}

	public DemsyUploadAdaptor(String path, int buffer) {
		this(path);
		context.setBufferSize(buffer);
	}

	public DemsyUploadAdaptor(String path, int buffer, String charset) {
		this(path);
		context.setBufferSize(buffer);
		context.setCharset(charset);
	}

	public DemsyUploadAdaptor(String path, int buffer, String charset, int poolSize) {
		context = new UploadingContext(new NutFilePool(path, poolSize));
		context.setBufferSize(buffer);
		context.setCharset(charset);
	}

	public DemsyUploadAdaptor(String path, int buffer, String charset, int poolSize, int maxFileSize) {
		context = new UploadingContext(new NutFilePool(path, poolSize));
		context.setBufferSize(buffer);
		context.setCharset(charset);
		context.setMaxFileSize(maxFileSize);
	}

	public UploadingContext getContext() {
		return context;
	}

	protected ParamInjector evalInjector(Class<?> type, Param param) {
		// Map
		if (Map.class.isAssignableFrom(type))
			return new MapSelfInjector();

		if (null == param)
			return new PathArgInjector(type);

		String paramName = param.value();

		// File
		if (File.class.isAssignableFrom(type))
			return new FileInjector(paramName);
		// FileMeta
		if (FieldMeta.class.isAssignableFrom(type))
			return new FileMetaInjector(paramName);
		// TempFile
		if (TempFile.class.isAssignableFrom(type))
			return new TempFileInjector(paramName);
		// InputStream
		if (InputStream.class.isAssignableFrom(type))
			return new InputStreamInjector(paramName);
		// Reader
		if (Reader.class.isAssignableFrom(type))
			return new ReaderInjector(paramName);
		// List
		if (List.class.isAssignableFrom(type))
			return new MapListInjector(paramName);
		// Array
		if (type.isArray())
			return new MapArrayInjector(type.getComponentType(), paramName);
		// POJO
		if ("..".equals(paramName)) {
			if (type.isAssignableFrom(Map.class))
				return new MapPairInjector();
			return new ObjectPairInjector(null, type);
			// return new MapReferInjector(null, type);
		}
		// POJO with prefix
		else if (paramName.startsWith("::") && paramName.length() > 2) {
			return new ObjectPairInjector(null, type);
			// return new MapReferInjector(paramName.substring(2), type);
		}

		// Default case
		return new MapItemInjector(paramName, type);
	}

	public Object[] adapt(ServletContext sc, HttpServletRequest request, HttpServletResponse response, String[] pathArgs) {
		Map<String, Object> map;
		try {
			Uploading ing = new FastUploading();
			map = ing.parse(request, context);
		} catch (UploadException e) {
			throw Ex.throwEx(e);
		} finally {
			Uploads.removeInfo(request);
		}
		// Try to make the args
		Object[] args = new Object[injs.length];

		// Inject another params
		int i = 0;
		for (; i < injs.length; i++) {
			ParamInjector inj = injs[i];
			if (inj instanceof PathArgInjector) {
				break;
			}
			args[i] = inj.get(sc, request, response, map);
		}
		// Loop path args
		if (null != pathArgs) {
			int len = Math.min(args.length - i, pathArgs.length);
			for (int j = 0; j < len; j++, i++)
				args[i] = injs[i].get(null, request, response, pathArgs[j]);
		}
		return args;
	}
}
