{*点击购买按钮——立即添加一份商品到购物车*}
{if $block.cellCount == null || $block.cellCount == 0} {if $block.horizontal}
<table width='100%' border="0" cellpadding="0" cellspacing="0">
    <tr>
        {foreach from=$data.items item="item"}
        <td class="i">
            <table width="100%">
              <tr>
                <td class="il">{if $item.obj != null}
                    <div class="price">{if $item.obj.nowPrice != null}￥{tostring bean=$item.obj prop="nowPrice" pattern="#,##0.00"}{else}<!-- 未上架 -->{/if}</div>
                    <div class="buy">{if $item.obj.nowPrice != null}<input type="submit" value="" pid="{$item.obj.id}" title='{$item.title}' poldprice='{tostring bean=$item.obj prop="oldPrice" pattern="#,##0.00"}' pprice='{tostring bean=$item.obj prop="nowPrice" pattern="#,##0.00"}' pbalance='{tostring bean=$item.obj prop="balance" pattern="#,##0.00"}' style="cursor:pointer;" class="btnBuy" />{/if}</div>
                    {else}&nbsp;{/if}
                </td>
              </tr>
              <tr><td class="it" valign="middle">{import file="../a.st" href=$item.href target=$item.target title=$item.title img=$item.img}</td></tr>
              <tr><td class="ir">&nbsp;{import file="../a.st" href=$item.href target=$item.target title=$item.title name=$item.name}</td></tr>
            </table>
        </td>{/foreach}
    </tr>
</table>
{else}
<ul>
    {foreach from=$data.items item="item"}
    <li class="i">
            <table width="100%">
              <tr>
                <td class="il">{if $item.obj != null}
                    <div class="price">{if $item.obj.nowPrice != null}￥{tostring bean=$item.obj prop="nowPrice" pattern="#,##0.00"}{else}<!-- 未上架 -->{/if}</div>
                    <div class="buy">{if $item.obj.nowPrice != null}<input type="submit" value="" pid="{$item.obj.id}" title='{$item.title}' poldprice='{tostring bean=$item.obj prop="oldPrice" pattern="#,##0.00"}' pprice='{tostring bean=$item.obj prop="nowPrice" pattern="#,##0.00"}' pbalance='{tostring bean=$item.obj prop="balance" pattern="#,##0.00"}' style="cursor:pointer;" class="btnBuy" />{/if}</div>
                    {else}&nbsp;{/if}
                </td>
              </tr>
              <tr><td class="it" valign="middle">{import file="../a.st" href=$item.href target=$item.target title=$item.title img=$item.img}</td></tr>
              <tr><td class="ir">&nbsp;{import file="../a.st" href=$item.href target=$item.target title=$item.title name=$item.name}</td></tr>
            </table>
    </li>{/foreach}
</ul>
{/if} {else}
<table width='100%' border="0" cellpadding="0" cellspacing="0">
    {foreach from=$data.items item="row"}
    <tr>
        {foreach from=$row.items item="item"}
        <td>
        <div class="i">
            <table width="100%">
              <tr>
                <td class="il">{if $item.obj != null}
                    <div class="price">{if $item.obj.nowPrice != null}￥{tostring bean=$item.obj prop="nowPrice" pattern="#,##0.00"}{else}<!-- 未上架 -->{/if}</div>
                    <div class="buy">{if $item.obj.nowPrice != null}<input type="submit" value="" pid="{$item.obj.id}" title='{$item.title}' poldprice='{tostring bean=$item.obj prop="oldPrice" pattern="#,##0.00"}' pprice='{tostring bean=$item.obj prop="nowPrice" pattern="#,##0.00"}' pbalance='{tostring bean=$item.obj prop="balance" pattern="#,##0.00"}' style="cursor:pointer;" class="btnBuy" />{/if}</div>
                    {else}&nbsp;{/if}
                </td>
              </tr>
              <tr><td class="it" valign="middle">{import file="../a.st" href=$item.href target=$item.target title=$item.title img=$item.img}</td></tr>
              <tr><td class="ir">&nbsp;{import file="../a.st" href=$item.href target=$item.target title=$item.title name=$item.name}</td></tr>
            </table>
        </div>
        </td> {/foreach}
    </tr>
    {/foreach}
</table>
{/if}
<div class="cartinfo dialog" style="position:absolute;display:none;">
    <form id="cartform_{$ui.uiid}" action="{$contextPath}/ui/{$block.titleLink.id}">
        <div class="title">&nbsp;</div>
        <div class="content">
            <table>
                <tr>
                    <td colspan=6 class="subtitle">添加商品到购物车成功！</td>
                </tr>
                <tr>
                    <td style="padding: 5px;">商品数量：</td><td><span class="amount"></span></td>
                    <td style="padding: 5px;">合计金额：</td><td><span class="price"></span></td>
                </tr>
                <tr>
                    <td colspan=4 class="subtitle">&nbsp;</td>
                </tr>
                <tr>
                    <td colspan=2></td>
                    <td colspan=2 style="text-align: right;">
                        <div class="ui-btns">
                           <div class="ui-btn" style="float: left;"><input type="submit" class="btnGoCart ui-btn" value="结算" /></div>
                           <div style="width:5px;float: left;">&nbsp;</div>
                           <div class="ui-btn" style="float: left;"><input type="reset" class="btnCartCancel ui-btn" value="再逛逛" /></div>
                       </div>
                   </td>
                </tr>
            </table>
        </div>
    </form>
</div>
<script type="text/javascript">
//$(function(){
    
var $buylistblock=$("#block{$block.id}")
var $cartDialog=$(".cartinfo",$buylistblock);
$(".btnBuy",$buylistblock).click(function(event){
    var $this=$(this);
    
	var offset=$this.offset();
    var blockOffset=$buylistblock.offset();
    var top=offset.top-blockOffset.top+$this.height();
    var left=offset.left-blockOffset.left;
    if(offset.left + $cartDialog.width() > blockOffset.left + $buylistblock.width()){
        left = left-$cartDialog.width()+$this.width()+20;
    }    

    $this.parent().block();
	$.post("{$contextPath}/bz/order/addtocart/"+$this.attr("pid")+"/1", "", function(cart) {
    	if(cart.success){
    	   $cartDialog.css({
                top: top,
                left: left
            }).demsyModal();
    	   $(".amount",$cartDialog).html(cart.totalAmount);
    	   $(".price",$cartDialog).html(parseFloat(cart.totalPrice).toFixed(2));
    	}else{
    	   alert("添加商品到购物车失败! "+cart.error);
    	}
	}, "json");
    return false;
});
$(".btnCartCancel",$cartDialog).click(function(){
	$cartDialog.hide();
});

//});
</script>