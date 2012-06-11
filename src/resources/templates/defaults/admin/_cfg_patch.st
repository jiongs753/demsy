
<form onsubmit="return false;" method="post" class="bzsys">
	<fieldset>
		<legend>更新补丁程序</legend>
		<table>
			<tr>
				<td>补丁文件：</td>
				<td>{import file="../common/upload.st" id="patchFile" name="filePath" value=""}</td>
			</tr>
		</table>
		<div class="space"></div>
		<input type="submit" class="ajaxSubmit" value="更新程序" onclick='this.form.action="{$contextPath}/config/patch"' />
	</fieldset>

	<fieldset>
		<legend>导出软件数据</legend>
		<table>
			<tr>
				<td>软件应用：</td>
				<td><select id="" name="exportDataFromSoft">
						<option value="">---选择平台应用软件---</option> {import file="../common/options.st" nodes=$softNodes.children}
				</select></td>

				<td>修改日期：</td>
				<td><input type="text" name="exportDataFromDate" class="datepicker" /></td>
                <td>导出附件：</td>
                <td><input type="checkbox" name="includeUpload" value="true" /></td>
			</tr>
		</table>
		<div class="space"></div>
		<input type="submit" value="导出数据" onclick='return exportToJson(form);' />
	</fieldset>

	<fieldset>
		<legend>导入软件数据</legend>
		<table>
			<tr>
				<td>软件应用：</td>
				<td><select id="" name="importDataToSoft">
						<option value="">---选择平台应用软件---</option> {import file="../common/options.st" nodes=$softNodes.children}
				</select></td>
				<td>数据文件：</td>
				<td>{import file="../common/upload.st" id="importDataFromZip" name="importDataFromZip" value=""}</td>
			</tr>
		</table>
		<div class="space"></div>
		<input type="submit" class="ajaxSubmit" value="导入数据" onclick='this.form.action="{$contextPath}/config/importFromJson"' />
	</fieldset>
</form>

<script type="text/javascript">
var exportToJson=function(form){
	var url="{$contextPath}/config/exportToJson?soft="+form.elements["exportDataFromSoft"].value+"&date="+form.elements["exportDataFromDate"].value+"&includeUpload="+form.elements["includeUpload"].checked;
	window.open(url, "_blank").focus();
}
$(function(){
	$("#patchFileFile").uploadify({
		'script' : '{$uploadUrl}?folder=patch',
		'uploader' : '{$stylePath}/uploadify/uploadify.swf',
		'cancelImg' : '{$stylePath}/uploadify/cancel.png',
		'auto' : true,
		'fileExt': '*.zip',
		'fileDesc': '*.zip',
		'queueID': 'patchFileFileProgress',
		'fileDataName': 'upload',
		'multi': false,
		'queueSizeLimit': 1,
		height: 14,
		width: 16,
		'sizeLimit': '512000000',
	    'buttonImg': '{$stylePath}/uploadify/uploadify.gif',
	    'simUploadLimit': 1,
		'onComplete': function (event, queueID, fileObj, response, data) {
			var json=window["eval"]("(" + response + ")");
			if(json.success){
	        	$("#patchFile").val(json.fileUrl);
	    	}else{
	        	alert(json.customMsg);
	    	}
	    	return true;
	    },
	    'onError': function(event, queueID, fileObj,errorObj) {
	    	alert(errorObj.type + ' Error: ' + errorObj.info);
	    	return true;
	    },
	    'onCancel':function(event, queueID, fileObj){
	    	return true;
	    },
	    wmode : "transparent"
	});

	$("#importDataFromZipFile").uploadify({
		'script' : '{$uploadUrl}?folder=patch',
		'uploader' : '{$stylePath}/uploadify/uploadify.swf',
		'cancelImg' : '{$stylePath}/uploadify/cancel.png',
		'auto' : true,
		'fileExt': '*.zip',
		'fileDesc': '*.zip',
		'queueID': 'importDataFromZipFileProgress',
		'fileDataName': 'upload',
		'multi': false,
		'queueSizeLimit': 1,
		height: 14,
		width: 16,
		'sizeLimit': '512000000',
	    'buttonImg': '{$stylePath}/uploadify/uploadify.gif',
	    'simUploadLimit': 1,
		'onComplete': function (event, queueID, fileObj, response, data) {
			var json=window["eval"]("(" + response + ")");
			if(json.success){
	        	$("#importDataFromZip").val(json.fileUrl);
	    	}else{
	        	alert(json.customMsg);
	    	}
	    	return true;
	    },
	    'onError': function(event, queueID, fileObj,errorObj) {
	    	alert(errorObj.type + ' Error: ' + errorObj.info);
	    	return true;
	    },
	    'onCancel':function(event, queueID, fileObj){
	    	return true;
	    },
	    wmode : "transparent"
	});
});
</script>
