{* 条目链接——链接到查看购物车， 标题链接——链接到直接购买付款页面*}
<link href="{$stylePath}/jquery.ui.spinner.css" rel="stylesheet" type="text/css" media="screen" />
<!--  script src="{$scriptPath}/demsy.xheditor.js" type="text/javascript"></script -->
<script src="{$scriptPath}/jquery.ui.core.js" type="text/javascript"></script>
<script src="{$scriptPath}/jquery.ui.widget.js" type="text/javascript"></script>
<script src="{$scriptPath}/jquery.ui.spinner.js" type="text/javascript"></script>
<script src="{$scriptPath}/PCASClass.js" type="text/javascript"></script>

<div class="t"></div>
<div class="d">
    <div class="topName">{$obj.name}</div>
    <table class="top">
        <tr>
            <td colspan=2 valign="middle" class="topImg">{import file="a.st" img=$obj.image}</td>
            <td valign="top">
                    <table class="topR">
                        <tr>
                            <td class="topLbl" nowrap>原&nbsp;&nbsp;&nbsp;&nbsp;价：</td>
                            <td class="topPrice">
                            {if $obj.oldPrice != null}
                                {tostring bean=$obj prop="oldPrice" pattern="#,##0.00"}
                            {/if}</td>
                            <td class="topLbl">规&nbsp;&nbsp;&nbsp;&nbsp;格：</td>
                            <td>{tostring bean=$obj prop="spec"}</td>
                        </tr>
                        {if $barginPrice == null}
                        <tr>
                            <td class="topLbl" nowrap>现&nbsp;&nbsp;&nbsp;&nbsp;价：</td>
                            <td class="topPrice"><input id="p_buy_unitprice" type="hidden" value="{$obj.nowPrice}" />
                            {if $obj.nowPrice != null}{tostring bean=$obj prop="nowPrice" pattern="#,##0.00"}{else}<!-- 未上架 -->{/if}</td>
                            <td class="topLbl">编&nbsp;&nbsp;&nbsp;&nbsp;号：</td>
                            <td>{tostring bean=$obj prop="code"}</td>
                        </tr>
                        {else}
                        <tr>
                            <td class="topLbl" nowrap>正&nbsp;&nbsp;&nbsp;&nbsp;常：</td>
                            <td class="topPrice">{if $obj.price != null}{tostring bean=$obj prop="price" pattern="#,##0.00"}{else}<!-- 未上架 -->{/if}</td>
                            <td class="topLbl">编&nbsp;&nbsp;&nbsp;&nbsp;号：</td>
                            <td>{tostring bean=$obj prop="code"}</td>
                        </tr>
                        <tr>
                            <td class="topLbl" nowrap>活&nbsp;&nbsp;&nbsp;&nbsp;动：</td>
                            <td class="topPrice">
                                <input id="p_buy_unitprice" type="hidden" value="{$obj.nowPrice}" />
                                {tostring bean=$obj prop="nowPrice" pattern="#,##0.00"}
                            </td>
                            <td class="topLbl">备&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
                            <td>{tostring bean=$obj prop="barginNote"}</td>
                        </tr>
                        {/if}
                        <tr>
                            <td class="topLbl" nowrap>节&nbsp;&nbsp;&nbsp;&nbsp;省：</td>
                            <td class="topBalance">{if $obj.nowPrice != null}{tostring bean=$obj prop="balance" pattern="#,##0.00"}{/if}</td>
                            <td class="topLbl">类&nbsp;&nbsp;&nbsp;&nbsp;别：</td>
                            <td>{tostring bean=$obj.catalog prop="name"}</td>
                        </tr>
                        <tr>
                            <td class="topLbl">数&nbsp;&nbsp;&nbsp;&nbsp;量：</td>
                            <td>{if $obj.nowPrice != null}<input id="p_buy_amount" name="amount" type="text" class="amount input spinner" style="width:60px;" value="1" />{/if}</td>
                            <td class="topLbl">部&nbsp;&nbsp;&nbsp;&nbsp;门：</td>
                            <td>{tostring bean=$obj.operator prop="name"}</td>
                        </tr>
                        <tr>
                            <td colspan=4>
                                <div>{if $obj.nowPrice != null}
                                    <form action="" id="buyform_{$ui.uiid}" onsubmit="return false;">
                                        <input type="submit" class="topBtn{if $obj.nowPrice!= null} btnDirectBuy{/if}" value="" />
                                        <input type="submit" class="topBtn{if $obj.nowPrice!= null} btnAddToCart{/if}" value=""/>
                                    </form>
                                   {else}
                                   {if $cfgDeniedSale != null}
                                   <span class="topPrice">{$cfgDeniedSale.desc}</span>
                                   {else}
                                   <span class="topPrice">产品未上架，暂不能购买!</span>
                                   {/if}
                                   {/if}
                                </div>
                                <div class="dialog directbuy_dialog" style="position:absolute;display:none;">
                                    <div class="title">请确认订单信息！</div>
                                    <div class="content">
                                        <form id="directbuyform_{$ui.uiid}" action="" onsubmit="return false;">
                                            <div class="subtitle">商品费用：</div>
                                            <div>
                                                <table>
                                                    <tr>
                                                        <td>数量：</td><td><input id="p_buy_amount2" name="amount" type="text" class="amount input spinner" style="width: 60px;" value="1" /></td>
                                                        <td>邮费：</td><td><span class="postFee"></span></td>
                                                        <td>合计：</td><td><span class="totalprice">{tostring bean=$totalPrice pattern="#,##0.00"}</span></td>
                                                    </tr>
                                                </table>
                                            </div>
                                            <div class="subtitle">收件地址：</div>
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
                                            <div class="subtitle">订单留言：</div>
                                            <div>
       	                                        <textarea class="input" name="note" style="width: 420px; height: 30px; font-size: 12px;"></textarea>
                                            </div>
                                            <div class="subtitle"></div>
                                            <table>
                                                <tr>
                                                    <td style="color: red;font-weight: bold;padding-top: 5px;text-align: center;">(注：{$cfgNotPostFeeDesc})</td>
                                                    <td class="ui-btns">
                                                        <div class="ui-btn" style="float: left;"><input type="submit" class="btnDirectNextStep ui-btn" title="下一步：选择支付方式并准备付款" value="下一步" /></div>
                                                        <div style="width:5px;float: left;">&nbsp;</div>
                                                        <div class="ui-btn" style="float: left;"><input type="reset" class="btnDirectCancel ui-btn" value="取消" /></div>
                                                    </td>
                                                </tr>
                                            </table>
                                        </form>
                                    </div>
                                </div>
                                <div class="dialog cartinfo_dialog" style="position:absolute;display:none;">
                                    <form id="cartform_{$ui.uiid}" action="{$contextPath}/ui/{$block.link.id}">
                                        <div class="title">添加商品到购物车成功！</div>
                                        <div class="content">
                                            <table>
                                                <tr>
                                                    <td colspan=4 class="subtitle">购物车信息：</td>
                                                </tr>
                                                <tr>
                                                    <td align=right style="padding: 5px;">商品数量：</td><td><span class="amount"></span></td>
                                                    <td align=right style="padding: 5px;">合计金额：</td><td><span class="price"></span></td>
                                                </tr>
                                                <tr>
                                                    <td colspan=4 class="subtitle"></td>
                                                </tr>
                                                <tr>
                                                    <td colspan=2></td>
                                                    <td colspan=2 style="text-align: right;">
                                                        <div class="ui-btns">
                                                            <div class="ui-btn" style="float: left;"><input type="submit" class="btnGoCart ui-btn" value="结算" /></div>
                                                            <div style="width:5px;float: left;">&nbsp;</div>
                                                            <div class="ui-btn" style="float: left;"><input type="reset" class="btnCartCancel ui-btn" value="再看看" /></div>
                                                        </div>
                                                    </td>
                                                </tr>
                                            </table>
                                        </div>
                                    </form>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="topLbl" valign="bottom">分&nbsp;&nbsp;&nbsp;&nbsp;享：</td>
                            <td colspan=3>{import file="_share1.st"}</td>
                        </tr>
                        <tr>
                            <td class="topLbl">点&nbsp;&nbsp;&nbsp;&nbsp;击：</td>
                            <td>{tostring bean=$obj prop="clickNum"} 次</td>
                            {if $obj.stockNum > 0}
                            <td class="topLbl">库&nbsp;&nbsp;&nbsp;&nbsp;存：</td>
                            <td>{tostring bean=$obj prop="stockNum"}</td>
                            {/if}
                        </tr>
                    </table>
            </td>
        </tr>
    </table>
    <dl class="tabs">
        <dt>
            <div>商品详情</div>
        </dt>
        <dd>
            <div class="detail">{tostring bean=$obj prop="content"}</div>
        </dd>
        <dt>
            <div>商品评论</div>
        </dt>
        <dd>
            <div class="cmt">
                <table class="cmtinfo">
                    <tr>
                        <td class="cmtinfo1" style="float: left">我要评论</td> {if $obj.commentNum != null}
                        <td class="cmtinfo2"><a href='javascript:ui.gotoPage("blockcomment{$block.id}","commentPage=1&pageSize=10");'>已有&nbsp;<span style="color: red;">{$obj.commentNum}</span>&nbsp;人参与评论</a>
                        </td> {/if}
                    </tr>
                </table>
                <div class="cmtform">
                    <form id="cmtform_{$ui.uiid}" action="{$commentUrl}" style="text-align: right;">
                        <input type="hidden" name="data.module.id" value="{$module.id}" /> <input type="hidden" name="data.subjectID" value="{$obj.id}" />
                        <textarea style="height: 200px;" id="textarea_{$ui.uiid}" class="noxheditor" name="data.content"></textarea>
                        <input type="submit" class="submitButton" value="" />
                    </form>
                </div>
                <div class="cmtlist" id="blockcomment{$block.id}" dataID="{$block.id}"></div>
            </div>
        </dd>
    </dl>
