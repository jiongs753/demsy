{assign var="scriptPath" value="/scripts2"} {assign var="stylePath" value="/themes2/defaults"} {assign var="designerPath" value="/scripts2/cssDesigner"} {assign var="tabsPath" value="/scripts2/tabs"}
<DIV class="tabs CssDesignerPad" style="display: none; margin-top: 5px;">
	<DIV class="tabsMenu">
		<UL>
			<LI><SPAN>边框背景</SPAN></LI>
			<LI><SPAN>字体文本</SPAN></LI>
			<LI><SPAN>表格列表</SPAN></LI>
			<LI><SPAN>定位布局</SPAN></LI>
			<LI><SPAN>其他</SPAN></LI>
		</UL>
	</DIV>
	<DIV class="tabsBox">
		<div class="tabsBoxItem">
			<table>
				<tr>
					<td><img src="{$designerPath}/img/arrowclose.gif" onClick="CssDesigner.switchCSS(event,'.tr_border');" /></td>
					<td class="QrCSSDesignerPadLeft" style="text-align: left;">边框样式</td>
					<td>{import file="spinner.st" name="borderWidth" style="border-width" value="" uom="px"}</td>
					<td>{assign var="borderStyleOptions" value=",solid,double,groove,ridge,inset,outset,dashed,dotted" delimiter=","} {import file="pulldown.st" name="borderStyle" style="border-style" value="" options=$borderStyleOptions}</td>
					<td>{import file="color.st" name="borderColor" style="border-color" value=""}</td>
				</tr>
				<tr class="tr_border" style="display: none;">
					<td></td>
					<td class="QrCSSDesignerPadLeft">上：</td>
					<td>{import file="spinner.st" name="borderTopWidth" style="border-top-width" value="" uom="px"}</td>
					<td>{import file="pulldown.st" name="borderTopStyle" style="border-top-style" value="" options=$borderStyleOptions}</td>
					<td>{import file="color.st" name="borderTopColor" style="border-top-color" value=""}</td>
				</tr>
				<tr class="tr_border" style="display: none;">
					<td></td>
					<td class="QrCSSDesignerPadLeft">下：</td>
					<td>{import file="spinner.st" name="borderBottomWidth" style="border-bottom-width" value="" uom="px"}</td>
					<td>{import file="pulldown.st" name="borderBottomStyle" style="border-bottom-style" value="" options=$borderStyleOptions}</td>
					<td>{import file="color.st" name="borderBottomColor" style="border-bottom-color" value=""}</td>
				</tr>
				<tr class="tr_border" style="display: none;">
					<td></td>
					<td class="QrCSSDesignerPadLeft">左：</td>
					<td>{import file="spinner.st" name="borderLeftWidth" style="border-left-width" value="" uom="px"}</td>
					<td>{import file="pulldown.st" name="borderLeftStyle" style="border-left-style" value="" options=$borderStyleOptions}</td>
					<td>{import file="color.st" name="borderLeftColor" style="border-left-color" value=""}</td>
				</tr>
				<tr class="tr_border" style="display: none;">
					<td></td>
					<td class="QrCSSDesignerPadLeft">右：</td>
					<td>{import file="spinner.st" name="borderRightWidth" style="border-right-width" value="" uom="px"}</td>
					<td>{import file="pulldown.st" name="borderRightStyle" style="border-right-style" value="" options=$borderStyleOptions}</td>
					<td>{import file="color.st" name="borderRightColor" style="border-right-color" value=""}</td>
				</tr>
			</table>
			<table>
				<tr>
					<td><img src="{$designerPath}/img/arrowclose.gif" onClick="CssDesigner.switchCSS(event,'.tr_padding');"></td>
					<td class="QrCSSDesignerPadLeft" style="text-align: left;">内侧边距</td>
					<td><span> {import file="spinner.st" name="padding" style="padding" value="" uom="px"} </span></td>
				</tr>
				<tr class="tr_padding" style="display: none;">
					<td></td>
					<td class="QrCSSDesignerPadLeft">上：</td>
					<td>{import file="spinner.st" name="paddingTop" style="padding-top" value="" uom="px"}</td>
					<td class="QrCSSDesignerPadLeft">下：</td>
					<td>{import file="spinner.st" name="paddingBottom" style="padding-bottom" value="" uom="px"}</td>
				</tr>
				<tr class="tr_padding" style="display: none;">
					<td></td>
					<td class="QrCSSDesignerPadLeft">左：</td>
					<td>{import file="spinner.st" name="paddingLeft" style="padding-left" value="" uom="px"}</td>
					<td class="QrCSSDesignerPadLeft">右：</td>
					<td>{import file="spinner.st" name="paddingRight" style="padding-right" value="" uom="px"}</td>
				</tr>
				<tr>
					<td><img src="{$designerPath}/img/arrowclose.gif" onClick="CssDesigner.switchCSS(event,'.tr_margin');"></td>
					<td class="QrCSSDesignerPadLeft" style="text-align: left;">外侧边距</td>
					<td><span> {import file="spinner.st" name="margin" style="margin" value="" uom="px"} </span></td>
				</tr>
				<tr class="tr_margin" style="display: none;">
					<td></td>
					<td class="QrCSSDesignerPadLeft">上：</td>
					<td>{import file="spinner.st" name="marginTop" style="margin-top" value="" uom="px"}</td>
					<td class="QrCSSDesignerPadLeft">下：</td>
					<td>{import file="spinner.st" name="marginBottom" style="margin-bottom" value="" uom="px"}</td>
				</tr>
				<tr class="tr_margin" style="display: none;">
					<td></td>
					<td class="QrCSSDesignerPadLeft">左：</td>
					<td>{import file="spinner.st" name="marginLeft" style="margin-left" value="" uom="px"}</td>
					<td class="QrCSSDesignerPadLeft">右：</td>
					<td>{import file="spinner.st" name="marginRight" style="margin-right" value="" uom="px"}</td>
				</tr>
				<tr>
					<td><img src="{$designerPath}/img/arrowopen.gif" onClick="CssDesigner.switchCSS(event,'.tr_bgimg');"></td>
					<td class="QrCSSDesignerPadLeft" style="text-align: left;">背景颜色</td>
					<td>{import file="color.st" name="backgroundColor" style="background-color" value=""}</td>
				</tr>
				<tr class="tr_bgimg">
					<td></td>
					<td class="QrCSSDesignerPadLeft">背景图片：</td>
					<td>{import file="upload.st" name="backgroundImage" style="background-image" value=""}</td>
					<td class="QrCSSDesignerPadLeft">图片伸展：</td>
					<td>{assign var="backgroundRepeatOptions" value=",repeat,repeat-x,repeat-y,no-repeat" delimiter=","} {import file="pulldown.st" name="backgroundRepeat" style="background-repeat" value="" options=$backgroundRepeatOptions}</td>
				</tr>
				<tr class="tr_bgimg">
					<td></td>
					<td class="QrCSSDesignerPadLeft">图片定位：</td>
					<td>{assign var="backgroundPositionOptions" value=",left,right,top,left top,right bottom,30% 50%,10px 20px" delimiter=","} {import file="pulldown.st" name="backgroundPosition" style="background-position" value="" options=$backgroundPositionOptions}</td>
					<td class="QrCSSDesignerPadLeft">附加属性：</td>
					<td>{assign var="backgroundAttachmentOptions" value=",fixed,scroll" delimiter=","} {import file="pulldown.st" name="backgroundAttachment" style="background-attachment" value="" options=$backgroundAttachmentOptions}</td>
				</tr>
                <tr>
                    <td></td>
                    <td class="QrCSSDesignerPadLeft">元素宽度：</td>
                    <td>{import file="spinner.st" name="width" style="width" value="" uom="px"}</td>
                    <td class="QrCSSDesignerPadLeft">元素高度：</td>
                    <td>{import file="spinner.st" name="height" style="height" value="" uom="px"}</td>
                </tr>
			</table>
			<hr size="1" width="100%" color="#A5BAC6">
			<span><img src="{$designerPath}/img/arrowclose.gif" onClick="CssDesigner.switchCSS(event,'#CssDesignerDemoContainer_Border');" /> CSS效果及说明</span>
			<div class="CssDesignerDemoContainer" id="CssDesignerDemoContainer_Border">
				<div class="CssDesignerDemo">
					<p>
						<strong>边框样式：</strong>元素的边框是围绕元素内容和内边距的一条或多条线，允许依次指定元素边框的宽度、样式和颜色，可以单独设置某一边的边框。宽度单位(px:像素,pt:1/72英寸,em:字体大小,pc:12pt,cm:厘米,mm:毫米,in:英寸,%:百分比)。
					<p>
						<strong>外侧边距：</strong>围绕在元素边框的空白区域是外边距，属性定义元素边框外的空白区域，可以单独设置某一边的外边距。长度单位同上，可以是负值。
					<p>
						<strong>内侧边距：</strong>元素的内边距在边框和内容区之间，属性定义元素边框与元素内容之间的空白区域，可以单独设置某一边的内边距。长度单位同上。
					<p>
						<strong>背景属性：</strong>允许应用纯色作为背景，也允许使用背景图像创建相当复杂的效果。
					<p>
						<strong>背景颜色：</strong>属性为元素设置一种纯色。这种颜色会填充元素的内容、内边距和边框区域，扩展到元素边框的外边界（但不包括外边距）。如果边框有透明部分（如虚线边框），会透过这些透明部分显示出背景色。
					<p>
						<strong>背景图片：</strong>属性为元素设置背景图像。 元素的背景占据了元素的全部尺寸，包括内边距和边框，但不包括外边距。 默认地，背景图像位于元素的左上角，并在水平和垂直方向上重复。 提示：请设置一种可用的背景颜色，这样的话，假如背景图像不可用，页面也可获得良好的视觉效果。
					<p>
						<strong>图片伸展：</strong>属性定义了图像的平铺模式。默认地，背景图像在水平和垂直方向上重复。
					<p>
						<strong>图片定位：</strong>属性设置背景图像的起始位置。 这个属性设置背景原图像（由 background-image 定义）的位置，背景图像如果要重复，将从这一点开始。 提示：您需要把 background-attachment 属性设置为 "fixed"，才能保证该属性在 Firefox 和 Opera 中正常工作。
					<p>
						<strong>附加属性：</strong>属性设置背景图像是否固定或者随着页面的其余部分滚动。
				</div>
			</div>
		</div>
		<div class="tabsBoxItem">
			<table>
				<tr>
					<td class="QrCSSDesignerPadLeft">字体样式：</td>
					<td>{assign var="fontStyleOptions" value=",normal,italic,oblique" delimiter=","} {import file="pulldown.st" name="fontStyle" style="font-style" value="" options=$fontStyleOptions}</td>
					<td class="QrCSSDesignerPadLeft">字体变量：</td>
					<td>{assign var="fontVariantOptions" value=",normal,small-caps" delimiter=","} {import file="pulldown.st" name="fontVariant" style="font-variant" value="" options=$fontVariantOptions}</td>
				</tr>
				<tr>
					<td class="QrCSSDesignerPadLeft">字体粗细：</td>
					<td>{assign var="fontWeightOptions" value=",normal,bold" delimiter=","} {import file="pulldown.st" name="fontWeight" style="font-weight" value="" options=$fontWeightOptions}</td>
					<td class="QrCSSDesignerPadLeft">字体大小：</td>
					<td>{import file="spinner.st" name="fontSize" style="font-size" value="" uom="px"}</td>
				</tr>
				<tr>
					<td class="QrCSSDesignerPadLeft">字体系列：</td>
					<td>{assign var="fontFamilyOptions" value=",宋体,黑体,微软雅黑,微软正黑体,新宋体,新细明体,细明体,仿宋,楷体,仿宋_GB2312,楷体_GB2312,Arial,Arial Black,Arial Narrow,Brush Script MT,Century Gothic,Comic Sans MS,Courier,Courier New,Tahoma,Times New Roman,Verdana" delimiter=","} {import file="pulldown.st" name="fontFamily" style="font-family" value="" options=$fontFamilyOptions}</td>
				</tr>
				<tr>
					<td colspan=4>
						<hr size="1" width="100%" color="#A5BAC6">
					</td>
				</tr>
				<tr>
					<td class="QrCSSDesignerPadLeft">文本颜色：</td>
					<td>{import file="color.st" name="color" style="color" value=""}</td>
					<td class="QrCSSDesignerPadLeft">水平对齐：</td>
					<td>{assign var="textAlignOptions" value=",left,right,center,justify" delimiter=","} {import file="pulldown.st" name="textAlign" style="text-align" value="" options=$textAlignOptions}</td>
				</tr>
				<tr>
					<td class="QrCSSDesignerPadLeft">垂直对齐：</td>
					<td>{assign var="verticalAlignOptions" value=",baseline,top,middle,bottom,text-top,text-bottom" delimiter=","} {import file="pulldown.st" name="verticalAlign" style="vertical-align" value="" options=$verticalAlignOptions}</td>
					<td class="QrCSSDesignerPadLeft">文本修饰：</td>
					<td>{assign var="textDecorationOptions" value=",underline,overline,underline,line-through,blink" delimiter=","} {import file="pulldown.st" name="textDecoration" style="text-decoration" value="" options=$textDecorationOptions}</td>
				</tr>
				<tr>
					<td class="QrCSSDesignerPadLeft">书写方向：</td>
					<td>{assign var="directionOptions" value=",ltr,rtl" delimiter=","} {import file="pulldown.st" name="direction" style="direction" value="" options=$directionOptions}</td>
					<td class="QrCSSDesignerPadLeft">首行缩进：</td>
					<td>{import file="spinner.st" name="textIndent" style="text-indent" value="" uom="em"}</td>
				</tr>
				<tr>
					<td class="QrCSSDesignerPadLeft">文本行高：</td>
					<td>{import file="spinner.st" name="lineHeight" style="line-height" value="" uom="px"}</td>
					<td class="QrCSSDesignerPadLeft">字母间距：</td>
					<td>{import file="spinner.st" name="letterSpacing" style="letter-spacing" value="" uom="px"}</td>
				</tr>
				<tr>
					<td class="QrCSSDesignerPadLeft">单词间距：</td>
					<td>{import file="spinner.st" name="wordSpacing" style="word-spacing" value="" uom="px"}</td>
					<td class="QrCSSDesignerPadLeft">文本转换：</td>
					<td>{assign var="textTransformOptions" value=",capitalize,lowercase,uppercase" delimiter=","} {import file="pulldown.st" name="textTransform" style="text-transform" value="" options=$textTransformOptions}</td>
				</tr>
				<tr>
					<td class="QrCSSDesignerPadLeft">文本空白：</td>
					<td>{assign var="whiteSpaceOptions" value=",normal,pre,nowrap,pre-wrap,pre-line" delimiter=","} {import file="pulldown.st" name="whiteSpace" style="white-space" value="" options=$whiteSpaceOptions}</td>
				</tr>
			</table>
			<hr size="1" width="100%" color="#A5BAC6">
			<span><img src="{$designerPath}/img/arrowclose.gif" onClick="CssDesigner.switchCSS(event,'#CssDesignerDemoContainer_Text');" /> CSS效果及说明</span>
			<div class="CssDesignerDemoContainer" id="CssDesignerDemoContainer_Text">
				<div class="CssDesignerDemo">
					<p>
						<strong>字体属性：</strong>定义文本的字体系列、大小、加粗、风格（如斜体）和变形（如小型大写字母）。
					<p>
						<strong>文本属性：</strong>可定义文本的外观，包括文本的颜色、字符间距、对齐文本、装饰文本、对文本进行缩进，等等。
				</div>
			</div>
		</div>
		<div class="tabsBoxItem">
			<table>
				<tr>
					<td class="QrCSSDesignerPadLeft">列表样式：</td>
					<td>{assign var="listStyleTypeOptions" value=",none,disc,circle,square,decimal,lower-roman,upper-roman,lower-alpha,upper-alpha" delimiter=","} {import file="pulldown.st" name="listStyleType" style="list-style-type" value="" options=$listStyleTypeOptions}</td>
					<td class="QrCSSDesignerPadLeft">标记位置：</td>
					<td>{assign var="listStylePositionOptions" value=",inside,outside" delimiter=","} {import file="pulldown.st" name="listStylePosition" style="list-style-position" value="" options=$listStylePositionOptions}</td>
				</tr>
				<tr>
					<td class="QrCSSDesignerPadLeft">标记图片：</td>
					<td>{import file="upload.st" name="listStyleImage" style="list-style-image" value=""}</td>
				</tr>
				<tr>
					<td colspan=4>
						<hr size="1" width="100%" color="#A5BAC6">
					</td>
				</tr>
				<tr>
					<td class="QrCSSDesignerPadLeft">边框折叠：</td>
					<td>{assign var="borderCollapseOptions" value=",collapse" delimiter=","} {import file="pulldown.st" name="borderCollapse" style="border-collapse" value="" options=$borderCollapseOptions}</td>
					<td class="QrCSSDesignerPadLeft">边框间距：</td>
					<td>{import file="spinner.st" name="borderSpacing" style="border-spacing" value="" uom="px"}</td>
				</tr>
				<tr>
					<td class="QrCSSDesignerPadLeft">标题位置：</td>
					<td>{assign var="captionSideOptions" value=",top,bottom,left,right" delimiter=","} {import file="pulldown.st" name="captionSide" style="caption-side" value="" options=$captionSideOptions}</td>
					<td class="QrCSSDesignerPadLeft">空单元格：</td>
					<td>{assign var="emptyCellsOptions" value=",hide,show" delimiter=","} {import file="pulldown.st" name="emptyCells" style="empty-cells" value="" options=$emptyCellsOptions}</td>
				</tr>
				<tr>
					<td class="QrCSSDesignerPadLeft">表格布局：</td>
					<td>{assign var="tableLayoutOptions" value=",auto,fixed" delimiter=","} {import file="pulldown.st" name="tableLayout" style="table-layout" value="" options=$tableLayoutOptions}</td>
				</tr>
			</table>
			<hr size="1" width="100%" color="#A5BAC6">
			<span><img src="{$designerPath}/img/arrowclose.gif" onClick="CssDesigner.switchCSS(event,'#CssDesignerDemoContainer_List');" /> CSS效果及说明</span>
			<div class="CssDesignerDemoContainer" id="CssDesignerDemoContainer_List">
				<UL class="CssDesignerDemo">
					<LI><strong>列表属性：</strong>允许你放置、改变列表项标志，或者将图像作为列表项标志。</LI>
					<LI><strong>列表类型：</strong>要影响列表的样式，最简单（同时支持最充分）的办法就是改变其标志类型</LI>
					<LI><strong>列表项图像：</strong>有时，常规的标志是不够的。你可能想对各标志使用一个图像</LI>
					<LI><strong>列表标志位置：</strong>可以确定标志出现在列表项内容之外还是内容内部</LI>
				</UL>
				<hr size="1" width="100%" color="#A5BAC6">
				<table class="CssDesignerDemo">
					<caption>
						<strong>标题：</strong>表格属性可以帮助改善表格的外观
					</caption>
					<tr>
						<td valign="top"><strong>表格边框</strong></td>
						<td>表格边框属性可以通过边框tab页设置</td>
					</tr>
					<tr>
						<td valign="top"><strong>表格宽度和高度</strong></td>
						<td>通过 width 和 height 属性定义表格的宽度和高度。</td>
					</tr>
					<tr>
						<td valign="top"><strong>表格文本对齐</strong></td>
						<td>text-align 和 vertical-align 属性设置表格中文本的对齐方式。</td>
					</tr>
					<tr>
						<td valign="top"><strong>表格内边距</strong></td>
						<td>如需控制表格中内容与边框的距离，请为 td 和 th 元素设置 padding 属性</td>
					</tr>
				</table>
			</div>
		</div>
		<div class="tabsBoxItem">
			<table>
				<tr>
					<td class="QrCSSDesignerPadLeft">资源定位：</td>
					<td>{assign var="positionOptions" value=",static,relative,absolute,fixed" delimiter=","} {import file="pulldown.st" name="position" style="position" value="" options=$positionOptions}</td>
					<td class="QrCSSDesignerPadLeft">优先等级：</td>
					<td>{import file="spinner.st" name="zIndex" style="z-index" value=""}</td>
				</tr>
				<tr>
					<td class="QrCSSDesignerPadLeft">距离左边：</td>
					<td>{import file="spinner.st" name="left" style="left" value="" uom="px"}</td>
					<td class="QrCSSDesignerPadLeft">距离顶部：</td>
					<td>{import file="spinner.st" name="top" style="top" value="" uom="px"}</td>
				</tr>
				<tr>
					<td class="QrCSSDesignerPadLeft">距离右边：</td>
					<td>{import file="spinner.st" name="right" style="right" value="" uom="px"}</td>
					<td class="QrCSSDesignerPadLeft">距离底部：</td>
					<td>{import file="spinner.st" name="bottom" style="bottom" value="" uom="px"}</td>
				</tr>
				<tr>
					<td colspan=4>
						<hr size="1" width="100%" color="#A5BAC6">
					</td>
				</tr>
				<tr>
					<td class="QrCSSDesignerPadLeft">显示属性：</td>
					<td>{assign var="displayOptions" value=",none,block,inline" delimiter=","} {import file="pulldown.st" name="display" style="display" value="" options=$displayOptions}</td>
					<td class="QrCSSDesignerPadLeft">可见属性：</td>
					<td>{assign var="visibilityOptions" value=",visible,hidden" delimiter=","} {import file="pulldown.st" name="visibility" style="visibility" value="" options=$visibilityOptions}</td>
				</tr>
				<tr>
					<td class="QrCSSDesignerPadLeft">浮动属性：</td>
					<td>{assign var="floatOptions" value=",left,right,none" delimiter=","} {import file="pulldown.st" name="float" style="float" value="" options=$floatOptions}</td>
					<td class="QrCSSDesignerPadLeft">清除属性：</td>
					<td>{assign var="clearOptions" value=",left,right,both,none" delimiter=","} {import file="pulldown.st" name="clear" style="clear" value="" options=$clearOptions}</td>
				</tr>
				<tr>
					<td class="QrCSSDesignerPadLeft">溢出选项：</td>
					<td>{assign var="overflowOptions" value=",visible,hidden,scroll,auto" delimiter=","} {import file="pulldown.st" name="overflow" style="overflow" value="" options=$overflowOptions}</td>
				</tr>
			</table>
			<hr size="1" width="100%" color="#A5BAC6">
			<span><img src="{$designerPath}/img/arrowclose.gif" onClick="CssDesigner.switchCSS(event,'#CssDesignerDemoContainer_Layout');" /> CSS效果及说明</span>
			<div class="CssDesignerDemoContainer" id="CssDesignerDemoContainer_Layout">
				<UL class="CssDesignerDemo">
					<LI><strong>资源定位：</strong>属性规定元素的定位类型</LI>
					<LI><strong>优先等级：</strong>属性设置元素的堆叠顺序。拥有更高堆叠顺序的元素总是会处于堆叠顺序较低的元素的前面。</LI>
					<LI><strong>距离左边：</strong>属性规定元素的左边缘。该属性定义了定位元素左外边距边界与其包含块左边界之间的偏移。注释：如果 "position" 属性的值为 "static"，那么设置该属性不会产生任何效果。</LI>
					<LI><strong>距离顶部：</strong>属性规定元素的顶部边缘。该属性定义了一个定位元素的上外边距边界与其包含块上边界之间的偏移。注释：如果 "position" 属性的值为 "static"，那么设置该属性不会产生任何效果。</LI>
					<LI><strong>距离右边：</strong>属性规定元素的右边缘。该属性定义了定位元素右外边距边界与其包含块右边界之间的偏移。注释：如果 "position" 属性的值为 "static"，那么设置该属性不会产生任何效果。</LI>
					<LI><strong>距离底部：</strong>属性规定元素的底部边缘。该属性定义了定位元素下外边距边界与其包含块下边界之间的偏移。注释：如果 "position" 属性的值为 "static"，那么设置该属性不会产生任何效果。</LI>
					<LI><strong>整体宽度：</strong>属性设置元素的宽度</LI>
					<LI><strong>整体高度：</strong>属性设置元素的高度</LI>
					<LI><strong>溢出选项：</strong>这个属性定义溢出元素内容区的内容会如何处理。如果值为 scroll，不论是否需要，用户代理都会提供一种滚动机制。因此，有可能即使元素框中可以放下所有内容也会出现滚动条。</LI>
					<LI><strong>显示属性：</strong>这个属性用于定义建立布局时元素生成的显示框类型。对于 HTML 等文档类型，如果使用 display 不谨慎会很危险，因为可能违反 HTML 中已经定义的显示层次结构。对于 XML，由于 XML 没有内置的这种层次结构，所有 display 是绝对必要的。</LI>
					<LI><strong>可见属性：</strong>属性规定元素是否可见。
						<p>提示：即使不可见的元素也会占据页面上的空间。请使用 "display" 属性来创建不占据页面空间的不可见元素。
						<p>说明： 这个属性指定是否显示一个元素生成的元素框。这意味着元素仍占据其本来的空间，不过可以完全不可见。值 collapse 在表中用于从表布局中删除列或行。</LI>
					<LI><strong>浮动属性：</strong>float 属性定义元素在哪个方向浮动。以往这个属性总应用于图像，使文本围绕在图像周围，不过在 CSS 中，任何元素都可以浮动。浮动元素会生成一个块级框，而不论它本身是何种元素。
						<p>如果浮动非替换元素，则要指定一个明确的宽度；否则，它们会尽可能地窄。
						<p>注释：假如在一行之上只有极少的空间可供浮动元素，那么这个元素会跳至下一行，这个过程会持续到某一行拥有足够的空间为止。</LI>
					<LI><strong>清除属性：</strong>属性规定元素的哪一侧不允许其他浮动元素。
						<p>说明： clear 属性定义了元素的哪边上不允许出现浮动元素。在 CSS1 和 CSS2 中，这是通过自动为清除元素（即设置了 clear 属性的元素）增加上外边距实现的。在 CSS2.1 中，会在元素上外边距之上增加清除空间，而外边距本身并不改变。不论哪一种改变，最终结果都一样，如果声明为左边或右边清除，会使元素的上外边框边界刚好在该边上浮动元素的下外边距边界之下。</LI>
				</UL>
			</div>
		</div>
		<div class="tabsBoxItem">
			<table>
				<tr>
					<td class="QrCSSDesignerPadLeft">鼠标指针：</td>
					<td>{assign var="cursorOptions" value=",crosshair,default,hand,pointer,move,text,wait,help,n-resize,s-resize,w-resize,e-resize,ne-resize,nw-resize,se-resize,sw-resize" delimiter=","} {import file="pulldown.st" name="cursor" style="cursor" value="" options=$cursorOptions}</td>
					<td class="QrCSSDesignerPadLeft">滤镜效果：</td>
					<td>{assign var="filterOptions" value=",Alpha,Blur,Chroma,DropShadow,FlipH,FlipV,Glow,Gray,invert,Mask,Shadow,wave,Xray" delimiter=","} {import file="pulldown.st" name="filter" style="filter" value="" options=$filterOptions}</td>
				</tr>
			</table>
			<hr size="1" width="100%" color="#A5BAC6">
			<span><img src="{$designerPath}/img/arrowclose.gif" onClick="CssDesigner.switchCSS(event,'#CssDesignerDemoContainer_Other');" /> CSS效果及说明</span>
			<div class="CssDesignerDemoContainer" id="CssDesignerDemoContainer_Other">
				<table class="CssDesignerDemo">
					<tr>
						<td valign="top"><strong>鼠标指针</strong></td>
						<td>属性规定要显示的光标的类型（形状）。
							<p>该属性定义了鼠标指针放在一个元素边界范围内时所用的光标形状（不过 CSS2.1 没有定义由哪个边界确定这个范围）。
						</td>
					</tr>
					<tr>
						<td valign="top"><strong>滤镜效果</strong></td>
					</tr>
					<tr>
						<td valign="top"><strong>Alpha：</strong></td>
						<td>设置透明层次。
							<p>语法：STYLE="filter:Alpha(Opacity=, FinishOpacity=, Style=, StartX=, StartY=, FinishX=, FinishY=)"
							<p>说明：
							<p>Opacity：起始值，取值为0～100, 0为透明，100为原图。
							<p>FinishOpacity：目标值。
							<p>Style：1或2或3
							<p>StartX：任意值
							<p>StartY：任意值
							<p>例子：filter:Alpha(Opacity="0", FinishOpacity="75", Style="2")
						</td>
					</tr>
					<tr>
						<td valign="top"><strong>Blur：</strong></td>
						<td>模糊效果。
							<p>语法：STYLE="filter:Blur(Add=, Direction=, Strength=)"
							<p>说明：
							<p>Add：一般为1，或0。
							<p>Direction：角度，0～315度，步长为45度。
							<p>Strength：效果增长的数值，一般5即可。
							<p>例子：filter:Blur(Add="1", Direction="45", Strength="5")
						</td>
					</tr>
					<tr>
						<td valign="top"><strong>Chroma：</strong></td>
						<td>制作专用颜色透明。
							<p>语法：STYLE="filter:Chroma(Color=color)"
							<p>说明：color：#rrggbb格式，任意。 #FFFFFF")
							<p>例子：filter:Chroma(Color="
						</td>
					</tr>
					<tr>
						<td valign="top"><strong>DropShadow：</strong></td>
						<td>创建对象的固定影子
							<p>语法：STYLE="filter:DropShadow(Color=, OffX=, OffY=, Positive=)"
							<p>说明：Color:#rrggbb格式，任意。
							<p>Offx：X轴偏离值。
							<p>Offy：Y轴偏离值。
							<p>Positive：1或0。
							<p>例子：filter:DropShadow(Color="#6699CC", OffX="5", OffY="5", Positive="1")
						</td>
					</tr>
					<tr>
						<td valign="top"><strong>FlipH：</strong></td>
						<td>创建水平镜像图片</td>
					</tr>
					<tr>
						<td valign="top"><strong>FlipV：</strong></td>
						<td>创建垂直镜像图片</td>
					</tr>
					<tr>
						<td valign="top"><strong>Glow：</strong></td>
						<td>加光辉在附近对象的边外。
							<p>语法：STYLE="filter:Glow(Color=, Strength=)"
							<p>说明：
							<p>Color：发光颜色。
							<p>Strength：强度(0－100)
							<p>例子：filter:Glow(Color="#6699CC", Strength="5")
						</td>
					</tr>
					<tr>
						<td valign="top"><strong>Gray：</strong></td>
						<td>把图片灰度化</td>
					</tr>
					<tr>
						<td valign="top"><strong>invert：</strong></td>
						<td>反色</td>
					</tr>
					<tr>
						<td valign="top"><strong>light：</strong></td>
						<td>创建光源在对象上</td>
					</tr>
					<tr>
						<td valign="top"><strong>Mask：</strong></td>
						<td>创建透明掩膜在对象上。
							<p>语法：STYLE="filter:Mask(Color=)
							<p>例子：filter:Mask (Color="#FFFFE0")
						</td>
					</tr>
					<tr>
						<td valign="top"><strong>shadow：</strong></td>
						<td>创建偏移固定影子。
							<p>语法：filter:Shadow(Color=, Direction=)
							<p>说明：
							<p>Color：#rrggbb格式。
							<p>Direction：角度，0－315度，步长为45度。
							<p>例子：filter:Shadow (Color="#6699CC", Direction="135")
						</td>
					</tr>
					<tr>
						<td valign="top"><strong>wave：</strong></td>
						<td>波纹效果。
							<p>语法：filter: Wave(Add=，Freq=， LightStrength=， Phase=， Strength=)
							<p>说明：
							<p>Add：一般为1，或0。
							<p>Freq：变形值。
							<p>LightStrength：变形百分比。
							<p>Phase：角度变形百分比。
							<p>Strength：变形强度。
							<p>例子：filter: wave(Add="0", Phase="4", Freq="5", LightStrength="5", Strength="2")
						</td>
					</tr>
					<tr>
						<td valign="top"><strong>Xray：</strong></td>
						<td>使对象变得像被x光照射一样</td>
					</tr>
				</table>
			</div>
		</div>
	</DIV>
</DIV>