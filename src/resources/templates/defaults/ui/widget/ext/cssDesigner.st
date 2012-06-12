{prop bean=$data name=$ui.propName pattern=$ui.pattern assign="value"}
{assign var="mode" value=$ui.mode}

{assign var="scriptPath" value="/scripts2"}
{assign var="stylePath" value="/themes2/defaults"}
{assign var="designerPath" value="/scripts2/cssDesigner"}
<link rel="stylesheet" type="text/css" href="{$designerPath}/cssDesigner.css">
<SCRIPT language="JavaScript" type="text/javascript" src="{$designerPath}/cssDesigner.js"></SCRIPT>
<script src="{$scriptPath}/demsy.uploadify.js" type="text/javascript"></script>
<script src="{$scriptPath}/swfobject.js" type="text/javascript"></script>
<link href="{$stylePath}/jquery.uploadify.css" rel="stylesheet" type="text/css" media="screen" />
<script src="{$scriptPath}/demsy.colorpicker.js" type="text/javascript"></script>
<link href="{$stylePath}/demsy.colorpicker.css" rel="stylesheet" type="text/css" media="screen" />

<SCRIPT language="JavaScript" type="text/javascript">
QrXPCOM.designerPath = "{$designerPath}";
</SCRIPT>

{if $mode == "H"}
<textarea id="{$ui.uiid}" name="{$ui.name}" style="display: none;">{tostring bean=$value}</textarea>
{else}
{prop bean=$data name=$ui.propName assign="subSystemObj"}
{if $mode == "M" || $mode == "E"}
<textarea id="{$ui.uiid}" name="{$ui.name}" style="display: none;">{tostring bean=$value}</textarea>
{/if}
<table>
    <tr>
        <td class="bzsubsys_container"><table id="bzsubsys_{$ui.uiid}" class="bzsubsys">
            <tr>
                <th class="bzsubfld_head" style="width:78px;">CSS名称</th>
                <th class="bzsubfld_head" style="width:200px;">CSS内容</th>
                <th class="bzsubfld_head_check">
                    {if $mode == "M" || $mode == "E"}
                     <a href="#" class="op_btn_del" title="删除" alt="删除" style="font-size: 17px;">×</a>
                     <a href="#" class="op_btn_add" title="添加" alt="添加">+</a>
                    {/if}
                </th>
            </tr>
            {foreach from=$subSystemObj.list item="subData" key="row"}
            <tr class="bzsubsys_item">
                <td class="bzsubfld_data" valign="top"><input type="text" style="width:74px;height: 20px;" class="input" name="code" value="{tostring bean=$subData.value}" /></td>
                <td class="bzsubfld_data" valign="top"><textarea name="desc" style="width:196px;height: 20px;" class="textarea">{tostring bean=$subData.value}</textarea></td>
                <td class="bzsubfld_data_check" valign="top">
                    <input type="checkbox" class="check" />
                    <input name="id" type="hidden" value="{tostring bean=$subData.id}"/>
                    {if $ui.fake}{else}<input name="statusForJsonData" type="hidden" class="statusForJsonData"/>{/if}
                </td>
            </tr>{/foreach}
        </table>
        </td>
    </tr>
</table>
{if $mode == "M" || $mode == "E"}
<table id="bzsubsys_tpl_{$ui.uiid}" style="display:none;">
    <tr class="bzsubsys_item">
        <td class="bzsubfld_data" valign="top"><input type="text" style="width:74px;height: 20px;" class="input" name="code" value="" /></td>
        <td class="bzsubfld_data" valign="top"><textarea name="desc" style="width:196px;height: 20px;" class="textarea"></textarea></td>
        <td class="bzsubfld_data_check" valign="top">
            <input type="checkbox" class="check" />
            <input name="id" type="hidden" />
            {if $ui.fake}{else}<input name="statusForJsonData" type="hidden" class="statusForJsonData"/>{/if}
        </td>
    </tr>
</table>
{/if}
        