</div>
<div class="b"></div>
<script type="text/javascript">
    var $buyform=$("#buyform_{$ui.uiid}");
    var $cmtform=$("#cmtform_{$ui.uiid}");
    var $directbuyDialog=$(".directbuy_dialog");
    var $cartinfoDialog=$(".cartinfo_dialog");
    $(".submitButton", $cmtform).click(function() {
        $cmtform.block(); 
        var form=$cmtform.get(0);
        $.post(form.action, $cmtform.serialize(), function(json) {
            alert(json.message);
            if(json.success){
                form.reset();
                ui.gotoPage("blockcomment{$block.id}","commentPage={if $pager.totalPage == null}1{else}{$pager.totalPage}{/if}");
            }
        }, "json");

        return false;
    });
    $(".btnAddToCart").click(function() {
        $directbuyDialog.hide();
        $buyform.block();
        $.post("{$contextPath}/bz/order/addtocart/{$obj.id}/"+$("#p_buy_amount").val(), "", function(cart) {
            if(cart.success){
            	$cartinfoDialog.demsyModal();
                $(".amount",$cartinfoDialog).html(cart.totalAmount);
                $(".price",$cartinfoDialog).html(parseFloat(cart.totalPrice).toFixed(2));
            }else{
                alert("添加商品到购物车失败! "+cart.error);
            }
        }, "json");

        return false;
    });
    $(".btnCartCancel").click(function(){
    	$cartinfoDialog.hide();
    });
    //立即购买
    $(".btnDirectBuy").click(function(){
    	$cartinfoDialog.hide();
    	$directbuyDialog.demsyModal();
    	$("#p_buy_amount2").val($("#p_buy_amount").val());
        changeAmount();
    });
    var changeAmount=function(){
        var num=parseInt($("#p_buy_amount2").val());
        var unit=parseFloat($("#p_buy_unitprice").val());
        var total=num*unit;
        if(total>={$cfgNotPostFee}){
            $(".postFee").html('0.00');
            $(".totalprice").html(total.toFixed(2));
        }else{
            $(".postFee").html('{tostring bean=$cfgPostFee pattern="#,##0.00"}');
            $(".totalprice").html((total+{$cfgPostFee}).toFixed(2));
        }
    }
    var $amount2=$("#p_buy_amount2");
    $amount2.bind("spin",changeAmount);
    $amount2.change(changeAmount);
    $(".address").click(function(){
        if(this.value==""){
            $("#newaddrdiv").show();
        }else{
        	$("#newaddrdiv").hide();
        }
    });
    $(".btnDirectNextStep",$(".directbuy_dialog")).click(function(){
        var $directbuyform=$("#directbuyform_{$ui.uiid}");
    	$directbuyDialog.block();
        $.post("{$contextPath}/bz/order/directbuy/{$obj.id}/"+$("#p_buy_amount2").val(), $directbuyform.serialize(), function(order) {
            if(order.success){
            	window.document.location.href="{$contextPath}/ui/{$block.titleLink.id}/"+order.sid;
            }else{
                alert(order.error);
            }
        }, "json");

        return false;
    });
    $(".btnDirectCancel").click(function(){
        $directbuyDialog.hide();
    });
    
    new PCAS("province","city","area");

    function gotoPage{$block.id}(){
    	if(ui){
    		ui.gotoPage("blockcomment{$block.id}","commentPage=1&pageSize=10");
    	}else{
    		setTimeout("gotoPage{$block.id}()",200);
    	}
    }
    gotoPage{$block.id}();
</script>
