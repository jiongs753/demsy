{assign var="designerPath" value="/scripts2/cssDesigner"}
<link rel="stylesheet" type="text/css" href="{$designerPath}/cssDesigner.css">
<SCRIPT language="JavaScript" type="text/javascript" src="{$designerPath}/cssDesigner.js"></SCRIPT>
<SCRIPT language="JavaScript" type="text/javascript">
QrXPCOM.designerPath = "{$designerPath}";
</SCRIPT>
<table border="0" cellpadding="1" cellspacing="1" class="QrCSSDesignerPad">
	<tr>
		<td colspan="3" align="center">边框
			<hr size="1" width="100%" color="#A5BAC6">
		</td>
	</tr>
	{assign var="borderWidthItems" value="px,pt,em,ex,pc,cm,mm,in,%" delimiter=","}
	{assign var="borderStyleItems" value="solid,double,groove,ridge,inset,outset,dashed,dotted" delimiter=","}
	<tr>
		<td><img id="border#switch" src="{$designerPath}/img/arrowclose.gif" onClick="collapseSwitch('border');"></td>
		<td class="QrCSSDesignerPadLeft">边框宽度：</td>
		<td><span id="border-width">
			{import file="cssDesigner/spinner.st" name="borderWidth" style="border-width" value="" size="1"}
			{import file="cssDesigner/pulldown.st" name="borderWidthUnit" value="" size="1" options=$borderWidthItems}
			{import file="cssDesigner/pulldown.st" name="borderStyle" style="border-style" value="" size="4" options=$borderStyleItems}
			{import file="cssDesigner/color.st" name="borderColor" style="border-color" value="transparent" size="4"}
			</span>
		</td>
	</tr>
                <tr id="border-top-width" style="display: none;">
                    <td></td>
                    <td class="QrCSSDesignerPadLeft">上边宽度：</td>
                    <td>
			{import file="cssDesigner/spinner.st" name="borderTopWidth" value=""}
			{import file="cssDesigner/pulldown.st" name="borderTopWidthUnit" value="" size="3" options=$borderWidthItems}
                    </td>
                </tr>
                <tr id="border-left-width" style="display: none;">
                    <td></td>
                    <td class="QrCSSDesignerPadLeft">左边宽度：</td>
                    <td>
			{import file="cssDesigner/spinner.st" name="borderLeftWidth" value=""}
			{import file="cssDesigner/pulldown.st" name="borderLeftWidthUnit" value="" size="3" options=$borderWidthItems}
                    </td>
                </tr>
                <tr id="border-right-width" style="display: none;">
                    <td></td>
                    <td class="QrCSSDesignerPadLeft">右边宽度：</td>
                    <td>
			{import file="cssDesigner/spinner.st" name="borderRightWidth" value=""}
			{import file="cssDesigner/pulldown.st" name="borderRightWidthUnit" value="" size="3" options=$borderWidthItems}
                    </td>
                </tr>
                <tr id="border-bottom-width" style="display: none;">
                    <td></td>
                    <td class="QrCSSDesignerPadLeft">底边宽度：</td>
                    <td>
			{import file="cssDesigner/spinner.st" name="borderBottomWidth" value=""}
			{import file="cssDesigner/pulldown.st" name="borderBottomWidthUnit" value="" size="3" options=$borderWidthItems}
                    </td>
                </tr>
</table>
