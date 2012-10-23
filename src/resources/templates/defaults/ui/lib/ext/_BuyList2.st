{*点击购买按钮弹出购买框*}
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
        <div class="title">添加商品到购物车成功！</div>
        <div class="content">
            <table>
                <tr>
                    <td colspan=6 class="subtitle">购物车信息：</td>
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
<div class="buydialog dialog" style="position:absolute;display:none;">
    <div class="title"></div>
    <div class="content">
    <form id="buyform_{$ui.uiid}" action="" onsubmit="return false;">
    <input type="hidden" class="pid" value="" />
        <table>
            <tr>
                <td colspan=6 class="subtitle">商品费用：</td>
            </tr>
            <tr>
                <td>原价：</td>
                <td><span class="oldPrice"></span></td>
            	<td>现价：</td>
                <td><span class="newPrice"></span></td>
                <td>节省：</td>
                <td><span class="balance"></span></td>
            </tr>
            <tr>
                <td>数量：</td>
                <td><input id="p_buy_amount" name="amount" type="text" class="input spinner" style="width: 60px;" value="1" /></td>
                <td>小计：</td>
                <td><span class="subtotalprice"></span></td>
            </tr>
            <tr>
                <td colspan=5></td>
                <td class="ui-btns">
                    <div class="ui-btn" style="float: left;"><input type="submit" class="btnAddToCart ui-btn" value="加入购物车" /></div>
                </td>
            </tr>
            <tr>
                <td colspan=6 class="subtitle">立即购买：(填写收件地址)</td>
            </tr>
            <tr>
                <td colspan=6>
                    <div>
                                            {foreach from=$contactList item="item" key="idx"}
                                            <div style="padding-top: 2px;">
                                                <input type="radio" name="address" class="address" id="address_{$item.id}" value="{$item.id}">
                                                <label for="address_{$item.id}">{$item.province}{$item.city}{$item.area}{$item.desc}({$item.name}&nbsp;&nbsp;{$item.code})</label>
                                            </div>
                                            {/foreach}
                                            <div style="padding-top: 2px;">
                                                <input type="radio" name="address" {if $contactList == null} ckecked{/if} class="address" id="address_0" value=""><label for="address_0"> 使用新地址</label>
                                            </div>
                                            <div id="newaddrdiv" style="{if $contactList != null}display:none;{/if} padding: 5px 0 5px 0;">
                                                <div style="padding: 2px;">
                                                                                                        省/市/区：<select class="input" name="province" style="width: 98px;height: 22px;font-size: 12px;"></select>
                                                            <select class="input" name="city" style="width: 98px;height: 22px;font-size: 12px;"></select>
                                                            <select class="input" name="area" style="width: 98px;height: 22px;font-size: 12px;"></select>
                                                   
                                                </div>
                                                <div style="padding: 2px;">
                                                                                                    街道地址：<input class="input" type="text" name="street" style="width:300px;"/>
                                                </div>
                                                <div style="padding: 2px;">
                                                                                                    邮政编码：<input class="input" type="text" name="postcode" style="width: 150px;"/>
                                                </div>
                                                <div style="padding: 2px;">
                                                                                                    真实姓名：<input class="input" type="text" name="person" style="width: 150px;" />
	                                            </div>
                                                <div style="padding: 2px;">
                                                                                                    手机号码：<input class="input" type="text" name="telcode" style="width: 150px;" />
                                                </div>
                                            </div>
                                            <table style="margin-top: 15px;">
                                                <tr>
                                                   <td>
                                                 		<div style="padding: 3px;">邮费：<span class="postFee"></span>&nbsp;&nbsp;合计(含邮费)：<span class="totalprice price"></span></div>
	                                                   <div style="color: red;font-weight: bold;">(注：{$cfgNotPostFeeDesc})</div>
                                                   </td>
                                                   <td class="ui-btns">
                                                        <div class="ui-btn" style="float: left;"><input type="submit" class="btnDirectBuy ui-btn" title="立即购买：选择支付方式并准备付款" value="立即购买" /></div>
                                                        <div style="width:5px;float: left;">&nbsp;</div>
                                                        <div class="ui-btn" style="float: left;"><input type="reset" class="btnBuyCancel ui-btn" value="取消" /></div>
                                                   </td>
                                                </tr>
                                            </table>
                       </div>
                   </div>
               </td>
            </tr>
        </table>
    </form>
    </div>
</div>
<script type="text/javascript">
//$(function(){
    
var $buylistblock=$("#block{$block.id}")
var $buyDialog=$(".buydialog",$buylistblock);
var $cartDialog=$(".cartinfo",$buylistblock);
$(".btnBuy",$buylistblock).click(function(event){
    var $this=$(this);
    
	var offset=$this.offset();
    var blockOffset=$buylistblock.offset();
    var top=offset.top-blockOffset.top+$this.height();
    var left=offset.left-blockOffset.left;
    if(offset.left + $buyDialog.width() > blockOffset.left + $buylistblock.width()){
        left = left-$buyDialog.width()+$this.width()+20;
    }
    $(".pid",$buyDialog).val($this.attr("pid")); 
    $(".title",$buyDialog).html($this.attr("title"));
    $(".oldPrice",$buyDialog).html($this.attr("poldprice"));
    $(".newPrice",$buyDialog).html($this.attr("pprice"));
    $(".balance",$buyDialog).html($this.attr("pbalance"));
    $("#p_buy_amount",$buyDialog).val("1");
    changeAmount();
    
    $cartDialog.hide();
    $buyDialog.css({
        top: top,
        left: left 
    }).demsyModal();
});

$(".btnBuyCancel",$buyDialog).click(function(){
    $buyDialog.hide();
});

//加入购物车
$(".btnAddToCart",$buyDialog).click(function() {
	$buyDialog.block();
	$.post("{$contextPath}/bz/order/addtocart/"+$(".pid",$buyDialog).val()+"/"+$("#p_buy_amount",$buyDialog).val(), "", function(cart) {
    	if(cart.success){
           $buyDialog.hide();
    	   $cartDialog.css({
                top: $buyDialog.css("top"),
                left: $buyDialog.css("left")
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

//立即购买
var changeAmount=function(){
    var num=parseInt($("#p_buy_amount",$buyDialog).val());
    var unit=parseFloat($(".newPrice",$buyDialog).html());
    var total=num*unit;
    if(total>={$cfgNotPostFee}){
        $(".postFee").html('0.00');
        $(".totalprice").html(total.toFixed(2));
    }else{
        $(".postFee").html('{tostring bean=$cfgPostFee pattern="#,##0.00"}');
        $(".totalprice").html((total+{$cfgPostFee}).toFixed(2));
    }
    $(".subtotalprice",$buyDialog).html(total.toFixed(2));
}
var $p_buy_amount=$("#p_buy_amount");
$p_buy_amount.bind("spin",changeAmount);
$p_buy_amount.change(changeAmount);
$(".address").click(function(){
    if(this.value==""){
        $("#newaddrdiv").show();
    }else{
        $("#newaddrdiv").hide();
    }
});
$(".btnDirectBuy",$buyDialog).click(function(){
    var $buyform=$("#buyform_{$ui.uiid}");
	$buyform.block();
    $.post("{$contextPath}/bz/order/directbuy/"+$(".pid",$buyDialog).val()+"/"+$("#p_buy_amount").val(), $buyform.serialize(), function(order) {
        if(order.success){
        	window.document.location.href="{$contextPath}/ui/{$block.params}/"+order.sid;
        }else{
            alert(order.error);
        }
    }, "json");

    return false;
});

new PCAS("province","city","area");

//});
</script>