{assign var="designerPath" value="/scripts2/cssDesigner"}
<link rel="stylesheet" type="text/css" href="{$designerPath}/cssDesigner.css">
<SCRIPT language="JavaScript" type="text/javascript" src="{$designerPath}/cssDesigner.js"></SCRIPT>
<SCRIPT language="JavaScript" type="text/javascript">
QrXPCOM.designerPath = "{$designerPath}";
</SCRIPT>
<table class="QrCSSDesignerPad">
	{assign var="borderWidthItems" value=",1px,2px,3px,4px,5px,6px,7px,8px" delimiter=","}
	{assign var="borderStyleItems" value="solid,double,groove,ridge,inset,outset,dashed,dotted" delimiter=","}
	<tr>
		<td><img id="border#switch" src="{$designerPath}/img/arrowclose.gif" onClick="collapseSwitch('border','border-top','border-left','border-right','border-bottom');"></td>
		<td class="QrCSSDesignerPadLeft">边框：</td>
		<td><span id="border">
			{import file="cssDesigner/pulldown.st" name="borderWidth" style="border-width" value="" size="4" options=$borderWidthItems}
			{import file="cssDesigner/pulldown.st" name="borderStyle" style="border-style" value="" size="4" options=$borderStyleItems}
			{import file="cssDesigner/color.st" name="borderColor" style="border-color" value="transparent" size="2"}
			</span>
		</td>
	</tr>
                <tr id="border-top" style="display: none;">
                    <td></td>
                    <td class="QrCSSDesignerPadLeft">上边框：</td>
                    <td>
			{import file="cssDesigner/pulldown.st" name="borderTopWidth" style="border-top-width" value="" size="4" options=$borderWidthItems}
			{import file="cssDesigner/pulldown.st" name="borderTopStyle" style="border-top-style" value="" size="4" options=$borderStyleItems}
			{import file="cssDesigner/color.st" name="borderTopColor" style="border-top-color" value="transparent" size="4"}
                    </td>
                </tr>
                <tr id="border-left" style="display: none;">
                    <td></td>
                    <td class="QrCSSDesignerPadLeft">左边框：</td>
                    <td>
			{import file="cssDesigner/pulldown.st" name="borderLeftWidth" style="border-left-width" value="" size="4" options=$borderWidthItems}
			{import file="cssDesigner/pulldown.st" name="borderLeftStyle" style="border-left-style" value="" size="4" options=$borderStyleItems}
			{import file="cssDesigner/color.st" name="borderLeftColor" style="border-left-color" value="transparent" size="4"}
                    </td>
                </tr>
                <tr id="border-right" style="display: none;">
                    <td></td>
                    <td class="QrCSSDesignerPadLeft">右边框：</td>
                    <td>
			{import file="cssDesigner/pulldown.st" name="borderRightWidth" style="border-right-width" value="" size="4" options=$borderWidthItems}
			{import file="cssDesigner/pulldown.st" name="borderRightStyle" style="border-right-style" value="" size="4" options=$borderStyleItems}
			{import file="cssDesigner/color.st" name="borderRightColor" style="border-right-color" value="transparent" size="4"}
                    </td>
                </tr>
                <tr id="border-bottom" style="display: none;">
                    <td></td>
                    <td class="QrCSSDesignerPadLeft">底边框：</td>
                    <td>
			{import file="cssDesigner/pulldown.st" name="borderBottomWidth" style="border-bottom-width" value="" size="4" options=$borderWidthItems}
			{import file="cssDesigner/pulldown.st" name="borderBottomStyle" style="border-bottom-style" value="" size="4" options=$borderStyleItems}
			{import file="cssDesigner/color.st" name="borderBottomColor" style="borde-bottomr-color" value="transparent" size="4"}
                    </td>
                </tr>
                <tr>
                    <td><img src="{$designerPath}/img/arrowclose.gif" id="padding#switch" onClick="collapseSwitch('padding','padding-top','padding-left','padding-right','padding-bottom');"></td>
                    <td class="QrCSSDesignerPadLeft">内边距：</td>
                    <td><span id="padding">
                    {import file="cssDesigner/pulldown.st" name="padding" style="padding" value="" size="4" options=$borderWidthItems}
                    </span></td>
                </tr>
                <tr id="padding-top" style="display: none;">
                    <td></td>
                    <td class="QrCSSDesignerPadLeft">顶内边距：</td>
                    <td>
					{import file="cssDesigner/pulldown.st" name="paddingTop" style="padding-top" value="" size="4" options=$borderWidthItems}
                    </td>
                </tr>
                <tr id="padding-left" style="display: none;">
                    <td></td>
                    <td class="QrCSSDesignerPadLeft">左内边距：</td>
                    <td>
					{import file="cssDesigner/pulldown.st" name="paddingLeft" style="padding-left" value="" size="4" options=$borderWidthItems}
                    </td>
                </tr>
                <tr id="padding-right" style="display: none;">
                    <td></td>
                    <td class="QrCSSDesignerPadLeft">右内边距：</td>
                    <td>
					{import file="cssDesigner/pulldown.st" name="paddingRight" style="padding-right" value="" size="4" options=$borderWidthItems}
                    </td>
                </tr>
                <tr id="padding-bottom" style="display: none;">
                    <td></td>
                    <td class="QrCSSDesignerPadLeft">底内边距：</td>
                    <td>
					{import file="cssDesigner/pulldown.st" name="paddingBottom" style="padding-bottom" value="" size="4" options=$borderWidthItems}
                    </td>
                </tr>
                <tr>
                    <td><img src="{$designerPath}/img/arrowclose.gif" id="margin#switch" onClick="collapseSwitch('margin','margin-top','margin-left','margin-right','margin-bottom');"></td>
                    <td class="QrCSSDesignerPadLeft">外边距：</td>
                    <td><span id="margin">
                    {import file="cssDesigner/pulldown.st" name="margin" style="margin" value="" size="4" options=$borderWidthItems}
                    </span></td>
                </tr>
                <tr id="margin-top" style="display: none;">
                    <td></td>
                    <td class="QrCSSDesignerPadLeft">顶外边距：</td>
                    <td>
					{import file="cssDesigner/pulldown.st" name="marginTop" style="margin-top" value="" size="4" options=$borderWidthItems}
                    </td>
                </tr>
                <tr id="margin-left" style="display: none;">
                    <td></td>
                    <td class="QrCSSDesignerPadLeft">左外边距：</td>
                    <td>
					{import file="cssDesigner/pulldown.st" name="marginLeft" style="margin-left" value="" size="4" options=$borderWidthItems}
                    </td>
                </tr>
                <tr id="margin-right" style="display: none;">
                    <td></td>
                    <td class="QrCSSDesignerPadLeft">右外边距：</td>
                    <td>
					{import file="cssDesigner/pulldown.st" name="marginRight" style="margin-right" value="" size="4" options=$borderWidthItems}
                    </td>
                </tr>
                <tr id="margin-bottom" style="display: none;">
                    <td></td>
                    <td class="QrCSSDesignerPadLeft">底外边距：</td>
                    <td>
					{import file="cssDesigner/pulldown.st" name="marginBottom" style="margin-bottom" value="" size="4" options=$borderWidthItems}
                    </td>
                </tr>
</table>
