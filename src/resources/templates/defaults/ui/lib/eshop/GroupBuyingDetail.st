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
                            <td class="topLbl" nowrap>原&nbsp;&nbsp;&nbsp;&nbsp;价:</td>
                            <td class="topPrice">{if $obj.price != null}{tostring bean=$obj prop="oldPrice" pattern="#,##0.00"}{/if}</td>
                            <td class="topLbl">规&nbsp;&nbsp;&nbsp;&nbsp;格:</td>
                            <td>{tostring bean=$obj prop="spec"}</td>
                        </tr>
                        <tr>
                            <td class="topLbl" nowrap>团&nbsp;&nbsp;&nbsp;&nbsp;购:</td>
                            <td class="topPrice"><input id="p_buy_unitprice" type="hidden" value="{$obj.price}" />
                            {if $obj.price != null}{tostring bean=$obj prop="price" pattern="#,##0.00"}{else}<!-- 未上架 -->{/if}</td>
                            <td class="topLbl">编&nbsp;&nbsp;&nbsp;&nbsp;号:</td>
                            <td>{tostring bean=$obj prop="code"}</td>
                        </tr>
                        <tr>
                            <td class="topLbl" nowrap>节&nbsp;&nbsp;&nbsp;&nbsp;省:</td>
                            <td class="topBalance">{if $obj.price != null}{tostring bean=$obj prop="balance" pattern="#,##0.00"}{/if}</td>
                            <td class="topLbl">类&nbsp;&nbsp;&nbsp;&nbsp;别:</td>
                            <td>{tostring bean=$obj.catalog prop="name"}</td>
                        </tr>
                        <tr>
                            <td class="topLbl">数&nbsp;&nbsp;&nbsp;&nbsp;量:</td>
                            <td>{if $obj.price != null}<input id="p_buy_amount" name="amount" type="text" class="input spinner" style="width: 50px;" value="1" />{/if}</td>
                            <td class="topLbl">部&nbsp;&nbsp;&nbsp;&nbsp;门:</td>
                            <td>{tostring bean=$obj.operator prop="name"}</td>
                        </tr>
                        <tr>
                            <td colspan=4>
                                <div>{if $obj.price != null}
                                    <form action="" id="buyform_{$ui.uiid}" onsubmit="return false;">
                                        <input type="submit" class="topBtn{if $obj.price!= null} btnDirectbuy{/if}" value="" />
                                    </form>
                                   {else}
                                   <span class="topPrice">产品未上架，暂不能购买!</span>
                                   {/if}
                                </div>
                                <div class="dialog directbuy_dialog" style="position:absolute;display:none;">
                                    <div class="title">请确认收件人地址</div>
                                    <div class="content">
                                        <form id="directbuyform_{$ui.uiid}" action="" onsubmit="return false;">
                                            {foreach from=$contactList item="item" key="idx"}
                                            <div style="padding: 2px;">
                                                <input type="radio" name="address" ckecked class="address" id="address_{$item.id}" value="{$item.id}">
                                                <label for="address_{$item.id}">{$item.province}{$item.city}{$item.area}{$item.desc}({$item.name}&nbsp;&nbsp;{$item.code})</label>
                                            </div>
                                            {/foreach}
                                            <div style="padding: 2px;">
                                                <input type="radio" name="address" ckecked class="address" id="address_0" value=""><label for="address_0"> 使用新地址</label>
                                            </div>
                                            <div id="newaddrdiv" style="{if $contactList != null}display:none;{/if} padding: 15px 0 10px 0;">
                                                <div style="padding: 2px;">省/市/区：<select class="input" name="province" style="width: 120px;height: 22px;font-size: 12px;"></select>
                                                            <select class="input" name="city" style="width: 120px;height: 22px;font-size: 12px;"></select>
                                                            <select class="input" name="area" style="width: 120px;height: 22px;font-size: 12px;"></select>
                                                   
                                                </div>
                                                <div style="padding: 2px;">街道地址：<input class="input" type="text" name="street" style="width:256px;"/>
                                                &nbsp;邮编：<input class="input" type="text" name="postcode" style="width: 60px;"/>
                                                </div>
                                                <div style="padding: 2px;">真实姓名：<input class="input" type="text" name="person" style="width: 150px;" />
                                                &nbsp;手机号码：<input class="input" type="text" name="telcode" style="width: 140px;" /></div>
                                            </div>
                                            <div style="padding: 5px 2px;">
                                            邮费：<span class="postFee topPrice"></span>&nbsp;&nbsp;
                                                                                        合计(含邮费)：<span class="totalprice">{tostring bean=$totalPrice pattern="#,##0.00"}</span>&nbsp;
                                            <br/><span style="color: red;font-weight: bold;">(注：{$cfgNotPostFeeDesc})</span>
                                            </div>
                                        </form>
                                    </div>
                                    <div class="btns"><a class="btn">确定</a></div>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="topLbl" valign="bottom">分&nbsp;&nbsp;&nbsp;&nbsp;享:</td>
                            <td colspan=3>{import file="_share1.st"}</td>
                        </tr>
                        <tr>
                            <td class="topLbl">点&nbsp;&nbsp;&nbsp;&nbsp;击:</td>
                            <td colspan=3>{tostring bean=$obj prop="clickNum"} 次</td>
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
    $(".btnDirectbuy").click(function(){
    	$directbuyDialog.demsyModal();
        changeAmount();
    });
    var changeAmount=function(){
        var num=parseInt($("#p_buy_amount").val());
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
    var $spinner=$(".spinner");
    $spinner.bind("spin",changeAmount);
    $spinner.change(changeAmount);
    $(".address").click(function(){
        if(this.value==""){
            $("#newaddrdiv").show();
        }else{
        	$("#newaddrdiv").hide();
        }
    });
    $(".btn",$(".directbuy_dialog")).click(function(){
        var $directbuyform=$("#directbuyform_{$ui.uiid}");
    	$directbuyDialog.block();
        $.post("{$contextPath}/bz/order/directbuy/{$obj.id}/"+$("#p_buy_amount").val(), $directbuyform.serialize(), function(order) {
            if(order.success){
                //alert("订单提交成功！为了保证您的购物安全，我们将使用支付宝担保交易方式支付，付款后我们将即时发货。");
            	window.document.location.href="{$contextPath}/bz/order/alipayto/"+order.guid;
            }else{
                alert(order.error);
            }
        }, "json");

        return false;
    });
    
    new PCAS("province","city","area");
</script>