<table class="CssDesignerPad" style="display:none;margin-top:5px;">
	<tr>
		<td><img src="{$designerPath}/img/arrowclose.gif" onClick="CssDesigner.switchCSS(event,'.tr_border');" /></td>
		<td class="QrCSSDesignerPadLeft" style="text-align: left;">边框</td>
		<td>
			{import file="cssDesigner/spinner.st" name="borderWidth" style="border-width" value="" uom="px"}</td>
        <td>
            {assign var="borderStyleOptions" value=",solid,double,groove,ridge,inset,outset,dashed,dotted" delimiter=","}
			{import file="cssDesigner/pulldown.st" name="borderStyle" style="border-style" value="" cssStyle="width:40px;" options=$borderStyleOptions}</td>
        <td>
			{import file="cssDesigner/color.st" name="borderColor" style="border-color" value=""}
		</td>
	</tr>
                <tr class="tr_border" style="display: none;">
                    <td></td>
                    <td class="QrCSSDesignerPadLeft">边框(上)：</td>
                    <td>
        			{import file="cssDesigner/spinner.st" name="borderTopWidth" style="border-top-width" value="" uom="px"}</td>
                    <td>
        			{import file="cssDesigner/pulldown.st" name="borderTopStyle" style="border-top-style" cssStyle="width:40px;" value="" options=$borderStyleOptions}</td>
                    <td>
        			{import file="cssDesigner/color.st" name="borderTopColor" style="border-top-color" value=""}
                    </td>
                </tr>
                <tr class="tr_border" style="display: none;">
                    <td></td>
                    <td class="QrCSSDesignerPadLeft">边框(下)：</td>
                    <td>
                    {import file="cssDesigner/spinner.st" name="borderBottomWidth" style="border-bottom-width" value="" uom="px"}</td>
                    <td>
                    {import file="cssDesigner/pulldown.st" name="borderBottomStyle" style="border-bottom-style" cssStyle="width:40px;" value="" options=$borderStyleOptions}</td>
                    <td>
                    {import file="cssDesigner/color.st" name="borderBottomColor" style="borde-bottomr-color" value=""}
                    </td>
                </tr>
                <tr class="tr_border" style="display: none;">
                    <td></td>
                    <td class="QrCSSDesignerPadLeft">边框(左)：</td>
                    <td>
        			{import file="cssDesigner/spinner.st" name="borderLeftWidth" style="border-left-width" value="" uom="px"}</td>
                    <td>
        			{import file="cssDesigner/pulldown.st" name="borderLeftStyle" style="border-left-style" cssStyle="width:40px;" value="" options=$borderStyleOptions}</td>
                    <td>
        			{import file="cssDesigner/color.st" name="borderLeftColor" style="border-left-color" value=""}
                    </td>
                </tr>
                <tr class="tr_border" style="display: none;">
                    <td></td>
                    <td class="QrCSSDesignerPadLeft">边框(右)：</td>
                    <td>
        			{import file="cssDesigner/spinner.st" name="borderRightWidth" style="border-right-width" value="" uom="px"}</td>
                    <td>
        			{import file="cssDesigner/pulldown.st" name="borderRightStyle" style="border-right-style" cssStyle="width:40px;" value="" options=$borderStyleOptions}</td>
                    <td>
        			{import file="cssDesigner/color.st" name="borderRightColor" style="border-right-color" value=""}
                    </td>
                </tr>
	<tr>
    	<td><img src="{$designerPath}/img/arrowclose.gif" onClick="CssDesigner.switchCSS(event,'.tr_padding');"></td>
        <td class="QrCSSDesignerPadLeft" style="text-align: left;">内边距</td>
        <td colspan=3><span>
        {import file="cssDesigner/spinner.st" name="padding" style="padding" value="" uom="px"}
        </span></td>
	</tr>
                <tr class="tr_padding" style="display: none;">
                    <td></td>
                    <td class="QrCSSDesignerPadLeft">内边距(上)：</td>
                    <td colspan=3>
					{import file="cssDesigner/spinner.st" name="paddingTop" style="padding-top" value="" uom="px"}
                    </td>
                </tr>
                <tr class="tr_padding" style="display: none;">
                    <td></td>
                    <td class="QrCSSDesignerPadLeft">内边距(下)：</td>
                    <td colspan=3>
					{import file="cssDesigner/spinner.st" name="paddingBottom" style="padding-bottom" value="" uom="px"}
                    </td>
                </tr>
                <tr class="tr_padding" style="display: none;">
                    <td></td>
                    <td class="QrCSSDesignerPadLeft">内边距(左)：</td>
                    <td colspan=3>
                    {import file="cssDesigner/spinner.st" name="paddingLeft" style="padding-left" value="" uom="px"}
                    </td>
                </tr>
                <tr class="tr_padding" style="display: none;">
                    <td></td>
                    <td class="QrCSSDesignerPadLeft">内边距(右)：</td>
                    <td colspan=3>
                    {import file="cssDesigner/spinner.st" name="paddingRight" style="padding-right" value="" uom="px"}
                    </td>
                </tr>
	<tr>
    	<td><img src="{$designerPath}/img/arrowclose.gif" onClick="CssDesigner.switchCSS(event,'.tr_margin');"></td>
        <td class="QrCSSDesignerPadLeft" style="text-align: left;">外边距</td>
        <td colspan=3><span>
        {import file="cssDesigner/spinner.st" name="margin" style="margin" value="" uom="px"}
        </span></td>
	</tr>
                <tr class="tr_margin" style="display: none;">
                    <td></td>
                    <td class="QrCSSDesignerPadLeft">外边距(上)：</td>
                    <td colspan=3>
					{import file="cssDesigner/spinner.st" name="marginTop" style="margin-top" value="" uom="px"}
                    </td>
                </tr>
                <tr class="tr_margin" style="display: none;">
                    <td></td>
                    <td class="QrCSSDesignerPadLeft">外边距(下)：</td>
                    <td colspan=3>
					{import file="cssDesigner/spinner.st" name="marginBottom" style="margin-bottom" value="" uom="px"}
                    </td>
                </tr>
                <tr class="tr_margin" style="display: none;">
                    <td></td>
                    <td class="QrCSSDesignerPadLeft">外边距(左)：</td>
                    <td colspan=3>
                    {import file="cssDesigner/spinner.st" name="marginLeft" style="margin-left" value="" uom="px"}
                    </td>
                </tr>
                <tr class="tr_margin" style="display: none;">
                    <td></td>
                    <td class="QrCSSDesignerPadLeft">外边距(右)：</td>
                    <td>
                    {import file="cssDesigner/spinner.st" name="marginRight" style="margin-right" value="" uom="px"}
                    </td>
                </tr>
	<tr>
		<td><img src="{$designerPath}/img/arrowopen.gif" onClick="CssDesigner.switchCSS(event,'.tr_font');"></td>
		<td class="QrCSSDesignerPadLeft" colspan=4 style="text-align: left;">字体</td>
	</tr>
                <tr class="tr_font">
                    <td></td>
                    <td class="QrCSSDesignerPadLeft">字体样式：</td>
                    <td colspan=3>
                    {assign var="fontStyleOptions" value=",normal,italic,oblique" delimiter=","}
                    {import file="cssDesigner/pulldown.st" name="fontStyle" style="font-style" value="" options=$fontStyleOptions}
                    </td>
                </tr>
                <tr class="tr_font">
                    <td></td>
                    <td class="QrCSSDesignerPadLeft">字体变量：</td>
                    <td colspan=3>
                    {assign var="fontVariantOptions" value=",normal,small-caps" delimiter=","}
                    {import file="cssDesigner/pulldown.st" name="fontVariant" style="font-variant" value="" options=$fontVariantOptions}
                    </td>
                </tr>
                <tr class="tr_font">
                    <td></td>
                    <td class="QrCSSDesignerPadLeft">字体粗细：</td>
                    <td colspan=3>
                    {assign var="fontWeightOptions" value=",normal,bold" delimiter=","}
                    {import file="cssDesigner/pulldown.st" name="fontWeight" style="font-weight" value="" options=$fontWeightOptions}
                    </td>
                </tr>
                <tr class="tr_font">
                    <td></td>
                    <td class="QrCSSDesignerPadLeft">字体大小：</td>
                    <td colspan=3>
                    {import file="cssDesigner/spinner.st" name="fontSize" style="font-size" value="" uom="px"}
                    </td>
                </tr>
                <tr class="tr_font">
                    <td></td>
                    <td class="QrCSSDesignerPadLeft">字体系列：</td>
                    <td colspan=3>
                    {assign var="fontFamilyOptions" value=",宋体,黑体,微软雅黑,微软正黑体,新宋体,新细明体,细明体,仿宋,楷体,仿宋_GB2312,楷体_GB2312,Arial,Arial Black,Arial Narrow,Brush Script MT,Century Gothic,Comic Sans MS,Courier,Courier New,Tahoma,Times New Roman,Verdana" delimiter=","}
			        {import file="cssDesigner/pulldown.st" name="fontFamily" style="font-family" value="" options=$fontFamilyOptions}
                    </td>
                </tr>
	<tr>
		<td><img src="{$designerPath}/img/arrowopen.gif" onClick="CssDesigner.switchCSS(event,'.tr_text');"></td>
		<td class="QrCSSDesignerPadLeft" colspan=4 style="text-align: left;">文本</td>
	</tr>
                <tr class="tr_text">
                    <td></td>
                    <td class="QrCSSDesignerPadLeft">文本颜色：</td>
                    <td colspan=3>
                    {import file="cssDesigner/color.st" name="color" style="color" value=""}
                    </td>
                </tr>
                <tr class="tr_text">
                    <td></td>
                    <td class="QrCSSDesignerPadLeft">水平对齐：</td>
                    <td colspan=3>
                    {assign var="textAlignOptions" value=",left,right,center,justify" delimiter=","}
                    {import file="cssDesigner/pulldown.st" name="textAlign" style="text-align" value="" options=$textAlignOptions}
                    </td>
                </tr>
                <tr class="tr_text">
                    <td></td>
                    <td class="QrCSSDesignerPadLeft">垂直对齐：</td>
                    <td colspan=3>
                    {assign var="verticalAlignOptions" value=",baseline,top,middle,bottom,text-top,text-bottom" delimiter=","}
                    {import file="cssDesigner/pulldown.st" name="verticalAlign" style="vertical-align" value="" options=$verticalAlignOptions}
                    </td>
                </tr>
                <tr class="tr_text">
                    <td></td>
                    <td class="QrCSSDesignerPadLeft">文本修饰：</td>
                    <td colspan=3>
                    {assign var="textDecorationOptions" value=",underline,overline,underline,line-through,blink" delimiter=","}
                    {import file="cssDesigner/pulldown.st" name="textDecoration" style="text-decoration" value="" options=$textDecorationOptions}
                    </td>
                </tr>
                <tr class="tr_text">
                    <td></td>
                    <td class="QrCSSDesignerPadLeft">书写方向：</td>
                    <td colspan=3>
                    {assign var="directionOptions" value=",ltr,rtl" delimiter=","}
                    {import file="cssDesigner/pulldown.st" name="direction" style="direction" value="" options=$directionOptions}
                    </td>
                </tr>
                <tr class="tr_text">
                    <td></td>
                    <td class="QrCSSDesignerPadLeft">首行缩进：</td>
                    <td colspan=3>
                    {import file="cssDesigner/spinner.st" name="textIndent" style="text-indent" value="" uom="em"}
                    </td>
                </tr>
                <tr class="tr_text">
                    <td></td>
                    <td class="QrCSSDesignerPadLeft">文本行高：</td>
                    <td colspan=3>
                    {import file="cssDesigner/spinner.st" name="lineHeight" style="line-height" value="" uom="px"}
                    </td>
                </tr>
                <tr class="tr_text">
                    <td></td>
                    <td class="QrCSSDesignerPadLeft">字母间距：</td>
                    <td colspan=3>
                    {import file="cssDesigner/spinner.st" name="letterSpacing" style="letter-spacing" value="" uom="px"}
                    </td>
                </tr>
                <tr class="tr_text">
                    <td></td>
                    <td class="QrCSSDesignerPadLeft">单词间距：</td>
                    <td colspan=3>
                    {import file="cssDesigner/spinner.st" name="wordSpacing" style="word-spacing" value="" uom="px"}
                    </td>
                </tr>
                <tr class="tr_text">
                    <td></td>
                    <td class="QrCSSDesignerPadLeft">文本转换：</td>
                    <td colspan=3>
                    {assign var="textTransformOptions" value=",capitalize,lowercase,uppercase" delimiter=","}
                    {import file="cssDesigner/pulldown.st" name="textTransform" style="text-transform" value="" options=$textTransformOptions}
                    </td>
                </tr>
                <tr class="tr_text">
                    <td></td>
                    <td class="QrCSSDesignerPadLeft">文本空白：</td>
                    <td colspan=3>
                    {assign var="whiteSpaceOptions" value=",normal,pre,nowrap,pre-wrap,pre-line" delimiter=","}
                    {import file="cssDesigner/pulldown.st" name="whiteSpace" style="white-space" value="" options=$whiteSpaceOptions}
                    </td>
                </tr>
	<tr>
		<td><img src="{$designerPath}/img/arrowopen.gif" onClick="CssDesigner.switchCSS(event,'.tr_bg');"></td>
		<td class="QrCSSDesignerPadLeft" colspan=4 style="text-align: left;">背景</td>
	</tr>
                <tr class="tr_bg">
                    <td></td>
                    <td class="QrCSSDesignerPadLeft">背景颜色：</td>
                    <td colspan=3>
                    {import file="cssDesigner/color.st" name="backgroundColor" style="background-color" value=""}
                    </td>
                </tr>
                <tr class="tr_bg">
                    <td></td>
                    <td class="QrCSSDesignerPadLeft">背景图片：</td>
                    <td colspan=3>
                    {import file="cssDesigner/upload.st" name="backgroundImage" style="background-image" value=""}
                    </td>
                </tr>
                <tr class="tr_bg">
                    <td></td>
                    <td class="QrCSSDesignerPadLeft">图片伸展：</td>
                    <td colspan=3>
                    {assign var="backgroundRepeatOptions" value=",repeat,repeat-x,repeat-y,no-repeat" delimiter=","}
                    {import file="cssDesigner/pulldown.st" name="backgroundRepeat" style="background-repeat" value="" options=$backgroundRepeatOptions}
                    </td>
                </tr>
                <tr class="tr_bg">
                    <td></td>
                    <td class="QrCSSDesignerPadLeft">图片定位：</td>
                    <td colspan=3>
                    {assign var="backgroundPositionOptions" value=",left,right,top,left top,right bottom,30% 50%,10px 20px" delimiter=","}
                    {import file="cssDesigner/pulldown.st" name="backgroundPosition" style="background-position" value="" options=$backgroundPositionOptions}
                    </td>
                </tr>
                <tr class="tr_bg">
                    <td></td>
                    <td class="QrCSSDesignerPadLeft">附加属性：</td>
                    <td colspan=3>
                    {assign var="backgroundAttachmentOptions" value=",fixed,scroll" delimiter=","}
                    {import file="cssDesigner/pulldown.st" name="backgroundAttachment" style="background-attachment" value="" options=$backgroundAttachmentOptions}
                    </td>
                </tr>
	<tr>
		<td><img src="{$designerPath}/img/arrowopen.gif" onClick="CssDesigner.switchCSS(event,'.tr_table');"></td>
		<td class="QrCSSDesignerPadLeft" colspan=4 style="text-align: left;">表格</td>
	</tr>
                <tr class="tr_table">
                    <td></td>
                    <td class="QrCSSDesignerPadLeft">边框折叠：</td>
                    <td colspan=3>
                    {assign var="borderCollapseOptions" value=",collapse" delimiter=","}
                    {import file="cssDesigner/pulldown.st" name="borderCollapse" style="border-collapse" value="" options=$borderCollapseOptions}
                    </td>
                </tr>
                <tr class="tr_table">
                    <td></td>
                    <td class="QrCSSDesignerPadLeft">边框间距：</td>
                    <td colspan=3>
                    {import file="cssDesigner/spinner.st" name="borderSpacing" style="border-spacing" value="" uom="px"}
                    </td>
                </tr>
                <tr class="tr_table">
                    <td></td>
                    <td class="QrCSSDesignerPadLeft">标题位置：</td>
                    <td colspan=3>
                    {assign var="captionSideOptions" value=",top,bottom,left,right" delimiter=","}
                    {import file="cssDesigner/pulldown.st" name="captionSide" style="caption-side" value="" options=$captionSideOptions}
                    </td>
                </tr>
                <tr class="tr_table">
                    <td></td>
                    <td class="QrCSSDesignerPadLeft">空单元格：</td>
                    <td colspan=3>
                    {assign var="emptyCellsOptions" value=",hide,show" delimiter=","}
                    {import file="cssDesigner/pulldown.st" name="emptyCells" style="empty-cells" value="" options=$emptyCellsOptions}
                    </td>
                </tr>
                <tr class="tr_table">
                    <td></td>
                    <td class="QrCSSDesignerPadLeft">表格布局：</td>
                    <td colspan=3>
                    {assign var="tableLayoutOptions" value=",auto,fixed" delimiter=","}
                    {import file="cssDesigner/pulldown.st" name="tableLayout" style="table-layout" value="" options=$tableLayoutOptions}
                    </td>
                </tr>
	<tr>
		<td><img src="{$designerPath}/img/arrowopen.gif" onClick="CssDesigner.switchCSS(event,'.tr_list');"></td>
		<td class="QrCSSDesignerPadLeft" colspan=4 style="text-align: left;">列表</td>
	</tr>
                <tr class="tr_list">
                    <td></td>
                    <td class="QrCSSDesignerPadLeft">列表样式：</td>
                    <td colspan=3>
                    {assign var="listStyleTypeOptions" value=",none,disc,circle,square,decimal,lower-roman,upper-roman,lower-alpha,upper-alpha" delimiter=","}
                    {import file="cssDesigner/pulldown.st" name="listStyleType" style="list-style-type" value="" options=$listStyleTypeOptions}
                    </td>
                </tr>
                <tr class="tr_list">
                    <td></td>
                    <td class="QrCSSDesignerPadLeft">标记位置：</td>
                    <td colspan=3>
                    {assign var="listStylePositionOptions" value=",inside,outside" delimiter=","}
                    {import file="cssDesigner/pulldown.st" name="listStylePosition" style="list-style-position" value="" options=$listStylePositionOptions}
                    </td>
                </tr>
                <tr class="tr_list">
                    <td></td>
                    <td class="QrCSSDesignerPadLeft">标记图片：</td>
                    <td colspan=3>
                    {import file="cssDesigner/upload.st" name="listStyleImage" style="list-style-image" value=""}
                    </td>
                </tr>
    <tr>
		<td><img src="{$designerPath}/img/arrowopen.gif" onClick="CssDesigner.switchCSS(event,'.tr_pos');"></td>
        <td class="QrCSSDesignerPadLeft" colspan=4 style="text-align: left;">定位</td>
    </tr>
                <tr class="tr_pos">
                    <td></td>
                    <td class="QrCSSDesignerPadLeft">资源定位：</td>
                    <td colspan=3>
                    {assign var="positionOptions" value=",static,relative,absolute,fixed" delimiter=","}
                    {import file="cssDesigner/pulldown.st" name="position" style="position" value="" options=$positionOptions}
                    </td>
                </tr>
                <tr class="tr_pos">
                    <td></td>
                    <td class="QrCSSDesignerPadLeft">优先等级：</td>
                    <td colspan=3>
                    {import file="cssDesigner/spinner.st" name="zIndex" style="z-index" value=""}
                    </td>
                </tr>
                <tr class="tr_pos">
                    <td></td>
                    <td class="QrCSSDesignerPadLeft">距离左边：</td>
                    <td colspan=3>
                    {import file="cssDesigner/spinner.st" name="left" style="left" value="" uom="px"}
                    </td>
                </tr>
                <tr class="tr_pos">
                    <td></td>
                    <td class="QrCSSDesignerPadLeft">距离顶部：</td>
                    <td colspan=3>
                    {import file="cssDesigner/spinner.st" name="top" style="top" value="" uom="px"}
                    </td>
                </tr>
                <tr class="tr_pos">
                    <td></td>
                    <td class="QrCSSDesignerPadLeft">距离右边：</td>
                    <td colspan=3>
                    {import file="cssDesigner/spinner.st" name="right" style="right" value="" uom="px"}
                    </td>
                </tr>
                <tr class="tr_pos">
                    <td></td>
                    <td class="QrCSSDesignerPadLeft">距离底部：</td>
                    <td colspan=3>
                    {import file="cssDesigner/spinner.st" name="bottom" style="bottom" value="" uom="px"}
                    </td>
                </tr>
    <tr>
		<td><img src="{$designerPath}/img/arrowopen.gif" onClick="CssDesigner.switchCSS(event,'.tr_lay');"></td>
        <td class="QrCSSDesignerPadLeft" colspan=4 style="text-align: left;">布局</td>
    </tr>
                <tr class="tr_lay">
                    <td></td>
                    <td class="QrCSSDesignerPadLeft">整体宽度：</td>
                    <td colspan=3>
                    {import file="cssDesigner/spinner.st" name="width" style="width" value="" uom="px"}
                    </td>
                </tr>
                <tr class="tr_lay">
                    <td></td>
                    <td class="QrCSSDesignerPadLeft">整体高度：</td>
                    <td colspan=3>
                    {import file="cssDesigner/spinner.st" name="height" style="height" value="" uom="px"}
                    </td>
                </tr>
                <tr class="tr_lay">
                    <td></td>
                    <td class="QrCSSDesignerPadLeft">溢出选项：</td>
                    <td colspan=3>
                    {assign var="overflowOptions" value=",visible,hidden,scroll,auto" delimiter=","}
                    {import file="cssDesigner/pulldown.st" name="overflow" style="overflow" value="" options=$overflowOptions}
                    </td>
                </tr>
                <tr class="tr_lay">
                    <td></td>
                    <td class="QrCSSDesignerPadLeft">显示属性：</td>
                    <td colspan=3>
                    {assign var="displayOptions" value=",none,block,inline" delimiter=","}
                    {import file="cssDesigner/pulldown.st" name="display" style="display" value="" options=$displayOptions}
                    </td>
                </tr>
                <tr class="tr_lay">
                    <td></td>
                    <td class="QrCSSDesignerPadLeft">可见属性：</td>
                    <td colspan=3>
                    {assign var="visibilityOptions" value=",visible,hidden" delimiter=","}
                    {import file="cssDesigner/pulldown.st" name="visibility" style="visibility" value="" options=$visibilityOptions}
                    </td>
                </tr>
                <tr class="tr_lay">
                    <td></td>
                    <td class="QrCSSDesignerPadLeft">浮动属性：</td>
                    <td colspan=3>
                    {assign var="floatOptions" value=",left,right,none" delimiter=","}
                    {import file="cssDesigner/pulldown.st" name="float" style="float" value="" options=$floatOptions}
                    </td>
                </tr>
                <tr class="tr_lay">
                    <td></td>
                    <td class="QrCSSDesignerPadLeft">清除属性：</td>
                    <td colspan=3>
                    {assign var="clearOptions" value=",left,right,both,none" delimiter=","}
                    {import file="cssDesigner/pulldown.st" name="clear" style="clear" value="" options=$clearOptions}
                    </td>
                </tr>
    <tr>
		<td><img src="{$designerPath}/img/arrowopen.gif" onClick="CssDesigner.switchCSS(event,'.tr_other');"></td>
        <td class="QrCSSDesignerPadLeft" colspan=4 style="text-align: left;">其他</td>
    </tr>
                <tr class="tr_other">
                    <td></td>
                    <td class="QrCSSDesignerPadLeft">鼠标指针：</td>
                    <td colspan=3>
                    {assign var="cursorOptions" value=",crosshair,default,hand,pointer,move,text,wait,help,n-resize,s-resize,w-resize,e-resize,ne-resize,nw-resize,se-resize,sw-resize" delimiter=","}
                    {import file="cssDesigner/pulldown.st" name="cursor" style="cursor" value="" options=$cursorOptions}
                    </td>
                </tr>
                <tr class="tr_other">
                    <td></td>
                    <td class="QrCSSDesignerPadLeft">滤镜效果：</td>
                    <td colspan=3>
                    {assign var="filterOptions" value=",Alpha,Blur,Chroma,DropShadow,FlipH,FlipV,Glow,Gray,invert,Mask,Shadow,wave,Xray" delimiter=","}
                    {import file="cssDesigner/pulldown.st" name="filter" style="filter" value="" options=$filterOptions}
                    </td>
                </tr>
    
