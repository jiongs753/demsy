<link href="{$stylePath}/jquery.ui.spinner.css" rel="stylesheet" type="text/css" media="screen" />
<script src="{$scriptPath}/jquery.ui.core.js" type="text/javascript"></script>
<script src="{$scriptPath}/jquery.ui.widget.js" type="text/javascript"></script>
<script src="{$scriptPath}/jquery.ui.spinner.js" type="text/javascript"></script>

<div class="t">
    <table width=100%>
        <tr>
            <td class="wf_c">查看购物车</td>
            <td class="wf_c_r"></td>
            <td class="wf">订单确认</td>
            <td class="wf_r"></td>
            <td class="wf">订单付款</td>
            <td class="wf_r"></td>
            <td class="wf">确认收货</td>
            <td class="wf_r"></td>
            <td class="wf wf_e">交易结束</td>
        </tr>
    </table>
</div>
<div class="d">
    <form id="cart_{$ui.uiid}" action="" onsubmit="return false;">
        <table width=100% class="cart">
            <tr>
                <th width="50px" align="center">全选<input type="checkbox" class="checkall" checked="true" /></th>
                <th>商品名称</th>
                <th>原价(元)</th>
                <th>现价(元)</th>
                <th>数量</th>
                <th width="80px" align="right">小计(元)</th>
                <th width="120px" align="center">&nbsp;</th>
            </tr>
            {foreach from=$data.items item="item" key="idx"}
            <tr class="item item_{$idx}">
                <td width="30px" align="center"><input type="checkbox" class="checkone" checked="true" name="products" value="{$item.obj.product.id}" /></td>
                <td class="name" valign="top">
                    <div class="img">{import file="a.st" href=$item.href target=$item.target title=$item.name img=$item.img}</div>
                    {import file="a.st" href=$item.href target=$item.target title=$item.name name=$item.name}
                    ({tostring bean=$item.obj prop="code"})
                </td>
                <td class="price">{tostring bean=$item.obj.product prop="oldPrice" pattern="#,##0.00"}</td>
                <td class="price">{tostring bean=$item.obj prop="price" pattern="#,##0.00"}(省{tostring bean=$item.obj.product prop="balance" pattern="#,##0.00"})<input type="hidden" value="{$item.obj.price}" /></td>
                <td class="amount"><input type="text" value="{$item.obj.amount}" class="spinner" name="amount_{$item.obj.product.id}" /></td>
                <td class="subtotal price1" width="80px" align="right">{tostring bean=$item.obj prop="subtotal" pattern="0.00"}</td>
                <td class="delete" width="120px" align="right"><a class="delete">删除</a></td>
            </tr>
            {/foreach}
        </table>
        <table width=100% class="tfoot">
            <tr>
                <td style="color:red;">
                    <span style="color: red;font-weight: bold;">(注：{$cfgNotPostFeeDesc})</span>&nbsp;&nbsp;<span class="postFee">邮费：<span class="price">{tostring bean=$postFee patten="#,##0.00"}</span> 元</span>
                </td>
                <td align="right">
                                    合计(含邮费)：<span class="totalprice price2">{tostring bean=$totalPrice pattern="#,##0.00"}</span>&nbsp;元
                </td>
                <td width="100px">
                    <div class="ui-btns"><div class="ui-btn"><input type="submit" class="btnNextStep ui-btn" title="下一步：填写收件人地址信息并确认订单" value="下一步" /></div></div>
                </td>
            </tr>
        </table>
    </form>
</div>
<div class="b"></div>
<script type="text/javascript">
//$(function() {
    // 复选框控制
    var $cart_form=$("#cart_{$ui.uiid}");
    var $checkall=$(".checkall", $cart_form);
    var $checkone=$(".checkone", $cart_form);
    $checkall.click(function() {
        var checked=this.checked;
        $checkone.each(function(){
          this.checked=checked;  
        });
    });
    $checkone.click(function() {
    	var checkedall=true;
        $checkone.each(function(){
    		checkedall=this.checked && checkedall;
        });
    	$checkall.get(0).checked=checkedall;
    });

    //计算金额
    var $spinner=$(".spinner", $cart_form);
    var evalLocal=function(){
    	var $tr =$(".item",$cart_form);
        var total=0;
    	$tr.each(function(){
            var $this=$(this);
            var price=parseFloat($("input",$(".price",$this)).val());
            var $amount=$(".spinner",$this);
            var amount=parseFloat($amount.val());
            if(""+amount == "NaN" || amount < 0){
            	$amount.val(1);
                amount = 1;
            }
            
            var subtotal = parseFloat(price * amount);
            $(".subtotal",$this).html(subtotal.toFixed(2));
            total+=subtotal;
        });
        $(".itemsprice",$cart_form).html(total.toFixed(2));
        if(total>={$cfgNotPostFee}){
            $(".price",$(".postFee",$cart_form)).html('0.00');
            $(".totalprice",$cart_form).html(total.toFixed(2));
        }else{
        	$(".price",$(".postFee",$cart_form)).html('{tostring bean=$cfgPostFee pattern="#,##0.00"}');
            $(".totalprice",$cart_form).html((total+{$cfgPostFee}).toFixed(2));
        }
    }
    var changeAmount=function(){
        $cart_form.block();
        $this=$(this);
        var $tr=$this.parent().parent().parent();
        var p=$(".checkone",$tr).val();
	    $.post("{$contextPath}/bz/order/refreshcart/"+p+"/"+$this.val(), $cart_form.serialize(), function(cart) {
	       if(cart.success){
                evalLocal();
	       }else{
	           alert("刷新购物车中的商品数量失败! "+cart.error);
	       }
	   }, "json");
    }
    $spinner.bind("spin",changeAmount);
    $spinner.change(changeAmount);

    //删除单条
    $("a.delete",$cart_form).click(function(){
        var $tr=$(this).parent().parent();
        var p=$(".checkone",$tr).val();
        $cart_form.block();
        $.post("{$contextPath}/bz/order/deletecart/"+p, "", function(cart) {
            if(cart.success){
                $tr.remove();
                evalLocal();
            }else{
                alert("删除购物车中的商品失败! "+cart.error);
            }
        }, "json");
    });
    
    //结算
    $(".btnNextStep",$cart_form).click(function(){
        $cart_form.block();
        $.post("{$contextPath}/bz/order/prepare", $cart_form.serialize(), function(cart) {
            if(cart.success){
                window.location.href="{$contextPath}/ui/{$block.titleLink.id}"
            }else{
                alert("结算失败! "+cart.error);
            }
        }, "json");
    });
//});
</script>