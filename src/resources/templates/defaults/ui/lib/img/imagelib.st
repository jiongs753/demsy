<SCRIPT src="/themes2/defaults/imagelib/ntes_jslib_1.x.js" language="javascript" type="text/javascript"></SCRIPT>
<LINK href="/themes2/defaults/imagelib/style.css" rel=stylesheet type=text/css/>
<LINK href="/themes2/defaults/imagelib/channel_nav.css" rel=stylesheet type=text/css/>
<LINK href="/themes2/defaults/imagelib/nph_gallery_2.12.css" rel=stylesheet type=text/css />
<link href="/themes2/defaults/imagelib/tieanywhere2.2.3.css" rel="stylesheet" type="text/css" />
{math assign="w" equation="v" v=$block.position.width}
{math assign="imgwidth" equation="v-60" v=$w}
{math assign="imgheight" equation="v*3/4" v=$imgwidth}
<div style="width: {$w}px;" class="nph_skin_white">
<DIV id=gallery class="nph_gallery clearfix">
<DIV class=nph_bg>
<DIV class="nph_cnt clearfix">
<DIV class=nph_set_info>
<DIV id=setInfo>
<DIV class=nph_set_title>
<H1>{$data.name}</H1><SPAN id=photoType class="nph_set_cur hidden">（<SPAN id=photoIndex class=nph_c_lh></SPAN>/{$data.size}）</SPAN>
</DIV>
</DIV>
</DIV>
</DIV>
<SPAN class=nph_hr_solid></SPAN>
<DIV id=modePhoto class=nph_photo>
<DIV class=nph_photo_view>
<DIV id=photoView class=nph_cnt><I></I><IMG id=photo src="/themes2/defaults/imagelib/bg06.png" /></DIV>
<DIV class=nph_photo_prev><A hideFocus="true" id=photoPrev class=nph_btn_pphoto href="#" target=_self></A></DIV>
<DIV class=nph_photo_next><A hideFocus="true" id=photoNext class=nph_btn_nphoto href="#" target=_self></A></DIV>
<DIV id=photoLoading class=nph_photo_loading></DIV>
</DIV>
<DIV class=nph_cnt>
<DIV class=nph_photo_ctrl>
<ul><li>
<a href='#comments' onclick='javascript:ui.gotoPage("blockcomment{$block.id}","commentPage=1&pageSize=10");' class="nph_icon_comment">评论<span id="tieCount" class="nph_c_lh">{if $data.obj.commentNum > 0}({$data.obj.commentNum}){/if}</span></a></li>
<li>|&nbsp;<a href="#viewOrig" target="_blank" class="nph_icon_orig" id="viewOrig">查看原图</a></li>
<li>&nbsp;&nbsp;&nbsp;&nbsp;</li>
</ul>
</DIV>
<DIV id=photoDesc class=nph_photo_desc></DIV>
</DIV>
<SPAN class=nph_hr_solid></SPAN>
<DIV class=nph_cnt>
<DIV class=clearfix>
<DIV class=nph_set>
<DIV class=nph_set_thumb>
<DIV id=scrl class="nph_photo_thumb clearfix">
<DIV class=clearfix>
<DIV class=nph_scrl>
<DIV class=nph_scrl_thumb>
<DIV class=nph_scrl_main><UL id=thumb class="nph_list_thumb clearfix"></UL></DIV>
<DIV class="nph_scrl_bar clearfix">
<SPAN class=nph_scrl_lt></SPAN><SPAN class=nph_scrl_rt></SPAN>
<DIV class=nph_scrl_bd>
<DIV class=nph_scrl_ct>
<a href="#" hidefocus="true" class="nph_btn_scrl" id="bar">
<b class="nph_btn_lt"></b>
<b class="nph_btn_rt"></b>
<span class="nph_btn_bd" style="text-align: center;"><span><b class="nph_btn_ct"></b></span></span>
</a>
</DIV></DIV></DIV></DIV></DIV>
<SPAN class=nph_scrl_prev><A hidefocus="true" id=scrlPrev class=nph_btn_pscrl href="#"></A></SPAN>
<SPAN class=nph_scrl_next><A hidefocus="true" id=scrlNext class=nph_btn_nscrl href="#"></A></SPAN>
</DIV></DIV></DIV></DIV>
<SPAN class=nph_set_prev>
<A alt='{tostring bean=$prev prop="name"}' title='{tostring bean=$prev prop="name"}' id=prevSet class=nph_btn_pset href="{if $prev != null}{$prev.href}{else}#{/if}" target=_self style="padding: 5px;"><IMG src="{if $prev != null}{thumbs src=$prev.img width=100 height=75}{else}/themes2/images/_blank.gif{/if}"><SPAN><br>&lt;上一图集</SPAN></A>
</SPAN> 
<SPAN class=nph_set_next>
<A alt='{tostring bean=$next prop="name"}' title='{tostring bean=$next prop="name"}' id=nextSet class=nph_btn_nset href="{if $next != null}{$next.href}{else}#{/if}" target=_self style="padding: 5px;"><IMG src="{if $next != null}{thumbs src=$next.img width=100 height=75}{else}/themes2/images/_blank.gif{/if}"><SPAN><br>下一图集 &gt;</SPAN></A>
</SPAN>
</DIV>
</DIV>
</DIV><TEXTAREA id=photoList style="display:none;">
{foreach from=$data.items item="item"}
&lt;li&gt;
&lt;a href="#p={$item.obj.id}" hidefocus="true"&gt;&lt;img src="{thumbs src=$item.img width=100 height=75}" alt="" /&gt;&lt;/a&gt;
&lt;h2&gt;&lt;/h2&gt;
&lt;p&gt;{$item.obj.desc}&lt;/p&gt;
&lt;i title="img"&gt;{thumbs src=$item.img width=$imgwidth height=$imgheight}&lt;/i&gt;
&lt;i title="timg"&gt;{thumbs src=$item.img width=100 height=75}&lt;/i&gt;
&lt;/li&gt;
{/foreach}
</TEXTAREA></DIV>
</DIV>

