<script src="{$scriptPath}/PCASClass.js" type="text/javascript"></script>
<div class="t">
    <table width=100%>
        <tr>
            <td class="wf">查看购物车</td>
            <td class="wf_r2"></td>
            <td class="wf_c">订单确认</td>
            <td class="wf_c_r"></td>
            <td class="wf">订单付款</td>
            <td class="wf_r"></td>
            <td class="wf">确认收货</td>
            <td class="wf_r"></td>
            <td class="wf wf_e">交易结束</td>
        </tr>
    </table>
</div>
<div class="d">
    <form id="orderform_{$ui.uiid}" action="" onsubmit="return false;">
	    <div class="confaddr">
	        <div><span class="caption"><strong>收货地址</strong> </span></div>
	    </div>
        <UL>
        {foreach from=$contactList item="item" key="idx"}
            <LI style="padding: 2px;">
                <input type="radio" name="address" id="address_{$item.id}" class="address" value="{$item.id}">
                <label for="address_{$item.id}">{$item.province}{$item.city}{$item.area}{$item.desc}({$item.name}&nbsp;&nbsp;{$item.code})</label>
            </LI>
        {/foreach}
            <LI style="padding: 2px;">
                <input type="radio" name="address" class="address" id="address_0" {if $contactList == null}checked{/if} value="">
                <label for="address_0"> 使用新地址</label>
            </LI>
        </UL>
        <div id="newaddrdiv" style="{if $contactList != null}display:none;{/if}">
                                                <div style="padding: 2px;">
                                                                                                        省/市/区：<select class="input" name="province" style="width: 120px;height: 22px;font-size: 12px;"></select>
                                                            <select class="input" name="city" style="width: 120px;height: 22px;font-size: 12px;"></select>
                                                            <select class="input" name="area" style="width: 120px;height: 22px;font-size: 12px;"></select>
                                                   
                                                </div>
                                                <div style="padding: 2px;">
                                                                                                    街道地址：<input class="input" type="text" name="street" style="width:365px;"/>
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
	    <div class="confaddr">
	        <div><span class="caption"><strong>订单留言</strong> </span></div>
	    </div>
        <div style="padding: 0px 0px 0px 30px;">
       	    <textarea class="input" name="note" style="width: 470px; height: 50px; font-size: 12px;"></textarea>
        </div>
        <table width=100% class="cart">
            <tr>
                <td colspan=4 class="caption"><strong>订单信息</strong></td>
            </tr>
            <tr>
                <th>商品名称</th>
                <th>原价(元)</th>
                <th>现价(元)</th>
                <th>数量</th>
                <th width="80px" align="right">小计(元)</th>
            </tr>
            {foreach from=$data.items item="item" key="idx"}
            <tr class="item item_{$idx}">
                <td class="name" valign="top"><div class="img">{import file="a.st" href=$item.href target=$item.target title=$item.name img=$item.img}</div>{import file="a.st" href=$item.href target=$item.target title=$item.name name=$item.name}</td>
                <td class="price">{tostring bean=$item.obj.product prop="oldPrice" pattern="#,##0.00"}</td>
                <td class="price">{tostring bean=$item.obj prop="price" pattern="#,##0.00"}(省{tostring bean=$item.obj.product prop="balance" pattern="#,##0.00"})</td>
                <td class="amount">{tostring bean=$item.obj prop="amount"}</td>
                <td class="subtotal" width="80px" align="right">{tostring bean=$item.obj prop="subtotal" pattern="#,##0.00"}</td>
            </tr>
            {/foreach}
        </table>
        <table width=100% class="tfoot">
            <tr>
                <td style="color:red;">
                    <span style="color: red;font-weight: bold;">(注：{$cfgNotPostFeeDesc})</span>&nbsp;&nbsp;<span class="postFee">邮费：<span class="price">{tostring bean=$postFee pattern="#,##0.00"}</span>元</span>
                </td>
                <td align="right">
                                    合计(含邮费)：<span class="totalprice">{tostring bean=$totalPrice pattern="#,##0.00"}</span> 元
                </td>
                <td width="100px">
                    <div class="ui-btns"><div class="ui-btn"><input type="submit" class="btnNextStep ui-btn"  title="下一步：选择支付方式并准备付款" value="下一步" /></div></div>
                </td>
            </tr>
        </table>
    </form>
</div>
<div class="b"></div>
<script language="javascript">
//$(function() {
    //添加联系地址
    $(".address").click(function(){
        if(this.value==""){
            $("#newaddrdiv").show();
        }else{
        	$("#newaddrdiv").hide();
        }
    });

    //提交订单
    var $orderform=$("#orderform_{$ui.uiid}");
    $(".btnNextStep", $orderform).click(function() {
        $orderform.block(); 
        $.post("{$contextPath}/bz/order/submit", $orderform.serialize(), function(order) {
            if(order.success){
            	window.document.location.href="{$contextPath}/ui/{$block.titleLink.id}/"+order.sid;
            }else{
                alert(order.error);
            }
        }, "json");

        return false;
    });
    new PCAS("province","city","area");
//});
</script>
