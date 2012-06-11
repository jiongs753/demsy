<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
</head>
<body>
	{if $redirect != null}
	<script type="text/javascript">
alert({tojson bean=$info});
if(window.parent){
  window.parent.location.href= '{$redirect}';
}else{
  window.location.href='{$redirect}';
}
</script>
	{else}
	<div style="margin: 5px; font-size: 14px; color: red;">{tohtml bean=$info}</div>
	{/if}
</body>
</html>
