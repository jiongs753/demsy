<script src="{$scriptPath}/jquery.ad-gallery-v.js" type="text/javascript"></script>
<link href="{$stylePath}/gallery/jquery.ad-gallery.css" rel="stylesheet" type="text/css" media="screen" />
{math assign="w" equation="v-120" v=$block.position.width}
{math assign="h" equation="v-28" v=$block.position.height}
<div class="ad-gallery">
    <table>
        <tr>
            <td vAlign="top">
                <div class="ad-image-wrapper" style="height:{$h}px; width:{$w}px;"></div>
                <div class="ad-controls">
                	<a href="#" class="srcimg" target="_blank">原图</a>
                    |<a href="#" class="comment" target="_blank">评论</a>
                    |<a href="javascript: void(0);" class="voteagree">顶</a>
                    |<a href="javascript: void(0);" class="voteoppose">踩</a>
                </div>
            </td>
            <td>
                <div class="ad-nav" style="height:{math equation="v-5" v=$block.position.height}px; width: 105px;">
                    <div class="ad-thumbs" style="height: {math equation="v-50" v=$block.position.height}px;">
                        <ul class="ad-thumb-list">{foreach from=$data.items item="item"}{assign var="obj" value=$item.obj}{assign var="srcPic" value=$obj.image}{thumbs assign="detailImg" src=$obj.image width=1024 height=768}{thumbs assign="bigPic" src=$obj.image width=$block.imageWidth height=$block.imageHeight press="true"}{thumbs assign="logoPic" src=$obj.image width=100 height=75}{if $item.obj.image != null && $logoPic != null && $logoPic != ""}
                            <li><a href="{$bigPic}"><div></div><img width="95" detailLink="{$item.href}" detailTarget="{$item.target}" detailImg="{$detailImg}" src="{$logoPic}" id="thumb_{$obj.id}" title="{$obj.catalog.name}-{$obj.name}" longdesc="编号：{$obj.code} 说明：{$obj.desc}" ></a></li>{/if}{/foreach}
                        </ul>
                    </div>
                </div>
                {if $block.naviButton && $pager.totalPage > 0}
                    <div class="pager">
                        {if $pager.pageIndex != 1}
                        <div class="first navi">
                            <a href='javascript: ui.gotoPage("block{$block.id}","page=1");'>第一组</a>
                        </div>
                        <div class="prev navi">
                            <a href='javascript: ui.gotoPage("block{$block.id}","page={$pager.prePage}");'>上一组</a>
                        </div>
                        {/if}
                        {if $pager.pageIndex != $pager.totalPage}
                        <div class="next navi">
                            <a href='javascript: ui.gotoPage("block{$block.id}","page={$pager.nextPage}");'>下一组</a>
                        </div>
                        <div class="last navi">
                            <a href='javascript: ui.gotoPage("block{$block.id}","page={$pager.totalPage}");'>最后组</a>
                        </div>
                        {/if}
                    </div>
                {/if}
            </td>
        </tr>
    </table>
    <input type="hidden" id="voteToken" value="{$voteToken}" />
</div>

<script language="javascript" type="text/javascript">
$(document).ready(function(){
    var gallery=$('.ad-gallery');
    var $srcimg=$(".srcimg",gallery);
    var $comment=$(".comment",gallery);
    var $voteagree=$(".voteagree",gallery);
    var $voteoppose=$(".voteoppose",gallery);
	var galleries = gallery.adGallery({
		loader_image: '/themes2/images/busy.gif',
		width: {$w},
		height: {$h},
		slideshow: {
        	enable: true,
            autostart: true,
            speed: 3000,
            start_label: '',
            stop_label: '',
            stop_on_scroll: false,
            countdown_prefix: '',
            countdown_sufix: '',
            onStart: false,
            onStop: false
        },
		callbacks:{
			afterImageVisible: function(context){
                var thumbID=this.images[this.current_index].id;
                var $thumb=$("#"+thumbID,gallery);
                $srcimg.attr("href",$thumb.attr("detailImg"));
                $comment.attr("href",$thumb.attr("detailLink"));
                $comment.attr("target",$thumb.attr("detailTarget"));
                var id=thumbID.substring(6);
                var title=$thumb.attr("title");
                $voteagree.attr("val",id);
                $voteagree.attr("title",title);
                $voteoppose.attr("val",id);
                $voteoppose.attr("title",title);
			},
			beforeImageVisible: function(context){
			}
		}
    });
	$voteagree.click(function(){
		$.post("{$voteMdl}/"+$("#voteToken").val(), "data.name="+$voteagree.attr("title")+"&data.type=1&data.score=1&data.moduleID={$blockParser.module.id}&data.subjectID="+$voteagree.attr("val"), function(json) {
            alert(json.message);
            if(json.success){
            	$("#voteToken").val(json.nextToken);
            }
		}, "json");
	});
	$voteoppose.click(function(){
		$.post("{$voteMdl}/"+$("#voteToken").val(), "data.name="+$voteagree.attr("title")+"&data.type=0&data.score=1&data.moduleID={$blockParser.module.id}&data.subjectID="+$voteoppose.attr("val"), function(json) {
            alert(json.message);
            if(json.success){
            	$("#voteToken").val(json.nextToken);
            }
		}, "json");
	});
	galleries[0].settings.effect = "fade";
});
</script>