</table>
<script type="text/javascript">
$(function(){
	var designer = new CssDesigner($(".CssDesignerPad"));
	var $bzsys = $("#bzsubsys_{$ui.uiid}");
	var selectItem = function(){
		var $item = $(this).parent().parent();
		var checked = true;
		if($(this).hasClass("check")){
			checked = this.checked;
		}
		$(".check", $bzsys).attr("checked", false);
		$(".check", $item).attr("checked", checked);
		
		//reset
		if(checked){
			var output = $(".textarea", $item);
			designer.output = output.get(0);
			designer.$pad.show();
		}else{
    		designer.output = null;
    		designer.$pad.hide();
		}
		designer.reset();
	}
	
    new SubModule($("#bzsubsys_{$ui.uiid}"),{
        field: $("#{$ui.uiid}"),
        itemTemplate: $("#bzsubsys_tpl_{$ui.uiid}"),
        onAddItem: function(self, $item, json){
            $(".check", $item).click(selectItem);
            $(".input", $item).click(selectItem);
            $(".textarea", $item).click(selectItem);
        },
        onDelItem: function(self, $item){
    		designer.output = null;
    		designer.reset();
    		designer.$pad.hide();
        }
    });
    
    $(".check", $bzsys).click(selectItem);
    $(".input", $bzsys).click(selectItem);
    $(".textarea", $bzsys).click(selectItem);
});
</script>
{/if}