<SPAN class="QrComponent" id="{$name}" onclick="javascript:void(QrColorPicker.popupPicker('{$name}'));">
<img src="{$designerPath}/img/transparentpixel.gif" width="1" height="1" align="absmiddle" id="{$name}#color" style="background-color:{$value};"/>
<A href="javascript:void('QrColorPicker{$name}');"><SPAN id="{$name}#text">{$value}</SPAN></A>
</SPAN>
<DIV style="display:none; position:absolute; border:solid 1px gray;background-color:white;z-index:2;" id="{$name}#menu" 
onmouseout="javascript:QrColorPicker.restoreColor('{$name}');" 
onclick="javascript:QrXPCOM.onPopup();">
	<NOBR><IMG SRC="{$designerPath}/img/colorpicker.jpg" NATURALSIZEFLAG="3" BORDER="0" WIDTH="192" HEIGHT="128" ALIGN="BOTTOM"
	style="cursor:crosshair"
	onMouseMove="javascript:QrColorPicker.setColor(event,'{$name}');"
	onClick="javascript:QrColorPicker.selectColor(event,'{$name}');" /></NOBR>
	<BR>
	<NOBR><IMG SRC="{$designerPath}/img/graybar.jpg" NATURALSIZEFLAG="3" BORDER="0" WIDTH="192" HEIGHT="8" ALIGN="BOTTOM"
	style="cursor:crosshair"
	onMouseMove="javascript:QrColorPicker.setColor(event,'{$name}');"
	onClick="javascript:QrColorPicker.selectColor(event,'{$name}');"/>
	<IMG SRC="{$designerPath}/img/blank.jpg" NATURALSIZEFLAG="3" BORDER="0" WIDTH="16" HEIGHT="8" ALIGN="BOTTOM"></NOBR>
	<BR>
	<NOBR><input type="text" size="8" id="{$name}#input" value="{$value}" style="border:solid 1px gray;font-size:12pt;margin:1px;" 
	onkeyup="QrColorPicker.keyColor('{$name}')"/>
	<a href="javascript:QrColorPicker.transparent('{$name}');">
	<img src="{$designerPath}/img/icon_grid.gif" style="height:16px; width:16px;" align="absmiddle" border="0"/>transparent
	</a></NOBR>
</DIV>
<script>
connectCSS(new QrColorPicker("{$name}"), "{$style}");
</script>