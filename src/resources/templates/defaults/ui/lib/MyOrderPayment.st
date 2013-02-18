<div class="t">
    <table width=100%>
        <tr>
            <td class="wf">查看购物车</td>
            <td class="wf_r"></td>
            <td class="wf">订单确认</td>
            <td class="wf_r2"></td>
            <td class="wf_c">订单付款</td>
            <td class="wf_c_r"></td>
            <td class="wf">确认收货</td>
            <td class="wf_r"></td>
            <td class="wf wf_e">交易结束</td>
        </tr>
    </table>
</div>
<div class="d">
<form action="{$contextPath}/bz/order/alipayto/{$order.orderID}" id="bankform_{$block.id}" method="post">
    <input type="hidden" name="payment" />
    <div class="title">订单信息：</div>
    <div class="orderinfo">
    订单号：{$order.orderID}&nbsp;&nbsp;&nbsp;&nbsp;
    收件人：{$order.buyerHideInfo}&nbsp;&nbsp;&nbsp;&nbsp;
    付款金额：<span class="price">￥{tostring bean=$order prop="totalCost" pattern="#,##0.00"}元</span>&nbsp;&nbsp;&nbsp;&nbsp;
    <a href="{$contextPath}/ui/{$block.titleLink.id}/{$orderID}" target="_blank">订单详情</a></div>
    {if $orderError != null}<div style="color: red;font-weight: bold;">{$orderError}</div>
    {else}
    <div class="title">订单留言：</div>
    <div style="margin-left: 60px;">
    	<textarea class="input" name="note" style="width: 800px; height: 50px; font-size: 12px;">{$order.note}</textarea>
	</div>
    <div class="title">请选择您的付款方式：<span style="font-size: 12px;font-weight: normal;">如果你想要方便快捷可以选择网上银行；如果你需要更多付款方式可以选择支付宝(如：信用卡等)。</span></div>
	<table style="margin-left: 50px;" width="94%">
		<tr>
			<td><input type="radio" name="pay_bank" id="ICBCB2C" value="ICBCB2C" /><label title="中国工商银行" for="ICBCB2C"><img src="{$contextPath}/themes2/images/banks/ICBC_OUT.gif" border="0" /> </label></td>
			<td><input type="radio" name="pay_bank" id="CMB" value="CMB" /><label title="招商银行" for="CMB"><img src="{$contextPath}/themes2/images/banks/CMB_OUT.gif" border="0" /> </label></td>
			<td><input type="radio" name="pay_bank" id="CCB" value="CCB" /><label title="中国建设银行" for="CCB"><img src="{$contextPath}/themes2/images/banks/CCB_OUT.gif" border="0" /> </label></td>
			<td><input type="radio" name="pay_bank" id="BOCB2C" value="BOCB2C" /><label title="中国银行" for="BOCB2C"><img src="{$contextPath}/themes2/images/banks/BOC_OUT.gif" border="0" /> </label></td>
		</tr>
		<tr>
			<td><input type="radio" name="pay_bank" id="ABC" value="ABC" /><label title="中国农业银行" for="ABC"><img src="{$contextPath}/themes2/images/banks/ABC_OUT.gif" border="0" /> </label></td>
			<td><input type="radio" name="pay_bank" id="COMM" value="COMM" /><label title="交通银行" for="COMM"><img src="{$contextPath}/themes2/images/banks/COMM_OUT.gif" border="0" /> </label></td>
			<td><input type="radio" name="pay_bank" id="SPDB" value="SPDB" /><label title="浦发银行" for="SPDB"><img src="{$contextPath}/themes2/images/banks/SPDB_OUT.gif" border="0" /> </label></td>
			<td><input type="radio" name="pay_bank" id="GDB" value="GDB" /><label title="广发银行" for="GDB"><img src="{$contextPath}/themes2/images/banks/gb1.gif" border="0" /> </label></td>
		</tr>
		<tr>
			<td><input type="radio" name="pay_bank" id="CITIC" value="CITIC" /><label title="中信银行" for="CITIC"><img src="{$contextPath}/themes2/images/banks/CITIC_OUT.gif" border="0" /> </label></td>
			<td><input type="radio" name="pay_bank" id="CEBBANK" value="CEBBANK" /><label title="光大银行" for="CEBBANK"><img src="{$contextPath}/themes2/images/banks/CEB_OUT.gif" border="0" /> </label></td>
			<td><input type="radio" name="pay_bank" id="CIB" value="CIB" /><label title="兴业银行" for="CIB"><img src="{$contextPath}/themes2/images/banks/CIB_OUT.gif" border="0" /> </label></td>
			<td><input type="radio" name="pay_bank" id="SDB" value="SDB" /><label title="深圳发展银行" for="SDB"><img src="{$contextPath}/themes2/images/banks/SDB_OUT.gif" border="0" /> </label></td>
		</tr>
		<tr>
			<td><input type="radio" name="pay_bank" id="CMBC" value="CMBC" /><label title="民生银行" for="CMBC"><img src="{$contextPath}/themes2/images/banks/CMBC_OUT.gif" border="0" /> </label></td>
			<td><input type="radio" name="pay_bank" id="HZCBB2C" value="HZCBB2C" /><label title="杭州银行" for="HZCBB2C"><img src="{$contextPath}/themes2/images/banks/HZCB_OUT.gif" border="0" /> </label></td>
			<td><input type="radio" name="pay_bank" id="SHBANK" value="SHBANK" /><label title="上海银行" for="SHBANK"><img src="{$contextPath}/themes2/images/banks/SHBANK_OUT.gif" border="0" /> </label></td>
			<td><input type="radio" name="pay_bank" id="NBBANK" value="NBBANK" /><label title="宁波银行" for="NBBANK"><img src="{$contextPath}/themes2/images/banks/NBBANK_OUT.gif" border="0" /> </label></td>
		</tr>
		<tr>
			<td><input type="radio" name="pay_bank" id="SPABANK" value="SPABANK" /><label title="平安银行" for="SPABANK"><img src="{$contextPath}/themes2/images/banks/SPABANK_OUT.gif" border="0" /> </label></td>
			<td><input type="radio" name="pay_bank" id="BJRCB" value="BJRCB" /><label title="北京农商银行" for="BJRCB"><img src="{$contextPath}/themes2/images/banks/BJRCB_OUT.gif" border="0" /> </label></td>
			<td><input type="radio" name="pay_bank" id="ICBCBTB" value="ICBCBTB" /><label title="中国工商银行(企业)" for="ICBCBTB"><img src="{$contextPath}/themes2/images/banks/ENV_ICBC_OUT.gif" border="0" /> </label></td>
			<td><input type="radio" name="pay_bank" id="CCBBTB" value="CCBBTB" /><label title="中国建设银行(企业)" for="CCBBTB"><img src="{$contextPath}/themes2/images/banks/ENV_CCB_OUT.gif" border="0" /> </label></td>
		</tr>
		<tr>
			<td><input type="radio" name="pay_bank" id="SPDBB2B" value="SPDBB2B" /><label title="浦发银行(企业)" for="SPDBB2B"><img src="{$contextPath}/themes2/images/banks/ENV_SPDB_OUT.gif" border="0" /> </label></td>
			<td><input type="radio" name="pay_bank" id="ABCBTB" value="ABCBTB" /><label title="中国农业银行(企业)" for="ABCBTB"><img src="{$contextPath}/themes2/images/banks/ENV_ABC_OUT.gif" border="0" /> </label></td>
			<td><input type="radio" name="pay_bank" id="fdb101" value="fdb101" /><label title="富滇银行" for="fdb101"><img src="{$contextPath}/themes2/images/banks/FDB_OUT.gif" border="0" /> </label></td>
			<td><input type="radio" name="pay_bank" id="PSBC-DEBIT" value="PSBC-DEBIT" /><label title="中国邮政储蓄" for="PSBC-DEBIT"><img src="{$contextPath}/themes2/images/banks/PSBC_OUT.gif" border="0" /> </label></td>
		</tr>
        <tr>
            <td><input type="radio" name="pay_bank" id="more" value="1" /><label title="支付宝即时到帐，可以选择更多付款方式" for="more"><img src="{$contextPath}/themes2/images/banks/pay1.gif" border="0" /> </label></td>
            <td><input type="radio" name="pay_bank" id="dbjy" value="0" /><label title="支付宝担保交易付款，先收货后付款，可以选择更多付款方式" for="dbjy"><img src="{$contextPath}/themes2/images/banks/pay2.gif" border="0" /> </label></td>
        </tr>
        <tr>
            <td class="ui-btns" colspan=4><div class="ui-btn"><input type="submit" class="btnNextStep ui-btn" title="下一步：使用选定的网银或支付宝付款，卖家发货。" value="下一步" /></div></td>
        </tr>
    </table>
    {/if}
</form>
</div>
<div class="b"></div>
<script language="javascript">
var $bankform=$("#bankform_{$block.id}");
$("input:radio",$bankform).click(function(){
	$("label",$bankform).each(function(){
		$(this).css("border","1px #9C9C9C solid");
	});
	$("label",$(this).parent()).css("border","1px solid #fd0200");
	$bankform.get(0).elements["payment"].value=this.value;
});
$("img",$bankform).click(function(){
    $(this).parent().get(0).click();
});
$("input:radio",$bankform).each(function(){
    if(this.checked)
        $("label",$(this).parent()).css("border","1px solid #fd0200");
});
$(".btnNextStep", $bankform).click(function() {
	var payment=$bankform.get(0).elements["payment"].value;
    if(payment==null||payment.length == 0){
        alert("请先选择付款方式！");
        return false;
    }
});
</script>