<SCRIPT src="/themes2/defaults/imagelib/ntes_ui_scroll.js" language="javascript" type="text/javascript"></SCRIPT>
<SCRIPT src="/themes2/defaults/imagelib/nph_gallery_2.14.js" language="javascript" type="text/javascript"></SCRIPT>

<SCRIPT type=text/javascript>
NTES.ready(function ($) {
    var nphLayoutGG = $('#nphLayoutGG');
    if (nphLayoutGG) {
        nphLayoutGG.addCss({ 
            display: 'block', 
            position: 'absolute', 
            left: 0, 
            top: 0, 
            width: '280px', 
            height: '210px' 
        });
        $('#photoLayout').addCss({ 
            width: '564px', 
            marginLeft: '-282px' 
        }).appendChild(nphLayoutGG);
        
        new nph.Gallery({ 
            photoViewMode: 2 
        });
    } else {
        new nph.Gallery({ 
            photoViewMode: 0 
        });
    }
    $('#tieCount').innerHTML = String.format('(%1)', NTES.one('#tieArea span.tieCount').innerHTML);
});
</SCRIPT>
<span class="blank15"></span>
<div class="nph_box_relat clearfix">
    <div class="nph_box_title"><h2>更多图集</h2></div>
    {foreach from=$datalist.items item="row"}
    <table class="nph_list_relat">
    <tr>
    {foreach from=$row.items item="item"}
    <td>
    <div><A href="{if $item != null}{$item.href}{else}#{/if}"><IMG src="{if $item != null}{thumbs src=$item.img width=100 height=75}{else}/themes2/images/_blank.gif{/if}"></A></div>
    <H5><A href="{if $item != null}{$item.href}{else}#{/if}">{tostring bean=$item prop="name"}</A></H5>
    </td>
    {/foreach}
    </tr></table>
    {/foreach}
</div>
<span class="blank15"></span>
<a id="comments"></a>
<div id="tieArea" class="tie-area">
<div class="tie-post">
        <div class="nph_box_title"><h2>我要评论</h2></div>
        <div class="cmtform">
    		<form id="form_{$ui.uiid}" action="{$commentUrl}" style="text-align: right;width:100%;">
    			<input type="hidden" name="data.module.id" value="{$module.id}" />
    			<input type="hidden" name="data.subjectID" value="{$data.obj.id}" />
    			<textarea style="width: 100%;height: 100px;" id="textarea_{$ui.uiid}" class="xheditor" name="data.content"></textarea>
                <div>
                    <input type="submit" class="submitButton" value="" />
                </div>
            </form>
        </div>
</div>
</div>
<span class="blank15"></span>
<div id="tieArea2" class="tie-area">
<div class="tie-post">
    <div class="nph_box_title"><h2>
        <a href='javascript:ui.gotoPage("blockcomment{$block.id}","commentPage=1&pageSize=10");'>已有&nbsp;<span style="color: red;">{$data.obj.commentNum}</span>&nbsp;人参与评论</a>
    </h2></div>
    <div class="cmtlist" id="blockcomment{$block.id}" dataID="{$block.id}"></div>
</div>
</div>
</div>
<script type="text/javascript">
$(function() {
	var $form=$("#form_{$ui.uiid}");
	$(".submitButton", $form).click(function() {
		$form.block(); 
        var form=$form.get(0);
		$.post(form.action, $form.serialize(), function(json) {
            alert(json.message);
            if(json.success){
                form.reset();
            	ui.gotoPage("blockcomment{$block.id}","commentPage={if $pager.totalPage == null}1{else}{$pager.totalPage}{/if}");
            }
		}, "json");

		return false;
	});
});
</script>