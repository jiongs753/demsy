<html>
<head>
<link rel="icon" href="" type="image/x-icon" />
<link rel="shortcut icon" href="" type="image/x-icon" />
<title>{$title}</title>
</head>
<base target="_parent">
<frameset id="page" name="page" cols="2,*,2" framespacing='0' marginwidth='0' marginheight='0' frameborder='0' border='0' scrolling='no'>
	<frame id="left0" name="left0" src="#" framespacing='0' marginwidth='0' marginheight='0' frameborder='0' border='0' scrolling='no' />
	<frameset id="index" name="index" rows="{$topHeight},*,2" framespacing='0' marginwidth='0' marginheight='0' frameborder='0' border='0' scrolling='no'>
		<frame id="top" name="top" src="{$topUrl}" framespacing='0' marginwidth='0' marginheight='0' frameborder='0' border='0' scrolling='no' />
		<frameset id="middle" name="middle" id="middle" cols="{$leftWidth},5,*" framespacing='0' marginwidth='0' marginheight='0' frameborder='0' border='0' scrolling='no'>
			<frame id="left" name="left" src="{$leftUrl}" framespacing='0' marginwidth='0' marginheight='0' frameborder='0' border='0' scrolling='no' />
			<frame src="#" framespacing='0' marginwidth='0' marginheight='0' frameborder='0' border='0' scrolling='no' />
			<frame id="body" name="body" src="{$rightUrl}" framespacing='0' marginwidth='0' marginheight='0' frameborder='0' border='0' scrolling='auto' />
		</frameset>
		<frame name="bottom" src="#" framespacing='0' marginwidth='0' marginheight='0' frameborder='0' border='0' scrolling='no' />
	</frameset>
	<frame name="right0" src="#" framespacing='0' marginwidth='0' marginheight='0' frameborder='0' border='0' scrolling='no' />
</frameset>
<noframes>
	<body>
		<p>此网页使用了框架，但您的浏览器不支持框架。</p>
	</body>
</noframes>

</html>
